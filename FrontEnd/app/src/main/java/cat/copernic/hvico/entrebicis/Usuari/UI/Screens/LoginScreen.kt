package cat.copernic.hvico.entrebicis.Usuari.UI.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.hvico.entrebicis.R
import cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel.UsuariViewModel

/**
 * Pantalla de login que permet als usuaris introduir el seu correu electrònic i contrasenya
 * per accedir a l’aplicació. Mostra errors si els camps estan buits o si les credencials no són vàlides.
 *
 * @param navController controlador de navegació per canviar de pantalla.
 * @param usuariViewModel viewmodel que gestiona la lògica de login.
 */
@Composable
fun LoginScreen(navController: NavController, usuariViewModel: UsuariViewModel) {
    var email by remember { mutableStateOf("") } // Estat per guardar l'email introduït
    var contrasenya by remember { mutableStateOf("") } // Estat per guardar la contrasenya
    val loginSuccess by usuariViewModel.loginSuccess.collectAsState() // Observa si l'inici ha estat correcte
    var errorText by remember { mutableStateOf<String?>(null) } // Missatge d'error (si escau)

    // Quan canviï el resultat del login, actua en conseqüència
    LaunchedEffect(loginSuccess) {
        Log.d("LoginScreen", "loginSuccess canviat: $loginSuccess")
        if (loginSuccess == true) {
            Log.d("LoginScreen", "Login correcte, navegant a iniciar_ruta")
            navController.navigate("iniciar_ruta") // Navega a la pantalla de ruta
        } else if (loginSuccess == false) {
            Log.d("LoginScreen", "Login incorrecte")
            if (email.isNotBlank() && contrasenya.isNotBlank()) {
                errorText = "Error d'inici de sessió." // Mostra error si les credencials no són vàlides
            }
        }
    }

    // Contenidor principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7FB46A)), // Fons verd
        contentAlignment = Alignment.Center
    ) {
        // Contingut centrat
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Logo de l'app
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 16.dp)
            )

            // Títol principal
            Text(
                text = "EntreBicis",
                fontSize = 32.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp)) // Espaiat

            // Caixa del formulari
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(24.dp)) // Fons gris suau
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Inici de Sessió",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Etiqueta d'email
                Text(
                    text = "Email:",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )

                // Camp d'email
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorText = null // Elimina errors quan es torna a escriure
                        Log.d("LoginScreen", "Email modificat: $email")
                    },
                    placeholder = { Text("Escriu...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(50),
                    singleLine = true
                )

                // Etiqueta de contrasenya
                Text(
                    text = "Contrasenya:",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )

                // Camp de contrasenya
                OutlinedTextField(
                    value = contrasenya,
                    onValueChange = {
                        contrasenya = it
                        errorText = null
                        Log.d("LoginScreen", "Contrasenya modificada")
                    },
                    placeholder = { Text("Escriu...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(50),
                    visualTransformation = PasswordVisualTransformation(), // Oculta el text
                    singleLine = true
                )

                // Missatge d'error si hi ha algun
                if (errorText != null) {
                    Text(
                        text = errorText!!,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Log.d("LoginScreen", "Missatge d'error mostrat: $errorText")
                }

                // Botó per confirmar el login
                Button(
                    onClick = {
                        Log.d("LoginScreen", "Botó confirmar clicat")
                        if (email.isBlank() || contrasenya.isBlank()) {
                            errorText = "Tots els camps són obligatoris."
                            Log.d("LoginScreen", "Camps buits detectats")
                            return@Button
                        }

                        errorText = null
                        usuariViewModel.login(email, contrasenya)
                        Log.d("LoginScreen", "Login intentat amb email: $email")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(45.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Verd
                ) {
                    Text("Confirmar", fontSize = 16.sp, color = Color.Black)
                }

                // Botó de recuperar contrasenya (placeholder)
                TextButton(
                    onClick = { Log.d("LoginScreen", "Botó recuperar contrasenya clicat") },
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text(
                        text = "Has oblidat la teva contrasenya?",
                        color = Color(0xFF4B4B4B)
                    )
                }
            }
        }
    }
}