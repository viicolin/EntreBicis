package hector.vico.EntreBicis.service.APIservice;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hector.vico.EntreBicis.entity.Usuari;
import hector.vico.EntreBicis.repository.UsuariRepository;

/**
 * Servei que permet modificar les dades personals d’un usuari a través de l’API.
 * Comprova la validesa dels camps i actualitza només aquells que han estat modificats.
 */
@Service
public class ApiUsuariService {

    private static final Logger log = LoggerFactory.getLogger(ApiUsuariService.class);

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Per encriptar la contrasenya si es modifica

    /**
     * Modifica les dades personals d’un usuari identificat pel seu email.
     * Només es modifiquen els camps que venen amb valors nous.
     *
     * @param email Email de l’usuari a modificar
     * @param usuariActualitzat Objecte amb els camps nous (pot tenir camps null)
     * @return Usuari actualitzat i desat a la base de dades
     */
    public Usuari modificaUsuariAPI(String email, Usuari usuariActualitzat) {
        log.info("S'ha entrat al mètode modificaUsuariAPI per l'email: {}", email);

        // Busquem l’usuari a la base de dades
        Optional<Usuari> usuariExistent = usuariRepository.findByEmail(email);
        if (usuariExistent.isEmpty()) {
            log.warn("No s'ha trobat cap usuari amb l'email: {}", email);
            throw new RuntimeException("No existeix cap usuari amb l'email: " + email);
        }

        Usuari usuariAntic = usuariExistent.get(); // L'usuari actual abans de modificar

        // Actualitzem nom si no és null
        if (usuariActualitzat.getNom() != null) {
            log.info("Actualitzant nom per l'usuari: {}", email);
            usuariAntic.setNom(usuariActualitzat.getNom());
        }

        // Actualitzem cognoms
        if (usuariActualitzat.getCognoms() != null) {
            log.info("Actualitzant cognoms per l'usuari: {}", email);
            usuariAntic.setCognoms(usuariActualitzat.getCognoms());
        }

        // Data de naixement: només si no és futura
        if (usuariActualitzat.getDataNaixement() != null) {
            LocalDate data = usuariActualitzat.getDataNaixement();
            log.info("Actualitzant data de naixement per l'usuari: {}", email);

            if (data.isAfter(LocalDate.now())) {
                log.warn("Data de naixement futura per l'usuari: {}", email);
                throw new RuntimeException("La data de naixement no pot ser futura.");
            }

            usuariAntic.setDataNaixement(data);
        }

        // Població
        if (usuariActualitzat.getPoblacio() != null) {
            log.info("Actualitzant població per l'usuari: {}", email);
            usuariAntic.setPoblacio(usuariActualitzat.getPoblacio());
        }

        // Telèfon: validació de 9 dígits exactes
        if (usuariActualitzat.getTelefon() != null) {
            String telefon = usuariActualitzat.getTelefon();
            log.info("Actualitzant telèfon per l'usuari: {}", email);

            if (!telefon.matches("\\d{9}")) {
                log.warn("Telèfon invàlid per l'usuari: {}", email);
                throw new RuntimeException("El telèfon ha de tenir exactament 9 dígits numèrics.");
            }

            usuariAntic.setTelefon(telefon);
        }

        // Contrasenya: si ve nova i no està en blanc, s’encripta
        if (usuariActualitzat.getContrasenya() != null && !usuariActualitzat.getContrasenya().isBlank()) {
            log.info("Actualitzant contrasenya per l'usuari: {}", email);
            usuariAntic.setContrasenya(passwordEncoder.encode(usuariActualitzat.getContrasenya()));
        }

        // Foto: només si ve amb contingut
        if (usuariActualitzat.getFoto() != null && usuariActualitzat.getFoto().length > 0) {
            log.info("Actualitzant foto per l'usuari: {}", email);
            usuariAntic.setFoto(usuariActualitzat.getFoto());
        }

        log.info("Usuari amb email {} modificat correctament.", usuariAntic.getEmail());

        // Desa l'usuari amb els canvis a la base de dades
        return usuariRepository.save(usuariAntic);
    }
}