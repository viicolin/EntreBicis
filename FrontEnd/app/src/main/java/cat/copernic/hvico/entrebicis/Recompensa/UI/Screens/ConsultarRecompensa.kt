package cat.copernic.hvico.entrebicis.Recompensa.UI.Screens

import android.util.Log
import androidx.compose.foundation.background
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
 * Pantalla per consultar el detall d’una recompensa seleccionada.
 *
 * Mostra informació com el nom, dates d’estat, punt de bescanvi i usuari associat si escau.
 * També permet reservar la recompensa si està DISPONIBLE o recollir-la si està ASSIGNADA.
 *
 * @param navController controlador de navegació per canviar entre pantalles.
 * @param usuariViewModel ViewModel amb l’usuari loguejat i les seves dades.
 */
@Composable
fun ConsultarRecompensaScreen(navController: NavController, usuariViewModel: UsuariViewModel) {
    // Crea les dependències (UseCases, Repository, ViewModel)
    val recompensaUseCases = RecompensaUseCases(RecompensaRepository())
    val recompensaViewModel: RecompensaViewModel = viewModel(factory = RecompensaViewModelFactory(recompensaUseCases))

    // Obté l’ID de la recompensa des dels arguments de navegació
    val recompensaId = navController.currentBackStackEntry?.arguments?.getString("recompensaId")?.toLongOrNull()

    // Observa l'estat de la recompensa i l'usuari
    val recompensa by recompensaViewModel.recompensaConsultada.collectAsState()
    val currentUser by usuariViewModel.currentUser.collectAsState()

    // En entrar a la pantalla, carrega la recompensa si tenim l'ID
    LaunchedEffect(recompensaId) {
        Log.d("ConsultarRecompensa", "recompensaId rebut: $recompensaId")
        if (recompensaId != null) {
            recompensaViewModel.consultarRecompensa(recompensaId)
        } else {
            Log.w("ConsultarRecompensa", "ID de recompensa nul!")
        }
    }

    // Fons amb degradat i espai per a la UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Color(0xFF7FB46A), Color(0xFFDCE775))))
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Barra superior amb títol i logout
            TopBar(navController = navController, usuariViewModel = usuariViewModel)

            Spacer(modifier = Modifier.height(16.dp))

            // Secció superior amb botó de perfil i saldo
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
                text = "Detall de la Recompensa",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Si hi ha recompensa carregada, es mostra el detall
            recompensa?.let { r ->
                Log.d("ConsultarRecompensa", "Recompensa carregada: ${r.nom}, estat: ${r.estat}")

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Targeta amb els detalls de la recompensa
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = r.nom,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Dates de canvi d'estat
                            Text("Data de creació: ${r.dataCreacio?.substringBefore("T") ?: "-"}", fontSize = 16.sp)
                            if (r.dataReserva != null) Text("Data de reserva: ${r.dataReserva.substringBefore("T")}", fontSize = 16.sp)
                            if (r.dataAssignacio != null) Text("Data d'assignació: ${r.dataAssignacio.substringBefore("T")}", fontSize = 16.sp)
                            if (r.dataRecollida != null) Text("Data de recollida: ${r.dataRecollida.substringBefore("T")}", fontSize = 16.sp)

                            // Usuari (només si no és DISPONIBLE)
                            if (r.usuari != null && r.estat != EstatRecompensa.DISPONIBLE) {
                                Text("Usuari: ${r.usuari.nom} ${r.usuari.cognoms}", fontSize = 16.sp)
                            }

                            // Punt de bescanvi
                            Text("Punt de bescanvi: ${r.puntBescambiNom}", fontSize = 16.sp)
                            Text("Adreça: ${r.puntBescambiAdreca}", fontSize = 16.sp)

                            Spacer(modifier = Modifier.height(10.dp))

                            // Estat visual amb color segons el valor
                            Text(
                                text = r.estat.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = when (r.estat) {
                                    EstatRecompensa.DISPONIBLE -> Color(0xFF388E3C)
                                    EstatRecompensa.RESERVADA -> Color(0xFFFF9800)
                                    EstatRecompensa.ASSIGNADA -> Color(0xFF388E3C)
                                    EstatRecompensa.RECOLLIDA -> Color.Gray
                                    else -> Color.Black
                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Botó segons estat: Reservar o Recollir
                            if (r.estat == EstatRecompensa.DISPONIBLE || r.estat == EstatRecompensa.ASSIGNADA) {
                                Button(
                                    onClick = {
                                        val email = currentUser?.email
                                        Log.d("ConsultarRecompensa", "Click al botó. Estat: ${r.estat}, email: $email")

                                        if (r.estat == EstatRecompensa.DISPONIBLE && recompensaId != null && email != null) {
                                            recompensaViewModel.reservarRecompensa(recompensaId, email)
                                        } else if (r.estat == EstatRecompensa.ASSIGNADA) {
                                            navController.navigate("recollir_recompensa")
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(30.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (r.estat == EstatRecompensa.DISPONIBLE) Color(0xFF4CAF50) else Color(0xFF388E3C)
                                    )
                                ) {
                                    Text(
                                        text = if (r.estat == EstatRecompensa.DISPONIBLE) "Reservar" else "Recollir",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Barra de navegació inferior
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(
                navController = navController,
                usuariViewModel = usuariViewModel,
                selectedItem = 2 // Marca la secció Recompenses com activa
            )
        }
    }
}