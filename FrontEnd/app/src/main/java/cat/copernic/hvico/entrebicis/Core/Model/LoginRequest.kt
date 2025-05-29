package cat.copernic.hvico.entrebicis.Core.Model

/**
 * Dades necessàries per fer una petició de login a l'API.
 *
 * Aquesta classe representa el cos (body) que s'envia a l'endpoint d'autenticació.
 *
 * @property email Correu electrònic de l'usuari.
 * @property contrasenya Contrasenya associada al compte de l'usuari.
 */
data class LoginRequest(
    val email: String,
    val contrasenya: String
)