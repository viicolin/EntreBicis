package cat.copernic.hvico.entrebicis.Recompensa.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.hvico.entrebicis.Recompensa.Domain.RecompensaUseCases

/**
 * Fàbrica per crear una instància de [RecompensaViewModel] amb dependències personalitzades.
 *
 * Aquesta classe permet injectar els [RecompensaUseCases] dins del ViewModel quan
 * no es poden passar paràmetres al constructor de manera directa.
 *
 * @param useCases instància dels casos d’ús de recompensa a injectar al ViewModel.
 */
class RecompensaViewModelFactory(private val useCases: RecompensaUseCases) : ViewModelProvider.Factory {

    /**
     * Crea una instància del ViewModel demanat.
     *
     * @param modelClass classe del ViewModel que es vol instanciar.
     * @return instància del ViewModel creat.
     * @throws IllegalArgumentException si la classe no és [RecompensaViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica que el ViewModel que es vol crear és RecompensaViewModel
        if (modelClass.isAssignableFrom(RecompensaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecompensaViewModel(useCases) as T
        }

        // Si no coincideix, llença una excepció
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

