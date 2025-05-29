package hector.vico.EntreBicis.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entitat que representa una ruta registrada per un usuari dins del sistema EntreBicis.
 * 
 * <p>Conté informació sobre distància, temps, velocitats, estat de validació,
 * i els punts GPS que formen la ruta.</p>
 *
 * Es guarda a la taula <b>ruta</b> de la base de dades.
 */
@Entity
@Table(name = "ruta")
@Data  // Lombok: genera getters, setters, toString, equals i hashCode
@AllArgsConstructor  // Constructor amb tots els camps
@NoArgsConstructor   // Constructor buit necessari per JPA
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Identificador únic de la ruta

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuari usuari;  // Usuari que ha fet la ruta

    @Column(nullable = false)
    private EstatRuta estatRuta;  // Estat actual de la ruta (PENDENT, VALIDA o NO_VALIDADA)

    @Column(nullable = false)
    private double distanciaTotal;  // Distància total recorreguda en km

    @Column(nullable = false)
    private double tempsTotal;  // Temps total en minuts

    @Column(nullable = false)
    private double velocitatMitjana;  // Velocitat mitjana durant la ruta

    @Column(nullable = false)
    private double velocitatMaxima;  // Velocitat màxima assolida durant la ruta

    @Column(nullable = false)
    private LocalDate dataCreacio;  // Data en què es va fer la ruta

    @Column(nullable = false)
    private boolean validar;  // Indica si la ruta ha estat validada per un administrador

    @Column(nullable = false)
    private Integer saldoAtorgat;  // Saldo assignat a l'usuari per aquesta ruta (si s'ha validat)

    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL)  // Relació amb punts GPS
    @JsonManagedReference  // Ajuda a evitar referències circulars en la serialització JSON
    private List<PuntsGPS> puntsGPS;  // Llista de punts GPS associats a la ruta
}