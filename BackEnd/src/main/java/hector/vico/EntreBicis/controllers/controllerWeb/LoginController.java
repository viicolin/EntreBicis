package hector.vico.EntreBicis.controllers.controllerWeb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador per gestionar l'accés a la pàgina de login.
 * Mostra la vista "login" i gestiona missatges d'error o tancament de sessió.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Mostra la pàgina de login.
     *
     * @param error  indica si hi ha hagut un error d'autenticació
     * @param logout indica si s'ha tancat la sessió correctament
     * @param model  el model per passar dades a la vista
     * @return el nom de la plantilla HTML ("login")
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {

        // Si hi ha error d'inici de sessió, es mostra un missatge d'error
        if (error != null) {
            logger.warn("Error d'autenticació a l'inici de sessió.");
            model.addAttribute("error", "Email o contrasenya incorrectes.");
        }

        // Si s'ha tancat la sessió, es mostra un missatge de confirmació
        if (logout != null) {
            logger.info("Sessió tancada correctament.");
            model.addAttribute("logout", "Sessió tancada correctament.");
        }

        // Es mostra la vista del formulari de login
        logger.info("Mostrant la pàgina de login.");
        return "login";
    }
}