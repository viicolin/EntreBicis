package cat.copernic.hvico.entrebicis.Usuari.Domain

import cat.copernic.hvico.entrebicis.Usuari.Data.UsuariRepository
import cat.copernic.hvico.entrebicis.Core.Model.Usuari

/**
 * Casos d'ús relacionats amb els usuaris.
 * Aquesta classe actua com a intermediària entre el ViewModel i el Repository,
 * encapsulant la lògica d'accés a dades i facilitant la reutilització.
 *
 * @property repository Repositori que fa les crides a la API REST.
 */
class UsuariUseCases(private val repository: UsuariRepository) {

    /**
     * Fa login amb les credencials indicades.
     *
     * @param email Correu electrònic de l’usuari.
     * @param contrasenya Contrasenya de l’usuari.
     * @return [Response]<[LoginResponse]> amb resultat del login.
     */
    suspend fun login(email: String, contrasenya: String) =
        repository.login(email, contrasenya) // Delegació directa al repositori

    /**
     * Obté l'usuari pel seu email.
     *
     * @param email Correu de l’usuari.
     * @return [Response]<[Usuari]> amb les dades de l’usuari.
     */
    suspend fun getUsuari(email: String) =
        repository.getUsuari(email) // Crida al mètode getUsuari

    /**
     * Consulta l’usuari amb un endpoint alternatiu.
     *
     * @param email Correu de l’usuari.
     * @return [Response]<[Usuari]> amb les dades.
     */
    suspend fun consultarUsuari(email: String) =
        repository.consultarUsuari(email) // Endpoint alternatiu per consultar

    /**
     * Modifica un usuari existent.
     *
     * @param email Email identificador.
     * @param usuari Objecte amb els camps a modificar.
     * @return [Response]<[Usuari]> amb l’usuari modificat.
     */
    suspend fun modificarUsuari(email: String, usuari: Usuari) =
        repository.modificarUsuari(email, usuari) // Crida al PUT del repositori

    /**
     * Obté els paràmetres globals del sistema.
     *
     * @return [Response]<[ParametresSistema]> amb els valors de configuració.
     */
    suspend fun obtenirParametresSistema() =
        repository.obtenirParametresSistema() // Crida al GET de paràmetres
}