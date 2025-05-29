package hector.vico.EntreBicis.controllers.controllerWeb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador per mostrar el menú principal de l'aplicació després d'autenticar-se.
 */
@Controller
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    /**
     * Mostra la vista del menú principal si l'usuari està autenticat.
     *
     * @param model model de la vista per passar atributs
     * @return la plantilla HTML del menú o redirecció al login
     */
    @GetMapping("/menu")
    public String mostrarMenu(Model model) {
        // Obtenim l'objecte d'autenticació actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Obté el correu de l'usuari autenticat

        // Comprovem si l'usuari està autenticat
        if (email == null) {
            logger.warn("Intent d'accedir al menú sense usuari autenticat.");
            return "redirect:/login";
        }

        // Registrem l'accés i passem l'email al model per mostrar-lo a la vista
        logger.info("Usuari autenticat accedint al menú: {}", email);
        model.addAttribute("userEmail", email);

        return "menu";
    }
}