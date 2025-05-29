package cat.copernic.hvico.entrebicis.Ruta.Data

import cat.copernic.hvico.entrebicis.Core.Model.PuntsGPS
import cat.copernic.hvico.entrebicis.Core.Model.Ruta
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfície Retrofit per accedir a les funcionalitats de rutes a través de l’API REST.
 *
 * Gestiona operacions com iniciar, afegir punts, finalitzar, llistar i consultar rutes.
 */
interface RutaApiRest {

    /**
     * Inicia una nova ruta per a un usuari.
     *
     * @param email correu electrònic de l’usuari que inicia la ruta.
     * @return ruta creada amb l’estat inicial.
     */
    @POST("ruta/iniciar")
    suspend fun iniciarRuta(@Query("email") email: String): Ruta

    /**
     * Afegeix un punt GPS a una ruta concreta.
     *
     * @param idRuta identificador de la ruta a la qual s’afegeix el punt.
     * @param punt objecte [PuntsGPS] amb coordenades i temps.
     * @return el punt GPS afegit (pot incloure ID generat pel servidor).
     */
    @POST("ruta/afegir/{rutaId}")
    suspend fun afegirPuntGPS(@Path("rutaId") idRuta: Long, @Body punt: PuntsGPS): PuntsGPS

    /**
     * Finalitza una ruta i calcula estadístiques com distància, temps, velocitats...
     *
     * @param idRuta identificador de la ruta a finalitzar.
     * @return ruta actualitzada amb estat FINALITZADA i camps calculats.
     */
    @PUT("ruta/finalitzar/{rutaId}")
    suspend fun finalitzarRuta(@Path("rutaId") idRuta: Long): Ruta

    /**
     * Llista totes les rutes d’un usuari.
     *
     * @param email correu electrònic de l’usuari.
     * @return llista de rutes associades a l’usuari.
     */
    @GET("ruta/llistar")
    suspend fun llistarRutes(@Query("email") email: String): List<Ruta>

    /**
     * Consulta una ruta concreta amb tots els seus detalls.
     *
     * @param idRuta identificador de la ruta.
     * @param email correu de l’usuari (pot servir per verificar permisos).
     * @return ruta completa amb punts GPS i estadístiques.
     */
    @GET("ruta/consultar/{id}")
    suspend fun consultarRuta(@Path("id") idRuta: Long, @Query("email") email: String): Ruta
}