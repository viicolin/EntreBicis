package hector.vico.EntreBicis.service.APIservice;

import hector.vico.EntreBicis.entity.*;
import hector.vico.EntreBicis.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servei API que gestiona la lògica relacionada amb les rutes: iniciar, afegir punts GPS,
 * finalitzar ruta i consultar dades.
 */
@Service
public class ApiRutaService {

    private static final Logger logger = LoggerFactory.getLogger(ApiRutaService.class);

    @Autowired private RutaRepository rutaRepository;
    @Autowired private UsuariRepository usuariRepository;
    @Autowired private PuntsGPSRepository puntsGPSRepository;
    @Autowired private ParametresSistemaRepository parametresSistemaRepository;

    /**
     * Inicia una nova ruta per un usuari si no té cap pendent.
     */
    public Ruta iniciarRuta(String email) {
        logger.info("Intentant iniciar una nova ruta per l'usuari: {}", email);

        // Comprovem si ja té una ruta pendent (no permetem tenir més d'una a la vegada)
        if (rutaRepository.findByUsuariEmailAndEstatRuta(email, EstatRuta.PENDENT).isPresent()) {
            logger.warn("L'usuari {} ja té una ruta pendent.", email);
            throw new RuntimeException("Ja tens una ruta pendent.");
        }

        // Cerquem l'usuari per email
        Usuari usuari = usuariRepository.findByEmail(email).orElse(null);
        if (usuari == null) {
            logger.warn("Usuari no trobat amb email: {}", email);
            throw new RuntimeException("Usuari no trobat.");
        }

        // Es crea una nova ruta buida amb valors per defecte
        Ruta novaRuta = new Ruta();
        novaRuta.setUsuari(usuari);
        novaRuta.setEstatRuta(EstatRuta.PENDENT); // Estat inicial
        novaRuta.setDataCreacio(LocalDate.now()); // Data actual
        novaRuta.setValidar(false);               // Encara no està validada
        novaRuta.setDistanciaTotal(0);
        novaRuta.setTempsTotal(0);
        novaRuta.setVelocitatMaxima(0);
        novaRuta.setVelocitatMitjana(0);
        novaRuta.setSaldoAtorgat(0);
        novaRuta.setPuntsGPS(List.of()); // Encara sense punts

        // Desa la ruta a la base de dades
        Ruta rutaGuardada = rutaRepository.save(novaRuta);
        logger.info("Ruta iniciada correctament amb id: {} per l'usuari: {}", rutaGuardada.getId(), email);
        return rutaGuardada;
    }

    /**
     * Afegeix un nou punt GPS a la ruta indicada.
     */
    public PuntsGPS afegirPuntGPS(Long rutaId, PuntsGPS punt) {
        logger.info("Afegint punt GPS a la ruta amb id: {}", rutaId);

        // Comprovem si existeix la ruta
        Ruta ruta = rutaRepository.findById(rutaId).orElseThrow(() -> {
            logger.warn("No s'ha trobat la ruta amb id: {}", rutaId);
            return new RuntimeException("Ruta no trobada");
        });

        // Assignem la ruta i la marca de temps actual al punt
        punt.setRuta(ruta);
        punt.setMarcaTemps(LocalDateTime.now());

        // Guardem el punt GPS
        PuntsGPS puntAfegit = puntsGPSRepository.save(punt);
        logger.info("Punt GPS afegit correctament a la ruta amb id: {}", rutaId);
        return puntAfegit;
    }

    /**
     * Finalitza una ruta: calcula distància, velocitat, temps i saldo atorgat.
     */
    public Ruta finalitzarRuta(Long rutaId) {
        logger.info("Intentant finalitzar la ruta amb id: {}", rutaId);

        // Obtenim la ruta
        Ruta ruta = rutaRepository.findById(rutaId).orElseThrow(() -> {
            logger.warn("No s'ha trobat la ruta amb id: {}", rutaId);
            return new RuntimeException("Ruta no trobada.");
        });

        // Comprovem que encara estigui pendent
        if (ruta.getEstatRuta() != EstatRuta.PENDENT) {
            logger.warn("La ruta amb id {} no està en estat pendent.", rutaId);
            throw new RuntimeException("La ruta no es troba en estat pendent.");
        }

        // Obtenim tots els punts GPS de la ruta
        List<PuntsGPS> punts = puntsGPSRepository.findByRutaId(rutaId);

        // Si no hi ha punts, marquem la ruta com NO_VALIDADA
        if (punts.isEmpty()) {
            logger.warn("La ruta amb id {} no té punts GPS.", rutaId);
            ruta.setDistanciaTotal(0);
            ruta.setTempsTotal(0);
            ruta.setVelocitatMitjana(0);
            ruta.setVelocitatMaxima(0);
            ruta.setSaldoAtorgat(0);
            ruta.setEstatRuta(EstatRuta.NO_VALIDADA);
            ruta.setValidar(false);
            return rutaRepository.save(ruta);
        }

        // Calculem el temps total entre el primer i l'últim punt
        LocalDateTime inici = punts.get(0).getMarcaTemps();
        LocalDateTime fi = punts.get(punts.size() - 1).getMarcaTemps();
        long segonsTotals = Duration.between(inici, fi).getSeconds();

        double distanciaTotalKm = 0;
        double velocitatMaxima = 0;

        // Recorrem els punts i calculem la distància i velocitat màxima entre trams
        for (int i = 1; i < punts.size(); i++) {
            double distancia = calcularDistanciaEntrePunts(punts.get(i - 1), punts.get(i));
            long segonsTram = Duration.between(punts.get(i - 1).getMarcaTemps(), punts.get(i).getMarcaTemps()).getSeconds();

            if (distancia < 0.001 || segonsTram < 1) continue; // Evitem errors

            distanciaTotalKm += distancia;

            double horesTram = segonsTram / 3600.0;
            double velocitatTram = distancia / horesTram;

            if (velocitatTram > velocitatMaxima) {
                velocitatMaxima = velocitatTram;
            }
        }

        // Calculem la velocitat mitjana de tota la ruta
        double horesTotals = segonsTotals / 3600.0;
        double velocitatMitjana = horesTotals > 0 ? distanciaTotalKm / horesTotals : 0;

        // Obtenim el paràmetre del sistema per calcular saldo
        ParametresSistema parametres = parametresSistemaRepository.findAll().get(0);
        int conversio = parametres.getConversioSaldoKm();
        int saldoAtorgat = (int) (distanciaTotalKm * conversio);

        // Assignem els valors finals a la ruta
        ruta.setDistanciaTotal(distanciaTotalKm);
        ruta.setTempsTotal((int) segonsTotals);
        ruta.setVelocitatMitjana(velocitatMitjana);
        ruta.setVelocitatMaxima(velocitatMaxima);
        ruta.setSaldoAtorgat(saldoAtorgat);
        ruta.setEstatRuta(EstatRuta.NO_VALIDADA); // Esperant validació manual
        ruta.setValidar(false);

        Ruta rutaFinalitzada = rutaRepository.save(ruta);
        logger.info("Ruta amb id {} finalitzada. Distància: {} km, Temps: {} s, VelMitja: {}, VelMax: {}, Saldo: {}",
                rutaId, distanciaTotalKm, segonsTotals, velocitatMitjana, velocitatMaxima, saldoAtorgat);
        return rutaFinalitzada;
    }

    /**
     * Fórmula Haversine per calcular la distància entre dos punts GPS.
     */
    private double calcularDistanciaEntrePunts(PuntsGPS p1, PuntsGPS p2) {
        final int R = 6371; // Radi de la Terra en km

        double lat1 = Math.toRadians(p1.getLatitud());
        double lon1 = Math.toRadians(p1.getLongitud());
        double lat2 = Math.toRadians(p2.getLatitud());
        double lon2 = Math.toRadians(p2.getLongitud());

        double difLat = lat2 - lat1;
        double difLon = lon2 - lon1;

        double a = Math.sin(difLat / 2) * Math.sin(difLat / 2)
                 + Math.cos(lat1) * Math.cos(lat2) * Math.sin(difLon / 2) * Math.sin(difLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distancia = R * c;
        logger.debug("Distància calculada entre punts: {} km", distancia);
        return distancia;
    }

    /**
     * Obté totes les rutes registrades per un usuari, ordenades per data.
     */
    public List<Ruta> obtenirRutesPerUsuari(String email) {
        logger.info("Obtenint rutes per l'usuari: {}", email);
        List<Ruta> rutes = rutaRepository.findByUsuariEmailOrderByDataCreacioDesc(email);
        logger.info("S'han trobat {} rutes per l'usuari: {}", rutes.size(), email);
        return rutes;
    }

    /**
     * Consulta una ruta concreta per ID i usuari (seguretat: ha de ser propietari).
     */
    public Ruta consultarRutaPerIdIEmail(Long id, String email) {
        logger.info("Consultant ruta amb id: {} per l'usuari: {}", id, email);
        return rutaRepository.findByIdAndUsuariEmail(id, email)
            .orElseThrow(() -> {
                logger.warn("No s'ha trobat la ruta amb id {} per l'usuari: {}", id, email);
                return new RuntimeException("Ruta no trobada o no accessible");
            });
    }
}