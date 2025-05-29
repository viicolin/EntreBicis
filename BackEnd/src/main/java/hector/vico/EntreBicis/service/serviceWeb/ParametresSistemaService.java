package hector.vico.EntreBicis.service.serviceWeb;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hector.vico.EntreBicis.entity.ParametresSistema;
import hector.vico.EntreBicis.repository.ParametresSistemaRepository;

/**
 * Servei web que gestiona els paràmetres globals del sistema EntreBicis.
 * Permet obtenir els paràmetres actuals o actualitzar-los amb validacions.
 */
@Service
public class ParametresSistemaService {

    // Logger per registrar informació i avisos
    private static final Logger logger = LoggerFactory.getLogger(ParametresSistemaService.class);

    // Valors mínims per validacions de camp
    private static final double VELOCITAT_MAXIMA_MIN = 60;
    private static final int TEMPS_ATURADA_MIN = 1;
    private static final int CONVERSIO_KM_MIN = 1;
    private static final int TEMPS_RECOLLIDA_MIN = 72;

    @Autowired
    private ParametresSistemaRepository repository;

    /**
     * Obté els paràmetres actuals del sistema.
     * Si no existeixen, en crea uns de per defecte i els desa.
     */
    public ParametresSistema obtenirParametres() {
        logger.info("Sol·licitud per obtenir els paràmetres del sistema.");
        
        List<ParametresSistema> tots = repository.findAll(); // Cerca a la base de dades

        if (tots.isEmpty()) {
            // Si no n'hi ha cap, es creen per defecte
            logger.warn("No s'han trobat paràmetres del sistema. Creant valors per defecte.");
            ParametresSistema perDefecte = new ParametresSistema();
            ParametresSistema guardat = repository.save(perDefecte);
            logger.info("Paràmetres per defecte creats i desats amb id: {}", guardat.getId());
            return guardat;
        }

        // Retorna el primer registre (només n'hauria d'haver un)
        logger.info("Paràmetres del sistema trobats amb id: {}", tots.get(0).getId());
        return tots.get(0);
    }

    /**
     * Actualitza els paràmetres del sistema amb les noves dades proporcionades,
     * fent validació dels valors mínims per evitar configuracions errònies.
     *
     * @param modificat Paràmetres nous introduïts des del formulari
     * @return Paràmetres actualitzats i desats
     * @throws IllegalArgumentException si algun valor no compleix els requisits
     */
    public ParametresSistema actualitzarParametres(ParametresSistema modificat) {
        logger.info("Sol·licitud per actualitzar els paràmetres del sistema.");

        // VALIDACIONS DE SEGURETAT per evitar valors incoherents o perillosos

        // Velocitat màxima vàlida
        if (modificat.getVelocitatMaximaValida() < VELOCITAT_MAXIMA_MIN) {
            logger.warn("Velocitat màxima vàlida inferior al mínim permès: {} km/h", modificat.getVelocitatMaximaValida());
            throw new IllegalArgumentException("La velocitat màxima vàlida no pot ser inferior a " + VELOCITAT_MAXIMA_MIN + " km/h.");
        }

        // Temps màxim d’aturada
        if (modificat.getTempsMaximAturada() < TEMPS_ATURADA_MIN) {
            logger.warn("Temps màxim d'aturada inferior al mínim permès: {} minuts", modificat.getTempsMaximAturada());
            throw new IllegalArgumentException("El temps màxim d’aturada no pot ser inferior a " + TEMPS_ATURADA_MIN + " minuts.");
        }

        // Conversió saldo/km
        if (modificat.getConversioSaldoKm() < CONVERSIO_KM_MIN) {
            logger.warn("Conversió saldo-km inferior al mínim permès: {}", modificat.getConversioSaldoKm());
            throw new IllegalArgumentException("La conversió saldo-kilòmetres no pot ser inferior a " + CONVERSIO_KM_MIN + ".");
        }

        // Temps màxim per recollir recompensa
        if (modificat.getTempsMaximRecollirRecompensa() < TEMPS_RECOLLIDA_MIN) {
            logger.warn("Temps màxim per recollir recompensa inferior al mínim permès: {} hores", modificat.getTempsMaximRecollirRecompensa());
            throw new IllegalArgumentException("El temps màxim per recollir una recompensa no pot ser inferior a " + TEMPS_RECOLLIDA_MIN + " hores.");
        }

        // Es fixa manualment l'id a 1L (per assegurar que només n’hi ha un registre)
        modificat.setId(1L);

        // Desa els nous valors a la base de dades
        ParametresSistema actualitzat = repository.save(modificat);
        logger.info("Paràmetres del sistema actualitzats correctament amb id: {}", actualitzat.getId());
        return actualitzat;
    }
}