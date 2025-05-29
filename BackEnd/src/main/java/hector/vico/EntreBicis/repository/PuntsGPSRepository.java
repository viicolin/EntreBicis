package hector.vico.EntreBicis.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hector.vico.EntreBicis.entity.PuntsGPS;

/**
 * Repositori per gestionar l'entitat {@link PuntsGPS}.
 * 
 * Aquesta interfície permet realitzar operacions CRUD i consultes relacionades amb els punts GPS
 * associats a una ruta concreta.
 * 
 * Estén {@link JpaRepository}, la qual cosa proporciona mètodes com:
 * <ul>
 *   <li><code>findAll()</code></li>
 *   <li><code>findById(Long id)</code></li>
 *   <li><code>save(PuntsGPS punt)</code></li>
 *   <li><code>deleteById(Long id)</code></li>
 * </ul>
 */
@Repository  // Marca aquesta interfície com un component Spring de tipus repositori
public interface PuntsGPSRepository extends JpaRepository<PuntsGPS, Long> {

    /**
     * Retorna la llista de punts GPS associats a una ruta concreta.
     *
     * @param rutaId ID de la ruta
     * @return Llista de punts GPS que pertanyen a la ruta indicada
     */
    List<PuntsGPS> findByRutaId(Long rutaId);  // Consulta personalitzada per ruta
}