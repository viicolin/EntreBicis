package cat.copernic.hvico.entrebicis.Usuari.UI.ViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.hvico.entrebicis.Core.Model.ParametresSistema
import cat.copernic.hvico.entrebicis.Core.Model.Usuari
import cat.copernic.hvico.entrebicis.Usuari.Domain.UsuariUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream

/**
 * ViewModel per gestionar les accions i l'estat relacionat amb l'usuari.
 *
 * @param useCases Casos d'ús que encapsulen la lògica del domini relacionada amb usuaris.
 */
class UsuariViewModel(private val useCases: UsuariUseCases) : ViewModel() {

    // Estat del resultat del login (null, true, false)
    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess: StateFlow<Boolean?> = _loginSuccess

    // Usuari actual actiu
    private val _currentUser = MutableStateFlow<Usuari?>(null)
    val currentUser: StateFlow<Usuari?> get() = _currentUser

    // Estat de si la modificació de l'usuari ha tingut èxit
    private val _modificacioSuccess = MutableStateFlow<Boolean?>(null)
    val modificacioSuccess: StateFlow<Boolean?> = _modificacioSuccess

    // Paràmetres del sistema recuperats després del login
    private val _parametresSistema = MutableStateFlow<ParametresSistema?>(null)
    val parametresSistema: StateFlow<ParametresSistema?> get() = _parametresSistema

    // Missatge d’error que es pot mostrar per pantalla
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    /**
     * Inicia sessió amb les credencials donades.
     *
     * Si té èxit, carrega també les dades de l'usuari i els paràmetres del sistema.
     */
    fun login(email: String, contrasenya: String) {
        Log.d("UsuariViewModel", "Iniciant login per $email")
        viewModelScope.launch {
            val response = useCases.login(email, contrasenya)
            _loginSuccess.value = response.isSuccessful

            if (response.isSuccessful) {
                // Obté usuari si login correcte
                val userResponse = useCases.getUsuari(email)
                if (userResponse.isSuccessful) {
                    _currentUser.value = userResponse.body()
                    _loginSuccess.value = true

                    // També carrega els paràmetres del sistema
                    val parametresResponse = useCases.obtenirParametresSistema()
                    if (parametresResponse.isSuccessful) {
                        _parametresSistema.value = parametresResponse.body()
                    } else {
                        _parametresSistema.value = null
                    }
                } else {
                    _currentUser.value = null
                    _loginSuccess.value = false
                }
            }
        }
    }

    /**
     * Consulta les dades d’un usuari pel seu email.
     */
    fun consultarUsuari(email: String) {
        Log.d("UsuariViewModel", "Consultant usuari amb email: $email")
        viewModelScope.launch {
            val response = useCases.consultarUsuari(email)
            if (response.isSuccessful) {
                val usuari = response.body()
                _currentUser.value = usuari
            } else {
                _currentUser.value = null
            }
        }
    }

    /**
     * Tanca la sessió de l'usuari actual.
     */
    fun logout() {
        Log.d("UsuariViewModel", "Tancant sessió")
        _currentUser.value = null
        _loginSuccess.value = null
    }

    /**
     * Modifica les dades d’un usuari i actualitza l’estat segons el resultat.
     *
     * @param email Email identificador de l’usuari.
     * @param usuari Dades actualitzades de l’usuari.
     */
    fun modificarUsuari(email: String, usuari: Usuari) {
        Log.d("UsuariViewModel", "Modificant usuari amb email: $email")
        viewModelScope.launch {
            try {
                val response = useCases.modificarUsuari(email, usuari)
                if (response.isSuccessful && response.body() != null) {
                    _currentUser.value = response.body()
                    _modificacioSuccess.value = true
                } else {
                    val errorText = response.errorBody()?.string() ?: "Error desconegut"
                    _modificacioSuccess.value = false
                    _errorMessage.value = errorText
                }
            } catch (e: Exception) {
                _modificacioSuccess.value = false
                _errorMessage.value = "Error inesperat: ${e.message}"
            }
        }
    }

    /**
     * Converteix una cadena Base64 a un objecte Bitmap.
     *
     * @param base64String Imatge codificada en Base64.
     * @return Bitmap resultant o null si falla.
     */
    fun base64ToBitmap(base64String: String): Bitmap? {
        Log.d("UsuariViewModel", "Convertint string base64 a Bitmap")
        return try {
            val pureBase64 = base64String.substringAfter(",")
            val decodedBytes = Base64.decode(pureBase64, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Converteix una imatge seleccionada per URI a una cadena en Base64.
     *
     * @param context Context actual per accedir al content resolver.
     * @param uri URI de la imatge seleccionada.
     * @return Imatge en format Base64 o null si falla.
     */
    fun uriToBase64(context: Context, uri: Uri): String? {
        Log.d("UsuariViewModel", "Convertint URI a Base64")
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val byteArrayOutputStream = ByteArrayOutputStream()

            inputStream?.use { stream ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (stream.read(buffer).also { bytesRead = it } != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead)
                }
            }

            val byteArray = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Neteja el missatge d’error actual.
     */
    fun clearErrorMessage() {
        Log.d("UsuariViewModel", "Esborrant missatge d'error")
        _errorMessage.value = null
    }

    /**
     * Neteja l’estat de si la modificació s’ha completat amb èxit.
     */
    fun clearModificacioSuccess() {
        Log.d("UsuariViewModel", "Esborrant estat de modificació")
        _modificacioSuccess.value = null
    }
}