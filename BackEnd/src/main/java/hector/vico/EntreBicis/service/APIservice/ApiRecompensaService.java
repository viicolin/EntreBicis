package hector.vico.EntreBicis.service.APIservice;

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
 * Servei que gestiona les operacions relacionades amb les recompenses a través de l'API.
 * Inclou funcionalitats com llistar recompenses, reservar, consultar, i confirmar recollides.
 */
@Service
public class ApiRecompensaService {

    private static final Logger logger = LoggerFactory.getLogger(ApiRecompensaService.class);

    @Autowired
    private RecompensaRepository recompensaRepository;

    @Autowired
    private UsuariRepository usuariRepository;

    /**
     * Llista totes les recompenses que estan disponibles.
     */
    public List<Recompensa> llistarDisponibles() {
        logger.info("Llistant recompenses disponibles.");
        List<Recompensa> llista = recompensaRepository.findByEstat(EstatRecompensa.DISPONIBLE);
        logger.info("S'han trobat {} recompenses disponibles.", llista.size());
        return llista;
    }

    /**
     * Llista totes les recompenses d’un usuari que no estan en estat DISPONIBLE.
     */
    public List<Recompensa> llistarHistorialUsuari(String emailUsuari) {
        logger.info("Llistant historial de recompenses per l'usuari: {}", emailUsuari);
        List<Recompensa> historial = recompensaRepository.findByUsuariEmailAndEstatIn(
                emailUsuari,
                List.of(
                    EstatRecompensa.RESERVADA,
                    EstatRecompensa.ASSIGNADA,
                    EstatRecompensa.RECOLLIDA
                )
        );
        logger.info("S'han trobat {} recompenses a l'historial de l'usuari: {}", historial.size(), emailUsuari);
        return historial;
    }

    /**
     * Consulta una recompensa pel seu ID.
     */
    public Recompensa consultarRecompensa(Long id) {
        logger.info("Consultant recompensa amb ID: {}", id);
        Optional<Recompensa> recompensaOptional = recompensaRepository.findById(id);

        if (recompensaOptional.isPresent()) {
            logger.info("Recompensa trobada amb ID: {}", id);
            return recompensaOptional.get();
        } else {
            logger.warn("No s'ha trobat la recompensa amb ID: {}", id);
            throw new RuntimeException("Recompensa amb ID " + id + " no trobada.");
        }
    }

    /**
     * Reserva una recompensa per a un usuari, sempre que estigui disponible i tingui saldo suficient.
     */
    public Recompensa reservarRecompensa(Long recompensaId, String email) {
        logger.info("Intentant reservar recompensa amb ID: {} per l'usuari: {}", recompensaId, email);

        // Buscar recompensa
        Recompensa recompensa = recompensaRepository.findById(recompensaId).orElse(null);
        if (recompensa == null) {
            logger.warn("Recompensa amb ID {} no trobada.", recompensaId);
            throw new RuntimeException("Recompensa no trobada");
        }

        // Verificar estat disponible
        if (recompensa.getEstat() != EstatRecompensa.DISPONIBLE) {
            logger.warn("La recompensa amb ID {} no està disponible.", recompensaId);
            throw new RuntimeException("La recompensa no està disponible");
        }

        // Verificar que l’usuari no té ja una recompensa reservada
        List<Recompensa> reservades = recompensaRepository.findByUsuariEmailAndEstatIn(
            email,
            List.of(EstatRecompensa.RESERVADA)
        );
        if (!reservades.isEmpty()) {
            logger.warn("L'usuari {} ja té una recompensa reservada.", email);
            throw new RuntimeException("Ja tens una recompensa reservada");
        }

        // Buscar usuari
        Usuari usuari = usuariRepository.findByEmail(email).orElse(null);
        if (usuari == null) {
            logger.warn("Usuari amb email {} no trobat.", email);
            throw new RuntimeException("Usuari no trobat");
        }

        // Verificar saldo suficient
        if (usuari.getSaldoPunts() < recompensa.getPuntsRequerits()) {
            logger.warn("Usuari {} no té prou saldo per reservar la recompensa.", email);
            throw new RuntimeException("No tens prou saldo");
        }

        // Actualitzar usuari i recompensa
        usuari.setSaldoPunts(usuari.getSaldoPunts() - recompensa.getPuntsRequerits());
        recompensa.setEstat(EstatRecompensa.RESERVADA);
        recompensa.setUsuari(usuari);
        recompensa.setDataReserva(new Date());

        usuariRepository.save(usuari);
        logger.info("Saldo actualitzat per l'usuari: {}", email);
        Recompensa reservada = recompensaRepository.save(recompensa);
        logger.info("Recompensa amb ID {} reservada correctament per l'usuari: {}", recompensaId, email);
        return reservada;
    }

    /**
     * Retorna la recompensa assignada (pendent de recollida) d’un usuari.
     */
    public Recompensa obtenirRecompensaAssignada(String email) {
        logger.info("Buscant recompensa assignada per l'usuari: {}", email);
        List<Recompensa> llista = recompensaRepository.findByUsuariEmailAndEstatIn(
            email,
            List.of(EstatRecompensa.ASSIGNADA)
        );

        if (llista.isEmpty()) {
            logger.warn("No hi ha cap recompensa assignada per a l'usuari: {}", email);
            throw new RuntimeException("No hi ha cap recompensa assignada per a aquest usuari.");
        }

        logger.info("Recompensa assignada trobada per l'usuari: {}", email);
        return llista.get(0);  // Només es permet una recompensa assignada per usuari
    }

    /**
     * Marca una recompensa com a recollida, si estava prèviament assignada.
     */
    public Recompensa confirmarRecollida(Long recompensaId) {
        logger.info("Intentant confirmar la recollida de la recompensa amb ID: {}", recompensaId);

        Recompensa recompensa = recompensaRepository.findById(recompensaId)
            .orElseThrow(() -> {
                logger.warn("Recompensa amb ID {} no trobada per confirmar recollida.", recompensaId);
                return new RuntimeException("Recompensa no trobada");
            });

        if (recompensa.getEstat() != EstatRecompensa.ASSIGNADA) {
            logger.warn("La recompensa amb ID {} no està en estat ASSIGNADA.", recompensaId);
            throw new RuntimeException("La recompensa no està en estat ASSIGNADA");
        }

        recompensa.setEstat(EstatRecompensa.RECOLLIDA);
        recompensa.setDataRecollida(new Date());

        Recompensa recollida = recompensaRepository.save(recompensa);
        logger.info("Recollida confirmada per la recompensa amb ID: {}", recompensaId);
        return recollida;
    }
}