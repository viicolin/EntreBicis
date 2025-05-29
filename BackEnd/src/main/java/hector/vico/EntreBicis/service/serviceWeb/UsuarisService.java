package hector.vico.EntreBicis.service.serviceWeb;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hector.vico.EntreBicis.entity.Usuari;
import hector.vico.EntreBicis.repository.UsuariRepository;

/**
 * Servei que conté la lògica de negoci per gestionar usuaris al sistema.
 */
@Service
public class UsuarisService {

    private static final Logger log = LoggerFactory.getLogger(UsuarisService.class);

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retorna la llista de tots els usuaris.
     */
    public List<Usuari> LlistaUsuaris() {
        log.info("Sol·licitud per obtenir la llista de tots els usuaris.");
        try {
            List<Usuari> usuaris = usuariRepository.findAll(); // Recupera tots els usuaris
            log.info("S'han trobat {} usuaris", usuaris.size());
            return usuaris;
        } catch (Exception e) {
            log.error("Error en obtenir tots els usuaris", e);
            throw new RuntimeException("Error en obtenir tots els usuaris", e);
        }
    }

    /**
     * Dona d'alta un nou usuari al sistema.
     * @param usuari Dades del nou usuari
     * @return Usuari creat
     */
    public Usuari AltaUsuari(Usuari usuari) {
        log.info("Intentant donar d'alta un nou usuari amb email: {}", usuari.getEmail());
        try {
            // Validar telèfon (exactament 9 dígits)
            String telefon = usuari.getTelefon();
            if (telefon == null || !telefon.matches("^\\d{9}$")) {
                log.warn("Telèfon no vàlid: {}", telefon);
                throw new RuntimeException("El telèfon no és vàlid. Ha de tenir exactament 9 dígits.");
            }

            // Comprova que no existeixi ja un usuari amb el mateix email
            if (usuariRepository.findByEmail(usuari.getEmail()).isPresent()) {
                log.warn("Intent d'alta amb email duplicat: {}", usuari.getEmail());
                throw new RuntimeException("Ja existeix un usuari amb aquest correu electrònic.");
            }

            // Comprova que la data de naixement no sigui futura
            if (usuari.getDataNaixement() != null && usuari.getDataNaixement().isAfter(LocalDate.now())) {
                log.warn("Intent d'alta amb data de naixement futura per l'usuari: {}", usuari.getEmail());
                throw new RuntimeException("La data de naixement no pot ser futura.");
            }

            // Inicialitza camps per defecte
            usuari.setContrasenya(passwordEncoder.encode(usuari.getContrasenya())); // Encripta la contrasenya
            usuari.setDataAlta(LocalDate.now()); // Data d'alta actual
            usuari.setSaldoPunts(0); // Comença amb 0 punts

            // Desa el nou usuari a la base de dades
            Usuari usuariGuardat = usuariRepository.save(usuari);
            log.info("Usuari creat correctament: {}", usuari.getEmail());
            return usuariGuardat;

        } catch (Exception e) {
            log.error("Error en guardar l'usuari", e);
            throw e;
        }
    }

    /**
     * Obté un usuari pel seu correu electrònic.
     * @param email Correu electrònic
     * @return Usuari trobat
     */
    public Usuari ObtenirUsuariPerEmail(String email) {
        log.info("Sol·licitud per obtenir l'usuari amb email: {}", email);
        try {
            Optional<Usuari> usuariOptional = usuariRepository.findByEmail(email);
            if (usuariOptional.isEmpty()) {
                log.warn("Usuari no trobat amb email: {}", email);
                throw new RuntimeException("Usuari no trobat amb email: " + email);
            }
            log.info("Usuari trobat amb email: {}", email);
            return usuariOptional.get();
        } catch (Exception e) {
            log.error("Error en obtenir l'usuari amb email: {}", email, e);
            throw new RuntimeException("Error en obtenir l'usuari", e);
        }
    }

    /**
     * Modifica les dades personals d'un usuari existent.
     * @param usuariActualitzat Usuari amb les dades actualitzades
     * @return Usuari modificat
     */
    public Usuari modificaUsuari(Usuari usuariActualitzat) {
        log.info("S'ha entrat al metode de modificaUsuari del servei per l'usuari: {}", usuariActualitzat.getEmail());

        Optional<Usuari> usuariExistent = usuariRepository.findByEmail(usuariActualitzat.getEmail());

        if (usuariExistent.isEmpty()) {
            log.warn("No s'ha trobat cap usuari amb l'email: {}", usuariActualitzat.getEmail());
            throw new RuntimeException("No existeix cap usuari amb l'email: " + usuariActualitzat.getEmail());
        }

        Usuari usuariAntic = usuariExistent.get();

        // Actualitza només els camps proporcionats
        if (usuariActualitzat.getNom() != null) {
            usuariAntic.setNom(usuariActualitzat.getNom());
        }
        if (usuariActualitzat.getCognoms() != null) {
            usuariAntic.setCognoms(usuariActualitzat.getCognoms());
        }
        if (usuariActualitzat.getDataNaixement() != null) {
            if (usuariActualitzat.getDataNaixement().isAfter(LocalDate.now())) {
                throw new RuntimeException("La data de naixement no pot ser futura.");
            }
            usuariAntic.setDataNaixement(usuariActualitzat.getDataNaixement());
        }
        if (usuariActualitzat.getPoblacio() != null) {
            usuariAntic.setPoblacio(usuariActualitzat.getPoblacio());
        }
        if (usuariActualitzat.getTelefon() != null) {
            String telefon = usuariActualitzat.getTelefon();
            if (!telefon.matches("^\\d{9}$")) {
                throw new RuntimeException("El telèfon no és vàlid. Ha de tenir exactament 9 dígits.");
            }
            usuariAntic.setTelefon(telefon);
        }
        if (usuariActualitzat.getContrasenya() != null && !usuariActualitzat.getContrasenya().isEmpty()) {
            usuariAntic.setContrasenya(passwordEncoder.encode(usuariActualitzat.getContrasenya()));
        }
        if (usuariActualitzat.getFoto() != null && usuariActualitzat.getFoto().length > 0) {
            usuariAntic.setFoto(usuariActualitzat.getFoto());
        }
        if (usuariActualitzat.getRol() != null) {
            usuariAntic.setRol(usuariActualitzat.getRol());
        }
        if (usuariActualitzat.getObservacions() != null) {
            usuariAntic.setObservacions(usuariActualitzat.getObservacions());
        }

        log.info("Usuari amb email {} modificat correctament.", usuariAntic.getEmail());
        return usuariRepository.save(usuariAntic);
    }
}