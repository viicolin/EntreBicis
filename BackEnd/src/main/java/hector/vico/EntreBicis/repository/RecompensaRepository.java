package hector.vico.EntreBicis.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hector.vico.EntreBicis.entity.EstatRecompensa;
import hector.vico.EntreBicis.entity.Recompensa;

/**
 * Repositori per a l'entitat {@link Recompensa}.
 *
 * Permet accedir i manipular les recompenses emmagatzemades a la base de dades.
 * Estén {@link JpaRepository}, que proporciona operacions CRUD bàsiques.
 *
 * Inclou consultes personalitzades per filtrar recompenses segons l'usuari o l'estat.
 */
@Repository
public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {

    /**
     * Retorna totes les recompenses associades a un usuari específic.
     *
     * @param email Email de l'usuari
     * @return Llista de recompenses d'aquest usuari
     */
    List<Recompensa> findByUsuariEmail(String email);

    /**
     * Busca una recompensa pel seu ID.
     *
     * @param id ID de la recompensa
     * @return Opcional amb la recompensa si existeix
     */
    Optional<Recompensa> findById(Long id);

    /**
     * Retorna totes les recompenses que estan en un estat determinat.
     *
     * @param estat Estat de la recompensa (DISPONIBLE, RESERVADA, etc.)
     * @return Llista de recompenses amb aquest estat
     */
    List<Recompensa> findByEstat(EstatRecompensa estat);

    /**
     * Retorna totes les recompenses d'un usuari que es troben en una llista d'estats.
     * Molt útil per comprovar si un usuari ja té una recompensa reservada, assignada o recollida.
     *
     * @param email Email de l'usuari
     * @param estats Llista d'estats a considerar
     * @return Llista de recompenses que compleixen les condicions
     */
    List<Recompensa> findByUsuariEmailAndEstatIn(String email, List<EstatRecompensa> estats);
}