package cat.copernic.hvico.entrebicis.Recompensa.Domain

import cat.copernic.hvico.entrebicis.Recompensa.Data.RecompensaRepository
import hector.vico.entrebicis.core.model.Recompensa
import retrofit2.Response

class RecompensaUseCases(private val repository: RecompensaRepository) {

    suspend fun getRecompensesDisponibles(): List<Recompensa> {
        return repository.getRecompensesDisponibles()
    }

    suspend fun getHistorialRecompenses(email: String): List<Recompensa> {
        return repository.getHistorialRecompenses(email)
    }

    suspend fun consultarRecompensa(id: Long): Recompensa {
        return repository.consultarRecompensa(id)
    }

    suspend fun reservarRecompensa(id: Long, email: String): Response<Recompensa> {
        return repository.reservarRecompensa(id, email)
    }

    suspend fun getRecompensaAssignada(email: String): Recompensa {
        return repository.getRecompensaAssignada(email)
    }

    suspend fun confirmarRecollida(id: Long): Response<Recompensa> {
        return repository.confirmarRecollida(id)
    }
}