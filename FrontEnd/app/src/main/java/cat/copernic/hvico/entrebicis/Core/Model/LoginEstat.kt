package cat.copernic.hvico.entrebicis.Core.Model

/**
 * Classe segellada que representa els diferents estats possibles durant el procés de login.
 *
 * Aquesta estructura és útil per manejar els resultats d'una operació de login en un ViewModel,
 * especialment quan es treballa amb LiveData o StateFlow.
 */
sealed class LoginEstat {

    /**
     * Estat que indica que el login s'ha completat correctament.
     *
     * @property loginResponse Conté la resposta obtinguda després d'un login exitós.
     */
    data class Success(val loginResponse: LoginResponse) : LoginEstat()

    /**
     * Estat que indica que hi ha hagut un error durant el login.
     *
     * @property exception Conté la excepció llançada per identificar l'error.
     */
    data class Failure(val exception: Throwable) : LoginEstat()

    /**
     * Estat que indica que el procés de login està en curs.
     * Es pot utilitzar per mostrar un spinner o missatge de càrrega.
     */
    object Loading : LoginEstat()
}