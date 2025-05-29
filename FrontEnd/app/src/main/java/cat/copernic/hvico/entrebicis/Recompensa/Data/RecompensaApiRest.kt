package cat.copernic.hvico.entrebicis.Recompensa.Data

import hector.vico.entrebicis.core.model.Recompensa
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfície Retrofit per accedir a l'API REST de recompenses.
 * Defineix totes les crides HTTP relacionades amb les recompenses de l'app EntreBicis.
 */
interface RecompensaApiRest {

    /**
     * Obté totes les recompenses disponibles (estat DISPONIBLE).
     * @return Llista de recompenses que es poden reservar.
     */
    @GET("recompenses/disponibles")
    suspend fun getRecompensesDisponibles(): List<Recompensa>

    /**
     * Obté l'historial de recompenses d'un usuari.
     *
     * @param email Email de l'usuari.
     * @return Llista de recompenses que ha tingut l’usuari (reservades, assignades, recollides).
     */
    @GET("recompenses/historial/{email}")
    suspend fun getHistorialRecompenses(@Path("email") email: String): List<Recompensa>

    /**
     * Consulta els detalls d'una recompensa específica.
     *
     * @param id ID de la recompensa a consultar.
     * @return Recompensa amb tota la informació.
     */
    @GET("recompenses/consultar/{id}")
    suspend fun consultarRecompensa(@Path("id") id: Long): Recompensa

    /**
     * Reserva una recompensa per a un usuari determinat.
     *
     * @param id ID de la recompensa que es vol reservar.
     * @param email Email de l'usuari que fa la reserva.
     * @return Resposta amb l'estat de la recompensa després de reservar.
     */
    @POST("recompenses/reservar/{id}")
    suspend fun reservarRecompensa(
        @Path("id") id: Long,
        @Query("email") email: String
    ): Response<Recompensa>

    /**
     * Obté la recompensa assignada actualment a un usuari (si n’hi ha).
     *
     * @param email Email de l’usuari.
     * @return Recompensa en estat ASSIGNADA (si existeix).
     */
    @GET("recompenses/assignada/{email}")
    suspend fun getRecompensaAssignada(@Path("email") email: String): Recompensa

    /**
     * Confirma la recollida física d’una recompensa prèviament assignada.
     *
     * @param id ID de la recompensa que es confirma com a recollida.
     * @return Resposta amb la recompensa actualitzada (estat RECOLLIDA).
     */
    @POST("recompenses/confirmar-recollida/{id}")
    suspend fun confirmarRecollida(@Path("id") id: Long): Response<Recompensa>
}