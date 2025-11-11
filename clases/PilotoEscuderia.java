package clases;


/**
 * Clase asociativa que vincula un Piloto a una Escuderia durante un período de tiempo.
 * Representa un contrato, con una fecha de inicio (desdeFecha) y una de fin (hastaFecha).
 */
public class PilotoEscuderia {
    private String desdeFecha;
    private String hastaFecha;
    private Piloto piloto;
    private Escuderia escuderia;

    /**
     * Constructor por defecto.
     * Inicializa todos los campos como nulos o vacíos.
     */
    public PilotoEscuderia() {
        this.desdeFecha = "";
        this.hastaFecha = "";
        this.piloto = null;
        this.escuderia = null;
    }

    /**
     * Constructor completo para crear el contrato.
     *
     * @param desdeFecha La fecha de inicio del contrato.
     * @param hastaFecha La fecha de fin del contrato (vacío si está activo).
     * @param piloto El Piloto contratado.
     * @param escuderia La Escuderia que contrata.
     */
    public PilotoEscuderia(String desdeFecha, String hastaFecha, Piloto piloto, Escuderia escuderia) {
        this.desdeFecha = desdeFecha;
        this.hastaFecha = hastaFecha;
        this.piloto = piloto;
        this.escuderia = escuderia;
    }

    // --- Getters y Setters ---

    /**
     * Obtiene la fecha de inicio del contrato.
     * @return La fecha de inicio.
     */
    public String getDesdeFecha() {
        return desdeFecha;
    }

    /**
     * Establece la fecha de inicio del contrato.
     * @param desdeFecha La nueva fecha de inicio.
     */
    public void setDesdeFecha(String desdeFecha) {
        this.desdeFecha = desdeFecha;
    }

    /**
     * Obtiene la fecha de fin del contrato.
     * @return La fecha de fin (puede ser vacía o nula si el contrato está activo).
     */
    public String getHastaFecha() {
        return hastaFecha;
    }

    /**
     * Establece la fecha de fin del contrato.
     * (Usado por LogicaGestion.desvincularPiloto).
     * @param hastaFecha La nueva fecha de fin.
     */
    public void setHastaFecha(String hastaFecha) {
        this.hastaFecha = hastaFecha;
    }

    /**
     * Obtiene el piloto de este contrato.
     * @return El Piloto.
     */
    public Piloto getPiloto() {
        return piloto;
    }

    /**
     * Establece el piloto de este contrato.
     * @param piloto El nuevo Piloto.
     */
    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }

    /**
     * Obtiene la escudería de este contrato.
     * @return La Escuderia.
     */
    public Escuderia getEscuderia() {
        return escuderia;
    }

    /**
     * Establece la escudería de este contrato.
     * @param escuderia La nueva Escuderia.
     */
    public void setEscuderia(Escuderia escuderia) {
        this.escuderia = escuderia;
    }
    @Override
    public String toString() {
        return "El piloto estará en la escuderia (" +
                "desdeFecha:'" + desdeFecha + '\'' +
                ", hastaFecha:'" + hastaFecha + '\'' +
                ", piloto:" + piloto +
                ", escuderia:" + escuderia +
                ')';
    }
}