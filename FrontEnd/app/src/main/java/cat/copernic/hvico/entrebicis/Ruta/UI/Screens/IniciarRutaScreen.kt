package cat.copernic.hvico.entrebicis.Rutes.UI.Screens

import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cat.copernic.hvico.entrebicis.Core.Model.PuntsGPS
import cat.copernic.hvico.entrebicis.Core.ui.TopBar
import cat.copernic.hvico.entrebicis.Core.ui.theme.BottomNavBar
import cat.copernic.hvico.entrebicis.Ruta.Data.RutaRepository
import cat.copernic.hvico.entrebicis.Ruta.Domain.RutaUseCases
import cat.copernic.hvico.entrebicis.Ruta.UI.ViewModel.RutaViewModel
import cat.copernic.hvico.entrebicis.Ruta.UI.ViewModel.RutaViewModelFactory
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pantalla que permet iniciar una ruta i registrar punts GPS en temps real.
 * Mostra un mapa, controla permisos i finalitza la ruta per inactivitat.
 */
@Composable
fun IniciarRutaScreen(navController: NavController, usuariViewModel: UsuariViewModel) {
    // Obté l'usuari actual i els paràmetres del sistema
    val usuari by usuariViewModel.currentUser.collectAsState()
    val parametres by usuariViewModel.parametresSistema.collectAsState()
    var rutaEnCurs by rememberSaveable { mutableStateOf(false) } // Estat per saber si una ruta està activa

    val rutaViewModel: RutaViewModel = viewModel(factory = RutaViewModelFactory(RutaUseCases(RutaRepository())))
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // Estat de la càmera del mapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.3851, 2.1734), 13f)
    }

    val rutaFinalitzada by rutaViewModel.rutaFinalitzada.collectAsState()

    // Lançador per demanar permís de localització
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        Log.d("IniciarRuta", "Permís concedit: $isGranted")
        if (!isGranted) {
            Toast.makeText(context, "Cal acceptar el permís de localització", Toast.LENGTH_LONG).show()
        }
    }

    // Demanar permís si no s'ha concedit
    LaunchedEffect(Unit) {
        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            Log.d("IniciarRuta", "Sol·licitant permís de localització")
            permissionLauncher.launch(permission)
        }
    }

    // Si la ruta s'ha finalitzat, navega automàticament als seus detalls
    LaunchedEffect(rutaFinalitzada?.id) {
        rutaFinalitzada?.id?.let { idRuta ->
            Log.d("IniciarRuta", "Ruta finalitzada amb id: $idRuta")
            navController.navigate("consultar_ruta/$idRuta") {
                popUpTo("iniciar_ruta") { inclusive = true }
            }
        }
    }

    // Registre de punts GPS en temps real mentre la ruta estigui en curs
    LaunchedEffect(rutaEnCurs) {
        if (rutaEnCurs) {
            Log.d("IniciarRuta", "Iniciant registre de punts GPS")
            var ultimaLatitud: Double? = null
            var ultimaLongitud: Double? = null
            var tempsParat = 0L // Temps acumulat sense moviment

            while (rutaEnCurs) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val lat = it.latitude
                        val lon = it.longitude
                        val marcaTemps = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())

                        // Crea punt GPS i envia al ViewModel
                        val punt = PuntsGPS(
                            id = null,
                            rutaId = rutaViewModel.ruta.value?.id,
                            latitud = lat,
                            longitud = lon,
                            marcaTemps = marcaTemps
                        )
                        rutaViewModel.afegirPuntGPS(punt)

                        // Calcula distància respecte l'últim punt
                        if (ultimaLatitud != null && ultimaLongitud != null) {
                            val resultat = FloatArray(1)
                            Location.distanceBetween(lat, lon, ultimaLatitud!!, ultimaLongitud!!, resultat)
                            val distancia = resultat[0]
                            Log.d("IniciarRuta", "Distància respecte anterior: $distancia m")

                            if (distancia < 2f) {
                                tempsParat += 1000
                            } else {
                                tempsParat = 0
                                ultimaLatitud = lat
                                ultimaLongitud = lon
                            }

                            val maxAturada = parametres?.tempsMaximAturada?.times(60_000) ?: 0
                            if (tempsParat >= maxAturada) {
                                Log.w("IniciarRuta", "Temps d'aturada superat. Finalitzant ruta.")
                                rutaViewModel.finalitzarRuta()
                                rutaEnCurs = false
                                Toast.makeText(context, "Ruta finalitzada automàticament per inactivitat", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            ultimaLatitud = lat
                            ultimaLongitud = lon
                        }
                    }
                }
                delay(1000)
            }
        }
    }

    // INTERFÍCIE
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFA1C961), Color(0xFF60993E))))
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        TopBar(navController = navController, usuariViewModel = usuariViewModel)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController.navigate("perfil_usuari/${usuari!!.email}") },
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text("Perfil d'Usuari", color = Color.Black)
                }

                Surface(
                    shape = RoundedCornerShape(30.dp),
                    shadowElevation = 8.dp,
                    color = Color(0xFF7B4E2D)
                ) {
                    Text(
                        text = "Saldo: ${usuari?.saldoPunts ?: 0}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (rutaEnCurs) "Ruta en curs" else "Iniciar Ruta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(zoomControlsEnabled = true, myLocationButtonEnabled = true)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!rutaEnCurs) {
                        usuari?.email?.let { rutaViewModel.iniciarRuta(it) }
                        rutaEnCurs = true
                    } else {
                        rutaViewModel.finalitzarRuta()
                        rutaEnCurs = false
                        Toast.makeText(context, "Ruta finalitzada correctament", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rutaEnCurs) Color.Red else Color(0xFF4C89D9)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = if (rutaEnCurs) "Finalitzar Ruta" else "Iniciar Ruta",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        BottomNavBar(
            navController = navController,
            usuariViewModel = usuariViewModel,
            selectedItem = 0
        )
    }
}