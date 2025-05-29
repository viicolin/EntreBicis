package hector.vico.EntreBicis.entity;

/**
 * Enum que representa els diferents estats possibles d'una ruta dins del sistema EntreBicis.
 *
 * <ul>
 *   <li><b>PENDENT:</b> La ruta ha estat registrada però encara no ha estat validada per un administrador.</li>
 *   <li><b>VALIDA:</b> La ruta ha estat revisada i acceptada com vàlida, i s'ha atorgat el saldo corresponent.</li>
 *   <li><b>NO_VALIDADA:</b> La ruta ha estat revisada i s'ha determinat que no compleix els requisits.</li>
 * </ul>
 */
public enum EstatRuta {
    PENDENT,       // Ruta pendent de validació per part d'un administrador
    VALIDA,        // Ruta validada i acceptada com a correcta
    NO_VALIDADA    // Ruta revisada i rebutjada per no complir els criteris
}