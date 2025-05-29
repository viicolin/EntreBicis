package hector.vico.EntreBicis.controllers.controllerWeb;

import hector.vico.EntreBicis.entity.ParametresSistema;
import hector.vico.EntreBicis.service.serviceWeb.ParametresSistemaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador per gestionar la visualització i modificació dels paràmetres del sistema des del panell web.
 */
@Controller
@RequestMapping("/parametres")
public class ParametresSistemaController {

    private static final Logger logger = LoggerFactory.getLogger(ParametresSistemaController.class);

    @Autowired
    private ParametresSistemaService parametresService;

    /**
     * Mostra el formulari amb els valors actuals dels paràmetres del sistema.
     *
     * @param model el model per passar dades a la vista
     * @return el nom de la plantilla HTML del formulari
     */
    @GetMapping
    public String mostrarFormulari(Model model) {
        logger.info("Sol·licitud per mostrar el formulari de paràmetres del sistema.");
        ParametresSistema parametres = parametresService.obtenirParametres();
        model.addAttribute("parametres", parametres);
        logger.info("Paràmetres carregats correctament.");
        return "parametres-formulari"; // nom del fitxer HTML
    }

    /**
     * Rep el formulari amb els nous valors i actualitza els paràmetres del sistema.
     *
     * @param parametres objecte amb els valors actualitzats
     * @param model el model per retornar errors si cal
     * @return redirecció al menú si tot va bé, o el mateix formulari amb error
     */
    @PostMapping("/modificar")
    public String actualitzar(@ModelAttribute("parametres") ParametresSistema parametres, Model model) {
        logger.info("Sol·licitud per modificar els paràmetres del sistema.");
        try {
            parametresService.actualitzarParametres(parametres);
            logger.info("Paràmetres del sistema actualitzats correctament.");
            return "redirect:/menu";
        } catch (IllegalArgumentException ex) {
            logger.warn("Error en actualitzar els paràmetres del sistema: {}", ex.getMessage());
            model.addAttribute("parametres", parametres);
            model.addAttribute("error", ex.getMessage());
            return "parametres-formulari";
        }
    }
}