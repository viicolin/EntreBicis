package cat.copernic.hvico.entrebicis.Usuari.UI.Screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cat.copernic.hvico.entrebicis.R
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel
import cat.copernic.hvico.entrebicis.Core.ui.TopBar
import cat.copernic.hvico.entrebicis.Core.ui.theme.BottomNavBar

/**
 * Pantalla per modificar les dades personals de l'usuari actual.
 *
 * @param navController Controlador de navegació per canviar de pantalla.
 * @param usuariViewModel ViewModel que conté l'usuari actual i les accions relacionades.
 */
@Composable
fun ModificarUsuariScreen(navController: NavController, usuariViewModel: UsuariViewModel) {
    val context = LocalContext.current
    val usuari by usuariViewModel.currentUser.collectAsState()
    val errorMessage by usuariViewModel.errorMessage.collectAsState()
    val modificacioSuccess by usuariViewModel.modificacioSuccess.collectAsState()

    // Variables d'estat per cada camp editable
    var nom by remember { mutableStateOf("") }
    var cognoms by remember { mutableStateOf("") }
    var dataNaixement by remember { mutableStateOf("") }
    var poblacio by remember { mutableStateOf("") }
    var telefon by remember { mutableStateOf("") }
    var novaContrasenya by remember { mutableStateOf("") }
    var repetirContrasenya by remember { mutableStateOf("") }
    var fotoPerfil by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher per seleccionar imatge de la galeria
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            Log.d("ModificarUsuari", "Imatge seleccionada: $it")
            imageUri = it
            fotoPerfil = usuariViewModel.uriToBase64(context, it) ?: ""
        }
    }

    // Quan es carrega l'usuari, omple els camps amb les dades actuals
    LaunchedEffect(usuari) {
        usuari?.let {
            Log.d("ModificarUsuari", "Usuari carregat: ${it.email}")
            nom = it.nom
            cognoms = it.cognoms
            dataNaixement = it.dataNaixement
            poblacio = it.poblacio
            telefon = it.telefon
            fotoPerfil = it.foto ?: ""
        }
    }

    // Mostra errors si n’hi ha
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Log.e("ModificarUsuari", "Error rebut: $it")
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            usuariViewModel.clearErrorMessage()
        }
    }

    // Si la modificació és correcta, navega al perfil
    LaunchedEffect(modificacioSuccess) {
        if (modificacioSuccess == true) {
            usuari?.let {
                Log.d("ModificarUsuari", "Modificació correcta, navegant al perfil de ${it.email}")
                navController.navigate("perfil_usuari/${it.email}") {
                    popUpTo("modificarUsuari/${it.email}") { inclusive = true }
                }
            }
            usuariViewModel.clearModificacioSuccess()
        }
    }

    val greenGradient = Brush.verticalGradient(colors = listOf(Color(0xFF9CCC65), Color(0xFFDCEDC8)))
    val brownColor = Color(0xFF6D4C41)

    // Contenidor principal de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = greenGradient)
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(navController, usuariViewModel) // Barra superior reutilitzable

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Edita les teves dades", fontSize = 22.sp)
                Spacer(modifier = Modifier.height(16.dp))

                // Targeta que conté els camps de formulari
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Zona de foto de perfil
                        Box(contentAlignment = Alignment.BottomEnd) {
                            // Mostra imatge seleccionada o actual o per defecte
                            if (imageUri != null) {
                                AsyncImage(
                                    model = imageUri,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                )
                            } else if (fotoPerfil.isNotEmpty()) {
                                Image(
                                    bitmap = usuariViewModel.base64ToBitmap(fotoPerfil)!!.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.default_user),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            // Icona per afegir nova imatge
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(brownColor, CircleShape)
                                    .padding(4.dp)
                                    .clickable {
                                        Log.d("ModificarUsuari", "S'ha fet clic per seleccionar imatge")
                                        launcher.launch("image/*")
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Camps de text
                        InputField("Nom", nom) { nom = it }
                        InputField("Cognoms", cognoms) { cognoms = it }

                        InputField(
                            label = "Data de naixement (yyyy-MM-dd)",
                            value = dataNaixement,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            onValueChange = { input ->
                                if (input.all { it.isDigit() || it == '-' }) {
                                    dataNaixement = input
                                }
                            }
                        )

                        InputField("Població", poblacio) { poblacio = it }

                        InputField(
                            "Telèfon", telefon,
                            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        ) { input -> telefon = input.filter { c -> c.isDigit() } }

                        InputField("Nova Contrasenya", novaContrasenya, isPassword = true) { novaContrasenya = it }
                        InputField("Repetir Contrasenya", repetirContrasenya, isPassword = true) { repetirContrasenya = it }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Botó per guardar els canvis
                        Button(
                            onClick = {
                                Log.d("ModificarUsuari", "Botó guardar clicat")

                                // Validació de la data
                                try {
                                    val formatter = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                                    val parsedDate = formatter.parse(dataNaixement)
                                    if (parsedDate != null && parsedDate.after(java.util.Date())) {
                                        Toast.makeText(context, "La data no pot ser futura", Toast.LENGTH_LONG).show()
                                        return@Button
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "La data ha de ser en format yyyy-MM-dd", Toast.LENGTH_LONG).show()
                                    return@Button
                                }

                                // Validació contrasenya
                                if (novaContrasenya != repetirContrasenya) {
                                    Toast.makeText(context, "Les contrasenyes no coincideixen", Toast.LENGTH_LONG).show()
                                    return@Button
                                }

                                // Validació telèfon
                                if (telefon.length != 9) {
                                    Toast.makeText(context, "El telèfon ha de tenir exactament 9 dígits.", Toast.LENGTH_LONG).show()
                                    return@Button
                                }

                                // Si tot és correcte, actualitza usuari
                                usuari?.let {
                                    val usuariActualitzat = it.copy(
                                        nom = nom,
                                        cognoms = cognoms,
                                        dataNaixement = dataNaixement,
                                        poblacio = poblacio,
                                        telefon = telefon,
                                        contrasenya = if (novaContrasenya.isNotEmpty()) novaContrasenya else "",
                                        foto = fotoPerfil
                                    )
                                    usuariViewModel.modificarUsuari(it.email, usuariActualitzat)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Text("Guardar Canvis", color = Color.Black)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Barra de navegació inferior
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(navController = navController, usuariViewModel = usuariViewModel, selectedItem = 0)
        }
    }
}

/**
 * Composable reutilitzable per mostrar un camp de text amb opcions de teclat i contrasenya.
 *
 * @param label Etiqueta mostrada sobre el camp.
 * @param value Valor actual del camp.
 * @param keyboardOptions Opcions del teclat (text, numèric...).
 * @param isPassword Si és un camp de contrasenya.
 * @param onValueChange Acció a executar quan el valor canvia.
 */
@Composable
fun InputField(
    label: String,
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange, // Guarda nou valor
        label = { Text(label) },
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(15.dp),
        singleLine = true // No permet múltiples línies
    )
}