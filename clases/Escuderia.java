package clases;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un equipo o Escudería de Fórmula 1.
 * Contiene listas de sus Mecánicos, Autos y los contratos con sus Pilotos (PilotoEscuderia).
 */
public class Escuderia {
    private String nombre;
    private List<Mecanico> mecanicos; // Relación con Mecanico
    private List<PilotoEscuderia> pilotosEscuderias = new ArrayList<>(); // Relación con (0...*) - (0...*) Piloto
    private Pais pais; // Relación con Pais
    private List<Auto> autos; //Asociación entre Escuderia (1) - (0...*) Auto

    /**
     * Constructor por defecto.
     * Inicializa una escudería con nombre "Desconocida" y listas vacías.
     */
    public Escuderia() {
        this.nombre = "Desconocida";
        this.mecanicos = new ArrayList<>();
        this.pilotosEscuderias = new ArrayList<>();
        this.autos = new ArrayList<>();
    }

    /**
     * Constructor completo para cargar una escudería con todas sus relaciones.
     *
     * @param nombre Nombre de la escudería.
     * @param mecanicos Lista de mecánicos.
     * @param pilotosEscuderias Lista de contratos piloto-escudería.
     * @param pais País de origen de la escudería.
     * @param autos Lista de autos de la escudería.
     */
    public Escuderia(String nombre, List<Mecanico> mecanicos, List<PilotoEscuderia> pilotosEscuderias, Pais pais, List<Auto> autos) {
        this.nombre = nombre;
        this.mecanicos = mecanicos;
        this.pilotosEscuderias = pilotosEscuderias;
        this.pais = pais;
        this.autos = autos;
    }

    /**
     * Constructor para crear una nueva escudería solo con su nombre.
     * Inicializa las listas para evitar NullPointerException.
     *
     * @param nombre El nombre de la escudería (ej. "ferrari").
     */
    public Escuderia(String nombre) {
        this.nombre = nombre;
        this.mecanicos = new ArrayList<>();
        this.pilotosEscuderias = new ArrayList<>();
        this.autos = new ArrayList<>();
    }
    
    // --- Getters y Setters ---

    /**
     * Obtiene el nombre de la escudería.
     * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la escudería.
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la lista de mecánicos de la escudería.
     * @return Lista de Mecanico.
     */
    public List<Mecanico> getMecanicos() {
        return mecanicos;
    }

    /**
     * Establece la lista de mecánicos de la escudería.
     * @param mecanicos La nueva lista de Mecanico.
     */
    public void setMecanicos(List<Mecanico> mecanicos) {
        this.mecanicos = mecanicos;
    }

    /**
     * Agrega un mecánico a la lista de la escudería.
     * @param mecanico El Mecanico a agregar.
     */
    public void agregarMecanico(Mecanico mecanico) {
        this.mecanicos.add(mecanico);
    }

    /**
     * Obtiene la lista de contratos (PilotoEscuderia) de la escudería.
     * @return Lista de PilotoEscuderia.
     */
    public List<PilotoEscuderia> getPilotosEscuderias() {
        return pilotosEscuderias;
    }

    /**
     * Establece la lista de contratos (PilotoEscuderia) de la escudería.
     * @param pilotosEscuderias La nueva lista de PilotoEscuderia.
     */
    public void setPilotosEscuderias(List<PilotoEscuderia> pilotosEscuderias) {
        this.pilotosEscuderias = pilotosEscuderias;
    }

    /**
     * Agrega un nuevo contrato (PilotoEscuderia) a la lista de la escudería.
     * @param pilotoEscuderia El contrato a agregar.
     */
    public void agregarPilotoEscuderia(PilotoEscuderia pilotoEscuderia) {
        this.pilotosEscuderias.add(pilotoEscuderia);
    }

    /**
     * Obtiene el país de origen de la escudería.
     * @return El Pais.
     */
    public Pais getPais() {
        return pais;
    }

    /**
     * Establece el país de origen de la escudería.
     * @param pais El nuevo Pais.
     */
    public void setPais(Pais pais) {
        this.pais = pais;
    }

    /**
     * Obtiene la lista de autos de la escudería.
     * @return Lista de Auto.
     */
    public List<Auto> getAutos() {
        return autos;
    }

    /**
     * Establece la lista de autos de la escudería.
     * @param autos La nueva lista de Auto.
     */
    public void setAutos(List<Auto> autos) {
        this.autos = autos;
    }

    /**
     * Agrega un auto a la escudería.
     * Establece la relación bidireccional asignando esta escudería al objeto Auto.
     *
     * @param a El Auto a agregar.
     */
    public void agregarAuto(Auto a) {
        if (a == null) return;
        this.autos.add(a);
        a.setEscuderia(this);
    }
    @Override
    public String toString() {
        return nombre;
    }
}