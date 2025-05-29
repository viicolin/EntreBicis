package cat.copernic.hvico.entrebicis.Usuari.Data

import cat.copernic.hvico.entrebicis.Core.Model.LoginRequest
import cat.copernic.hvico.entrebicis.Core.Model.LoginResponse
import cat.copernic.hvico.entrebicis.Core.Model.ParametresSistema
import cat.copernic.hvico.entrebicis.Core.Model.Usuari
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interfície que defineix les crides a la API REST relacionades amb els usuaris i paràmetres del sistema.
 * Aquesta interfície s'utilitza amb Retrofit per fer peticions HTTP de manera declarativa.
 */
interface UsuariApiRest {

    /**
     * Realitza el login d’un usuari amb credencials.
     *
     * @param loginRequest Conté l’email i la contrasenya.
     * @return [LoginResponse] amb les dades del token i usuari si és correcte.
     */
    @POST("auth/login") // Endpoint per fer login
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    /**
     * Consulta un usuari pel seu email (usada en algunes pantalles).
     *
     * @param email Email de l’usuari a consultar.
     * @return [Usuari] dins d’un [Response].
     */
    @GET("usuari/consultar/{email}") // Endpoint per consultar usuari (v1)
    suspend fun getUsuari(@Path("email") email: String): Response<Usuari>

    /**
     * Consulta d’un usuari pel seu email (potser versió duplicada o similar a l’anterior).
     *
     * @param email Email de l’usuari.
     * @return [Usuari] dins d’un [Response].
     */
    @GET("usuari/{email}") // Endpoint alternatiu per consultar usuari (v2)
    suspend fun consultarUsuari(@Path("email") email: String): Response<Usuari>

    /**
     * Modifica les dades d’un usuari existent.
     *
     * @param email Email de l’usuari a modificar.
     * @param usuariActualitzat Nova informació de l’usuari.
     * @return [Usuari] actualitzat dins d’un [Response].
     */
    @PUT("usuari/modificar/{email}") // Endpoint per modificar usuari
    suspend fun modificarUsuari(
        @Path("email") email: String,
        @Body usuariActualitzat: Usuari
    ): Response<Usuari>

    /**
     * Obté els paràmetres generals del sistema (com el temps màxim d’aturada).
     *
     * @return [ParametresSistema] dins d’un [Response].
     */
    @GET("parametres") // Endpoint per obtenir configuració del sistema
    suspend fun obtenirParametresSistema(): Response<ParametresSistema>
}