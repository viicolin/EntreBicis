package cat.copernic.hvico.entrebicis.Ruta.UI.Screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*

/**
 * Pantalla per consultar els detalls d’una ruta finalitzada.
 * Mostra dades com la distància, temps, velocitats, i punts GPS en un mapa.
 *
 * @param navController controlador de navegació.
 * @param usuariViewModel viewModel per accedir a l’usuari i paràmetres del sistema.
 */
@Composable
fun ConsultarRutaScreen(
    navController: NavController,
    usuariViewModel: UsuariViewModel
) {
    // ViewModel amb injecció de dependències
    val rutaViewModel: RutaViewModel = viewModel(factory = RutaViewModelFactory(RutaUseCases(RutaRepository())))

    // Estat de la ruta i paràmetres del sistema
    val ruta by rutaViewModel.ruta.collectAsState()
    val parametresSistema by usuariViewModel.parametresSistema.collectAsState()
    val email = usuariViewModel.currentUser.collectAsState().value?.email
    val context = LocalContext.current

    // Obtenció de l’ID de ruta des del backstack
    val rutaId = navController.currentBackStackEntry?.arguments?.getString("id")?.toLongOrNull()

    // En entrar a la pantalla, es consulta la ruta
    LaunchedEffect(rutaId, email) {
        if (rutaId != null && email != null) {
            rutaViewModel.consultarRuta(rutaId, email)
        } else {
            Log.w("ConsultarRutaScreen", "Falten dades per consultar la ruta.")
        }
    }

    // Disseny principal amb fons verd degradat
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Color(0xFF7FB46A), Color(0xFFDCE775))))
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        // Barra superior amb botó logout
        TopBar(navController = navController, usuariViewModel = usuariViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Títol
        Text(
            text = "Detalls Ruta",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ruta?.let { ruta ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                // Targeta de dades i mapa
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        // Mapa amb punts GPS i polilínia si hi ha punts
                        if (ruta.puntsGPS.isNotEmpty()) {
                            val boundsBuilder = LatLngBounds.builder()
                            ruta.puntsGPS.forEach {
                                boundsBuilder.include(LatLng(it.latitud, it.longitud))
                            }

                            val cameraPositionState = rememberCameraPositionState()

                            // Centra el mapa segons els punts GPS
                            LaunchedEffect(ruta.puntsGPS) {
                                cameraPositionState.move(
                                    CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100)
                                )
                            }

                            GoogleMap(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .clip(RoundedCornerShape(15.dp)),
                                cameraPositionState = cameraPositionState,
                                uiSettings = MapUiSettings(zoomControlsEnabled = true)
                            ) {
                                val punts = ruta.puntsGPS.map { LatLng(it.latitud, it.longitud) }
                                Polyline(points = punts) // Dibuixa la línia de la ruta

                                // Marca cada punt GPS
                                ruta.puntsGPS.forEach { punt ->
                                    Marker(
                                        state = MarkerState(position = LatLng(punt.latitud, punt.longitud)),
                                        title = "Lat: ${punt.latitud}",
                                        snippet = "Lng: ${punt.longitud}",
                                        onInfoWindowClick = {
                                            Toast.makeText(
                                                context,
                                                "Lat: ${punt.latitud}, Lng: ${punt.longitud}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Dades generals de la ruta
                        Text("Data: ${ruta.dataCreacio}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Distància: ${"%.2f".format(ruta.distanciaTotal)} km", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        // Conversió del temps total de segons a hores/minuts/segons
                        val totalSegons = ruta.tempsTotal.toLong()
                        val hores = totalSegons / 3600
                        val minuts = (totalSegons % 3600) / 60
                        val segons = totalSegons % 60
                        Text("Temps total: ${hores}h ${minuts}min ${segons}s", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        // Velocitats
                        Text("Velocitat mitjana: ${"%.2f".format(ruta.velocitatMitjana)} km/h", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                        val maxValida = parametresSistema?.velocitatMaximaValida ?: 0.0
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Velocitat màxima: ", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "${"%.2f".format(ruta.velocitatMaxima)} km/h",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (ruta.velocitatMaxima > maxValida) Color.Red else Color.Black
                            )
                        }
                    }
                }
            }
        } ?: run {
            // Si no s’ha pogut carregar la ruta
            Log.w("ConsultarRutaScreen", "No s'ha pogut carregar la ruta.")
        }

        // Barra de navegació inferior
        BottomNavBar(navController = navController, usuariViewModel = usuariViewModel, selectedItem = 1)
    }
}