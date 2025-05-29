package cat.copernic.hvico.entrebicis.Recompensa.UI.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.hvico.entrebicis.Recompensa.Domain.RecompensaUseCases
import hector.vico.entrebicis.core.model.Recompensa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encarregat de gestionar l'estat i la lògica relacionada amb les recompenses.
 *
 * Es comunica amb els UseCases per obtenir les dades i exposa els resultats amb StateFlow.
 * Permet consultar, reservar, confirmar recollida i veure l'historial o recompenses disponibles.
 *
 * @param useCases Casos d'ús de negoci que encapsulen la lògica de dades.
 */
class RecompensaViewModel(private val useCases: RecompensaUseCases) : ViewModel() {

    // Flow de recompenses disponibles per reservar
    private val _recompensesDisponibles = MutableStateFlow<List<Recompensa>>(emptyList())
    val recompensesDisponibles: StateFlow<List<Recompensa>> = _recompensesDisponibles.asStateFlow()

    // Flow d'historial de recompenses (reservades, assignades, recollides)
    private val _historialRecompenses = MutableStateFlow<List<Recompensa>>(emptyList())
    val historialRecompenses: StateFlow<List<Recompensa>> = _historialRecompenses

    // Recompensa específica que s'està consultant
    private val _recompensaConsultada = MutableStateFlow<Recompensa?>(null)
    val recompensaConsultada: StateFlow<Recompensa?> = _recompensaConsultada

    // Recompensa assignada actualment a l'usuari
    private val _recompensaAssignada = MutableStateFlow<Recompensa?>(null)
    val recompensaAssignada: StateFlow<Recompensa?> = _recompensaAssignada

    // Missatge de resposta de la reserva (èxit o error)
    private val _reservaMissatge = MutableStateFlow<String?>(null)
    val reservaMissatge = _reservaMissatge.asStateFlow()

    /**
     * Carrega totes les recompenses disponibles.
     */
    fun carregarDisponibles() {
        viewModelScope.launch {
            try {
                Log.d("RecompensaVM", "Carregant recompenses disponibles...")
                val resultat = useCases.getRecompensesDisponibles()
                _recompensesDisponibles.value = resultat
                Log.d("RecompensaVM", "Recompenses carregades: ${resultat.size}")
            } catch (e: Exception) {
                Log.e("RecompensaVM", "Error: ${e.message}")
                _recompensesDisponibles.value = emptyList()
            }
        }
    }

    /**
     * Carrega l'historial de recompenses d'un usuari.
     *
     * @param emailUsuari correu electrònic de l'usuari.
     */
    fun carregarHistorial(emailUsuari: String) {
        viewModelScope.launch {
            try {
                Log.d("RecompensaVM", "Carregant historial per $emailUsuari")
                val resultat = useCases.getHistorialRecompenses(emailUsuari)
                _historialRecompenses.value = resultat
                Log.d("RecompensaVM", "Historial: ${resultat.size} recompenses")
            } catch (e: Exception) {
                Log.e("RecompensaVM", "Error: ${e.message}")
                _historialRecompenses.value = emptyList()
            }
        }
    }

    /**
     * Consulta els detalls d’una recompensa pel seu ID.
     *
     * @param id ID de la recompensa.
     */
    fun consultarRecompensa(id: Long) {
        viewModelScope.launch {
            try {
                Log.d("RecompensaVM", "Consultant recompensa amb ID $id")
                val recompensa = useCases.consultarRecompensa(id)
                _recompensaConsultada.value = recompensa
                Log.d("RecompensaVM", "Recompensa trobada: ${recompensa?.nom}")
            } catch (e: Exception) {
                Log.e("RecompensaVM", "Error consultant: ${e.message}")
                _recompensaConsultada.value = null
            }
        }
    }

    /**
     * Reserva una recompensa per a un usuari.
     *
     * @param id ID de la recompensa a reservar.
     * @param email correu de l’usuari.
     */
    fun reservarRecompensa(id: Long, email: String) {
        viewModelScope.launch {
            Log.d("RecompensaVM", "Intentant reservar recompensa $id per $email")
            val response = useCases.reservarRecompensa(id, email)
            if (response.isSuccessful) {
                _recompensaConsultada.value = response.body()
                _reservaMissatge.value = "Reserva completada correctament."
                Log.d("RecompensaVM", "Reserva completada.")
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error inesperat"
                _reservaMissatge.value = errorMsg
                Log.e("RecompensaVM", "Error reservant recompensa: $errorMsg")
            }
        }
    }

    /**
     * Carrega la recompensa assignada actualment a l’usuari.
     *
     * @param email correu electrònic de l’usuari.
     */
    fun carregarRecompensaAssignada(email: String) {
        viewModelScope.launch {
            try {
                Log.d("RecompensaVM", "Carregant assignada per $email")
                val recompensa = useCases.getRecompensaAssignada(email)
                _recompensaAssignada.value = recompensa
                Log.d("RecompensaVM", "Assignada: ${recompensa?.nom}")
            } catch (e: Exception) {
                Log.e("RecompensaVM", "Error: ${e.message}")
                _recompensaAssignada.value = null
            }
        }
    }

    /**
     * Confirma que l’usuari ha recollit la recompensa (canvi d’estat a RECOLLIDA).
     *
     * @param id ID de la recompensa a confirmar.
     */
    fun confirmarRecollida(id: Long) {
        viewModelScope.launch {
            try {
                Log.d("RecompensaVM", "Confirmant recollida de $id")
                val response = useCases.confirmarRecollida(id)
                if (response.isSuccessful) {
                    _recompensaAssignada.value = response.body()
                    Log.d("RecompensaVM", "Recollida confirmada.")
                } else {
                    Log.e("RecompensaVM", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("RecompensaVM", "Excepció: ${e.message}")
            }
        }
    }
}