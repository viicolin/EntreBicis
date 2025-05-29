package cat.copernic.hvico.entrebicis.Core.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.hvico.entrebicis.R
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel

/**
 * Barra de navegació inferior de l'aplicació EntreBicis.
 * Permet als usuaris canviar entre les pantalles principals: iniciar ruta, llistar rutes i recompenses.
 *
 * @param navController controlador de navegació per gestionar les rutes.
 * @param usuariViewModel ViewModel de l'usuari actual (no utilitzat directament aquí, però pot ser útil).
 * @param selectedItem índex de l’element seleccionat per destacar-lo visualment.
 */
@Composable
fun BottomNavBar(
    navController: NavController,
    usuariViewModel: UsuariViewModel,
    selectedItem: Int = 0
) {
    // Contenidor de la barra de navegació inferior amb color de fons marró
    NavigationBar(containerColor = Color(0xFF7B4E2D)) {

        // Llista de recursos d'icones per als ítems de la barra
        val icons = listOf(
            R.drawable.playruta,   // Icona per "Iniciar Ruta"
            R.drawable.listaruta,  // Icona per "Llistar Rutes"
            R.drawable.present     // Icona per "Recompenses"
        )

        // Llista de destinacions de navegació per cada ítem
        val destinations = listOf(
            "iniciar_ruta",
            "rutes",
            "recompenses"
        )

        // Descripcions de cada icona (per a l'accessibilitat)
        val descriptions = listOf(
            "Iniciar Ruta",
            "Llistar Rutes",
            "Recompenses"
        )

        // Crea un NavigationBarItem per cada icona/destinació
        icons.forEachIndexed { index, iconRes ->
            NavigationBarItem(
                icon = {
                    // Mostra la icona corresponent
                    Image(
                        painter = painterResource(id = iconRes), // Carrega la imatge de recursos
                        contentDescription = descriptions[index], // Descripció per a screen readers
                        modifier = Modifier.size(24.dp) // Mida de la icona
                    )
                },
                selected = selectedItem == index, // Marca l’ítem com seleccionat si coincideix
                onClick = {
                    // Navega a la destinació corresponent si l’índex és vàlid
                    if (index < destinations.size) {
                        navController.navigate(destinations[index])
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,     // Color icona seleccionada
                    unselectedIconColor = Color.LightGray, // Color icona no seleccionada
                    indicatorColor = Color(0xFFFFFFFF)   // Color de l’indicador sota la icona
                ),
                modifier = Modifier.size(40.dp) // Mida total del botó de la icona
            )
        }
    }
}