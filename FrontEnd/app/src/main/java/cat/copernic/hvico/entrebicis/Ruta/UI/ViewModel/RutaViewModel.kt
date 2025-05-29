package cat.copernic.hvico.entrebicis.Ruta.UI.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.hvico.entrebicis.Core.Model.PuntsGPS
import cat.copernic.hvico.entrebicis.Core.Model.Ruta
import cat.copernic.hvico.entrebicis.Ruta.Domain.RutaUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de gestionar les rutes:
 * iniciar, finalitzar, consultar i afegir punts GPS.
 * Utilitza un cas d'ús (use case) per accedir al repositori.
 */
class RutaViewModel(private val useCases: RutaUseCases) : ViewModel() {

    // Ruta actual iniciada o consultada
    private val _ruta = MutableStateFlow<Ruta?>(null)
    val ruta: StateFlow<Ruta?> = _ruta

    // Llista de rutes de l'usuari
    private val _rutes = MutableStateFlow<List<Ruta>>(emptyList())
    val rutes: StateFlow<List<Ruta>> = _rutes

    // Ruta finalitzada recentment
    private val _rutaFinalitzada = MutableStateFlow<Ruta?>(null)
    val rutaFinalitzada: StateFlow<Ruta?> = _rutaFinalitzada

    // Missatge d'error si passa algun problema
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Inicia una nova ruta per l'usuari amb el correu donat.
     * @param email Correu electrònic de l'usuari
     */
    fun iniciarRuta(email: String) {
        viewModelScope.launch {
            try {
                Log.d("RutaViewModel", "Iniciant ruta per email: $email")
                _ruta.value = useCases.iniciarRuta(email) // Obté nova ruta del cas d'ús
                Log.d("RutaViewModel", "Ruta iniciada correctament: ${_ruta.value}")
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message // Guarda l'error per mostrar-lo a la UI
                Log.e("RutaViewModel", "Error iniciant ruta: ${e.message}")
            }
        }
    }

    /**
     * Afegeix un punt GPS a la ruta actual.
     * @param punt Objecte de tipus PuntsGPS amb latitud, longitud i temps
     */
    fun afegirPuntGPS(punt: PuntsGPS) {
        val idRuta = _ruta.value?.id
        if (idRuta != null) {
            viewModelScope.launch {
                try {
                    Log.d("RutaViewModel", "Afegint punt GPS a la ruta $idRuta: $punt")
                    useCases.afegirPuntGPS(idRuta, punt) // Afegeix el punt al backend
                    Log.d("RutaViewModel", "Punt GPS afegit correctament")
                    _error.value = null
                } catch (e: Exception) {
                    _error.value = e.message
                    Log.e("RutaViewModel", "Error afegint punt GPS: ${e.message}")
                }
            }
        } else {
            // Si no hi ha ruta, avisa l'usuari
            _error.value = "No hi ha cap ruta en curs."
            Log.w("RutaViewModel", "Intent d'afegir punt sense ruta en curs.")
        }
    }

    /**
     * Finalitza la ruta actual, marcant-la com a completada.
     */
    fun finalitzarRuta() {
        viewModelScope.launch {
            val ruta = _ruta.value
            if (ruta != null) {
                try {
                    Log.d("RutaViewModel", "Finalitzant ruta amb id: ${ruta.id}")
                    val finalitzada = useCases.finalitzarRuta(ruta.id!!) // Marca com a finalitzada al backend
                    _ruta.value = finalitzada
                    _rutaFinalitzada.value = finalitzada
                    Log.d("RutaViewModel", "Ruta finalitzada: $finalitzada")
                } catch (e: Exception) {
                    _error.value = e.message
                    Log.e("RutaViewModel", "Error finalitzant ruta: ${e.message}")
                }
            } else {
                Log.w("RutaViewModel", "No hi ha ruta per finalitzar.")
            }
        }
    }

    /**
     * Llista totes les rutes registrades per l'usuari.
     * @param email Correu de l'usuari
     */
    fun llistarRutes(email: String) {
        viewModelScope.launch {
            try {
                Log.d("RutaViewModel", "Llistant rutes per email: $email")
                _rutes.value = useCases.llistarRutes(email) // Carrega totes les rutes de l'usuari
                Log.d("RutaViewModel", "Rutes carregades: ${_rutes.value.size}")
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("RutaViewModel", "Error llistant rutes: ${e.message}")
            }
        }
    }

    /**
     * Consulta una ruta concreta per ID i usuari.
     * @param id ID de la ruta
     * @param email Correu de l'usuari
     */
    fun consultarRuta(id: Long, email: String) {
        viewModelScope.launch {
            try {
                Log.d("RutaViewModel", "Consultant ruta ID $id per email: $email")
                _ruta.value = useCases.consultarRuta(id, email) // Carrega ruta concreta
                Log.d("RutaViewModel", "Ruta consultada: ${_ruta.value}")
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("RutaViewModel", "Error consultant ruta: ${e.message}")
            }
        }
    }
}
