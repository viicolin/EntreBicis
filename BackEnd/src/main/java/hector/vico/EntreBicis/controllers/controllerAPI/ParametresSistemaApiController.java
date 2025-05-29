package hector.vico.EntreBicis.controllers.controllerAPI;

import hector.vico.EntreBicis.entity.ParametresSistema;
import hector.vico.EntreBicis.service.serviceWeb.ParametresSistemaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST per obtenir els paràmetres del sistema.
 * Aquesta API permet als clients accedir a la configuració global.
 */
@RestController
@RequestMapping("/api/parametres")
@CrossOrigin(origins = "*")
public class ParametresSistemaApiController {

    private static final Logger logger = LoggerFactory.getLogger(ParametresSistemaApiController.class);

    @Autowired
    private ParametresSistemaService parametresSistemaService;

    /**
     * Endpoint GET per obtenir els paràmetres actuals del sistema.
     * 
     * @return ResponseEntity amb els paràmetres del sistema i codi HTTP 200
     */
    @GetMapping
    public ResponseEntity<ParametresSistema> obtenirParametresSistema() {
        logger.info("Sol·licitud per obtenir els paràmetres del sistema.");

        // Obté els paràmetres des del servei
        ParametresSistema parametres = parametresSistemaService.obtenirParametres();

        logger.info("Paràmetres del sistema retornats correctament.");
        return ResponseEntity.ok(parametres);
    }
}