package logica;

import clases.Piloto;

/**
 * Clase contenedora temporal usada para los informes de ranking.
 * Almacena un objeto Piloto y su puntaje total calculado.
 * Implementa {@link Comparable} para facilitar el ordenamiento descendente.
 */
public class PilotoPuntaje implements Comparable<PilotoPuntaje> {

    private Piloto piloto;
    private int puntajeTotal;

    /**
     * Constructor de PilotoPuntaje.
     *
     * @param piloto El objeto Piloto.
     * @param puntajeTotal El puntaje total calculado para este piloto.
     */
    public PilotoPuntaje(Piloto piloto, int puntajeTotal) {
        this.piloto = piloto;
        this.puntajeTotal = puntajeTotal;
    }

    /**
     * Obtiene el objeto Piloto.
     * @return El Piloto.
     */
    public Piloto getPiloto() {
        return piloto;
    }

    /**
     * Obtiene el puntaje total.
     * @return El puntaje total.
     */
    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    /**
     * Representación en String, útil para debugging o listas simples.
     * @return Una cadena (ej. "Max Verstappen: 300 puntos").
     */
    @Override
    public String toString() {
        return piloto.getNombre() + " " + piloto.getApellido() + ": " + puntajeTotal + " puntos";
    }

    /**
     * Compara este objeto con otro PilotoPuntaje.
     * Implementado para ordenar de mayor a menor puntaje (orden descendente).
     *
     * @param otro El otro objeto PilotoPuntaje a comparar.
     * @return Un entero negativo si este puntaje es mayor, positivo si es menor.
     */
    @Override
    public int compareTo(PilotoPuntaje otro) {
        // Orden descendente (el más alto primero)
        return Integer.compare(otro.puntajeTotal, this.puntajeTotal);
    }
}