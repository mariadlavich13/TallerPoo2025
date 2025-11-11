package clases;

/**
 * Clase asociativa que vincula un Piloto y un Auto a una Carrera específica.
 * Representa la participación de un piloto con un auto en un evento.
 */
public class AutoPiloto {
    private String fechaAsignacion;
    private Piloto piloto; // Relación con Piloto
    private Auto auto; // Relación con Auto
    private Carrera carrera; // Relación con Carrera

    /**
     * Constructor por defecto.
     * Inicializa todos los campos como nulos o vacíos.
     */
    public AutoPiloto() {
        this.fechaAsignacion = "";
        this.piloto = null;
        this.auto = null;
        this.carrera = null;
    }

    /**
     * Constructor completo para crear la asociación.
     *
     * @param fechaAsignacion La fecha en que se realiza la asignación.
     * @param piloto El Piloto que participa.
     * @param auto El Auto utilizado.
     * @param carrera La Carrera en la que participan.
     */
    public AutoPiloto(String fechaAsignacion, Piloto piloto, Auto auto, Carrera carrera) {
        this.fechaAsignacion = fechaAsignacion;
        this.piloto = piloto;
        this.auto = auto;
        this.carrera = carrera;
    }

    // --- Getters y Setters ---

    /**
     * Obtiene la fecha de asignación.
     * @return La fecha.
     */
    public String getFechaAsignacion() {
        return fechaAsignacion;
    }

    /**
     * Establece la fecha de asignación.
     * @param fechaAsignacion La nueva fecha.
     */
    public void setFechaAsignacion(String fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    /**
     * Obtiene el piloto de esta participación.
     * @return El Piloto.
     */
    public Piloto getPiloto() {
        return piloto;
    }

    /**
     * Establece el piloto de esta participación.
     * @param piloto El nuevo piloto.
     */
    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }

    /**
     * Obtiene el auto de esta participación.
     * @return El Auto.
     */
    public Auto getAuto() {
        return auto;
    }

    /**
     * Establece el auto de esta participación.
     * @param auto El nuevo auto.
     */
    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    /**
     * Obtiene la carrera de esta participación.
     * @return La Carrera.
     */
    public Carrera getCarrera() {
        return carrera;
    }

    /**
     * Establece la carrera de esta participación.
     * (Usado por Carrera.agregarParticipante para mantener la relación bidireccional).
     * @param carrera La nueva carrera.
     */
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    @Override
    public String toString() {
        return "Fecha asignado: " + fechaAsignacion +
                " - Piloto: " + (piloto != null ? piloto.getNombre() + " " + piloto.getApellido() : "N/A") +
                ", Auto: " + (auto != null ? auto.getModelo() : "N/A") +
                ", Carrera: " + (carrera != null ? carrera.getCircuito().getNombre() : "N/A");
    }
}