package clases;


/**
 * Clase para almacenar el resultado final de un piloto en una carrera específica.
 * Asocia un Piloto con su posición final y la Carrera en la que compitió.
 */
public class ResultadoCarrera {
    private Piloto piloto;
    private int posicion;
    private Carrera carrera;

    /**
     * Constructor para crear un nuevo resultado de carrera.
     *
     * @param piloto El Piloto que obtuvo el resultado.
     * @param posicion La posición final (1, 2, 3...).
     * @param carrera La Carrera en la que se obtuvo el resultado.
     */
    public ResultadoCarrera(Piloto piloto, int posicion, Carrera carrera) {
        this.piloto = piloto;
        this.posicion = posicion;
        this.carrera = carrera;
    }

    /**
     * Obtiene el piloto de este resultado.
     * @return El Piloto.
     */
    public Piloto getPiloto() {
        return piloto;
    }

    /**
     * Obtiene la posición final de este resultado.
     * @return La posición.
     */
    public int getPosicion() {
        return posicion;
    }

    /**
     * Obtiene la carrera de este resultado.
     * @return La Carrera.
     */
    public Carrera getCarrera() {
        return carrera;
    }
}