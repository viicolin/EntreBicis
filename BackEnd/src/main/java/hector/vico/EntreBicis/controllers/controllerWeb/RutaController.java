package hector.vico.EntreBicis.controllers.controllerWeb;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import hector.vico.EntreBicis.entity.ParametresSistema;
import hector.vico.EntreBicis.entity.Ruta;
import hector.vico.EntreBicis.repository.ParametresSistemaRepository;
import hector.vico.EntreBicis.service.serviceWeb.RutaService;

/**
 * Controlador web per gestionar les rutes.
 * Permet llistar, consultar, validar i invalidar rutes.
 */
@Controller
public class RutaController {

    private static final Logger logger = LoggerFactory.getLogger(RutaController.class);

    @Autowired
    private RutaService rutaService;

    @Autowired
    private ParametresSistemaRepository parametresSistemaRepository;

    /**
     * Mostra totes les rutes disponibles a la vista "rutes.html".
     * 
     * @param model Model de Spring per passar dades a la vista.
     * @return la vista "rutes"
     */
    @GetMapping("/rutes")
    public String llistarTotesLesRutes(Model model) {
        logger.info("Sol·licitud per llistar totes les rutes.");
        List<Ruta> rutes = rutaService.obtenirTotesLesRutes(); // Obté totes les rutes
        model.addAttribute("rutes", rutes); // Afegeix-les al model
        logger.info("S'han carregat {} rutes.", rutes.size());
        return "rutes";
    }

    /**
     * Mostra l'historial de rutes d'un usuari concret.
     * 
     * @param email Email de l'usuari
     * @param model Model per passar dades a la vista
     * @return la vista "ruta-historial"
     */
    @GetMapping("/historialRutes/{email}")
    public String historialRutes(@PathVariable String email, Model model) {
        logger.info("Sol·licitud per veure l'historial de rutes de l'usuari: {}", email);
        try {
            List<Ruta> rutes = rutaService.obtenirRutesPerEmail(email); // Obté les rutes de l'usuari
            model.addAttribute("rutes", rutes); // Afegeix-les al model
            logger.info("S'han carregat {} rutes d'historial per l'usuari: {}", rutes.size(), email);
        } catch (RuntimeException e) {
            logger.error("Error en obtenir l'historial de rutes per l'usuari {}: {}", email, e.getMessage());
            model.addAttribute("missatge", e.getMessage());
        }
        return "ruta-historial";
    }

    /**
     * Consulta i mostra els detalls d'una ruta específica.
     * 
     * @param id    ID de la ruta
     * @param model Model per passar dades a la vista
     * @return la vista "visualitzarRuta"
     */
    @GetMapping("/rutes/consultar/{id}")
    public String consultarRuta(@PathVariable Long id, Model model) {
        logger.info("Sol·licitud per consultar la ruta amb id: {}", id);
        Ruta ruta = rutaService.consultarRutaPerId(id); // Ruta per ID
        ParametresSistema parametres = parametresSistemaRepository.findAll().get(0); // Només hi ha 1 registre

        model.addAttribute("ruta", ruta);
        model.addAttribute("parametres", parametres);
        model.addAttribute("puntGPS", ruta.getPuntsGPS()); // Afegeix punts GPS
        logger.info("Ruta i paràmetres carregats correctament per la ruta amb id: {}", id);
        return "visualitzarRuta";
    }

    /**
     * Valida una ruta si compleix els criteris i li assigna saldo a l'usuari.
     * 
     * @param id                 ID de la ruta
     * @param model              Model per la vista
     * @param redirectAttributes Per passar errors amb redirect
     * @return Redirigeix a la mateixa ruta
     */
    @PostMapping("/rutes/validar/{id}")
    public String validarRuta(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        logger.info("Intentant validar la ruta amb id: {}", id);
        try {
            rutaService.validarRuta(id); // Canvia l'estat a VALIDADA
            logger.info("Ruta amb id {} validada correctament.", id);
        } catch (RuntimeException e) {
            logger.error("Error en validar la ruta amb id {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/rutes/consultar/" + id;
        }
        return "redirect:/rutes/consultar/" + id;
    }

    /**
     * Invalida una ruta validada i resta saldo si cal.
     * 
     * @param id    ID de la ruta
     * @param model Model per la vista
     * @return redirigeix a la ruta o torna a la vista amb error
     */
    @PostMapping("/rutes/invalidar/{id}")
    public String invalidarRuta(@PathVariable Long id, Model model) {
        logger.info("Intentant invalidar la ruta amb id: {}", id);
        try {
            rutaService.invalidarRuta(id); // Passa a estat NOVALIDADA
            logger.info("Ruta amb id {} invalidada correctament.", id);
            return "redirect:/rutes/consultar/" + id;
        } catch (IllegalStateException e) {
            logger.error("Error en invalidar la ruta amb id {}: {}", id, e.getMessage());
            Ruta ruta = rutaService.consultarRutaPerId(id);
            ParametresSistema parametres = parametresSistemaRepository.findAll().get(0);

            model.addAttribute("ruta", ruta);
            model.addAttribute("parametres", parametres);
            model.addAttribute("error", e.getMessage());
            return "visualitzarRuta";
        }
    }
}