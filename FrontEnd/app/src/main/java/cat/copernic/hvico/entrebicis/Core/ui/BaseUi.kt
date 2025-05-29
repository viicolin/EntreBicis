package cat.copernic.hvico.entrebicis.Core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.hvico.entrebicis.Core.ui.theme.BottomNavBar
import cat.copernic.hvico.entrebicis.R
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel

/**
 * Barra superior (TopBar) reutilitzable amb logo, títol i icona de logout.
 *
 * @param navController controlador de navegació per redirigir a la pantalla de login.
 * @param usuariViewModel ViewModel que conté la lògica per fer logout.
 */
@Composable
fun TopBar(navController: NavController, usuariViewModel: UsuariViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Ocupa tot l'ample
            .background(Color(0xFF7B4E2D)) // Fons marró
            .padding(8.dp) // Marge interior
    ) {
        // Logo a l'esquerra
        Image(
            painter = painterResource(id = R.drawable.logo), // Carrega el logo
            contentDescription = "Logo", // Per a screen readers
            modifier = Modifier
                .align(Alignment.CenterStart) // S'alinea a l'esquerra
                .size(36.dp) // Mida del logo
        )

        // Títol centrat
        Text(
            text = "EntreBicis", // Nom de l'app
            fontWeight = FontWeight.Bold, // Lletres en negreta
            fontSize = 18.sp, // Mida de lletra
            color = Color.White, // Color blanc
            modifier = Modifier.align(Alignment.Center) // Centrat dins la Box
        )

        // Icona de logout a la dreta
        Image(
            painter = painterResource(id = R.drawable.logout), // Icona de logout
            contentDescription = "Logout",
            modifier = Modifier
                .align(Alignment.CenterEnd) // S'alinea a la dreta
                .size(24.dp)
                .clickable {
                    // Quan es clica: es fa logout i es navega a la pantalla de login
                    usuariViewModel.logout()
                    navController.navigate("login")
                }
        )
    }
}

/**
 * Secció inferior de la pantalla que conté la barra de navegació.
 *
 * @param navController controlador de navegació per canviar de pantalla.
 * @param usuarirViewModel ViewModel de l'usuari (s'utilitza a la BottomNavBar).
 * @param posicion posició seleccionada per marcar l’ítem actiu de la barra.
 */
@Composable
fun BottomSection(navController: NavController, usuarirViewModel: UsuariViewModel, posicion: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa tot l'espai disponible
            .navigationBarsPadding(), // Deixa espai per la barra de navegació del sistema (si n'hi ha)
        verticalArrangement = Arrangement.Bottom // Situa els continguts a la part inferior
    ) {
        // Mostra la barra de navegació inferior
        BottomNavBar(navController, usuarirViewModel, selectedItem = posicion)
    }
}
