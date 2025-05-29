package cat.copernic.hvico.entrebicis.Ruta.Data

import cat.copernic.hvico.entrebicis.Core.Model.PuntsGPS
import cat.copernic.hvico.entrebicis.Core.Model.Ruta

/**
 * Repositori encarregat de gestionar les operacions de xarxa relacionades amb les rutes.
 *
 * Aquesta classe encapsula les crides a l’API REST proporcionada per [RetrofitInstance].
 * Forma part de la capa de dades i s’utilitza des dels UseCases.
 */
class RutaRepository {

    /**
     * Inicia una nova ruta per a l’usuari especificat.
     *
     * @param email correu electrònic de l’usuari que inicia la ruta.
     * @return ruta inicialitzada amb ID i estat inicial.
     */
    suspend fun iniciarRuta(email: String): Ruta {
        return RetrofitInstance.api.iniciarRuta(email)
    }

    /**
     * Afegeix un punt GPS a una ruta en curs.
     *
     * @param idRuta identificador de la ruta.
     * @param punt objecte amb coordenades i marca de temps.
     * @return el punt GPS afegit (pot incloure ID o confirmació).
     */
    suspend fun afegirPuntGPS(idRuta: Long, punt: PuntsGPS): PuntsGPS {
        return RetrofitInstance.api.afegirPuntGPS(idRuta, punt)
    }

    /**
     * Finalitza una ruta activa, marcant-la com a completada.
     *
     * @param idRuta identificador de la ruta.
     * @return ruta amb les dades actualitzades (temps, distància, velocitats...).
     */
    suspend fun finalitzarRuta(idRuta: Long): Ruta {
        return RetrofitInstance.api.finalitzarRuta(idRuta)
    }

    /**
     * Llista totes les rutes associades a l’usuari.
     *
     * @param email correu electrònic de l’usuari.
     * @return llista de rutes registrades per l’usuari.
     */
    suspend fun llistarRutes(email: String): List<Ruta> {
        return RetrofitInstance.api.llistarRutes(email)
    }

    /**
     * Consulta una ruta específica pel seu identificador.
     *
     * @param id ID de la ruta.
     * @param email correu de l’usuari per verificar permisos.
     * @return ruta completa amb els detalls i punts GPS.
     */
    suspend fun consultarRuta(id: Long, email: String): Ruta {
        return RetrofitInstance.api.consultarRuta(id, email)
    }
}