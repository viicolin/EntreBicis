package hector.vico.EntreBicis.controllers.controllerWeb;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import hector.vico.EntreBicis.entity.Recompensa;
import hector.vico.EntreBicis.service.serviceWeb.RecompensaService;

/**
 * Controlador web per gestionar les recompenses des de la interfície d'administrador.
 */
@Controller
public class RecompensaController {

    private static final Logger log = LoggerFactory.getLogger(RecompensaController.class);

    @Autowired
    private RecompensaService recompensaService;

    /**
     * Mostra la llista de totes les recompenses.
     */
    @GetMapping("/recompenses")
    public String llistarRecompenses(Model model) {
        log.info("Sol·licitud per llistar totes les recompenses.");
        try {
            List<Recompensa> recompenses = recompensaService.llistarRecompenses();
            model.addAttribute("recompenses", recompenses);
            log.info("S'han carregat {} recompenses.", recompenses.size());
            return "recompenses";
        } catch (Exception e) {
            log.error("Error en obtenir les recompenses: {}", e.getMessage());
            model.addAttribute("error", "Error en obtenir les recompenses");
            return "error";
        }
    }

    /**
     * Mostra l'historial de recompenses associades a un usuari.
     */
    @GetMapping("/historialRecompenses/{email}")
    public String historialRecompenses(@PathVariable String email, Model model) {
        log.info("Sol·licitud per veure l'historial de recompenses de l'usuari: {}", email);
        try {
            List<Recompensa> recompenses = recompensaService.obtenirRecompensesPerEmail(email);
            model.addAttribute("recompenses", recompenses);
            log.info("S'han carregat {} recompenses d'historial per l'usuari: {}", recompenses.size(), email);
        } catch (RuntimeException e) {
            log.error("Error en obtenir l'historial de recompenses per l'usuari {}: {}", email, e.getMessage());
            model.addAttribute("missatge", e.getMessage());
        }
        return "recompensa-historial";
    }

    /**
     * Mostra el formulari per crear una nova recompensa.
     */
    @GetMapping("/nou_recompensa")
    public String mostrarFormulariNou(Model model) {
        log.info("Mostrant formulari per crear una nova recompensa.");
        model.addAttribute("recompensa", new Recompensa());
        return "nou_recompensa";
    }

    /**
     * Gestiona la creació d'una nova recompensa.
     */
    @PostMapping("/recompenses/crear")
    public String crearRecompensa(@ModelAttribute Recompensa recompensa, Model model) {
        log.info("Intentant crear una nova recompensa.");
        try {
            recompensaService.crearRecompensa(recompensa);
            log.info("Recompensa creada correctament.");
            return "redirect:/recompenses";
        } catch (RuntimeException e) {
            log.error("Error en crear la recompensa: {}", e.getMessage());
            model.addAttribute("recompensa", recompensa);
            model.addAttribute("error", e.getMessage());
            return "nou_recompensa";
        }
    }

    /**
     * Elimina una recompensa pel seu identificador.
     */
    @PostMapping("/recompenses/eliminar/{id}")
    public String eliminarRecompensa(@PathVariable Long id, Model model) {
        log.info("Intentant eliminar la recompensa amb id: {}", id);
        try {
            recompensaService.eliminarRecompensa(id);
            log.info("Recompensa amb id {} eliminada correctament.", id);
            return "redirect:/recompenses";
        } catch (RuntimeException e) {
            log.error("Error en eliminar la recompensa amb id {}: {}", id, e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     * Mostra els detalls d'una recompensa específica.
     */
    @GetMapping("/detallRecompensa/{id}")
    public String obtenirDetallRecompensa(@PathVariable Long id, Model model) {
        log.info("Sol·licitud per obtenir els detalls de la recompensa amb id: {}", id);
        try {
            Recompensa recompensa = recompensaService.obtenirDetallRecompensa(id);
            model.addAttribute("recompensa", recompensa);
            log.info("Detalls de la recompensa amb id {} carregats correctament.", id);
            return "detallRecompensa";
        } catch (RuntimeException e) {
            log.error("Error al obtenir els detalls de la recompensa amb id {}: {}", id, e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     * Assigna una recompensa que està reservada a un usuari.
     */
    @PostMapping("/recompenses/acceptar/{id}")
    public String assignarReserva(@PathVariable Long id, Model model) {
        log.info("Intentant assignar la recompensa amb id: {}", id);
        try {
            recompensaService.assignarReserva(id);
            log.info("Recompensa amb id {} assignada correctament.", id);
            model.addAttribute("success", "Recompensa assignada correctament.");
        } catch (RuntimeException e) {
            log.error("Error al assignar la recompensa amb id {}: {}", id, e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/recompenses";
    }
}