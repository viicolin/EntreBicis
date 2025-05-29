package hector.vico.EntreBicis.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Entitat que representa un usuari dins del sistema EntreBicis.
 * 
 * <p>Els usuaris poden tenir diferents rols (ADMIN o CICLISTA) i estan associats a rutes i recompenses.</p>
 * 
 * Es guarda a la taula <b>usuari</b> de la base de dades.
 */
@Entity
@Data  // Lombok: genera getters, setters, toString, equals i hashCode
@Table(name = "usuari")
@NoArgsConstructor  // Constructor buit per a JPA
@AllArgsConstructor // Constructor amb tots els camps
public class Usuari {

    @Id
    private String email;  // Clau primària, identificador únic de l’usuari

    private String nom;  // Nom de l'usuari

    private String cognoms;  // Cognoms de l'usuari

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNaixement;  // Data de naixement de l’usuari

    private String poblacio;  // Població o ciutat de residència

    private String telefon;  // Número de telèfon de contacte

    private String contrasenya;  // Contrasenya xifrada per accedir al sistema

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAlta;  // Data en què l’usuari es va registrar

    private int saldoPunts;  // Punts acumulats per l’usuari (serveixen per recompenses)

    @Lob  // Indica que aquest camp pot ser gran (foto guardada com a byte[])
    private byte[] foto;  // Foto de perfil de l'usuari

    @Enumerated(EnumType.STRING)
    private PermisUsuari rol;  // Rol de l’usuari: ADMIN o CICLISTA

    @Column(length = 255)
    private String observacions;  // Notes o comentaris addicionals sobre l’usuari
}