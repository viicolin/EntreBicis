package hector.vico.EntreBicis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Entitat que representa una recompensa dins del sistema EntreBicis.
 * 
 * Cada recompensa pot estar disponible o vinculada a un usuari, 
 * i inclou informació com punts requerits, estat, dates importants 
 * i lloc de bescanvi.
 *
 * Es guarda a la taula <b>recompenses</b> de la base de dades.
 */
@Entity
@Table(name = "recompenses")
@Data  // Lombok: getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Constructor buit per a JPA
@AllArgsConstructor // Constructor amb tots els camps
public class Recompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recompensa")
    private Long id;  // Identificador únic de la recompensa

    @Column(name = "nom", nullable = false, length = 255)
    private String nom;  // Nom de la recompensa

    @Column(name = "descripcio", columnDefinition = "TEXT")
    private String descripcio;  // Descripció detallada de la recompensa

    @Column(name = "punts_requerits", nullable = false)
    private Integer puntsRequerits;  // Punts que cal acumular per obtenir-la

    @ManyToOne
    @JoinColumn(name = "userEmail", referencedColumnName = "email")
    private Usuari usuari;  // Usuari que ha reservat o recollit la recompensa (null si és DISPONIBLE)

    @Column(name = "dataCreacio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCreacio;  // Data en què es va crear la recompensa

    @Column(name = "dataReserva")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataReserva;  // Data en què l’usuari va reservar la recompensa

    @Column(name = "dataRecollida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRecollida;  // Data en què l’usuari va recollir la recompensa

    @Column(name = "dataAssignacio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAssignacio;  // Data en què l’administrador va assignar la recompensa

    @Enumerated(EnumType.STRING)
    @Column(name = "estat", nullable = false, length = 20)
    private EstatRecompensa estat;  // Estat actual de la recompensa (DISPONIBLE, RESERVADA, ASSIGNADA, RECOLLIDA)

    @Column(name = "puntBescambi_nom", length = 255)
    private String puntBescambiNom;  // Nom del comerç o lloc on es recull la recompensa

    @Column(name = "puntBescambi_adreça", length = 255)
    private String puntBescambiAdreca;  // Adreça del punt de bescanvi

    @Column(name = "observacions", columnDefinition = "TEXT")
    private String observacions;  // Comentaris addicionals o informació extra de la recompensa
}