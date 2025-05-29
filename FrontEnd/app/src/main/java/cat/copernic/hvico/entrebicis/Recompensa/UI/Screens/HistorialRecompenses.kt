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
import cat.copernic.hvico.entrebicis.Core.Model.EstatRecompensa
import cat.copernic.hvico.entrebicis.Core.ui.TopBar
import cat.copernic.hvico.entrebicis.Core.ui.theme.BottomNavBar
import cat.copernic.hvico.entrebicis.Recompensa.Data.RecompensaRepository
import cat.copernic.hvico.entrebicis.Recompensa.Domain.RecompensaUseCases
import cat.copernic.hvico.entrebicis.Recompensa.UI.ViewModel.RecompensaViewModel
import cat.copernic.hvico.entrebicis.Recompensa.UI.ViewModel.RecompensaViewModelFactory
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel

/**
 * Pantalla que mostra l’historial de recompenses d’un usuari.
 * Inclou una llista de recompenses amb estat RESERVADA, ASSIGNADA o RECOLLIDA,
 * així com informació del local, saldo requerit, i permet consultar-ne els detalls.
 *
 * @param navController controlador per navegar entre pantalles.
 * @param usuariViewModel ViewModel que proporciona informació de l’usuari loguejat.
 */
@Composable
fun HistorialRecompensesScreen(navController: NavController, usuariViewModel: UsuariViewModel) {
    // Crea i injecta dependències
    val recompensaUseCases = RecompensaUseCases(RecompensaRepository())
    val recompensaViewModel: RecompensaViewModel = viewModel(factory = RecompensaViewModelFactory(recompensaUseCases))

    // Observa l’usuari i l’historial de recompenses
    val currentUser by usuariViewModel.currentUser.collectAsState()
    val historial by recompensaViewModel.historialRecompenses.collectAsState()

    // Carrega l’historial un cop es detecta un email vàlid
    LaunchedEffect(currentUser?.email) {
        currentUser?.email?.let {
            recompensaViewModel.carregarHistorial(it)
        } ?: Log.w("HistorialRecompenses", "Email d'usuari nul")
    }

    // Disseny general amb degradat verd
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Color(0xFF7FB46A), Color(0xFFDCE775))))
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Barra superior
        TopBar(navController = navController, usuariViewModel = usuariViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Botó de perfil i saldo a la part superior
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

        // Títol de la pantalla
        Text(
            text = "Historial de Premis",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Contingut principal: llista de recompenses
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            historial.forEach { recompensa ->
                // Color segons l’estat de la recompensa
                val estatColor = when (recompensa.estat) {
                    EstatRecompensa.RESERVADA -> Color(0xFFFF9800)
                    EstatRecompensa.ASSIGNADA -> Color(0xFF388E3C)
                    EstatRecompensa.RECOLLIDA -> Color.Gray
                    else -> Color.Black
                }

                // Targeta clicable per veure detalls de cada recompensa
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
                        // Informació general
                        Text("Nom: ${recompensa.nom}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Nom del Local: ${recompensa.puntBescambiNom}", color = Color(0xFF1E3A5F), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Adreça: ${recompensa.puntBescambiAdreca}", color = Color(0xFF1E3A5F), fontSize = 18.sp)

                        Spacer(modifier = Modifier.height(10.dp))

                        // Estat de la recompensa i usuari (si aplica)
                        Column {
                            Text(
                                text = recompensa.estat.name,
                                color = estatColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            recompensa.usuari?.let {
                                Text(
                                    text = "Usuari: ${it.nom} ${it.cognoms}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Mostra el saldo necessari per la recompensa
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
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
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Barra inferior de navegació
        BottomNavBar(
            navController = navController,
            usuariViewModel = usuariViewModel,
            selectedItem = 2 // Marca la secció de recompenses com activa
        )
    }
}