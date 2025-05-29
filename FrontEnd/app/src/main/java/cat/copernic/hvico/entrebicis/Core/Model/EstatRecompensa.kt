package cat.copernic.hvico.entrebicis.Core.Model

/**
 * Enum que representa els diferents estats que pot tenir una recompensa.
 *
 * @property DISPONIBLE La recompensa encara no ha estat reservada per cap usuari.
 * @property RESERVADA La recompensa ha estat reservada per un usuari però encara no assignada.
 * @property ASSIGNADA La recompensa ha estat validada i assignada a l'usuari.
 * @property RECOLLIDA La recompensa ja ha estat recollida físicament per l'usuari.
 */
enum class EstatRecompensa {
    DISPONIBLE,
    RESERVADA,
    ASSIGNADA,
    RECOLLIDA
}