package cat.copernic.hvico.entrebicis.Ruta.Domain

import cat.copernic.hvico.entrebicis.Core.Model.PuntsGPS
import cat.copernic.hvico.entrebicis.Core.Model.Ruta
import cat.copernic.hvico.entrebicis.Ruta.Data.RutaRepository

/**
 * Casos d’ús per a la gestió de rutes dins de l’aplicació EntreBicis.
 *
 * Aquesta classe representa la capa de negoci i actua com a intermediària
 * entre el ViewModel i el repositori de dades.
 *
 * @param repository instància de [RutaRepository] que proporciona l’accés a les dades de rutes.
 */
class RutaUseCases(private val repository: RutaRepository) {

    /**
     * Inicia una nova ruta per a un usuari.
     *
     * @param email correu electrònic de l’usuari.
     * @return la ruta iniciada.
     */
    suspend fun iniciarRuta(email: String): Ruta = repository.iniciarRuta(email)

    /**
     * Afegeix un punt GPS a una ruta específica.
     *
     * @param idRuta identificador de la ruta.
     * @param punt objecte [PuntsGPS] amb coordenades i temps.
     * @return el punt GPS afegit.
     */
    suspend fun afegirPuntGPS(idRuta: Long, punt: PuntsGPS): PuntsGPS = repository.afegirPuntGPS(idRuta, punt)

    /**
     * Finalitza una ruta i calcula les dades associades.
     *
     * @param idRuta identificador de la ruta.
     * @return la ruta finalitzada amb dades actualitzades.
     */
    suspend fun finalitzarRuta(idRuta: Long): Ruta = repository.finalitzarRuta(idRuta)

    /**
     * Llista totes les rutes associades a un usuari.
     *
     * @param email correu electrònic de l’usuari.
     * @return llista de rutes.
     */
    suspend fun llistarRutes(email: String): List<Ruta> = repository.llistarRutes(email)

    /**
     * Consulta els detalls d’una ruta concreta.
     *
     * @param id identificador de la ruta.
     * @param email correu de l’usuari (per verificar permisos).
     * @return la ruta amb tots els detalls.
     */
    suspend fun consultarRuta(id: Long, email: String): Ruta = repository.consultarRuta(id, email)
}