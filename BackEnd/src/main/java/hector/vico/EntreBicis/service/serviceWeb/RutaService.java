package hector.vico.EntreBicis.service.serviceWeb;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hector.vico.EntreBicis.entity.EstatRuta;
import hector.vico.EntreBicis.entity.Ruta;
import hector.vico.EntreBicis.entity.Usuari;
import hector.vico.EntreBicis.repository.RutaRepository;
import hector.vico.EntreBicis.repository.UsuariRepository;

/**
 * Servei per gestionar les rutes al panell d'administració web.
 */
@Service
public class RutaService {

    private static final Logger logger = LoggerFactory.getLogger(RutaService.class);

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private UsuariRepository usuariRepository;

    /**
     * Obté totes les rutes ordenades per data de creació descendent.
     * @return Llista de totes les rutes
     */
    public List<Ruta> obtenirTotesLesRutes() {
        logger.info("Sol·licitud per obtenir totes les rutes.");
        List<Ruta> rutes = rutaRepository.findAllByOrderByDataCreacioDesc();
        logger.info("S'han trobat {} rutes.", rutes.size());
        return rutes;
    }

    /**
     * Consulta una ruta a partir del seu identificador.
     * @param id Identificador de la ruta
     * @return Ruta trobada
     * @throws RuntimeException si no es troba la ruta
     */
    public Ruta consultarRutaPerId(Long id) {
        logger.info("Consultant ruta amb id: {}", id);
        return rutaRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Ruta no trobada amb id: {}", id);
                return new RuntimeException("Ruta no trobada");
            });
    }

    /**
     * Obté totes les rutes d’un usuari pel seu email.
     * @param email Correu electrònic de l'usuari
     * @return Llista de rutes d’aquest usuari
     */
    public List<Ruta> obtenirRutesPerEmail(String email) {
        logger.info("Sol·licitud per obtenir rutes de l'usuari: {}", email);
        List<Ruta> rutes = rutaRepository.findByUsuariEmail(email);

        if (rutes.isEmpty()) {
            logger.warn("L'usuari {} encara no ha fet cap ruta.", email);
            throw new RuntimeException("Aquest usuari encara no ha fet cap ruta.");
        }

        logger.info("S'han trobat {} rutes per l'usuari: {}", rutes.size(), email);
        return rutes;
    }

    /**
     * Valida una ruta (canvia l'estat a VALIDA) i suma el saldo al compte del ciclista.
     * Només es pot validar si estava en estat NO_VALIDADA.
     * @param idRuta Identificador de la ruta
     */
    public void validarRuta(Long idRuta) {
        logger.info("Intentant validar la ruta amb id: {}", idRuta);
        Ruta ruta = consultarRutaPerId(idRuta);

        // Només si està en estat NO_VALIDADA
        if (ruta != null && ruta.getEstatRuta() == EstatRuta.NO_VALIDADA) {
            ruta.setEstatRuta(EstatRuta.VALIDA); // Canvi d'estat
            ruta.setValidar(true); // Marquem com validada

            Usuari usuari = ruta.getUsuari();
            // Suma del saldo
            usuari.setSaldoPunts(usuari.getSaldoPunts() + ruta.getSaldoAtorgat());

            usuariRepository.save(usuari);
            rutaRepository.save(ruta);
            logger.info("Ruta amb id {} validada correctament i saldo atorgat a l'usuari {}.", idRuta, usuari.getEmail());
        }
    }

    /**
     * Invalidar una ruta (canvia l'estat a NO_VALIDADA) i resta el saldo de l'usuari.
     * Només es pot invalidar si estava en estat VALIDA i si el saldo del ciclista ho permet.
     * @param idRuta Identificador de la ruta
     * @throws IllegalStateException si el saldo del ciclista és insuficient
     */
    public void invalidarRuta(Long idRuta) {
        logger.info("Intentant invalidar la ruta amb id: {}", idRuta);
        Ruta ruta = consultarRutaPerId(idRuta);

        // Només si estava prèviament validada
        if (ruta != null && ruta.getEstatRuta() == EstatRuta.VALIDA) {
            Usuari usuari = ruta.getUsuari();

            // Només es pot invalidar si té saldo suficient per descomptar
            if (usuari.getSaldoPunts() >= ruta.getSaldoAtorgat()) {
                usuari.setSaldoPunts(usuari.getSaldoPunts() - ruta.getSaldoAtorgat()); // Resta
                ruta.setEstatRuta(EstatRuta.NO_VALIDADA); // Canvi d'estat
                ruta.setValidar(false); // Marca com no validada

                usuariRepository.save(usuari);
                rutaRepository.save(ruta);
                logger.info("Ruta amb id {} invalidada correctament i saldo descomptat a l'usuari {}.", idRuta, usuari.getEmail());
            } else {
                logger.error("El saldo del ciclista {} és insuficient per descomptar {} punts.",
                             usuari.getEmail(), ruta.getSaldoAtorgat());
                throw new IllegalStateException("El saldo del ciclista és insuficient.");
            }
        }
    }
}