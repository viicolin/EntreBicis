package hector.vico.EntreBicis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hector.vico.EntreBicis.entity.EstatRuta;
import hector.vico.EntreBicis.entity.Ruta;

import java.util.List;
import java.util.Optional;

/**
 * Repositori per gestionar les rutes enregistrades al sistema {@link Ruta}.
 * 
 * Proporciona mètodes per consultar rutes per usuari, estat i ordenació per data,
 * aprofitant la potència de Spring Data JPA.
 */
@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {

    /**
     * Busca una ruta concreta d'un usuari amb un estat determinat.
     * Útil per comprovar si l'usuari té una ruta en curs.
     *
     * @param email Email de l'usuari
     * @param estatRuta Estat de la ruta (ex: PENDENT)
     * @return Ruta trobada (si existeix)
     */
    Optional<Ruta> findByUsuariEmailAndEstatRuta(String email, EstatRuta estatRuta);

    /**
     * Retorna totes les rutes d’un usuari ordenades per data (de més nova a més antiga).
     *
     * @param email Email de l'usuari
     * @return Llista de rutes ordenades
     */
    List<Ruta> findByUsuariEmailOrderByDataCreacioDesc(String email);

    /**
     * Retorna totes les rutes del sistema, ordenades per data de creació descendent.
     * Útil per mostrar un historial global al panell d’administració.
     *
     * @return Llista de rutes ordenades per data
     */
    List<Ruta> findAllByOrderByDataCreacioDesc();

    /**
     * Busca una ruta pel seu ID.
     *
     * @param id ID de la ruta
     * @return Ruta trobada (si existeix)
     */
    Optional<Ruta> findById(Long id);

    /**
     * Busca una ruta concreta d’un usuari pel seu ID (seguretat extra).
     *
     * @param id ID de la ruta
     * @param email Email de l’usuari
     * @return Ruta si l’usuari és el propietari
     */
    Optional<Ruta> findByIdAndUsuariEmail(Long id, String email);

    /**
     * Retorna totes les rutes associades a un usuari (sense ordenar).
     *
     * @param email Email de l’usuari
     * @return Llista de rutes de l’usuari
     */
    List<Ruta> findByUsuariEmail(String email);
}