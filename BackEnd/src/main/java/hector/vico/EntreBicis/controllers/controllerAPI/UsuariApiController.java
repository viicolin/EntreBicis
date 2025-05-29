package hector.vico.EntreBicis.controllers.controllerAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hector.vico.EntreBicis.entity.Usuari;
import hector.vico.EntreBicis.service.APIservice.ApiUsuariService;
import hector.vico.EntreBicis.service.serviceWeb.UsuarisService;

/**
 * Controlador REST per gestionar operacions relacionades amb l'usuari a través de l'API.
 * Inclou la consulta i modificació d'usuaris per email.
 */
@RestController
@RequestMapping("/api/usuari")
@CrossOrigin(origins = "*")
public class UsuariApiController {

    private static final Logger log = LoggerFactory.getLogger(UsuariApiController.class);

    @Autowired
    private UsuarisService usuarisService;

    @Autowired
    private ApiUsuariService apiUsuariService;

    /**
     * Consulta un usuari pel seu email.
     * Retorna un error 404 si no es troba.
     */
    @GetMapping("/consultar/{email}")
    public ResponseEntity<Usuari> consultarUsuari(@PathVariable String email) {
        log.info("Sol·licitud per consultar l'usuari amb email: {}", email);
        try {
            Usuari usuari = usuarisService.ObtenirUsuariPerEmail(email);

            if (usuari == null) {
                log.warn("No s'ha trobat cap usuari amb email: {}", email);
                return ResponseEntity.status(404).body(null);
            }

            log.info("Usuari trobat amb email: {}", email);
            return ResponseEntity.ok(usuari);
        } catch (Exception e) {
            log.error("Error en consultar l'usuari amb email {}: {}", email, e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Endpoint alternatiu per veure un usuari pel seu email.
     * Equivalent a /consultar/{email}.
     */
    @GetMapping("/{email}")
    public ResponseEntity<Usuari> VeureUsuari(@PathVariable String email) {
        log.info("Sol·licitud per veure l'usuari amb email: {}", email);
        try {
            Usuari usuari = usuarisService.ObtenirUsuariPerEmail(email);
            log.info("Usuari retornat amb email: {}", email);
            return ResponseEntity.ok(usuari);
        } catch (Exception e) {
            log.error("Error en veure l'usuari amb email {}: {}", email, e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Modifica un usuari existent donat el seu email i la nova informació.
     * Retorna el nou usuari modificat si tot va bé, o un error amb el missatge corresponent.
     */
    @PutMapping("/modificar/{email}")
    public ResponseEntity<?> modificarUsuari(@PathVariable String email, @RequestBody Usuari usuariActualitzat) {
        log.info("Modificant usuari amb email: {}", email);
        try {
            Usuari usuariModificat = apiUsuariService.modificaUsuariAPI(email, usuariActualitzat);
            log.info("Usuari modificat correctament amb email: {}", email);
            return ResponseEntity.ok(usuariModificat);
        } catch (RuntimeException e) {
            log.error("Error en modificar l'usuari amb email {}: {}", email, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperat en modificar l'usuari amb email {}: {}", email, e.getMessage());
            return ResponseEntity.status(500).body("Error inesperat.");
        }
    }
}