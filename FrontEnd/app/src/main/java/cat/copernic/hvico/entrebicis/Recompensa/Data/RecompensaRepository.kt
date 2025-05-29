package cat.copernic.hvico.entrebicis.Recompensa.Data

import hector.vico.entrebicis.core.model.Recompensa
import retrofit2.Response

/**
 * Repositori de dades per accedir a les operacions relacionades amb recompenses.
 * Aquesta classe encapsula les crides a l'API REST i permet mantenir una separació
 * entre la capa de dades i la lògica de negoci (UseCases o ViewModel).
 */
class RecompensaRepository {

    /**
     * Obté totes les recompenses disponibles (amb estat DISPONIBLE).
     * @return Llista de recompenses disponibles.
     */
    suspend fun getRecompensesDisponibles(): List<Recompensa> {
        return RetrofitInstance.api.getRecompensesDisponibles()
    }

    /**
     * Obté l’historial de recompenses d’un usuari específic.
     *
     * @param email Email de l’usuari.
     * @return Llista de recompenses de l’usuari (reservades, assignades, recollides).
     */
    suspend fun getHistorialRecompenses(email: String): List<Recompensa> {
        return RetrofitInstance.api.getHistorialRecompenses(email)
    }

    /**
     * Consulta una recompensa pel seu identificador.
     *
     * @param id ID de la recompensa.
     * @return Recompensa amb els detalls corresponents.
     */
    suspend fun consultarRecompensa(id: Long): Recompensa {
        return RetrofitInstance.api.consultarRecompensa(id)
    }

    /**
     * Reserva una recompensa per a un usuari concret.
     *
     * @param id ID de la recompensa a reservar.
     * @param email Email de l’usuari que fa la reserva.
     * @return Resposta amb la recompensa actualitzada o error si falla.
     */
    suspend fun reservarRecompensa(id: Long, email: String): Response<Recompensa> {
        return RetrofitInstance.api.reservarRecompensa(id, email)
    }

    /**
     * Obté la recompensa actualment assignada a un usuari, si n’hi ha.
     *
     * @param email Email de l’usuari.
     * @return Recompensa assignada (estat ASSIGNADA).
     */
    suspend fun getRecompensaAssignada(email: String): Recompensa {
        return RetrofitInstance.api.getRecompensaAssignada(email)
    }

    /**
     * Confirma que una recompensa ha estat recollida físicament.
     *
     * @param id ID de la recompensa a confirmar com a recollida.
     * @return Resposta amb la recompensa en estat actualitzat (RECOLLIDA).
     */
    suspend fun confirmarRecollida(id: Long): Response<Recompensa> {
        return RetrofitInstance.api.confirmarRecollida(id)
    }
}