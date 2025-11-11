package clases;

import java.util.List;
import java.util.ArrayList;

/**
 * Representa a un Piloto de carreras, extendiendo la clase Persona.
 * Almacena estadísticas de carrera (victorias, podios) y sus relaciones
 * con autos (AutoPiloto) y escuderías (PilotoEscuderia).
 */
public class Piloto extends Persona {
    private int numeroCompetencia;
    private int victorias;
    private int polePosition;
    private int vueltasRapidas;
    private int podios;
    private List<AutoPiloto> autosPiloto;
    private List<PilotoEscuderia> pilotosEscuderias;

    /**
     * Constructor completo para cargar un piloto con todas sus relaciones.
     *
     * @param dni DNI del piloto.
     * @param nombre Nombre del piloto.
     * @param apellido Apellido del piloto.
     * @param pais País de origen.
     * @param numeroCompetencia Número de competición.
     * @param victorias Conteo de victorias.
     * @param polePosition Conteo de poles.
     * @param vueltasRapidas Conteo de vueltas rápidas.
     * @param podios Conteo de podios.
     * @param autosPiloto Lista de participaciones en carreras.
     * @param pilotosEscuderias Lista de contratos con escuderías.
     */
    public Piloto(String dni, String nombre, String apellido, Pais pais, int numeroCompetencia, int victorias, int polePosition, int vueltasRapidas, int podios, List<AutoPiloto> autosPiloto, List<PilotoEscuderia> pilotosEscuderias) {
        super(dni, nombre, apellido, pais);
        this.numeroCompetencia = numeroCompetencia;
        this.victorias = victorias;
        this.polePosition = polePosition;
        this.vueltasRapidas = vueltasRapidas;
        this.podios = podios;
        this.autosPiloto = autosPiloto;
        this.pilotosEscuderias = pilotosEscuderias;
    }

    /**
     * Constructor para crear un nuevo piloto o cargarlo desde CSV.
     * Inicializa las listas de relaciones (autosPiloto, pilotosEscuderias) como vacías.
     *
     * @param dni DNI del piloto.
     * @param nombre Nombre del piloto.
     * @param apellido Apellido del piloto.
     * @param pais Objeto Pais de origen.
     * @param numeroCompetencia Número de competición.
     * @param victorias Conteo inicial de victorias.
     * @param polePosition Conteo inicial de poles.
     * @param vueltasRapidas Conteo inicial de vueltas rápidas.
     * @param podios Conteo inicial de podios.
     */
    public Piloto(String dni, String nombre, String apellido, Pais pais, int numeroCompetencia, int victorias, int polePosition, int vueltasRapidas, int podios) {
        super(dni, nombre, apellido, pais);
        this.numeroCompetencia = numeroCompetencia;
        this.victorias = victorias;
        this.polePosition = polePosition;
        this.vueltasRapidas = vueltasRapidas;
        this.podios = podios;
        this.autosPiloto = new ArrayList<>();
        this.pilotosEscuderias = new ArrayList<>();
    }

    /**
     * Constructor por defecto.
     * Inicializa un piloto con valores por defecto y listas vacías.
     */
    public Piloto() {
        super();
        this.numeroCompetencia = 0;
        this.victorias = 0;
        this.polePosition = 0;
        this.vueltasRapidas = 0;
        this.podios = 0;
        this.autosPiloto = new ArrayList<>();
        this.pilotosEscuderias = new ArrayList<>();
    }

    // --- Getters y Setters ---

    /**
     * Obtiene el número de competencia del piloto.
     * @return El número.
     */
    public int getNumeroCompetencia() {
        return numeroCompetencia;
    }

    /**
     * Obtiene el número de victorias del piloto.
     * @return El conteo total de victorias.
     */
    public int getVictorias() {
        return victorias;
    }

    /**
     * Obtiene el número de pole positions del piloto.
     * @return El conteo total de poles.
     */
    public int getPolePosition() {
        return polePosition;
    }

    /**
     * Obtiene el número de vueltas rápidas del piloto.
     * @return El conteo total de vueltas rápidas.
     */
    public int getVueltasRapidas() {
        return vueltasRapidas;
    }

    /**
     * Obtiene el número de podios del piloto.
     * @return El conteo total de podios.
     */
    public int getPodios() {
        return podios;
    }

    /**
     * Establece el número de competencia del piloto.
     * @param numeroCompetencia El nuevo número.
     */
    public void setNumeroCompetencia(int numeroCompetencia) {
        this.numeroCompetencia = numeroCompetencia;
    }

    /**
     * Establece el número de victorias del piloto.
     * @param victorias El nuevo conteo total de victorias.
     */
    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    /**
     * Establece el número de pole positions del piloto.
     * @param polePosition El nuevo conteo total de poles.
     */
    public void setPolePosition(int polePosition) {
        this.polePosition = polePosition;
    }

    /**
     * Establece el número de vueltas rápidas del piloto.
     * @param vueltasRapidas El nuevo conteo total de vueltas rápidas.
     */
    public void setVueltasRapidas(int vueltasRapidas) {
        this.vueltasRapidas = vueltasRapidas;
    }

    /**
     * Establece el número de podios del piloto.
     * @param podios El nuevo conteo total de podios.
     */
    public void setPodios(int podios) {
        this.podios = podios;
    }

    /**
     * Obtiene la lista de participaciones en carreras (AutoPiloto) del piloto.
     * @return Lista de AutoPiloto.
     */
    public List<AutoPiloto> getAutosPiloto() {
        return autosPiloto;
    }

    /**
     * Establece la lista de participaciones en carreras (AutoPiloto) del piloto.
     * @param autosPiloto La nueva lista de AutoPiloto.
     */
    public void setAutosPiloto(List<AutoPiloto> autosPiloto) {
        this.autosPiloto = autosPiloto;
    }

    /**
     * Obtiene la lista de contratos (PilotoEscuderia) del piloto.
     * @return Lista de PilotoEscuderia.
     */
    public List<PilotoEscuderia> getPilotosEscuderias() {
        return pilotosEscuderias;
    }

    /**
     * Establece la lista de contratos (PilotoEscuderia) del piloto.
     * @param pilotosEscuderias La nueva lista de PilotoEscuderia.
     */
    public void setPilotosEscuderias(List<PilotoEscuderia> pilotosEscuderias) {
        this.pilotosEscuderias = pilotosEscuderias;
    }

    /**
     * Agrega una participación en carrera (AutoPiloto) a la lista del piloto.
     * @param autoPiloto La participación a agregar.
     */
    public void agregarAutoPiloto(AutoPiloto autoPiloto) {
        this.autosPiloto.add(autoPiloto);
    }

    /**
     * Agrega un contrato (PilotoEscuderia) a la lista del piloto.
     * @param pilotoEscuderia El contrato a agregar.
     */
    public void agregarPilotoEscuderia(PilotoEscuderia pilotoEscuderia) {
        this.pilotosEscuderias.add(pilotoEscuderia);
    }
}