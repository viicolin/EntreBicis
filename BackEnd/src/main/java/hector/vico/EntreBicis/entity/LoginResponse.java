package hector.vico.EntreBicis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe que representa la resposta després d’un login correcte.
 * Aquesta informació és retornada al client (web o app) després de verificar les credencials.
 *
 * <p>Conté els camps següents:</p>
 * <ul>
 *   <li><b>email:</b> Correu electrònic de l'usuari identificat.</li>
 *   <li><b>nom:</b> Nom de l'usuari.</li>
 *   <li><b>rol:</b> Rol de l'usuari (per exemple: "ADMIN", "CICLISTA").</li>
 * </ul>
 *
 * <p>Els mètodes com getters, setters, equals, hashCode i toString es generen automàticament amb Lombok.</p>
 */
@Data  // Lombok: genera getters, setters, equals, hashCode i toString
@AllArgsConstructor  // Constructor amb tots els camps
@NoArgsConstructor   // Constructor buit
public class LoginResponse {
    private String email;  // Correu electrònic de l’usuari autenticat
    private String nom;    // Nom complet o nom visible de l’usuari
    private String rol;    // Rol assignat a l’usuari (ex: "ADMIN" o "CICLISTA")
}