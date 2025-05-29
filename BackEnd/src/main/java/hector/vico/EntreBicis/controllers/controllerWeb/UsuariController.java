package hector.vico.EntreBicis.controllers.controllerWeb;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import hector.vico.EntreBicis.entity.Usuari;
import hector.vico.EntreBicis.service.serviceWeb.UsuarisService;

/**
 * Controlador web per gestionar operacions relacionades amb els usuaris,
 * com mostrar, crear, modificar i veure detalls.
 */
@Controller
public class UsuariController {

    // Logger per registrar informació de seguiment i errors
    private static final Logger log = LoggerFactory.getLogger(UsuariController.class);

    @Autowired
    private UsuarisService usuarisService;

    /**
     * Mostra la llista de tots els usuaris.
     * @param model Model per enviar dades a la vista
     * @return Pàgina HTML amb la llista d’usuaris
     */
    @GetMapping("/usuaris")
    public String MostrarUsuaris(Model model) {
        log.info("Sol·licitud per llistar tots els usuaris.");
        try {
            // Obté la llista d'usuaris des del servei
            List<Usuari> usuaris = usuarisService.LlistaUsuaris();

            // Mapa per emmagatzemar les fotos codificades en Base64
            Map<String, String> imatgesBase64 = new HashMap<>();

            // Converteix les imatges a format Base64 per mostrar-les en HTML
            for (Usuari usuari : usuaris) {
                if (usuari.getFoto() != null) {
                    String fotoBase64 = Base64.getEncoder().encodeToString(usuari.getFoto());
                    imatgesBase64.put(usuari.getEmail(), fotoBase64);
                }
            }

            // Afegeix usuaris i fotos al model per mostrar a la vista
            model.addAttribute("usuaris", usuaris);
            model.addAttribute("imatgesBase64", imatgesBase64);

            log.info("S'han carregat {} usuaris.", usuaris.size());
            return "usuaris";
        } catch (Exception e) {
            log.error("Error en obtenir tots els usuaris: {}", e.getMessage());
            model.addAttribute("error", "Error en obtenir tots els usuaris");
            return "error";
        }
    }

    /**
     * Mostra el formulari per crear un nou usuari.
     * @param model Model per la vista
     * @return Pàgina del formulari
     */
    @GetMapping("/nou_usuari")
    public String FormulariUsuari(Model model) {
        log.info("Mostrant formulari per crear un nou usuari.");
        model.addAttribute("usuari", new Usuari()); // Afegeix un usuari buit al formulari
        return "nou_usuari";
    }

    /**
     * Dona d'alta un nou usuari.
     * @param usuari Dades de l’usuari
     * @param fotoFile Fitxer de la imatge
     * @param model Model per la vista
     * @return Redirecció a la llista d’usuaris o error
     */
    @PostMapping("/nou_usuari")
    public String AltaUsuari(@ModelAttribute Usuari usuari,
                             @RequestParam("fotoFile") MultipartFile fotoFile,
                             Model model) {
        log.info("Iniciant procés per guardar un nou usuari amb email: {}", usuari.getEmail());

        try {
            if (fotoFile != null && !fotoFile.isEmpty()) {
                usuari.setFoto(fotoFile.getBytes()); // Assigna la imatge com a byte[]
                log.info("S'ha associat una foto al usuari.");
            } else {
                usuari.setFoto(null); // Si no hi ha foto, assigna null
                log.info("No s'ha proporcionat cap foto per l'usuari.");
            }

            Usuari usuariGuardat = usuarisService.AltaUsuari(usuari); // Guarda l'usuari
            log.info("Usuari creat correctament: {}", usuariGuardat.getEmail());
            return "redirect:/usuaris"; // Redirecciona a la vista amb la llista d'usuaris
        } catch (IOException e) {
            model.addAttribute("usuari", usuari);
            model.addAttribute("error", e.getMessage());
            log.error("Error en alta de usuari: {}", e.getMessage());
            return "nou_usuari";
        } catch (RuntimeException e) {
            model.addAttribute("usuari", usuari);
            model.addAttribute("error", e.getMessage());
            log.error("Error en crear l'usuari: {}", e.getMessage());
            return "nou_usuari";
        }
    }

    /**
     * Mostra el detall d’un usuari concret.
     * @param email Email de l’usuari
     * @param model Model per la vista
     * @return Pàgina amb el detall de l’usuari
     */
    @GetMapping("/usuari/{email}")
    public String MostrarDetallUsuari(@PathVariable String email, Model model) {
        log.info("Iniciant procés per mostrar el detall de l'usuari amb email: {}", email);

        try {
            Usuari usuari = usuarisService.ObtenirUsuariPerEmail(email);

            if (usuari.getFoto() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(usuari.getFoto());
                model.addAttribute("imatgeBase64", imatgeBase64); // Afegeix imatge codificada
                log.info("S'ha carregat la imatge de l'Usuari.");
            } else {
                model.addAttribute("imatgeBase64", null);
                log.info("L'Usuari no té cap imatge associada.");
            }

            model.addAttribute("usuari", usuari);
            return "detallUsuari";
        } catch (Exception e) {
            log.error("Error en obtenir el detall de l'usuari amb email: {}", email, e);
            model.addAttribute("error", "No s'ha pogut obtenir el detall de l'usuari.");
            return "error";
        }
    }

    /**
     * Mostra el formulari per modificar un usuari existent.
     * @param email Email de l’usuari
     * @param model Model per la vista
     * @return Pàgina per modificar usuari
     */
    @GetMapping("/usuari/modificar/{email}")
    public String FormulariModificarUsuari(@PathVariable String email, Model model) {
        log.info("Iniciant procés per mostrar el formulari de modificació de l'usuari amb email: {}", email);

        try {
            Usuari usuari = usuarisService.ObtenirUsuariPerEmail(email);

            if (usuari.getFoto() != null) {
                String imatgeBase64 = Base64.getEncoder().encodeToString(usuari.getFoto());
                model.addAttribute("imatgeBase64", imatgeBase64);
                log.info("S'ha carregat la imatge de l'Usuari per modificar.");
            } else {
                model.addAttribute("imatgeBase64", null);
                log.info("L'Usuari no té cap imatge associada per modificar.");
            }

            model.addAttribute("usuari", usuari);
            return "modificarUsuari";
        } catch (Exception e) {
            log.error("Error en obtenir el detall de l'usuari amb email: {}", email, e);
            model.addAttribute("error", "No s'ha pogut obtenir el detall de l'usuari.");
            return "error";
        }
    }

    /**
     * Modifica un usuari existent.
     * @param usuari Dades modificades de l’usuari
     * @param fotoFile Nova foto (opcional)
     * @param model Model per la vista
     * @return Redirecció o error
     */
    @PostMapping("/usuari/modificar")
    public String ModificarUsuari(@ModelAttribute Usuari usuari,
                                  @RequestParam(value = "fotoFile", required = false) MultipartFile fotoFile,
                                  Model model) {
        log.info("Iniciant procés per modificar l'usuari amb email: {}", usuari.getEmail());

        try {
            if (fotoFile != null && !fotoFile.isEmpty()) {
                usuari.setFoto(fotoFile.getBytes()); // Assigna la nova imatge
                log.info("S'ha associat una nova foto al usuari.");
            }

            Usuari usuariModificat = usuarisService.modificaUsuari(usuari);
            log.info("Usuari modificat correctament: {}", usuariModificat.getEmail());
            return "redirect:/usuaris";
        } catch (IOException e) {
            // Torna a mostrar la imatge prèvia si hi ha un error
            if (usuari.getFoto() != null && usuari.getFoto().length > 0) {
                model.addAttribute("imatgeBase64", Base64.getEncoder().encodeToString(usuari.getFoto()));
            }
            model.addAttribute("usuari", usuari);
            model.addAttribute("error", "Error en processar la imatge: " + e.getMessage());
            log.error("Error en processar la imatge: {}", e.getMessage());
            return "modificarUsuari";
        } catch (RuntimeException e) {
            // Torna a recuperar la foto existent si no es passa una nova
            if (usuari.getFoto() != null && usuari.getFoto().length > 0) {
                model.addAttribute("imatgeBase64", Base64.getEncoder().encodeToString(usuari.getFoto()));
            } else {
                Usuari existent = usuarisService.ObtenirUsuariPerEmail(usuari.getEmail());
                if (existent.getFoto() != null) {
                    model.addAttribute("imatgeBase64", Base64.getEncoder().encodeToString(existent.getFoto()));
                    usuari.setFoto(existent.getFoto());
                }
            }

            model.addAttribute("usuari", usuari);
            model.addAttribute("error", e.getMessage());
            log.error("Error en modificar l'usuari: {}", e.getMessage());
            return "modificarUsuari";
        }
    }
}