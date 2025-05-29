package hector.vico.EntreBicis.controllers.controllerAPI;

import hector.vico.EntreBicis.entity.PuntsGPS;
import hector.vico.EntreBicis.entity.Ruta;
import hector.vico.EntreBicis.service.APIservice.ApiRutaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST per gestionar les rutes de l'usuari.
 * Permet iniciar, afegir punts, finalitzar, consultar i llistar rutes.
 */
@RestController
@RequestMapping("/api/ruta")
@CrossOrigin(origins = "*")
public class RutaApiController {

    private static final Logger logger = LoggerFactory.getLogger(RutaApiController.class);

    @Autowired
    private ApiRutaService rutaService;

    /**
     * Inicia una nova ruta per a l'usuari especificat per email.
     */
    @PostMapping("/iniciar")
    public ResponseEntity<Ruta> iniciarRuta(@RequestParam String email) {
        logger.info("Sol·licitud per iniciar una nova ruta per l'usuari: {}", email);
        try {
            Ruta ruta = rutaService.iniciarRuta(email);
            logger.info("Ruta iniciada correctament amb id: {} per l'usuari: {}", ruta.getId(), email);
            return ResponseEntity.ok(ruta);
        } catch (RuntimeException e) {
            logger.warn("No s'ha pogut iniciar la ruta per l'usuari: {}", email);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Afegeix un punt GPS a la ruta identificada pel seu ID.
     */
    @PostMapping("/afegir/{rutaId}")
    public PuntsGPS afegirPunt(@PathVariable Long rutaId, @RequestBody PuntsGPS punt) {
        logger.info("Afegint punt GPS a la ruta amb id: {}", rutaId);
        PuntsGPS puntAfegit = rutaService.afegirPuntGPS(rutaId, punt);
        logger.info("Punt GPS afegit a la ruta amb id: {}", rutaId);
        return puntAfegit;
    }

    /**
     * Finalitza una ruta identificada pel seu ID.
     */
    @PutMapping("/finalitzar/{rutaId}")
    public ResponseEntity<Ruta> finalitzarRuta(@PathVariable Long rutaId) {
        logger.info("Sol·licitud per finalitzar la ruta amb id: {}", rutaId);
        try {
            Ruta rutaFinalitzada = rutaService.finalitzarRuta(rutaId);
            logger.info("Ruta finalitzada correctament amb id: {}", rutaId);
            return ResponseEntity.ok(rutaFinalitzada);
        } catch (RuntimeException e) {
            logger.warn("No s'ha pogut finalitzar la ruta amb id: {}", rutaId);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Llista totes les rutes associades a un usuari donat el seu email.
     */
    @GetMapping("/llistar")
    public ResponseEntity<?> llistarRutesUsuari(@RequestParam String email) {
        logger.info("Sol·licitud per llistar rutes de l'usuari: {}", email);
        try {
            var rutes = rutaService.obtenirRutesPerUsuari(email);
            logger.info("S'han trobat {} rutes per l'usuari: {}", rutes.size(), email);
            return ResponseEntity.ok(rutes);
        } catch (RuntimeException e) {
            logger.warn("Error en obtenir les rutes de l'usuari: {}", email);
            return ResponseEntity.badRequest().body("Error en obtenir les rutes de l'usuari.");
        }
    }

    /**
     * Consulta una ruta concreta per ID i email d'usuari.
     */
    @GetMapping("/consultar/{id}")
    public ResponseEntity<Ruta> consultarRutaPerUsuari(@PathVariable Long id, @RequestParam String email) {
        logger.info("Sol·licitud per consultar la ruta amb id: {} per l'usuari: {}", id, email);
        try {
            Ruta ruta = rutaService.consultarRutaPerIdIEmail(id, email);
            logger.info("Ruta trobada amb id: {} per l'usuari: {}", id, email);
            return ResponseEntity.ok(ruta);
        } catch (RuntimeException e) {
            logger.warn("No s'ha trobat la ruta amb id: {} per l'usuari: {}", id, email);
            return ResponseEntity.badRequest().build();
        }
    }
}