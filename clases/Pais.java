package clases;

import java.util.List;
import java.util.ArrayList;

/**
 * Representa un País.
 * Funciona como una entidad central para agrupar Personas, Escuderias,
 * Circuitos y Carreras por nacionalidad u ubicación.
 */
public class Pais {
    private int idPais;
    private String descripcion;
    private List<Persona> personas;   // Relación con Persona
    private List<Escuderia> escuderias; // Relación con Escuderia
    private List<Circuito> circuitos; // Relación con Circuito
    private List<Carrera> carreras; // Relación con Carrera

    /**
     * Constructor por defecto.
     * Inicializa todas las listas como vacías.
     */
    public Pais() {
        this.personas = new ArrayList<>();
        this.escuderias = new ArrayList<>();
        this.circuitos = new ArrayList<>();
        this.carreras = new ArrayList<>();
    }

    /**
     * Constructor completo para crear un país.
     *
     * @param idPais El ID único del país.
     * @param descripcion El nombre del país (ej. "Italia").
     * @param personas Lista de personas de este país.
     * @param escuderias Lista de escuderías de este país.
     * @param circuitos Lista de circuitos en este país.
     * @param carreras Lista de carreras en este país.
     */
    public Pais(int idPais, String descripcion, List<Persona> personas, List<Escuderia> escuderias, List<Circuito> circuitos, List<Carrera> carreras) {
        this.idPais = idPais;
        this.descripcion = descripcion;
        this.personas = personas;
        this.escuderias = escuderias;
        this.circuitos = circuitos;
        this.carreras = carreras;
    }

    // --- Getters y Setters ---

    /**
     * Obtiene el ID del país.
     * @return El ID.
     */
    public int getIdPais() {
        return idPais;
    }

    /**
     * Establece el ID del país.
     * @param idPais El nuevo ID.
     */
    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    /**
     * Obtiene la descripción (nombre) del país.
     * @return El nombre del país.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción (nombre) del país.
     * @param descripcion El nuevo nombre del país.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Agrega una persona a la lista de personas de este país.
     * @param persona La Persona a agregar.
     */
    public void agregarPersona(Persona persona) {
        this.personas.add(persona);
    }

    /**
     * Agrega una escudería a la lista de escuderías de este país.
     * @param escuderia La Escuderia a agregar.
     */
    public void agregarEscuderia(Escuderia escuderia) {
        this.escuderias.add(escuderia);
    }

    /**
     * Agrega un circuito a la lista de circuitos de este país.
     * @param circuito El Circuito a agregar.
     */
    public void agregarCircuito(Circuito circuito) {
        this.circuitos.add(circuito);
    }

    /**
     * Agrega una carrera a la lista de carreras de este país.
     * @param carrera La Carrera a agregar.
     */
    public void agregarCarrera(Carrera carrera) {
        this.carreras.add(carrera);
    }

    @Override
    public String toString() {
        return descripcion;
    }
}