package logica;

import archivos.SistemaGestion;
import clases.Piloto;
import clases.Auto;
import clases.Carrera;
import clases.AutoPiloto;
import clases.ResultadoCarrera;
import clases.Puntaje;
import clases.Escuderia;
import clases.Mecanico;
import clases.PilotoEscuderia;
import java.util.List;
import java.util.ArrayList;

/**
 * Contiene la lógica para gestionar las operaciones
 * del sistema, como asociar entidades, registrar resultados y calcular puntajes.
 */
public class LogicaGestion {

    /**
     * Asocia un piloto a un auto en una carrera específica.
     * Verifica que el auto no esté ya asignado a otro piloto en esa carrera.
     * Verifica que el piloto no esté ya participando con otro auto en esa carrera.
     *
     * @param carrera La carrera a la que se asocian.
     * @param piloto El piloto a asociar.
     * @param auto El auto a asociar.
     * @param fechaAsignacion La fecha de la asignación.
     * @return El objeto AutoPiloto creado si la asignación es exitosa.
     * @throws LogicaException Si el auto o el piloto ya están asignados en esa carrera o si no pertenecen a la misma escudería.
     */
    public AutoPiloto asociarPilotoAutoACarrera(Carrera carrera, Piloto piloto, Auto auto, String fechaAsignacion) throws LogicaException {
        
        // Verifica que un auto no sea asignado a más de un piloto en la misma carrera
        for (AutoPiloto participante : carrera.getParticipantes()) {
            if (participante.getAuto().equals(auto)) {
                throw new LogicaException("El auto " + auto.getModelo() + " ya está asignado a otro piloto en esta carrera.");
            }
        }
        
        // Verifica que un piloto no esté asignado a más de un auto en la misma carrera
        for (AutoPiloto participante : carrera.getParticipantes()) {
            if (participante.getPiloto().equals(piloto)) {
                throw new LogicaException("El piloto " + piloto.getNombre() + " ya está participando con otro auto en esta carrera.");
            }
        }
        // Verifica que el piloto y el auto pertenezcan a la misma escudería
        Escuderia escuderiaPiloto = null;
        for (PilotoEscuderia pe : piloto.getPilotosEscuderias()) {
            String hf = pe.getHastaFecha();
            // Un contrato activo no tiene fecha de fin (es null o vacía)
            if (hf == null || hf.trim().isEmpty()) {
                escuderiaPiloto = pe.getEscuderia();
                break;
            }
        }

        //Obtener la escudería del auto
        Escuderia escuderiaAuto = auto.getEscuderia();

        //Validar que ambos existan
        if (escuderiaPiloto == null) {
            throw new LogicaException("El piloto " + piloto.getNombre() + " " + piloto.getApellido() + " no tiene un contrato activo con ninguna escudería.");
        }
        if (escuderiaAuto == null) {
            throw new LogicaException("El auto " + auto.getModelo() + " no está asignado a ninguna escudería.");
        }
        
        //Comparar que sean la misma escudería
        if (!escuderiaPiloto.equals(escuderiaAuto)) {
            throw new LogicaException("Error de consistencia: El piloto " + piloto.getNombre() 
                    + " pertenece a " + escuderiaPiloto.getNombre()
                    + ", pero el auto " + auto.getModelo()
                    + " pertenece a " + escuderiaAuto.getNombre() + ".");
        }

        // Si pasa la verificación, crea la asociación
        AutoPiloto nuevaASociacion = new AutoPiloto(fechaAsignacion, piloto, auto, carrera);
        // Guarda la asignación en Persistencia (listas en memoria)
        carrera.agregarParticipante(nuevaASociacion);
        piloto.agregarAutoPiloto(nuevaASociacion);
        auto.agregarAutoPiloto(nuevaASociacion);
        
        return nuevaASociacion; // Devolvemos el objeto creado
    }

/**
     * Registra el resultado final de un piloto en una carrera.
     * Esto guarda el resultado en la lista de SistemaGestion, actualiza las
     * estadísticas del piloto (victorias, podios, vueltas rápidas).
     *
     * @param datos El sistema de gestión con todas las listas.
     * @param carrera La carrera que finalizó.
     * @param piloto El piloto que obtuvo el resultado.
     * @param posicion La posición final (1, 2, 3...).
     * @param tuvoVueltaRapida true si este piloto hizo la vuelta rápida, false si no.
     * @throws LogicaException Si la posición es inválida (< 1), si el piloto
     * no participó en la carrera, o si ya tiene un resultado registrado para la misma,
     * O SI LA POSICIÓN YA HA SIDO ASIGNADA A OTRO PILOTO.
     */
    public void registrarResultado(SistemaGestion datos, Carrera carrera, Piloto piloto, int posicion, boolean tuvoVueltaRapida) throws LogicaException {
        
        // --- 1. Validaciones ---
        if (posicion < 1) {
            throw new LogicaException("La posición debe ser mayor o igual a 1.");
        }
        //Máximo de 20 posiciones en F1.
        if (posicion > 20) {
            throw new LogicaException("La posición máxima permitida en Fórmula 1 es 20. Ingrese una posición válida.");
        }
        // ¿El piloto realmente participó en la carrera?
        boolean participo = false;
        for (AutoPiloto ap : carrera.getParticipantes()) {
            if (ap.getPiloto().equals(piloto)) {
                participo = true;
                break;
            }
        }
        if (!participo) {
            throw new LogicaException("El piloto " + piloto.getNombre() + " no participó en esta carrera.");
        }
        
        // ¿Ya existe un resultado para este piloto en esta carrera?
        for (ResultadoCarrera r : datos.getResultadosCarreras()) {
            if (r.getCarrera().equals(carrera) && r.getPiloto().equals(piloto)) {
                throw new LogicaException("El piloto " + piloto.getNombre() + " ya tiene un resultado registrado para esta carrera.");
            }
        }
        // ¿Ya existe un resultado con la misma posición en esta carrera? (no se permiten empates)
        for (ResultadoCarrera r : datos.getResultadosCarreras()) {
            // Revisa si, para la misma carrera, ya existe un resultado con la misma posición
            if (r.getCarrera().equals(carrera) && r.getPosicion() == posicion) {
                throw new LogicaException("La posición " + posicion + " ya ha sido asignada al piloto " + r.getPiloto().getNombre() + " " + r.getPiloto().getApellido() + " en esta carrera.");
            }
        }

        // --- 2. Crear y guardar el objeto resultado en memoria ---
        ResultadoCarrera resultado = new ResultadoCarrera(piloto, posicion, carrera);
        datos.agregarResultadoCarrera(resultado); 

        // --- 3. Actualizar estadísticas del Piloto ---
        if (posicion == 1) {
            piloto.setVictorias(piloto.getVictorias() + 1);
        }
        if (posicion <= 3) {
            piloto.setPodios(piloto.getPodios() + 1);
        }
        if (tuvoVueltaRapida) {
            piloto.setVueltasRapidas(piloto.getVueltasRapidas() + 1);
        }
    }

    /**
     * Calcula el puntaje total de todos los pilotos basado en los resultados de
     * todas las carreras cargadas en el sistema.
     *
     * @param datos El objeto SistemaGestion que contiene la lista de pilotos y resultados.
     * @return Una lista de objetos PilotoPuntaje, sin ordenar.
     */
    public List<PilotoPuntaje> calcularPuntajes(SistemaGestion datos) {
        
        List<PilotoPuntaje> puntajesFinales = new ArrayList<>();
        List<Piloto> todosLosPilotos = datos.getPilotos();
        List<ResultadoCarrera> todosLosResultados = datos.getResultadosCarreras();

        // Bucle exterior: Itera sobre cada piloto
        for (Piloto piloto : todosLosPilotos) {
            int puntajeTotalDelPiloto = 0; // Inicia el contador para este piloto

            // Bucle interior: Itera sobre todos los resultados de todas las carreras
            for (ResultadoCarrera resultado : todosLosResultados) {
                
                // Comprueba si el resultado pertenece al piloto actual
                if (resultado.getPiloto().getDni().equals(piloto.getDni())) {
                    // Obtiene los puntos para esa posición
                    int puntosObtenidos = Puntaje.obtenerPuntaje(resultado.getPosicion());
                    puntajeTotalDelPiloto += puntosObtenidos; // Los suma al total del piloto
                }
            }
            // Una vez contados todos los resultados, crea el objeto contenedor
            PilotoPuntaje pp = new PilotoPuntaje(piloto, puntajeTotalDelPiloto);
            puntajesFinales.add(pp);
        }
        return puntajesFinales;
    }

    /**
     * Asocia un piloto a una escudería (crea un contrato).
     * Verifica que el piloto no tenga ya un contrato activo (sin 'hastaFecha')
     * y que la nueva fecha de inicio no se superponga con contratos anteriores.
     *
     * @param piloto El piloto a asociar.
     * @param escuderia La escudería a la que se une.
     * @param desdeFecha La fecha de inicio del contrato (ej. "01-01-2025").
     * @return El objeto de asociación PilotoEscuderia creado.
     * @throws LogicaException Si el piloto ya tiene un contrato activo o si las fechas se superponen.
     */
    public PilotoEscuderia asociarPilotoAEscuderia(Piloto piloto, Escuderia escuderia, String desdeFecha) throws LogicaException {
        
        //Usamos la utilidad para formatear
        String nuevaFechaInicioF = Utilidades.formatearFecha(desdeFecha);
        String ultimaFechaFinF = "0000-00-00"; 

        for (PilotoEscuderia pe : piloto.getPilotosEscuderias()) {
            String hastaFecha = pe.getHastaFecha();
            // Control 1: ¿Tiene un contrato activo?
            if (hastaFecha == null || hastaFecha.trim().isEmpty()) {
                throw new LogicaException("El piloto " + piloto.getNombre()
                        + " ya tiene un contrato activo con la escudería " + pe.getEscuderia().getNombre() + ".");
            }

            // Control 2: Buscar la última fecha de fin
            String finExistenteF = Utilidades.formatearFecha(hastaFecha);
            if (finExistenteF != null && finExistenteF.compareTo(ultimaFechaFinF) > 0) {
                ultimaFechaFinF = finExistenteF;
            }
        }

        // Control 3: de superposición de fechas
        if (nuevaFechaInicioF.compareTo(ultimaFechaFinF) <= 0) {
            throw new LogicaException("La fecha de inicio (" + desdeFecha + ") se superpone con un contrato anterior."
                    + " Debe ser posterior a " + ultimaFechaFinF + " (formato YYYY-MM-DD).");
        }

        // Si pasa, crea la asociación
        PilotoEscuderia nuevaAsociacion = new PilotoEscuderia(desdeFecha, "", piloto, escuderia);
        piloto.agregarPilotoEscuderia(nuevaAsociacion);
        escuderia.agregarPilotoEscuderia(nuevaAsociacion);

        return nuevaAsociacion;
    }

    /**
     * Termina el contrato de un piloto con una escudería, asignando "hastaFecha".
     *
     * @param piloto El piloto a desvincular.
     * @param escuderia La escudería de la que se desvincula.
     * @param hastaFecha La fecha de fin de contrato (ej. "31-12-2025").
     * @throws LogicaException Si no se encuentra un contrato activo entre ambos,
     * o si la fecha de fin es anterior a la fecha de inicio del contrato.
     */
    public void desvincularPilotoDeEscuderia(Piloto piloto, Escuderia escuderia, String hastaFecha) throws LogicaException {
        
        PilotoEscuderia asociacionActiva = null;
        // Busca el contrato activo
        for (PilotoEscuderia pe : piloto.getPilotosEscuderias()) {
            String hf = pe.getHastaFecha();
            if (pe.getEscuderia().equals(escuderia) && (hf == null || hf.trim().isEmpty())) {
                asociacionActiva = pe;
                break;
            }
        }

        if (asociacionActiva != null) {
            //Validamos que la fecha de fin sea posterior a la de inicio
            String nuevaFechaFinF = Utilidades.formatearFecha(hastaFecha);
            String fechaInicioActivoF = Utilidades.formatearFecha(asociacionActiva.getDesdeFecha());

            if (nuevaFechaFinF.compareTo(fechaInicioActivoF) < 0) {
                throw new LogicaException("La fecha de fin (" + hastaFecha + ") no puede ser anterior a la fecha de inicio (" + asociacionActiva.getDesdeFecha() + ").");
            }
            
            asociacionActiva.setHastaFecha(hastaFecha);
        } else {
            throw new LogicaException("El piloto " + piloto.getNombre()
                    + " no tiene un contrato activo con " + escuderia.getNombre() + ".");
        }
    }

    /**
     * Asocia un auto a una escudería.
     * CONTROL: Verifica que el auto no pertenezca ya a otra escudería.
     *
     * @param auto El auto a asignar.
     * @param escuderia La escudería propietaria.
     * @throws LogicaException Si el auto ya tiene dueña.
     */
    public void asociarAutoAEscuderia(Auto auto, Escuderia escuderia) throws LogicaException {
        
        // Control: Un auto solo puede pertenecer a una escudería a la vez
        if (auto.getEscuderia() != null) {
            throw new LogicaException("El auto " + auto.getModelo()
                    + " ya pertenece a la escudería " + auto.getEscuderia().getNombre() + ".");
        }
        
        // El método agregarAuto de Escuderia maneja la relación bidireccional
        escuderia.agregarAuto(auto);
    }

    /**
     * Asocia un mecánico a una escudería.
     * Control: Verifica que el mecánico no esté ya asignado a esa escudería.
     *
     * @param mecanico El mecánico a contratar.
     * @param escuderia La escudería que contrata.
     * @throws LogicaException Si el mecánico ya trabaja en esa escudería.
     */
    public void asociarMecanicoAEscuderia(Mecanico mecanico, Escuderia escuderia) throws LogicaException {
        
        // Control: Evitar duplicados
        if (escuderia.getMecanicos().contains(mecanico)) {
            throw new LogicaException("El mecánico " + mecanico.getNombre()
                    + " ya está asignado a " + escuderia.getNombre());
        }

        // Establecemos la relación bidireccional
        escuderia.agregarMecanico(mecanico);
        mecanico.agregarEscuderia(escuderia);
    }

    /**
     * Asigna una pole position a un piloto y actualiza sus estadísticas en memoria.
     * (No persiste este cambio en el CSV, solo en el objeto).
     *
     * @param piloto El piloto que obtuvo la pole.
     * @throws LogicaException Si el piloto es nulo.
     */
    public void asignarPolePosition(Piloto piloto) throws LogicaException {
        if (piloto == null) {
            throw new LogicaException("El piloto no puede ser nulo.");
        }

        // Actualiza la estadística del piloto
        piloto.setPolePosition(piloto.getPolePosition() + 1);
    }
}