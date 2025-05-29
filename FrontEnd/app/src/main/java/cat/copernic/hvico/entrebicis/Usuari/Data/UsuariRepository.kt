package cat.copernic.hvico.entrebicis.Usuari.Data

import cat.copernic.hvico.entrebicis.Core.Model.LoginRequest
import cat.copernic.hvico.entrebicis.Core.Model.LoginResponse
import cat.copernic.hvico.entrebicis.Core.Model.ParametresSistema
import cat.copernic.hvico.entrebicis.Core.Model.Usuari
import retrofit2.Response

/**
 * Repositori per gestionar les operacions relacionades amb l'usuari mitjançant l'API REST.
 * Aquest repositori encapsula l'accés a la API i s'utilitza a la capa de casos d'ús.
 */
class UsuariRepository {

    /**
     * Fa login amb email i contrasenya.
     *
     * @param email Correu electrònic de l'usuari.
     * @param contrasenya Contrasenya de l'usuari.
     * @return Resposta de tipus [LoginResponse] amb token i dades de l'usuari.
     */
    suspend fun login(email: String, contrasenya: String): Response<LoginResponse> {
        val request = LoginRequest(email, contrasenya) // Es crea un objecte amb les credencials
        return RetrofitInstance.api.login(request) // Es fa la crida a l’endpoint de login
    }

    /**
     * Consulta un usuari a través del seu correu electrònic.
     *
     * @param email Correu de l’usuari a consultar.
     * @return Resposta amb les dades de l'usuari.
     */
    suspend fun getUsuari(email: String): Response<Usuari> {
        return RetrofitInstance.api.getUsuari(email) // Crida a la API per obtenir l'usuari
    }

    /**
     * Consulta una altra versió de l'usuari pel seu email.
     * Aquesta funció pot servir per a un endpoint diferent amb mateix resultat.
     *
     * @param email Correu de l’usuari.
     * @return Resposta amb l’usuari trobat.
     */
    suspend fun consultarUsuari(email: String): Response<Usuari> {
        return RetrofitInstance.api.consultarUsuari(email) // Endpoint alternatiu
    }

    /**
     * Modifica les dades d’un usuari.
     *
     * @param email Email de l’usuari a modificar.
     * @param usuari Objecte amb les dades actualitzades.
     * @return Resposta amb l’usuari modificat.
     */
    suspend fun modificarUsuari(email: String, usuari: Usuari): Response<Usuari> {
        return RetrofitInstance.api.modificarUsuari(email, usuari) // Crida al PUT per modificar
    }

    /**
     * Obté els paràmetres globals del sistema com temps, saldo/km, etc.
     *
     * @return Resposta amb els paràmetres del sistema.
     */
    suspend fun obtenirParametresSistema(): Response<ParametresSistema> {
        return RetrofitInstance.api.obtenirParametresSistema() // Crida al GET de paràmetres
    }
}