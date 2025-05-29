package hector.vico.EntreBicis.controllers.controllerAPI;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hector.vico.EntreBicis.entity.LoginRequest;
import hector.vico.EntreBicis.entity.LoginResponse;
import hector.vico.EntreBicis.entity.Usuari;
import hector.vico.EntreBicis.repository.UsuariRepository;

/**
 * Controlador REST per gestionar l'inici de sessió dels usuaris via API.
 * Permet validar les credencials d'un usuari i retornar-ne informació si és correcte.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class LoginApiController {

    private static final Logger logger = LoggerFactory.getLogger(LoginApiController.class);

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Endpoint per iniciar sessió. Comprova si l'usuari existeix i si la contrasenya és correcta.
     *
     * @param loginRequest dades d'inici de sessió (email i contrasenya)
     * @return LoginResponse amb l'estat de l'autenticació
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> LoginUsuari(@RequestBody LoginRequest loginRequest) {
        logger.info("Intent d'inici de sessió per email: {}", loginRequest.getEmail());

        // Buscar l'usuari per email
        Optional<Usuari> optionalUsuari = usuariRepository.findByEmail(loginRequest.getEmail());

        // Si no es troba l'usuari, retornar error 404
        if (!optionalUsuari.isPresent()) {
            logger.warn("Usuari no trobat amb email: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginResponse(null, null, "Usuari no trobat"));
        }

        Usuari usuari = optionalUsuari.get();

        // Validar contrasenya
        if (!passwordEncoder.matches(loginRequest.getContrasenya(), usuari.getContrasenya())) {
            logger.warn("Contrasenya incorrecta per l'usuari: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, "Contrasenya incorrecta"));
        }

        logger.info("Sessió iniciada correctament per l'usuari: {}", usuari.getEmail());

        // Retornar resposta amb informació de l'usuari
        LoginResponse response = new LoginResponse(
                usuari.getEmail(),
                usuari.getNom(),
                usuari.getRol().toString()
        );

        return ResponseEntity.ok(response);
    }

}