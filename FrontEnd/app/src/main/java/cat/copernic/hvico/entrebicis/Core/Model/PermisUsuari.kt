package cat.copernic.hvico.entrebicis.Core.Model

/**
 * Representa els diferents rols que pot tenir un usuari dins del sistema.
 *
 * @property CICLISTA Usuari amb permisos limitats, pot realitzar rutes, veure recompenses i modificar el seu perfil.
 * @property ADMIN Usuari amb permisos complets, pot gestionar usuaris, recompenses i paràmetres del sistema.
 */
enum class PermisUsuari {
    CICLISTA,
    ADMIN
}
