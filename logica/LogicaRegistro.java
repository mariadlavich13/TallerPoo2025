package logica;

import archivos.SistemaGestion;
import clases.Piloto;
import clases.Mecanico;
import clases.Especialidad;
import clases.Auto;
import clases.Escuderia;
import clases.Circuito;
import clases.Pais;
import clases.Carrera;

/**
 * Contiene la lógica para registrar nuevas entidades en el sistema.
 * Realiza validaciones de campos obligatorios, formato de datos y
 * control de duplicados antes de crear y agregar los objetos.
 */
public class LogicaRegistro {

/**
     * Registra un nuevo piloto en el sistema.
     * Verifica que el DNI no esté duplicado, que el Nombre y Apellido no
     * estén duplicados, y que todos los campos obligatorios (DNI, Nombre, Apellido, Nro. Competencia, País)
     * estén presentes.
     *
     * @param datos El SistemaGestion donde se agregará el piloto.
     * @param dni DNI del piloto.
     * @param nombre Nombre del piloto.
     * @param apellido Apellido del piloto.
     * @param pais País de origen (debe ser un objeto no nulo).
     * @param nroCompString Número de competición como String para validación en la capa de lógica.
     * @param victorias Conteo inicial de victorias.
     * @param polePosition Conteo inicial de poles.
     * @param vueltasRapidas Conteo inicial de vueltas rápidas.
     * @param podios Conteo inicial de podios.
     * @throws LogicaException Si un campo obligatorio está vacío/nulo, el DNI o el nombre completo ya existen,
     * o si el formato de DNI o número de competencia es inválido.
     */
    public void registrarPiloto(SistemaGestion datos, String dni, String nombre, String apellido, Pais pais, String nroCompString, int victorias, int polePosition, int vueltasRapidas, int podios)
            throws LogicaException {
        
        // --- VALIDACIÓN DE CAMPOS OBLIGATORIOS (Null/Vacío) ---
        if (dni == null || dni.trim().isEmpty()) {
            throw new LogicaException("El DNI del piloto es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new LogicaException("El nombre del piloto es obligatorio.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new LogicaException("El apellido del piloto es obligatorio.");
        }
        if (nroCompString == null || nroCompString.trim().isEmpty()) {
            throw new LogicaException("El número de competencia es obligatorio.");
        }
        if (pais == null) {
            throw new LogicaException("Debe seleccionar un país de origen para el piloto.");
        }
//----------------------------------------
        //Correcion de formatos que hacian falta
        if(!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s+]")){
            throw new LogicaException("El nombre no puede tener numeros ni simbolos.");
        }
        if(!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s+]")){
            throw new LogicaException("El apellido no puedo tener numeros ni simbolos.");
        }
//----------------------------------------
        // --- VALIDACIÓN DE FORMATO ---
        try {
            // Se intenta convertir el DNI a un número.
            Long.parseLong(dni.trim());
        } catch (NumberFormatException e) {
            throw new LogicaException("Formato de DNI inválido: El DNI debe contener solo números.");
        }
//----------------------------------------
        //Correcion de dni
        if(dni.trim().length()<7 || dni.trim().length() > 8){
            throw new LogicaException("El dni debe tener entre 7 y 8 digitos.");
        }
        try{
            long dniNum= Long.parseLong(dni.trim());
            if(dniNum <= 0) throw new LogicaException("El DNI debe ser un numero positivo.");
        } catch(NumberFormatException e){
            throw new LogicaException("El DNI solo debe contener numeros.");
        }
//----------------------------------------
        int nroComp;
        try {
            // Control de Número de Competencia: Convierte.
            nroComp = Integer.parseInt(nroCompString.trim());
        } catch (NumberFormatException e) {
            throw new LogicaException("Error de formato de número: El número de competencia debe ser un número entero válido.");
        }

        //Correcion Numero competencia
        int numeroComp;
        try{
            numeroComp = Integer.parseInt(nroCompString.trim());
            if(numeroComp < 0){
                throw new LogicaException("EL numero de competencia no puede ser negativo.");
            }
        } catch(NumberFormatException e){
            throw new LogicaException("El numero de competencia debe ser un numero valido.");
        }

        // --- VERIFICACIÓN DE DUPLICADOS ---
        for (Piloto p : datos.getPilotos()) {
            // Control de DNI duplicado
            if (p.getDni().equals(dni)) {
                throw new LogicaException("Ya existe un piloto con DNI " + dni);
            }
            
            // Control de Nombre y Apellido duplicado (case-insensitive)
            if (p.getNombre().equalsIgnoreCase(nombre) && p.getApellido().equalsIgnoreCase(apellido)) {
                throw new LogicaException("Ya existe un piloto con el nombre '" + nombre + " " + apellido + "'");
            }
        }

        // --- REGISTRO ---
        Piloto nuevoPiloto = new Piloto(dni, nombre, apellido, pais, nroComp, victorias, polePosition, vueltasRapidas, podios);
        datos.agregarPiloto(nuevoPiloto);
    }


    /**
     * Registra un nuevo mecánico en el sistema.
     * Verifica que el DNI no esté duplicado, y que todos los campos obligatorios 
     * (DNI, Nombre, Apellido, País, Especialidad) estén presentes.
     *
     * @param datos El SistemaGestion donde se agregará el mecánico.
     * @param dni DNI del mecánico.
     * @param nombre Nombre del mecánico.
     * @param apellido Apellido del mecánico.
     * @param pais País de origen (debe ser un objeto no nulo).
     * @param especialidad Especialidad del mecánico (debe ser un objeto no nulo).
     * @param aniosExperiencia Años de experiencia.
     * @throws LogicaException Si un campo obligatorio está vacío/nulo, el DNI ya existe, o el formato de DNI es inválido.
     */
    public void registrarMecanico(SistemaGestion datos, String dni, String nombre, String apellido, Pais pais, Especialidad especialidad, int aniosExperiencia)
            throws LogicaException {
        
        // --- VALIDACIÓN DE CAMPOS OBLIGATORIOS (Null/Vacío) ---
        if (dni == null || dni.trim().isEmpty()) {
            throw new LogicaException("El DNI del mecánico es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new LogicaException("El nombre del mecánico es obligatorio.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new LogicaException("El apellido del mecánico es obligatorio.");
        }
        if (pais == null) {
            throw new LogicaException("Debe seleccionar un país de origen para el mecánico.");
        }
        if (especialidad == null) {
            throw new LogicaException("Debe seleccionar una especialidad para el mecánico.");
        }
//----------------------------------------
        //Correcion de formatos que hacian falta
        if(!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s+]")){
            throw new LogicaException("El nombre no puede tener numeros ni simbolos.");
        }
        if(!apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s+]")){
            throw new LogicaException("El apellido no puedo tener numeros ni simbolos.");
        }
//----------------------------------------
        // --- VALIDACIÓN DE FORMATO ---
        try {
            Long.parseLong(dni.trim());
        } catch (NumberFormatException e) {
            throw new LogicaException("Formato de DNI inválido: El DNI del mecánico debe contener solo números.");
        }

//----------------------------------------
        //Correcion de dni
        if(dni.trim().length()<7 || dni.trim().length() > 8){
            throw new LogicaException("El dni debe tener entre 7 y 8 digitos.");
        }
        try{
            long dniNum= Long.parseLong(dni.trim());
            if(dniNum <= 0) throw new LogicaException("El DNI debe ser un numero positivo.");
        } catch(NumberFormatException e){
            throw new LogicaException("El DNI solo debe contener numeros.");
        }

        //Correcion de Años de experiencia
        if(aniosExperiencia < 0){
            throw new LogicaException("Los años de experiencia no deben ser negativos.");
        }
//----------------------------------------

        // --- VERIFICACIÓN DE DUPLICADOS ---
        for (Mecanico m : datos.getMecanicos()) {
            if (m.getDni().equals(dni)) {
                throw new LogicaException("Ya existe un mecanico con DNI " + dni);
            }
        }
        
        // --- REGISTRO ---
        Mecanico nuevoMecanico = new Mecanico(dni, nombre, apellido, pais, especialidad, aniosExperiencia, new java.util.ArrayList<>());
        datos.agregarMecanico(nuevoMecanico);
    }


    /**
     * Registra un nuevo auto en el sistema.
     * Verifica que los campos obligatorios (Modelo, Motor) estén presentes.
     *
     * @param datos El SistemaGestion donde se agregará el auto.
     * @param modelo Modelo del auto.
     * @param motor Motor del auto.
     * @throws LogicaException Si un campo obligatorio (Modelo o Motor) es nulo o vacío.
     */
    public void registrarAuto(SistemaGestion datos, String modelo, String motor) throws LogicaException {
        
        // --- VALIDACIÓN DE CAMPOS OBLIGATORIOS (Null/Vacío) ---
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new LogicaException("El modelo del auto es obligatorio.");
        }

//----------------------------------------
        //Correcion de Ingreso de auto(Si es muy corto no lo toma)
        if(modelo.trim().length() < 2){
            throw new LogicaException("El modelo del auto es muy corto, ingrese un modelo valido.");
        }
//----------------------------------------

        if (motor == null || motor.trim().isEmpty()) {
            throw new LogicaException("El motor del auto es obligatorio.");
        }
        // --- FIN VALIDACIÓN ---

        Auto nuevoAuto = new Auto(modelo, motor);
        datos.agregarAuto(nuevoAuto);
    }


    /**
     * Registra una nueva escudería en el sistema.
     * Verifica que el nombre no esté duplicado y que los campos obligatorios (Nombre, País)
     * estén presentes.
     *
     * @param datos El SistemaGestion donde se agregará la escudería.
     * @param nombre Nombre de la escudería.
     * @param pais País de origen (debe ser un objeto no nulo).
     * @throws LogicaException Si un campo obligatorio está vacío/nulo o si el nombre de la escudería ya existe.
     */
    public void registrarEscuderia(SistemaGestion datos, String nombre, Pais pais) throws LogicaException {
        
        // --- VALIDACIÓN DE CAMPOS OBLIGATORIOS (Null/Vacío) ---
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new LogicaException("El nombre de la escudería es obligatorio.");
        }
        if (pais == null) {
            throw new LogicaException("Debe seleccionar un país de origen para la escudería.");
        }
        // --- FIN VALIDACIÓN ---

        // --- VERIFICACIÓN DE DUPLICADOS ---
        for (Escuderia e : datos.getEscuderias()) {
            if (e.getNombre().equalsIgnoreCase(nombre)) {
                throw new LogicaException("Ya existe una escudería con el nombre " + nombre);
            }
        }
        
        // --- REGISTRO ---
        Escuderia nuevaEscuderia = new Escuderia(nombre);
        nuevaEscuderia.setPais(pais); // Asigna el país
        datos.agregarEscuderia(nuevaEscuderia);
    }


    /**
     * Registra un nuevo circuito en el sistema.
     * Verifica que el nombre no esté duplicado y que los campos obligatorios (Nombre, País)
     * estén presentes.
     *
     * @param datos El SistemaGestion donde se agregará el circuito.
     * @param nombre Nombre del circuito.
     * @param longitud Longitud del circuito.
     * @param pais País de ubicación (debe ser un objeto no nulo).
     * @throws LogicaException Si un campo obligatorio está vacío/nulo o si el nombre del circuito ya existe.
     */
    public void registrarCircuito(SistemaGestion datos, String nombre, int longitud, Pais pais) throws LogicaException {
        
        // --- VALIDACIÓN DE CAMPOS OBLIGATORIOS (Null/Vacío) ---
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new LogicaException("El nombre del circuito es obligatorio.");
        }
        if (pais == null) {
            throw new LogicaException("Debe seleccionar un país de ubicación para el circuito.");
        }

//----------------------------------------
        //Correcion para validacion de longitud
        if(longitud <= 0){
            throw new LogicaException("La longitud del circuito debe ser mayor a 0km.");
        }
//----------------------------------------
        // --- FIN VALIDACIÓN ---

        // --- VERIFICACIÓN DE DUPLICADOS ---
        for (Circuito c : datos.getCircuitos()) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                throw new LogicaException("Ya existe un circuito con el nombre " + nombre);
            }
        }
        
        // --- REGISTRO ---
        Circuito nuevoCircuito = new Circuito(nombre, longitud, pais);
        datos.agregarCircuito(nuevoCircuito);
    }


    /**
     * Registra un nuevo país en el sistema.
     * Verifica que el ID y la descripción no estén duplicados y que el campo obligatorio (Descripción)
     * esté presente.
     *
     * @param datos El SistemaGestion donde se agregará el país.
     * @param idPais ID único del país.
     * @param descripcion Nombre del país.
     * @throws LogicaException Si un campo obligatorio está vacío/nulo o si el ID o la descripción del país ya existen.
     */
    public void registrarPais(SistemaGestion datos, int idPais, String descripcion) throws LogicaException {
        
        // --- VALIDACIÓN DE CAMPOS OBLIGATORIOS (Null/Vacío) ---
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new LogicaException("El nombre del país es obligatorio.");
        }
//----------------------------------------
        //Correcion para validar la descripcion
        if(!descripcion.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s+]")){
            throw new LogicaException("La descripcion solo puede contener letras.");
        }

        //Correcion para validar el id del pais (debe se mayor que 0)
        if(idPais<=0){
            throw new LogicaException("El ID del pais debe ser mayor a 0.");
        }
//----------------------------------------

        // --- FIN VALIDACIÓN ---

        // --- VERIFICACIÓN DE DUPLICADOS ---
        for (Pais p : datos.getPaises()) {
            if (p.getIdPais() == idPais) {
                throw new LogicaException("Ya existe un país con el ID " + idPais);
            }

            if (p.getDescripcion().equalsIgnoreCase(descripcion)) {
                throw new LogicaException("Ya existe un país con el nombre " + descripcion);
            }
        }
        
        // --- REGISTRO ---
        Pais nuevoPais = new Pais(idPais, descripcion, new java.util.ArrayList<>(), new java.util.ArrayList<>(), new java.util.ArrayList<>(), new java.util.ArrayList<>());
        datos.agregarPais(nuevoPais);
    }

    /**
     * Registra una nueva carrera en el sistema.
     * Verifica que no haya dos carreras en el mismo circuito en la misma fecha y que los campos
     * obligatorios (Fecha, Hora, País, Circuito) estén presentes.
     *
     * @param datos El SistemaGestion donde se agregará la carrera.
     * @param fechaRealizacion Fecha de la carrera (ej. "dd-MM-yyyy").
     * @param nroVueltas Número de vueltas.
     * @param horaRealizacion Hora de la carrera (ej. "HH:mm").
     * @param pais País (obtenido del circuito) (debe ser un objeto no nulo).
     * @param circuito Circuito de la carrera (debe ser un objeto no nulo).
     * @throws LogicaException Si un campo obligatorio está vacío/nulo, o si ya existe una carrera en ese circuito y fecha.
     */
    public void registrarCarrera(SistemaGestion datos, String fechaRealizacion, int nroVueltas, String horaRealizacion, Pais pais, Circuito circuito)
            throws LogicaException {
        
        // --- VALIDACIÓN DE CAMPOS OBLIGATORIOS (Null/Vacío) ---
        if (fechaRealizacion == null || fechaRealizacion.trim().isEmpty()) {
            throw new LogicaException("La fecha de realización de la carrera es obligatoria.");
        }

//----------------------------------------
        //Correccion de fecha
        if(!Utilidades.esFechaValida(fechaRealizacion)){
            throw new LogicaException("La fecha ingresada "+fechaRealizacion+" no es valida. Use formato dd-MM-yyyy (ej: 15-03-2025)");
        }

        if (horaRealizacion == null || horaRealizacion.trim().isEmpty()) {
            throw new LogicaException("La hora de realización de la carrera es obligatoria.");
        }

        //Nueva Validacion para numeros de vueltas
        if(nroVueltas <=0){
            throw new LogicaException("La carrera debe tener al menos 1 vuelta.");
        }
//----------------------------------------

        // -------------------------------------------------------
        // --- VALIDACIÓN DE FORMATO DE HORA ---
        // -------------------------------------------------------
        // Explicación del Regex "([01]?[0-9]|2[0-3]):[0-5][0-9]":
        // 1. ([01]?[0-9]|2[0-3]): Acepta horas de 0 a 19 (con o sin cero delante) O de 20 a 23.
        // 2. :                  : Debe haber dos puntos.
        // 3. [0-5][0-9]         : Acepta minutos de 00 a 59.
        if (!horaRealizacion.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
            throw new LogicaException("Formato de hora inválido. Debe ser HH:mm (ej: 14:30 o 9:00) y estar entre 00:00 y 23:59.");
        }
        // -------------------------------------------------------

        if (pais == null) {
            throw new LogicaException("El país de la carrera no puede ser nulo.");
        }
        if (circuito == null) {
            throw new LogicaException("El circuito de la carrera no puede ser nulo.");
        }
        // --- FIN VALIDACIÓN ---

        // --- VERIFICACIÓN DE DUPLICADOS ---
        // Controla que no haya dos carreras en el mismo circuito el mismo día
        for (Carrera c : datos.getCarreras()) {
            if (c.getFechaRealizacion().equals(fechaRealizacion) && c.getCircuito().equals(circuito)) {
                throw new LogicaException("Ya existe una carrera planificada para el circuito " + circuito.getNombre() + " en la fecha " + fechaRealizacion);
            }
        }
        
        // ---REGISTRO ---
        Carrera nuevaCarrera = new Carrera(fechaRealizacion, nroVueltas, horaRealizacion, pais, circuito);
        datos.agregarCarrera(nuevaCarrera);
    }
}