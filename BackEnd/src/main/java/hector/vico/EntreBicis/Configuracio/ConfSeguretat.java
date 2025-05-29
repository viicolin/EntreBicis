package hector.vico.EntreBicis.Configuracio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de configuració de la seguretat per a l'aplicació EntreBicis.
 * Aquesta classe defineix quines rutes són públiques, quines requereixen autenticació
 * i com s'ha de gestionar l'inici i tancament de sessió.
 */
@Configuration
@EnableWebSecurity
public class ConfSeguretat {

    @Autowired
    private ValidadorUsuaris validadorUsuaris;

    /**
     * Configura la cadena de filtres de seguretat de Spring Security.
     *
     * @param http objecte HttpSecurity per configurar les regles de seguretat
     * @return la SecurityFilterChain configurada
     * @throws Exception si hi ha algun error de configuració
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva la protecció CSRF per facilitar proves i API
            .securityMatcher("/**") // Aplica la configuració a totes les rutes
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // Permet l'accés lliure a les rutes de l'API
                .requestMatchers("/login", "/images/**", "/css/**", "/js/**", "/favicon.ico").permitAll() // Pàgina de login i recursos públics
                .requestMatchers("/admin/**").hasRole("ADMIN") // Rutes restringides a rols d'administrador
                .anyRequest().authenticated() // Qualsevol altra ruta requereix estar autenticat
            )
            .formLogin(login -> login
                .loginPage("/login") // Ruta del formulari d'inici de sessió
                .defaultSuccessUrl("/menu", true) // Ruta per defecte després d'iniciar sessió correctament
                .failureUrl("/login?error=true") // Ruta si hi ha error d'autenticació
                .usernameParameter("email") // Nom del camp per a l'email
                .passwordParameter("password") // Nom del camp per a la contrasenya
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // Ruta per fer logout
                .logoutSuccessUrl("/login?logout=true") // Ruta després de tancar sessió correctament
                .invalidateHttpSession(true) // Invalida la sessió actual
                .deleteCookies("JSESSIONID") // Elimina la cookie de sessió
                .permitAll()
            )
            .exceptionHandling(handling -> handling
                .accessDeniedPage("/errorPermisos")); // Ruta per a errors d'accés denegat

        return http.build();
    }

    /**
     * Codificador de contrasenyes amb l'algorisme BCrypt.
     *
     * @return instància de PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el gestor d'autenticació amb el servei de validació d'usuaris personalitzat.
     *
     * @param http objecte HttpSecurity per obtenir la configuració compartida
     * @return una instància d'AuthenticationManager
     * @throws Exception si hi ha un error de configuració
     */
    @SuppressWarnings("removal")
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                   .userDetailsService(validadorUsuaris)
                   .passwordEncoder(passwordEncoder())
                   .and()
                   .build();
    }
}