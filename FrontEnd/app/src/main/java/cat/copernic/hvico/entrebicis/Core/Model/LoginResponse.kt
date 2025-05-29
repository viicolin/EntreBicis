package cat.copernic.hvico.entrebicis.Core.Model

/**
 * Dades que es reben com a resposta d'una petició de login correcta.
 *
 * Aquesta classe representa la informació de l'usuari retornada pel servidor
 * després d'una autenticació satisfactòria.
 *
 * @property email Correu electrònic de l'usuari autenticat.
 * @property nom Nom de l'usuari.
 * @property rol Permís o rol de l'usuari (per exemple, CICLISTA o ADMIN).
 */
data class LoginResponse(
    val email: String,
    val nom: String,
    val rol: PermisUsuari
)