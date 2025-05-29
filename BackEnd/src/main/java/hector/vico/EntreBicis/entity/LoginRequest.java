package hector.vico.EntreBicis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa una petició de login.
 * Aquesta classe s'utilitza per capturar les dades que l'usuari introdueix
 * quan intenta iniciar sessió al sistema.
 *
 * <p>Conté els camps següents:</p>
 * <ul>
 *   <li><b>email:</b> Correu electrònic de l'usuari.</li>
 *   <li><b>contrasenya:</b> Contrasenya de l'usuari.</li>
 * </ul>
 *
 * <p>Inclou constructors, getters, setters i mètodes equals/hashCode/toString generats per Lombok.</p>
 */
@Data  // Lombok: genera automàticament getters, setters, equals, hashCode i toString
@AllArgsConstructor  // Lombok: constructor amb tots els arguments
@NoArgsConstructor   // Lombok: constructor buit (sense arguments)
public class LoginRequest {
    private String email;        // Correu electrònic introduït per l'usuari
    private String contrasenya;  // Contrasenya introduïda per l'usuari
}