package cat.copernic.hvico.entrebicis.Ruta.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.hvico.entrebicis.Ruta.Domain.RutaUseCases

/**
 * Fàbrica de ViewModel per crear una instància de [RutaViewModel].
 * Proporciona la dependència [RutaUseCases] necessària per al ViewModel.
 *
 * @property useCases Casos d'ús de la lògica de rutes que es passen al ViewModel.
 */
class RutaViewModelFactory(private val useCases: RutaUseCases) : ViewModelProvider.Factory {

    /**
     * Crea una instància del ViewModel especificat si és de tipus [RutaViewModel].
     *
     * @param modelClass La classe del ViewModel sol·licitat.
     * @return Una instància de [RutaViewModel] si és compatible.
     * @throws IllegalArgumentException si el ViewModel sol·licitat no és compatible.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica si el ViewModel sol·licitat és de tipus RutaViewModel
        if (modelClass.isAssignableFrom(RutaViewModel::class.java)) {
            // Retorna una nova instància de RutaViewModel amb els casos d'ús passats
            @Suppress("UNCHECKED_CAST")
            return RutaViewModel(useCases) as T
        }

        // Llança excepció si no es pot crear el ViewModel
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}