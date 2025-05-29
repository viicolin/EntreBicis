package cat.copernic.hvico.entrebicis.Recompensa.UI.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Pantalla per confirmar la recollida física d'una recompensa assignada.
 *
 * Mostra la informació de la recompensa assignada i permet al ciclista marcar-la com "entregada".
 * Un cop feta la recollida, es mostra un diàleg de confirmació i es redirigeix al historial.
 *
 * @param navController controlador de navegació.
 * @param usuariViewModel ViewModel de l'usuari loguejat, utilitzat per obtenir el correu i saldo.
 */
@Composable
fun RecollidaRecompensaScreen(
    navController: NavController,
    usuariViewModel: UsuariViewModel
) {
    // Injecció de dependències del ViewModel
    val recompensaUseCases = RecompensaUseCases(RecompensaRepository())
    val recompensaViewModel: RecompensaViewModel = viewModel(factory = RecompensaViewModelFactory(recompensaUseCases))

    // Estats observats
    val currentUser by usuariViewModel.currentUser.collectAsState()
    val recompensaAssignada by recompensaViewModel.recompensaAssignada.collectAsState()

    // Estat per mostrar el diàleg de "Entregada"
    var showDialog by remember { mutableStateOf(false) }

    // Scope per llançar corrutines (delay)
    val coroutineScope = rememberCoroutineScope()

    // En carregar la pantalla, consulta si hi ha recompensa assignada
    LaunchedEffect(currentUser?.email) {
        currentUser?.email?.let {
            recompensaViewModel.carregarRecompensaAssignada(it)
        }
    }

    // Diàleg que apareix quan es confirma la recollida
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {}, // No es pot tancar clicant fora
            confirmButton = {}, // No hi ha botó de confirmació (es tanca sol)
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ENTREGADA!!!",
                        fontSize = 24.sp,
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight()
        )
    }

    // Fons i contingut principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Color(0xFF7FB46A), Color(0xFFDCE775))))
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Barra superior
            TopBar(navController = navController, usuariViewModel = usuariViewModel)

            Spacer(modifier = Modifier.height(16.dp))

            // Botó perfil i saldo actual
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

            // Contingut principal: targeta de recompensa assignada
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                recompensaAssignada?.let { r ->
                    // Targeta amb nom del local i recompensa
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(0.95f)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 36.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = r.puntBescambiNom ?: "Punt desconegut",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4E342E)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = r.nom,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF33691E)
                            )

                            Spacer(modifier = Modifier.height(36.dp))

                            // Botó per confirmar recollida
                            Button(
                                onClick = {
                                    r.id?.let { id ->
                                        recompensaViewModel.confirmarRecollida(id)
                                        showDialog = true
                                        coroutineScope.launch {
                                            delay(10000) // Espera 10 segons
                                            showDialog = false
                                            navController.navigate("historial_recompenses") {
                                                popUpTo("recollida_recompensa") { inclusive = true }
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(75.dp),
                                shape = RoundedCornerShape(30.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                            ) {
                                Text(
                                    "Entregar",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Barra inferior de navegació
            BottomNavBar(
                navController = navController,
                usuariViewModel = usuariViewModel,
                selectedItem = 2
            )
        }
    }
}