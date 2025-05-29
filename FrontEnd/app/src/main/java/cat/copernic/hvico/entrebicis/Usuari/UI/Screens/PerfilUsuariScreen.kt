package cat.copernic.hvico.entrebicis.Usuari.UI.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.hvico.entrebicis.Core.ui.TopBar
import cat.copernic.hvico.entrebicis.Core.ui.theme.BottomNavBar
import cat.copernic.hvico.entrebicis.R
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel

/**
 * Pantalla de perfil de l'usuari on es mostren les dades personals,
 * el saldo i l'accés a l'historial de rutes i recompenses.
 *
 * @param navController controlador de navegació per canviar de pantalla.
 * @param usuariViewModel ViewModel que gestiona les dades de l'usuari.
 */
@Composable
fun PerfilUsuariScreen(navController: NavController, usuariViewModel: UsuariViewModel) {
    // Obté l'usuari actual des del ViewModel
    val currentUser by usuariViewModel.currentUser.collectAsState()

    // Mostra indicador de càrrega si l'usuari encara no s'ha carregat
    if (currentUser == null) {
        Log.d("PerfilUsuari", "currentUser és null, mostrant indicador de càrrega")
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Log.d("PerfilUsuari", "currentUser carregat correctament: ${currentUser?.email}")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(Color(0xFF9CCC65), Color(0xFFDCEDC8))))
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(navController, usuariViewModel) // Barra superior
            Spacer(modifier = Modifier.height(16.dp))

            // Filera superior amb botó de "Perfil d'Usuari" i saldo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { Log.d("PerfilUsuari", "Botó de Perfil d'Usuari premut") },
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
                text = "Perfil d’Usuari",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Targeta central amb la informació de l'usuari
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 80.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1)),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        // Nom i cognoms al centre
                        Text(
                            text = "${currentUser?.nom} ${currentUser?.cognoms}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Center)
                        )

                        // Botó per editar el perfil (icona del llapis)
                        IconButton(
                            onClick = {
                                Log.d("PerfilUsuari", "Editant usuari: ${currentUser?.email}")
                                navController.navigate("modificarUsuari/${currentUser?.email}")
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(28.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Mostra la imatge de perfil si existeix
                    val bitmap = currentUser!!.foto?.let {
                        Log.d("PerfilUsuari", "Carregant imatge de perfil des de base64")
                        usuariViewModel.base64ToBitmap(it)
                    }

                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Imatge per defecte si no hi ha foto
                        Log.d("PerfilUsuari", "No s'ha trobat cap imatge de perfil, mostrant imatge per defecte")
                        Image(
                            painter = painterResource(R.drawable.default_user),
                            contentDescription = "Foto per defecte",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Correu i rol
                    Text("Email: ${currentUser?.email}", color = Color(0xFF3F51B5), fontSize = 14.sp)
                    Text("Rol: ${currentUser?.rol}", color = Color.Black, fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    Text("Historials:", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Botons d'accés a pantalles d'historial de rutes i recompenses
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                Log.d("PerfilUsuari", "Anant a pantalla de rutes")
                                navController.navigate("rutes")
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            elevation = ButtonDefaults.buttonElevation(4.dp)
                        ) {
                            Text("Rutes", color = Color.Black)
                        }

                        Button(
                            onClick = {
                                Log.d("PerfilUsuari", "Anant a pantalla de recompenses")
                                navController.navigate("historial_recompenses")
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            elevation = ButtonDefaults.buttonElevation(4.dp)
                        ) {
                            Text("Recompenses", color = Color.Black)
                        }
                    }
                }
            }
        }

        // Barra de navegació inferior
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, usuariViewModel = usuariViewModel, selectedItem = 0)
        }
    }
}