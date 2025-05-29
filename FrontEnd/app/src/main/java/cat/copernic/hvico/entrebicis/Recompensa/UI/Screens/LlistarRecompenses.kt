package cat.copernic.hvico.entrebicis.Recompensa.UI.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.hvico.entrebicis.Core.ui.TopBar
import cat.copernic.hvico.entrebicis.Core.ui.theme.BottomNavBar
import cat.copernic.hvico.entrebicis.Recompensa.Data.RecompensaRepository
import cat.copernic.hvico.entrebicis.Recompensa.Domain.RecompensaUseCases
import cat.copernic.hvico.entrebicis.Recompensa.UI.ViewModel.RecompensaViewModel
import cat.copernic.hvico.entrebicis.Recompensa.UI.ViewModel.RecompensaViewModelFactory
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel

/**
 * Pantalla que mostra una llista de recompenses disponibles per al ciclista.
 * Cada targeta mostra el nom, el local, l'adreça, el saldo requerit i l'estat.
 * Permet navegar al detall de la recompensa per reservar-la.
 *
 * @param navController Controlador per gestionar la navegació entre pantalles.
 * @param usuariViewModel ViewModel que conté les dades de l’usuari actual loguejat.
 */
@Composable
fun LlistarRecompensesScreen(navController: NavController, usuariViewModel: UsuariViewModel) {
    // Injecció de dependències per al ViewModel
    val recompensaUseCases = RecompensaUseCases(RecompensaRepository())
    val recompensaViewModel: RecompensaViewModel = viewModel(factory = RecompensaViewModelFactory(recompensaUseCases))

    // Estat observat: recompenses disponibles i usuari actual
    val recompenses by recompensaViewModel.recompensesDisponibles.collectAsState()
    val currentUser by usuariViewModel.currentUser.collectAsState()

    // Carrega inicial de recompenses disponibles
    LaunchedEffect(Unit) {
        Log.d("LlistarRecompenses", "Carregant recompenses disponibles")
        recompensaViewModel.carregarDisponibles()
    }

    // Contenidor principal amb fons verd degradat
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Color(0xFF7FB46A), Color(0xFFDCE775))))
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Barra superior amb logo, títol i logout
        TopBar(navController = navController, usuariViewModel = usuariViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Botó perfil i saldo de punts
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { navController.navigate("perfil_usuari/${currentUser?.email}") },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                Text("Perfil d'Usuari", color = Color.Black, fontSize = 18.sp)
            }

            Surface(
                shape = RoundedCornerShape(30.dp),
                shadowElevation = 8.dp,
                color = Color(0xFF7B4E2D)
            ) {
                Text(
                    text = "Saldo: ${currentUser?.saldoPunts ?: 0}",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Títol principal
        Text(
            text = "Premis Disponibles",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Llista de recompenses
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()) // Scroll vertical si hi ha moltes recompenses
        ) {
            recompenses.forEach { recompensa ->
                Log.d("LlistarRecompenses", "Recompensa: ${recompensa.nom} (ID: ${recompensa.id}) - Estat: ${recompensa.estat}")

                // Targeta clicable per consultar recompensa
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .clickable {
                            navController.navigate("consultar_recompensa/${recompensa.id}")
                        }
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Nom: ${recompensa.nom}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Nom del Local: ${recompensa.puntBescambiNom}", color = Color(0xFF1E3A5F), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Adreça: ${recompensa.puntBescambiAdreca}", color = Color(0xFF1E3A5F), fontSize = 18.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        // Estat i punts requerits
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = recompensa.estat.name, // En aquest cas sempre és DISPONIBLE
                                color = Color(0xFF388E3C),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Surface(
                                shape = RoundedCornerShape(30.dp),
                                color = Color(0xFF7B4E2D),
                                shadowElevation = 4.dp
                            ) {
                                Text(
                                    text = "Saldo: ${recompensa.puntsRequerits}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Barra inferior amb navegació activa en “Recompenses”
        BottomNavBar(
            navController = navController,
            usuariViewModel = usuariViewModel,
            selectedItem = 2
        )
    }
}