package clases;

/**
 * Enumeración que define el sistema de puntuación oficial de la Fórmula 1.
 * Asigna puntos fijos a las posiciones del 1º al 10º.
 */
public enum Puntaje {
    PRIMERO(25),
    SEGUNDO(18),
    TERCERO(15),
    CUARTO(12),
    QUINTO(10),
    SEXTO(8),
    SEPTIMO(6),
    OCTAVO(4),
    NOVENO(2),
    DECIMO(1);

    private final int puntos;
    
    /**
     * Constructor interno del Enum.
     * @param puntos El valor en puntos para la posición.
     */
    Puntaje(int puntos) {
        this.puntos = puntos;
    }
    
    /**
     * Obtiene el valor en puntos de esta constante de Enum.
     * @return Los puntos.
     */
    public int getPuntos() {
        return puntos;
    }
    
    /**
     * Método estático para obtener el puntaje según la posición final.
     * Devuelve 0 si la posición es mayor a 10.
     *
     * @param posicion La posición final en la carrera (1, 2, 3...).
     * @return Los puntos correspondientes a esa posición.
     */
    public static int obtenerPuntaje(int posicion) {
        for (Puntaje p : values()) {
            // Compara la posición (1-based) con el ordinal del enum (0-based)
            if (p.ordinal() + 1 == posicion) {
                return p.getPuntos();
            }
        }
        return 0; // No hay puntos para posiciones fuera del top 10
    }
}