package hector.vico.EntreBicis.service.serviceWeb;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hector.vico.EntreBicis.entity.EstatRecompensa;
import hector.vico.EntreBicis.entity.Recompensa;
import hector.vico.EntreBicis.entity.Usuari;
import hector.vico.EntreBicis.repository.RecompensaRepository;
import hector.vico.EntreBicis.repository.UsuariRepository;

/**
 * Servei per gestionar les recompenses a la part web d'administració.
 */
@Service
public class RecompensaService {

    private static final Logger log = LoggerFactory.getLogger(RecompensaService.class);

    @Autowired
    private RecompensaRepository recompensaRepository;

    @Autowired
    private UsuariRepository usuariRepository;

    /**
     * Llista totes les recompenses de la base de dades.
     * Només mostra l'usuari si la recompensa no està disponible.
     */
    public List<Recompensa> llistarRecompenses() {
        log.info("Sol·licitud per llistar totes les recompenses.");
        List<Recompensa> recompenses = recompensaRepository.findAll();

        // Elimina la referència a l'usuari si la recompensa no ha estat assignada.
        for (Recompensa recompensa : recompenses) {
            if (recompensa.getEstat() != EstatRecompensa.RESERVADA &&
                recompensa.getEstat() != EstatRecompensa.ASSIGNADA &&
                recompensa.getEstat() != EstatRecompensa.RECOLLIDA) {
                recompensa.setUsuari(null);
            }
        }

        log.info("S'han carregat {} recompenses.", recompenses.size());
        return recompenses;
    }

    /**
     * Obté totes les recompenses d’un usuari a partir del seu email.
     * @param email Correu de l'usuari
     * @return llista de recompenses d'aquest usuari
     */
    public List<Recompensa> obtenirRecompensesPerEmail(String email) {
        log.info("Sol·licitud per obtenir recompenses de l'usuari: {}", email);
        List<Recompensa> recompenses = recompensaRepository.findByUsuariEmail(email);

        if (recompenses.isEmpty()) {
            log.warn("L'usuari {} encara no ha obtingut cap recompensa.", email);
            throw new RuntimeException("Aquest usuari encara no ha obtingut cap recompensa.");
        }

        log.info("S'han trobat {} recompenses per l'usuari: {}", recompenses.size(), email);
        return recompenses;
    }

    /**
     * Crea una nova recompensa, assegurant que compleix els requisits mínims.
     * @param recompensa Recompensa nova a crear
     * @return recompensa creada
     */
    public Recompensa crearRecompensa(Recompensa recompensa) {
        log.info("Intentant crear una nova recompensa.");
        int punts = recompensa.getPuntsRequerits();

        // Validacions de punts
        if (punts < 0) {
            throw new RuntimeException("Els punts requerits no poden ser negatius.");
        }
        if (punts > 9999) {
            throw new RuntimeException("Els punts requerits no poden superar els 9999 punts.");
        }
        if (punts % 1 != 0) {
            throw new RuntimeException("Els punts requerits han de ser un número enter.");
        }

        // Inicialització dels valors
        recompensa.setEstat(EstatRecompensa.DISPONIBLE);
        recompensa.setDataCreacio(new Date());

        Recompensa creada = recompensaRepository.save(recompensa);
        log.info("Recompensa creada correctament amb id: {}", creada.getId());
        return creada;
    }

    /**
     * Elimina una recompensa si el seu estat és DISPONIBLE.
     * @param id ID de la recompensa a eliminar
     */
    public void eliminarRecompensa(Long id) {
        log.info("Intentant eliminar la recompensa amb id: {}", id);
        Recompensa recompensa = recompensaRepository.findById(id).orElse(null);

        if (recompensa == null) {
            throw new RuntimeException("Recompensa no trobada amb id: " + id);
        }

        if (recompensa.getEstat() == EstatRecompensa.DISPONIBLE) {
            recompensaRepository.deleteById(id);
            log.info("Recompensa amb id {} eliminada correctament.", id);
        } else {
            throw new RuntimeException("No es pot eliminar la recompensa, ja que no està disponible.");
        }
    }

    /**
     * Obté el detall d’una recompensa pel seu ID.
     * @param id ID de la recompensa
     * @return objecte Recompensa si existeix
     */
    public Recompensa obtenirDetallRecompensa(Long id) {
        log.info("Sol·licitud per obtenir el detall de la recompensa amb id: {}", id);
        Optional<Recompensa> optionalRecompensa = recompensaRepository.findById(id);

        if (!optionalRecompensa.isPresent()) {
            throw new RuntimeException("Recompensa no trobada amb id: " + id);
        }

        Recompensa recompensa = optionalRecompensa.get();
        log.info("Detalls de la recompensa amb id {} obtinguts correctament.", id);
        return recompensa;
    }

    /**
     * Assigna una recompensa en estat RESERVADA, sempre que el saldo de l'usuari sigui suficient.
     * @param recompensaId ID de la recompensa
     */
    public void assignarReserva(Long recompensaId) {
        log.info("Intentant assignar la recompensa amb id: {}", recompensaId);

        // Buscar la recompensa
        Optional<Recompensa> optionalRecompensa = recompensaRepository.findById(recompensaId);
        if (!optionalRecompensa.isPresent()) {
            throw new RuntimeException("Recompensa no trobada amb id: " + recompensaId);
        }
        Recompensa recompensa = optionalRecompensa.get();

        // Verificar que l'estat sigui RESERVADA
        if (recompensa.getEstat() != EstatRecompensa.RESERVADA) {
            throw new RuntimeException("Només es poden acceptar recompenses en estat RESERVADA.");
        }

        // Comprovar que té usuari
        Usuari usuari = recompensa.getUsuari();
        if (usuari == null) {
            throw new RuntimeException("No hi ha cap usuari associat a aquesta recompensa.");
        }

        // Verificar saldo de l'usuari
        if (usuari.getSaldoPunts() < recompensa.getPuntsRequerits()) {
            throw new RuntimeException("Saldo insuficient per assignar la recompensa.");
        }

        // Descomptar punts i actualitzar estat
        usuari.setSaldoPunts(usuari.getSaldoPunts() - recompensa.getPuntsRequerits());
        recompensa.setEstat(EstatRecompensa.ASSIGNADA);
        recompensa.setDataAssignacio(new Date()); // Data d'assignació

        // Guardar els canvis
        usuariRepository.save(usuari);
        recompensaRepository.save(recompensa);

        log.info("Recompensa amb id {} assignada correctament a l'usuari {}.", recompensaId, usuari.getEmail());
    }
}