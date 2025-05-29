package hector.vico.EntreBicis.Configuracio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hector.vico.EntreBicis.entity.Usuari;
import hector.vico.EntreBicis.repository.UsuariRepository;

/**
 * Classe encarregada de carregar els detalls d'un usuari per a la seguretat de Spring.
 * Aquesta implementació només permet accés a usuaris amb rol ADMIN.
 */
@Service
public class ValidadorUsuaris implements UserDetailsService {

    // Logger per registrar accions importants i errors
    private static final Logger log = LoggerFactory.getLogger(ValidadorUsuaris.class);

    private final UsuariRepository usuariRepository;

    /**
     * Constructor amb injecció del repositori d'usuaris.
     *
     * @param usuariRepository repositori per accedir als usuaris de la base de dades
     */
    public ValidadorUsuaris(UsuariRepository usuariRepository) {
        this.usuariRepository = usuariRepository;
    }

    /**
     * Mètode que carrega un usuari pel seu email. Només permet usuaris amb rol ADMIN.
     *
     * @param email email de l'usuari a buscar
     * @return els detalls de l'usuari si és trobat i és administrador
     * @throws UsernameNotFoundException si l'usuari no existeix o no és ADMIN
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        // Buscar l'usuari pel seu email
        Usuari usuari = usuariRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuari no trobat amb email: {}", email);
                    return new UsernameNotFoundException("Usuari no trobat: " + email);
                });

        // Comprovació del rol
        if (!"ADMIN".equals(usuari.getRol().name())) {
            log.warn("Accés denegat. L'usuari {} no és administrador.", usuari.getEmail());
            throw new UsernameNotFoundException("noAdmin");
        }

        log.info("Usuari administrador carregat correctament: {}", usuari.getEmail());

        // Retornar els detalls de l'usuari per a Spring Security
        return User.withUsername(usuari.getEmail())
                .password(usuari.getContrasenya())
                .roles(usuari.getRol().name())
                .build();
    }
}
