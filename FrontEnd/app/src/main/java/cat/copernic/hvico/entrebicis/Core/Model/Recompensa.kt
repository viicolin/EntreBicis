package hector.vico.entrebicis.core.model

import cat.copernic.hvico.entrebicis.Core.Model.EstatRecompensa
import cat.copernic.hvico.entrebicis.Core.Model.Usuari

/**
 * Representa una recompensa disponible dins del sistema EntreBicis.
 *
 * @property id Identificador únic de la recompensa.
 * @property nom Nom de la recompensa (ex: "Cafè gratis").
 * @property descripcio Descripció opcional de la recompensa (pot ser null).
 * @property puntsRequerits Punts que l'usuari necessita per obtenir aquesta recompensa.
 * @property usuari Usuari que ha reservat o obtingut la recompensa (null si està disponible).
 * @property dataCreacio Data en què es va crear la recompensa (format ISO: yyyy-MM-dd).
 * @property dataReserva Data en què es va reservar (null si encara no està reservada).
 * @property dataRecollida Data en què es va recollir (null si no s'ha recollit).
 * @property dataAssignacio Data en què es va assignar definitivament a un usuari.
 * @property estat Estat actual de la recompensa (DISPONIBLE, RESERVADA, ASSIGNADA, RECOLLIDA).
 * @property puntBescambiNom Nom del punt de bescanvi (ex: "Botiga X", pot ser null).
 * @property puntBescambiAdreca Adreça del punt de bescanvi (pot ser null).
 * @property observacions Observacions addicionals sobre la recompensa (pot ser null).
 */
data class Recompensa(
    val id: Long,
    val nom: String,
    val descripcio: String?,
    val puntsRequerits: Int,
    val usuari: Usuari?,   // Pot ser null si la recompensa és DISPONIBLE
    val dataCreacio: String,
    val dataReserva: String?,
    val dataRecollida: String?,
    val dataAssignacio: String?,
    val estat: EstatRecompensa,
    val puntBescambiNom: String?,
    val puntBescambiAdreca: String?,
    val observacions: String?
)