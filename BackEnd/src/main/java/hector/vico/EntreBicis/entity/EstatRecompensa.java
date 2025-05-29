package hector.vico.EntreBicis.entity;

/**
 * Enum que representa els possibles estats d'una recompensa dins del sistema EntreBicis.
 * 
 * <ul>
 *   <li><b>DISPONIBLE:</b> Recompensa visible i pot ser reservada per un usuari.</li>
 *   <li><b>RESERVADA:</b> Ja ha estat reservada per un usuari, però encara no assignada.</li>
 *   <li><b>ASSIGNADA:</b> Confirmació que la recompensa ha estat aprovada per l'equip i està pendent de recollir.</li>
 *   <li><b>RECOLLIDA:</b> L'usuari ha recollit físicament la recompensa.</li>
 * </ul>
 */
public enum EstatRecompensa {
    DISPONIBLE,  // La recompensa està lliure per ser reservada
    RESERVADA,   // Ja ha estat reservada per un usuari
    ASSIGNADA,   // Ha estat assignada de manera oficial a un usuari
    RECOLLIDA    // L'usuari ha recollit la recompensa
}
