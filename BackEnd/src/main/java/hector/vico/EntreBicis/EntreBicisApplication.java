package hector.vico.EntreBicis;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import hector.vico.EntreBicis.entity.PermisUsuari;
import hector.vico.EntreBicis.entity.Usuari;
import hector.vico.EntreBicis.repository.RecompensaRepository;
import hector.vico.EntreBicis.repository.UsuariRepository;

/**
 * Classe principal de l'aplicació EntreBicis.
 * Inicia l'aplicació Spring Boot i crea dades per defecte si cal.
 */
@SpringBootApplication
public class EntreBicisApplication {

    /**
     * Mètode principal que inicia l'aplicació.
     */
    public static void main(String[] args) {
        SpringApplication.run(EntreBicisApplication.class, args);
    }

    /**
     * Bean que s'executa en iniciar l'aplicació.
     * Crea un usuari administrador i un ciclista si no existeixen.
     * 
     * @param usuariRepository repositori d'usuaris
     * @param recompensaRepository repositori de recompenses (no s'utilitza aquí, però es podria usar per inicialitzar recompenses)
     * @param passwordEncoder encriptador de contrasenyes
     * @return lògica a executar en iniciar l'aplicació
     */
    @Bean
    CommandLineRunner init(UsuariRepository usuariRepository, RecompensaRepository recompensaRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {
            // Si no existeix un usuari amb aquest correu, es crea un administrador
            if (usuariRepository.findByEmail("admin@entrebicis.com").isEmpty()) {
                Usuari admin = new Usuari(
                    "admin@entrebicis.com",                     // email
                    "Administrador",                            // nom
                    "Entrebicis",                               // cognoms
                    LocalDate.of(1990, 1, 1),                   // data de naixement
                    "Barcelona",                                // població
                    "123456789",                                // telèfon
                    passwordEncoder.encode("admin123"),         // contrasenya encriptada
                    LocalDate.now(),                            // data d'alta
                    0,                                          // saldo inicial
                    null,                                       // foto (null per defecte)
                    PermisUsuari.ADMIN,                         // rol ADMIN
                    "Usuari administrador per defecte"          // observacions
                );
                usuariRepository.save(admin); // Desa l'usuari a la base de dades
            }

            // Si no existeix, es crea també un ciclista de prova amb saldo
            if (usuariRepository.findByEmail("ciclista@entrebicis.com").isEmpty()) {
                Usuari ciclista = new Usuari(
                    "ciclista@entrebicis.com",
                    "Ciclista",
                    "Entrebicis",
                    LocalDate.of(2000, 5, 20),
                    "Madrid",
                    "987654321",
                    passwordEncoder.encode("ciclista123"),
                    LocalDate.now(),
                    100,                                       // saldo de prova
                    null,
                    PermisUsuari.CICLISTA,
                    "Usuari de prova amb saldo inicial"
                );
                usuariRepository.save(ciclista);
            }
        };
    }
}