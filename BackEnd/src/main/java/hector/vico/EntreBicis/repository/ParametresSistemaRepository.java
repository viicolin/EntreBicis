package hector.vico.EntreBicis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import hector.vico.EntreBicis.entity.ParametresSistema;

/**
 * Repositori de l'entitat {@link ParametresSistema}.
 * 
 * Aquesta interfície proporciona accés a operacions CRUD (crear, llegir, actualitzar, eliminar)
 * sobre la taula <b>parametres_sistema</b>, sense necessitat d'implementar-les manualment.
 * 
 * Estén JpaRepository, que ja inclou mètodes com:
 * <ul>
 *   <li><code>findAll()</code></li>
 *   <li><code>findById(Long id)</code></li>
 *   <li><code>save(ParametresSistema parametre)</code></li>
 *   <li><code>deleteById(Long id)</code></li>
 * </ul>
 */
@Repository  // Indica que aquesta interfície és un component Spring de tipus repositori
public interface ParametresSistemaRepository extends JpaRepository<ParametresSistema, Long> {
    // No cal definir res addicional si només es volen operacions bàsiques
}