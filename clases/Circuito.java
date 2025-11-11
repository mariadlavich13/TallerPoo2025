package clases;

/**
 * Representa una pista de carreras (Circuito).
 * Contiene su nombre, longitud y el país donde se ubica.
 */
public class Circuito {
    private String nombre;
    private int longitud;
    private Pais pais; // Relación con Pais

    /**
     * Constructor por defecto.
     * Inicializa un circuito con nombre vacío y longitud 0.
     */
    public Circuito() {
        this.nombre = "";
        this.longitud = 0;
    }

    /**
     * Constructor para crear un nuevo circuito.
     *
     * @param nombre El nombre del circuito (ej. "Circuito de Monaco").
     * @param longitud La longitud de la pista en kilómetros (ej. 3.337).
     * @param pais El objeto Pais donde se ubica el circuito.
     */
    public Circuito(String nombre, int longitud, Pais pais) {
        this.nombre = nombre;
        this.longitud = longitud;
        this.pais = pais;
    }
    
    // --- Getters y Setters ---

    /**
     * Obtiene el nombre del circuito.
     * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la longitud del circuito.
     * @return La longitud.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Establece el nombre del circuito.
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la longitud del circuito.
     * @param longitud La nueva longitud.
     */
    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    /**
     * Obtiene el país del circuito.
     * @return El Pais.
     */
    public Pais getPais() {
        return pais;
    }

    /**
     * Establece el país del circuito.
     * @param pais El nuevo Pais.
     */
    public void setPais(Pais pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return nombre + " (" + longitud + " km), " + pais.getDescripcion();
    }
}