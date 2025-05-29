package hector.vico.EntreBicis.controllers.controllerAPI;

import hector.vico.EntreBicis.entity.Recompensa;
import hector.vico.EntreBicis.service.APIservice.ApiRecompensaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST per gestionar les operacions relacionades amb les recompenses.
 * Inclou funcionalitats com consultar, reservar i confirmar recompenses.
 */
@RestController
@RequestMapping("/api/recompenses")
@CrossOrigin(origins = "*")
public class RecompensaApiController {

    private static final Logger logger = LoggerFactory.getLogger(RecompensaApiController.class);

    @Autowired
    private ApiRecompensaService recompensaApiService;

    /**
     * Obté totes les recompenses que es troben disponibles.
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Recompensa>> getRecompensesDisponibles() {
        logger.info("Sol·licitud per obtenir recompenses disponibles.");
        List<Recompensa> llista = recompensaApiService.llistarDisponibles();
        logger.info("Recompenses disponibles retornades: {}", llista.size());
        return ResponseEntity.ok(llista);
    }

    /**
     * Obté l'historial de recompenses d'un usuari a partir del seu email.
     */
    @GetMapping("/historial/{email}")
    public ResponseEntity<List<Recompensa>> getHistorialUsuari(@PathVariable String email) {
        logger.info("Sol·licitud per obtenir l'historial de recompenses de l'usuari: {}", email);
        List<Recompensa> historial = recompensaApiService.llistarHistorialUsuari(email);
        logger.info("Historial retornat amb {} recompenses per l'usuari: {}", historial.size(), email);
        return ResponseEntity.ok(historial);
    }

    /**
     * Consulta una recompensa concreta a partir del seu ID.
     */
    @GetMapping("/consultar/{id}")
    public ResponseEntity<Recompensa> consultarRecompensa(@PathVariable Long id) {
        logger.info("Sol·licitud per consultar la recompensa amb id: {}", id);
        try {
            Recompensa recompensa = recompensaApiService.consultarRecompensa(id);
            logger.info("Recompensa trobada: {}", recompensa.getId());
            return ResponseEntity.ok(recompensa);
        } catch (RuntimeException e) {
            logger.warn("No s'ha trobat la recompensa amb id: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Permet a un usuari reservar una recompensa concreta.
     */
    @PostMapping("/reservar/{id}")
    public ResponseEntity<Recompensa> reservarRecompensa(@PathVariable Long id, @RequestParam String email) {
        logger.info("Sol·licitud per reservar la recompensa amb id: {} per l'usuari: {}", id, email);
        try {
            Recompensa reservada = recompensaApiService.reservarRecompensa(id, email);
            logger.info("Recompensa reservada correctament amb id: {} per l'usuari: {}", id, email);
            return ResponseEntity.ok(reservada);
        } catch (RuntimeException e) {
            logger.warn("No s'ha pogut reservar la recompensa amb id: {} per l'usuari: {}", id, email);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obté la recompensa assignada a un usuari si n'hi ha una.
     */
    @GetMapping("/assignada/{email}")
    public ResponseEntity<Recompensa> obtenirRecompensaAssignada(@PathVariable String email) {
        logger.info("Sol·licitud per obtenir la recompensa assignada a l'usuari: {}", email);
        try {
            Recompensa recompensa = recompensaApiService.obtenirRecompensaAssignada(email);
            logger.info("Recompensa assignada trobada per l'usuari: {}", email);
            return ResponseEntity.ok(recompensa);
        } catch (RuntimeException e) {
            logger.warn("No s'ha trobat recompensa assignada per l'usuari: {}", email);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Confirma que l'usuari ha recollit una recompensa.
     */
    @PostMapping("/confirmar-recollida/{id}")
    public ResponseEntity<Recompensa> confirmarRecollida(@PathVariable Long id) {
        logger.info("Sol·licitud per confirmar la recollida de la recompensa amb id: {}", id);
        try {
            Recompensa recollida = recompensaApiService.confirmarRecollida(id);
            logger.info("Recollida confirmada per la recompensa amb id: {}", id);
            return ResponseEntity.ok(recollida);
        } catch (RuntimeException e) {
            logger.warn("No s'ha pogut confirmar la recollida per la recompensa amb id: {}", id);
            return ResponseEntity.badRequest().build();
        }
    }
}