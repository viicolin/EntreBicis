package cat.copernic.hvico.entrebicis.Core.Model

/**
 * Representa els paràmetres de configuració del sistema EntreBicis.
 *
 * Aquests valors són definits per l'administrador i afecten al comportament
 * de les funcionalitats com la validació de rutes o la reserva de recompenses.
 *
 * @property id Identificador únic del conjunt de paràmetres.
 * @property velocitatMaximaValida Velocitat màxima permesa en km/h perquè una ruta sigui considerada vàlida.
 * @property tempsMaximAturada Temps màxim permès en minuts que un ciclista pot estar aturat durant una ruta.
 * @property conversioSaldoKm Conversió de quilòmetres recorreguts a saldo (punts).
 * @property tempsMaximRecollirRecompensa Temps màxim permès en hores per recollir una recompensa reservada.
 */
data class ParametresSistema(
    val id: Long,
    val velocitatMaximaValida: Double,
    val tempsMaximAturada: Int,
    val conversioSaldoKm: Int,
    val tempsMaximRecollirRecompensa: Int
)