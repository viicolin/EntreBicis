package hector.vico.EntreBicis.entity;

/**
 * Enum que defineix els tipus de permisos o rols que pot tenir un usuari dins del sistema EntreBicis.
 *
 * <ul>
 *   <li><b>ADMIN:</b> Usuari amb permisos d’administració completa (gestió de rutes, usuaris, recompenses, etc.).</li>
 *   <li><b>CICLISTA:</b> Usuari normal que pot registrar rutes, consultar recompenses, etc.</li>
 * </ul>
 *
 * Aquest enum es pot utilitzar, per exemple, per controlar l'accés a funcionalitats o vistes específiques.
 */
public enum PermisUsuari {
    ADMIN,    // Permís d’administrador del sistema
    CICLISTA  // Permís d’usuari ciclista normal
}