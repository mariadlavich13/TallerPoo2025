package clases;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un Mecánico, extendiendo la clase Persona.
 * Almacena su especialidad, años de experiencia y las escuderías para las que trabaja.
 */
public class Mecanico extends Persona {
    private Especialidad especialidad;
    private int aniosExperiencia;
    private List<Escuderia> escuderias; // Relación con Escuderia

    /**
     * Constructor completo para crear un mecánico.
     *
     * @param dni DNI del mecánico.
     * @param nombre Nombre del mecánico.
     * @param apellido Apellido del mecánico.
     * @param pais País de origen.
     * @param especialidad La Especialidad del mecánico (MOTOR, NEUMATICOS, etc.).
     * @param aniosExperiencia Años de experiencia.
     * @param escuderias Lista de escuderías para las que trabaja.
     */
    public Mecanico(String dni, String nombre, String apellido, Pais pais, Especialidad especialidad, int aniosExperiencia, List<Escuderia> escuderias) {
        super(dni, nombre, apellido, pais);
        this.especialidad = especialidad;
        this.aniosExperiencia = aniosExperiencia;
        this.escuderias = escuderias;
    }

    /**
     * Constructor por defecto.
     * Se asigna  MOTOR como valor por defecto.
     */
    public Mecanico() {
        super();
        this.especialidad = Especialidad.MOTOR;
        this.aniosExperiencia = 0;
        this.escuderias = new ArrayList<>();
    }

    // --- Getters y Setters ---

    /**
     * Obtiene la especialidad del mecánico.
     * @return La Especialidad.
     */
    public Especialidad getEspecialidad() {
        return especialidad;
    }

    /**
     * Obtiene los años de experiencia del mecánico.
     * @return Los años de experiencia.
     */
    public int getAniosExperiencia() {
        return aniosExperiencia;
    }

    /**
     * Establece la especialidad del mecánico.
     * @param especialidad La nueva Especialidad.
     */
    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Establece los años de experiencia del mecánico.
     * @param aniosExperiencia Los nuevos años de experiencia.
     */
    public void setAniosExperiencia(int aniosExperiencia) {
        this.aniosExperiencia = aniosExperiencia;
    }

    /**
     * Obtiene la lista de escuderías para las que trabaja el mecánico.
     * @return Lista de Escuderia.
     */
    public List<Escuderia> getEscuderias() {
        return escuderias;
    }

    /**
     * Establece la lista de escuderías para las que trabaja el mecánico.
     * @param escuderias La nueva lista de Escuderia.
     */
    public void setEscuderias(List<Escuderia> escuderias) {
        this.escuderias = escuderias;
    }

    /**
     * Agrega una escudería a la lista del mecánico.
     * @param escuderia La Escuderia a agregar.
     */
    public void agregarEscuderia(Escuderia escuderia) {
        this.escuderias.add(escuderia);
    }
    @Override
    public String toString() {
        return "Mecánico: " + getNombre() + " " + getApellido() +
                ", Especialidad: " + especialidad +
                ", Años de experiencia: " + aniosExperiencia;
    }
}