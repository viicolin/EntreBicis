package cat.copernic.hvico.entrebicis.Core.Model

/**
 * Enum que representa els diferents estats possibles d'una ruta en el sistema.
 *
 * @property PENDENT La ruta està en procés o encara no ha estat revisada.
 * @property VALIDA La ruta ha estat revisada i validada per un administrador.
 * @property NO_VALIDADA La ruta ha estat revisada però ha estat rebutjada (no vàlida).
 */
enum class EstatRuta {
    PENDENT,
    VALIDA,
    NO_VALIDADA
}