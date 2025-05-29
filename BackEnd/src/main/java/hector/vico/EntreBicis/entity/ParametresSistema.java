package hector.vico.EntreBicis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entitat que representa els paràmetres globals del sistema EntreBicis.
 * Aquests valors són configurables per l'administrador i afecten el funcionament general de l'aplicació.
 *
 * <p>Es guarda a la taula <b>parametres_sistema</b> de la base de dades.</p>
 * 
 * Camps destacats:
 * <ul>
 *   <li><b>velocitatMaximaValida:</b> Límit de velocitat per validar una ruta (en km/h).</li>
 *   <li><b>tempsMaximAturada:</b> Temps màxim (en minuts) que un ciclista pot estar aturat abans que es tanqui la ruta.</li>
 *   <li><b>conversioSaldoKm:</b> Nombre de quilòmetres necessaris per obtenir 1 unitat de saldo.</li>
 *   <li><b>tempsMaximRecollirRecompensa:</b> Temps màxim (en hores) per recollir una recompensa un cop assignada.</li>
 * </ul>
 */
@Entity  // Indica que aquesta classe és una entitat JPA (taula a la BBDD)
@Table(name = "parametres_sistema")  // Nom explícit de la taula
@Data  // Lombok: genera getters/setters, equals, hashCode, toString
@NoArgsConstructor  // Constructor buit per JPA
@AllArgsConstructor // Constructor amb tots els camps
public class ParametresSistema {

    @Id  // Clau primària de la taula
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Autoincremental
    private Long id;

    @Column(nullable = false)  // No pot ser null a la BBDD
    private double velocitatMaximaValida = 60;  // Límit de velocitat (ex: 60 km/h)

    @Column(nullable = false)
    private Integer tempsMaximAturada = 5;  // Temps màxim permès d’aturada (en minuts)

    @Column(nullable = false)
    private Integer conversioSaldoKm = 1;  // Conversió de km a saldo (ex: 1 saldo cada X km)

    @Column(nullable = false)
    private Integer tempsMaximRecollirRecompensa = 72;  // Temps límit per recollir una recompensa (hores)
}
