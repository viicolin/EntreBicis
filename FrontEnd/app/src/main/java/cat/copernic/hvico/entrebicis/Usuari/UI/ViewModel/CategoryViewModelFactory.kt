package cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.hvico.entrebicis.R
import cat.copernic.hvico.entrebicis.Usuari.Domain.UsuariUseCases

/**
 * Fàbrica personalitzada per crear instàncies de [UsuariViewModel]
 *
 * Aquesta classe permet passar paràmetres al constructor del ViewModel,
 * en aquest cas, la classe de casos d'ús (UsuariUseCases), que conté la lògica de negoci.
 *
 * @param useCases Casos d'ús del domini relacionats amb l'usuari.
 */
class UsuariViewModelFactory(private val useCases: UsuariUseCases) : ViewModelProvider.Factory {

    /**
     * Crea una nova instància del ViewModel especificat si és del tipus [UsuariViewModel].
     *
     * @param modelClass Classe del ViewModel que es vol crear.
     * @return Nova instància de ViewModel si coincideix amb UsuariViewModel.
     * @throws IllegalArgumentException si la classe no és compatible.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Comprova si la classe sol·licitada és UsuariViewModel
        if (modelClass.isAssignableFrom(UsuariViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Supressió segura del cast genèric
            return UsuariViewModel(useCases) as T // Retorna una nova instància del ViewModel amb els casos d'ús
        }

        // Si la classe no coincideix, llança excepció
        throw IllegalArgumentException(R.string.unkViewModel.toString()) // Missatge d'error en cas de ViewModel desconegut
    }
}