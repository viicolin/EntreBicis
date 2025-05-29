package cat.copernic.hvico.entrebicis.Ruta.UI.Screens

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
import cat.copernic.hvico.entrebicis.Core.ui.TopBar
import cat.copernic.hvico.entrebicis.Core.ui.theme.BottomNavBar
import cat.copernic.hvico.entrebicis.Ruta.Data.RutaRepository
import cat.copernic.hvico.entrebicis.Ruta.Domain.RutaUseCases
import cat.copernic.hvico.entrebicis.Ruta.UI.ViewModel.RutaViewModel
import cat.copernic.hvico.entrebicis.Ruta.UI.ViewModel.RutaViewModelFactory
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel

/**
 * Mostra una llista de rutes registrades per l'usuari actual.
 * Inclou el saldo, accés al perfil i targetes amb informació de cada ruta.
 *
 * @param navController Controlador de navegació.
 * @param usuariViewModel ViewModel que conté l'usuari actual i els seus paràmetres.
 */
@Composable
fun LlistarRutesScreen(navController: NavController, usuariViewModel: UsuariViewModel) {
    // Inicialitza el ViewModel de rutes amb la seva fàbrica
    val rutaViewModel: RutaViewModel = viewModel(factory = RutaViewModelFactory(RutaUseCases(RutaRepository())))

    // Estat reactiu per a les rutes i l'usuari actual
    val rutes by rutaViewModel.rutes.collectAsState()
    val currentUser by usuariViewModel.currentUser.collectAsState()

    // Carrega les rutes de l'usuari quan es mostra la pantalla
    LaunchedEffect(Unit) {
        currentUser?.email?.let {
            Log.d("LlistarRutesScreen", "Carregant rutes per usuari amb email: $it")
            rutaViewModel.llistarRutes(it)
        }
    }

    Log.d("LlistarRutesScreen", "Nombre de rutes carregades: ${rutes.size}")

    // Columna principal de la pantalla
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

        // Botons de perfil i saldo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botó per anar al perfil
            Button(
                onClick = { navController.navigate("perfil_usuari/${currentUser?.email}") },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
            ) {
                Text("Perfil d'Usuari", color = Color.Black, fontSize = 18.sp)
            }

            // Targeta amb saldo
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
            text = "Rutes Registrades",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Llista de targetes de rutes en una columna scrollable
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            rutes.forEach { ruta ->
                Log.d("LlistarRutesScreen", "Ruta ID: ${ruta.id}, Data: ${ruta.dataCreacio}")

                // Targeta per cada ruta
                Card(
                    onClick = {
                        Log.d("LlistarRutesScreen", "Navegant a detall de ruta amb ID: ${ruta.id}")
                        navController.navigate("consultar_ruta/${ruta.id}")
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    // Punt superior amb saldo
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            shape = RoundedCornerShape(30.dp),
                            color = Color(0xFF7B4E2D),
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "${ruta.saldoAtorgat} punts",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // Informació detallada de la ruta
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Data: ${ruta.dataCreacio}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Distància: ${"%.2f".format(ruta.distanciaTotal)} km", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                        // Conversió de segons a hores, minuts i segons
                        val totalSegons = ruta.tempsTotal.toLong()
                        val hores = totalSegons / 3600
                        val minuts = (totalSegons % 3600) / 60
                        val segons = totalSegons % 60
                        Text("Temps: ${hores}h ${minuts}min ${segons}s", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                        Text("Velocitat mitjana: ${"%.2f".format(ruta.velocitatMitjana)} km/h", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Velocitat màxima: ${"%.2f".format(ruta.velocitatMaxima)} km/h", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                        // Estat de validació de la ruta
                        Text(
                            text = if (ruta.estatRuta.name == "VALIDA") "VALIDADA" else "NO VALIDADA",
                            color = if (ruta.estatRuta.name == "VALIDA") Color(0xFF388E3C) else Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }

        // Barra de navegació inferior
        BottomNavBar(
            navController = navController,
            usuariViewModel = usuariViewModel,
            selectedItem = 1 // Indica que aquesta pantalla és la segona del menú
        )
    }
}
