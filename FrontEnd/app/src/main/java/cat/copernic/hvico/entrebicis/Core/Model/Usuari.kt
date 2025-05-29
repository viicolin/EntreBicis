package cat.copernic.hvico.entrebicis.Core.Model

/**
 * Representa un usuari del sistema EntreBicis, ja sigui ciclista o administrador.
 *
 * @property contrasenya Contrasenya codificada de l'usuari.
 * @property nom Nom de l'usuari.
 * @property cognoms Cognoms de l'usuari.
 * @property email Correu electrònic de l'usuari (actua com a identificador principal).
 * @property dataNaixement Data de naixement de l'usuari (format ISO: yyyy-MM-dd).
 * @property telefon Número de telèfon de l'usuari (ha de tenir 9 dígits).
 * @property poblacio Ciutat o poble de residència de l'usuari.
 * @property dataAlta Data d'alta o registre al sistema (format ISO: yyyy-MM-dd).
 * @property rol Rol de l'usuari (CICLISTA o ADMIN).
 * @property saldoPunts Quantitat actual de punts disponibles a l'usuari.
 * @property foto Ruta o URL de la foto de perfil (opcional).
 */
data class Usuari(
    val contrasenya: String,
    val nom: String,
    val cognoms: String,
    val email: String,
    val dataNaixement: String,
    val telefon: String,
    val poblacio: String,
    val dataAlta: String,
    val rol: PermisUsuari,
    val saldoPunts: Int?,
    val foto: String? = null
)