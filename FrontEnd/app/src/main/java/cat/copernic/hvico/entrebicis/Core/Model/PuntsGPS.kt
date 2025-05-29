package cat.copernic.hvico.entrebicis.Core.Model

/**
 * Representa un punt GPS registrat durant una ruta.
 *
 * @property id Identificador únic del punt GPS (pot ser nul si encara no s'ha guardat).
 * @property rutaId ID de la ruta a la qual pertany aquest punt (pot ser nul si no està assignat).
 * @property latitud Coordenada de latitud del punt GPS.
 * @property longitud Coordenada de longitud del punt GPS.
 * @property marcaTemps Marca temporal en format ISO 8601 que indica quan es va enregistrar el punt.
 */
data class PuntsGPS(
    val id: Long? = null,
    val rutaId: Long? = null,
    val latitud: Double,
    val longitud: Double,
    val marcaTemps: String
)