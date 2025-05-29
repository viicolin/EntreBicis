package cat.copernic.hvico.entrebicis.Core.Model

/**
 * Representa una ruta feta per un usuari dins l'aplicació EntreBicis.
 *
 * @property id Identificador únic de la ruta (generat automàticament, pot ser null si encara no està guardada).
 * @property usuari Usuari que ha realitzat la ruta (pot ser null en certs contexts).
 * @property estatRuta Estat actual de la ruta (PENDENT, VALIDA, NO_VALIDADA).
 * @property distanciaTotal Distància total recorreguda en la ruta, en quilòmetres.
 * @property tempsTotal Temps total invertit en fer la ruta, en segons.
 * @property velocitatMitjana Velocitat mitjana durant la ruta (en km/h).
 * @property velocitatMaxima Velocitat màxima registrada durant la ruta (en km/h).
 * @property dataCreacio Data de creació de la ruta (en format ISO: yyyy-MM-dd).
 * @property saldoAtorgat Punts que es poden atorgar per aquesta ruta (pot ser null si no s’ha validat).
 * @property validar Indica si la ruta ha estat validada per un administrador.
 * @property puntsGPS Llista de punts GPS registrats durant la ruta.
 */
data class Ruta(
    val id: Long? = null,
    val usuari: Usuari? = null,
    val estatRuta: EstatRuta = EstatRuta.PENDENT,
    val distanciaTotal: Double = 0.0,
    val tempsTotal: Double = 0.0,
    val velocitatMitjana: Double = 0.0,
    val velocitatMaxima: Double = 0.0,
    val dataCreacio: String,
    val saldoAtorgat: Int? = null,
    val validar: Boolean = false,
    val puntsGPS: List<PuntsGPS> = emptyList()
)