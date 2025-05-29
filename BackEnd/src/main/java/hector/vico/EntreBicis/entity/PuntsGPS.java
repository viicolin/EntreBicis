package hector.vico.EntreBicis.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entitat que representa un punt GPS registrat durant una ruta en el sistema EntreBicis.
 * Cada punt conté latitud, longitud i el moment exacte en què es va registrar.
 *
 * <p>Es relaciona amb la taula <b>puntsgps</b> de la base de dades.</p>
 *
 * <p>Relació: molts punts GPS pertanyen a una sola ruta ({@link Ruta}).</p>
 */
@Entity  // Marca aquesta classe com a entitat JPA (persistida a BBDD)
@Table(name = "puntsgps")  // Defineix el nom explícit de la taula
@Data  // Lombok: crea automàticament getters, setters, toString, equals i hashCode
@AllArgsConstructor  // Constructor amb tots els camps
@NoArgsConstructor   // Constructor buit necessari per a JPA
public class PuntsGPS {

    @Id  // Clau primària
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Autoincremental
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // Molts punts GPS poden estar associats a una mateixa ruta
    @JoinColumn(name = "ruta_id", nullable = false)  // Clau forana cap a la taula 'ruta'
    @JsonBackReference  // Evita referències circulars en serialització JSON
    private Ruta ruta;  // Ruta a la qual pertany aquest punt GPS

    @Column(nullable = false)
    private double latitud;  // Coordenada de latitud del punt GPS

    @Column(nullable = false)
    private double longitud;  // Coordenada de longitud del punt GPS

    @Column(name = "marca_temps", nullable = false)
    private LocalDateTime marcaTemps;  // Moment exacte en què es va enregistrar el punt GPS
}