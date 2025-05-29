package cat.copernic.hvico.entrebicis.Core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cat.copernic.hvico.entrebicis.Recompensa.UI.Screens.ConsultarRecompensaScreen
import cat.copernic.hvico.entrebicis.Recompensa.UI.Screens.HistorialRecompensesScreen
import cat.copernic.hvico.entrebicis.Recompensa.UI.Screens.LlistarRecompensesScreen
import cat.copernic.hvico.entrebicis.Recompensa.UI.Screens.RecollidaRecompensaScreen
import cat.copernic.hvico.entrebicis.Ruta.UI.Screens.ConsultarRutaScreen
import cat.copernic.hvico.entrebicis.Ruta.UI.Screens.LlistarRutesScreen
import cat.copernic.hvico.entrebicis.Usuari.UI.Screens.LoginScreen
import cat.copernic.hvico.entrebicis.Rutes.UI.Screens.IniciarRutaScreen
import cat.copernic.hvico.entrebicis.Usuari.UI.Screens.ModificarUsuariScreen
import cat.copernic.hvico.entrebicis.Usuari.UI.Screens.PerfilUsuariScreen
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel

/**
 * Funció principal de navegació de l'app EntreBicis.
 * Defineix les rutes i les pantalles que es poden navegar dins l'aplicació.
 *
 * @param usuariViewModel ViewModel compartit que conté l'estat i accions relacionades amb l'usuari.
 */
@Composable
fun AppNavigation(usuariViewModel: UsuariViewModel) {
    // Crea i recorda un controlador de navegació per moure's entre pantalles
    val navController = rememberNavController()

    // Defineix les rutes disponibles dins l'aplicació
    NavHost(navController = navController, startDestination = "login") {

        // Pantalla de login
        composable("login") {
            LoginScreen(navController = navController, usuariViewModel = usuariViewModel)
        }

        // Pantalla per iniciar una nova ruta en bici
        composable("iniciar_ruta") {
            IniciarRutaScreen(navController = navController, usuariViewModel = usuariViewModel)
        }

        // Pantalla de perfil de l'usuari, rebent l'email com a paràmetre
        composable("perfil_usuari/{email}") {
            PerfilUsuariScreen(navController = navController, usuariViewModel = usuariViewModel)
        }

        // Pantalla per modificar el perfil de l'usuari
        composable("modificarUsuari/{email}") {
            ModificarUsuariScreen(navController = navController, usuariViewModel = usuariViewModel)
        }

        // Llista de recompenses disponibles
        composable("recompenses") {
            LlistarRecompensesScreen(navController = navController, usuariViewModel = usuariViewModel)
        }

        // Historial de recompenses del propi usuari
        composable("historial_recompenses") {
            HistorialRecompensesScreen(navController = navController, usuariViewModel = usuariViewModel)
        }

        // Pantalla per consultar una recompensa específica (per ID)
        composable("consultar_recompensa/{recompensaId}") {
            ConsultarRecompensaScreen(navController = navController, usuariViewModel = usuariViewModel)
        }

        // Pantalla per confirmar la recollida d’una recompensa
        composable("recollir_recompensa") {
            RecollidaRecompensaScreen(navController = navController, usuariViewModel = usuariViewModel)
        }

        // Llista de rutes realitzades per l'usuari
        composable("rutes") {
            LlistarRutesScreen(navController = navController, usuariViewModel = usuariViewModel)
        }

        // Pantalla de detalls d’una ruta específica
        composable("consultar_ruta/{id}") {
            ConsultarRutaScreen(navController = navController, usuariViewModel = usuariViewModel)
        }
    }
}