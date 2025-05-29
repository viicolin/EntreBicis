package hector.vico.EntreBicis.controllers.controllerWeb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador per gestionar la sortida de sessió (logout) dels usuaris.
 */
@Controller
@RequestMapping("/logout")
public class LogOutController {

    private static final Logger log = LoggerFactory.getLogger(LogOutController.class);

    /**
     * Finalitza la sessió de l'usuari autenticat i redirigeix a la pàgina de login.
     *
     * @param request  petició HTTP del client
     * @param response resposta HTTP al client
     * @return redirecció a la vista de login amb el missatge de logout
     */
    @GetMapping
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Obtenim l'autenticació de l'usuari actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Si l'usuari està autenticat, es tanca la seva sessió
        if (auth != null) {
            log.info("Finalitzant la sessió per a l'usuari: {}", auth.getName());
            new SecurityContextLogoutHandler().logout(request, response, auth);
            log.info("Sessió finalitzada correctament per a l'usuari: {}", auth.getName());
        }

        // Redirigeix a la pàgina de login amb el missatge de sessió tancada
        log.info("Redirigint a la pàgina de login amb el missatge de logout.");
        return "redirect:/login?logout=true";
    }
}