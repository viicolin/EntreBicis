package hector.vico.EntreBicis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hector.vico.EntreBicis.entity.Usuari;

/**
 * Repositori per a l'entitat {@link Usuari}.
 *
 * Gestiona l'accés a la base de dades per a operacions relacionades amb els usuaris,
 * com buscar per email o executar operacions CRUD.
 *
 * Estén {@link JpaRepository}, que proporciona mètodes com:
 * <ul>
 *   <li><code>findAll()</code></li>
 *   <li><code>findById(String id)</code></li>
 *   <li><code>save(Usuari usuari)</code></li>
 *   <li><code>deleteById(String id)</code></li>
 * </ul>
 */
@Repository
public interface UsuariRepository extends JpaRepository<Usuari, String> {

    /**
     * Cerca un usuari pel seu correu electrònic.
     * Aquesta consulta és útil tant per a login com per accedir al perfil.
     *
     * @param email Correu electrònic de l'usuari
     * @return {@link Optional} amb l'usuari si existeix
     */
    Optional<Usuari> findByEmail(String email);
}