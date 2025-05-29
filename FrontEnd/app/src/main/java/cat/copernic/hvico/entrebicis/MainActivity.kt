package cat.copernic.hvico.entrebicis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import cat.copernic.hvico.entrebicis.Core.ui.navigation.AppNavigation
import cat.copernic.hvico.entrebicis.Usuari.Data.UsuariRepository
import cat.copernic.hvico.entrebicis.Usuari.Domain.UsuariUseCases
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModelFactory

/**
 * Activitat principal de l'aplicació EntreBicis.
 *
 * Aquesta classe inicialitza la interfície d'usuari amb Jetpack Compose i configura
 * la navegació principal, el ViewModel d'usuari i els casos d'ús corresponents.
 */
class MainActivity : ComponentActivity() {

    /**
     * Es crida quan l'activitat es crea per primera vegada.
     * Configura el contenidor principal de l'app amb composables.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Permet que el contingut arribi fins als marges de la pantalla (status bar, nav bar)
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        // Defineix el contingut de la UI usant Compose
        setContent {

            // 1. Crea la instància dels casos d'ús (domini) a partir del repositori
            val useCases = UsuariUseCases(UsuariRepository())

            // 2. Crea una instància del ViewModel amb la fàbrica personalitzada
            val usuariViewModel: UsuariViewModel = viewModel(
                factory = UsuariViewModelFactory(useCases)
            )

            // 3. Inicia la navegació de l'aplicació i passa el ViewModel d'usuari
            AppNavigation(usuariViewModel)
        }
    }
}