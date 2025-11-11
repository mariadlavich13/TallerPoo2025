package clases;

/**
 * Clase base abstracta para Piloto y Mecanico.
 * Contiene información personal básica como DNI, nombre, apellido y país.
 */
public class Persona {
    private String dni;
    private String nombre;
    private String apellido;
    private Pais pais;          // Relación con Pais

    /**
     * Constructor por defecto.
     */
    public Persona() {
    }

    /**
     * Constructor para crear una persona.
     *
     * @param dni DNI de la persona.
     * @param nombre Nombre de la persona.
     * @param apellido Apellido de la persona.
     * @param pais País de origen.
     */
    public Persona(String dni, String nombre, String apellido, Pais pais) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.pais = pais;
    }

    // --- Getters y Setters ---

    /**
     * Obtiene el DNI de la persona.
     * @return El DNI.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI de la persona.
     * @param dni El nuevo DNI.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre de la persona.
     * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la persona.
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido de la persona.
     * @return El apellido.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido de la persona.
     * @param apellido El nuevo apellido.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el país de origen de la persona.
     * @return El Pais.
     */
    public Pais getPais() {
        return pais;
    }

    /**
     * Establece el país de origen de la persona.
     * @param pais El nuevo Pais.
     */
    public void setPais(Pais pais) {
        this.pais = pais;
    }
    
    /**
     * Representación en String de la Persona.
     * @return Una cadena con DNI, nombre, apellido y ID del país.
     */
    @Override
    public String toString() {
        return  "Nombre: " + nombre + " " + apellido + " " +"País: " + (pais != null ? pais.getDescripcion() : "Sin país");
    }
}