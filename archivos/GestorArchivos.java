package archivos;

import java.io.*;
import java.util.*;
import clases.Auto;
import clases.Escuderia;
import clases.Carrera;
import clases.Circuito;
import clases.Mecanico;
import clases.Piloto;
import clases.Pais;
import clases.ResultadoCarrera;
import clases.Especialidad;
import logica.LogicaException;

/**
 * Clase de utilidad estática para manejar la lectura y escritura
 * de los archivos CSV (Capa de Persistencia).
 * Convierte líneas de texto de CSV en objetos de las 'clases' y viceversa.
 */
public class GestorArchivos {

    // --- MÉTODOS HELPER (Buscadores) ---

    /**Busca un País en la lista por su ID.
     *
     * @param paises Lista de países donde buscar.
     * @param idPais ID a encontrar.
     * @return El objeto Pais.
     * @throws LogicaException Si el ID del país no se encuentra en la lista.
     */
    public static Pais buscarPaisPorId(List<Pais> paises, int idPais) throws LogicaException {
        for (Pais p : paises) {
            if (p.getIdPais() == idPais) {
                return p;
            }
        }
        //Lanza excepción si no se encuentra el país
        throw new LogicaException("Error de integridad de datos: El ID de país '" + idPais + "' referenciado en un CSV no existe en DatosPais.csv");
    }

    /** Busca un Circuito en la lista por su nombre (ignorando mayúsculas/minúsculas).
     *
     * @param circuitos Lista de circuitos donde buscar.
     * @param nombre    Nombre a encontrar.
     * @return El objeto Circuito.
     * @throws LogicaException Si el nombre del circuito no se encuentra.
     */
    public static Circuito buscarCircuitoPorNombre(List<Circuito> circuitos, String nombre) throws LogicaException {
        for (Circuito c : circuitos) {
            if (c.getNombre().equalsIgnoreCase(nombre.trim())) {
                return c;
            }
        }
        //Lanza excepción si no se encuentra el circuito
        throw new LogicaException("Error de integridad de datos: El circuito con nombre '" + nombre + "' referenciado en DatosCarrera.csv no existe en DatosCircuito.csv");
    }

    /**Busca una Escudería en la lista por su nombre (ignorando mayúsculas/minúsculas).
     *
     * @param escuderias Lista de escuderías donde buscar.
     * @param nombre     Nombre a encontrar.
     * @return El objeto Escuderia.
     * @throws LogicaException Si el nombre de la escudería no se encuentra.
     */
    public static Escuderia buscarEscuderiaPorNombre(List<Escuderia> escuderias, String nombre) throws LogicaException {
        for (Escuderia e : escuderias) {
            if (e.getNombre().equalsIgnoreCase(nombre.trim())) {
                return e;
            }
        }
        //Lanza excepción si no se encuentra la escudería
        throw new LogicaException("Error de integridad de datos: La escudería con nombre '" + nombre + "' referenciada en un CSV no existe en DatosEscuderia.csv");
    }

    /**Busca un Mecánico en la lista por su DNI.
     *
     * @param mecanicos Lista de mecánicos donde buscar.
     * @param dni       DNI a encontrar.
     * @return El objeto Mecanico.
     * @throws LogicaException Si el DNI del mecánico no se encuentra.
     */
    public static Mecanico buscarMecanicoPorDNI(List<Mecanico> mecanicos, String dni) throws LogicaException {
        for (Mecanico m : mecanicos) {
            if (m.getDni().equals(dni.trim())) {
                return m;
            }
        }
        //Lanza excepción si no se encuentra el mecánico
        throw new LogicaException("Error de integridad de datos: El mecánico con DNI '" + dni + "' referenciado en DatosMecanicoEscuderia.csv no existe en DatosMecanico.csv");
    }

    /**Busca un Piloto en la lista por su DNI.
     *
     * @param pilotos Lista de pilotos donde buscar.
     * @param dni    DNI a encontrar.
     * @return El objeto Piloto.
     * @throws LogicaException Si el DNI del piloto no se encuentra.
     */
    public static Piloto buscarPilotoPorDNI(List<Piloto> pilotos, String dni) throws LogicaException {
        for (Piloto p : pilotos) {
            if (p.getDni().equals(dni.trim())) {
                return p;
            }
        }
        //Lanza excepción si no se encuentra el piloto
        throw new LogicaException("Error de integridad de datos: El piloto con DNI '" + dni + "' referenciado en DatosResultadoCarrera.csv no existe en DatosPiloto.csv");
    }

    /**Busca una Carrera por su fecha en una lista.
     * @param carreras Lista de Carreras.
     * @param fecha Fecha a buscar.
     * @return La Carrera encontrada.
     * @throws LogicaException Si no se encuentra una carrera con esa fecha.
     */
    private static Carrera buscarCarreraPorFecha(List<Carrera> carreras, String fecha) throws LogicaException {
        for (Carrera c : carreras) {
            if (c.getFechaRealizacion().equals(fecha.trim())) {
                return c;
            }
        }
        // MODIFICACIÓN: Lanza excepción en lugar de devolver null
        throw new LogicaException("Error de integridad de datos: No se encontró la carrera con fecha '" + fecha + "' referenciada en el CSV.");
    }

    // --- MÉTODOS DE LECTURA (CSV a Objetos) ---

    /**
     * Lee el archivo "DatosPaises.csv".
     * @param path Ruta al archivo.
     * @return Lista de objetos Pais.
     * @throws LogicaException Si el archivo no se encuentra o hay un error de formato.
     */
    public static List<Pais> leerPaisesDesdeCSV(String path) throws LogicaException {
        List<Pais> paises = new ArrayList<>();
        int nroLinea = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            br.readLine(); // Saltear encabezado
            nroLinea++;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) { nroLinea++; continue; }
                String[] valores = linea.split(",");
                if (valores.length < 2) throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Línea incompleta.");
                
                try {
                    int idPais = Integer.parseInt(valores[0].trim());
                    String descripcion = valores[1].trim();
                    Pais p = new Pais(idPais, descripcion, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                    paises.add(p);
                    nroLinea++;
                } catch (NumberFormatException e) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): ID de país no es un número.");
                }
            }
        } catch (IOException e) {
            throw new LogicaException("Error  al leer el archivo " + path + ": " + e.getMessage());
        }
        return paises;
    }

    /**
     * Lee el archivo "DatosAutos.csv".
     * @param path Ruta al archivo.
     * @param escuderias Lista de escuderías para asignar la relación.
     * @return Lista de objetos Auto.
     * @throws LogicaException Si el archivo no se encuentra o hay un error de formato.
     */
    public static List<Auto> leerAutosDesdeCSV(String path, List<Escuderia> escuderias) throws LogicaException {
        List<Auto> autos = new ArrayList<>();
        int nroLinea = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            br.readLine(); // Saltear encabezado
            nroLinea++;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) { nroLinea++; continue; }
                String[] valores = linea.split(",");
                if (valores.length < 3) throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Línea incompleta.");

                String modelo = valores[0].trim();
                String motor = valores[1].trim();
                String nombreEscuderia = valores[2].trim();

                Auto a = new Auto(modelo, motor);
                Escuderia escuderiaAsignada = buscarEscuderiaPorNombre(escuderias, nombreEscuderia);
                
                if (escuderiaAsignada == null) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): No se encontró la escudería '" + nombreEscuderia + "'.");
                }
                
                escuderiaAsignada.agregarAuto(a); // Establece relación bidireccional
                autos.add(a);
                nroLinea++;
            }
        } catch (IOException e) {
            throw new LogicaException("Error  al leer el archivo " + path + ": " + e.getMessage());
        }
        return autos;
    }

    /**
     * Lee el archivo "DatosCircuitos.csv".
     * @param path Ruta al archivo.
     * @param paises Lista de países para asignar la relación.
     * @return Lista de objetos Circuito.
     * @throws LogicaException Si el archivo no se encuentra o hay un error de formato.
     */
    public static List<Circuito> leerCircuitosDesdeCSV(String path, List<Pais> paises) throws LogicaException {
        List<Circuito> circuitos = new ArrayList<>();
        int nroLinea = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Saltear encabezado
            nroLinea++;
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) { nroLinea++; continue; }
                String[] valores = linea.split(",");
                if (valores.length < 3) throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Línea incompleta.");

                try {
                    String nombre = valores[0].trim();
                    int longitud = Integer.parseInt(valores[1].trim());
                    int idPais = Integer.parseInt(valores[2].trim());

                    Pais paisAsignado = buscarPaisPorId(paises, idPais);
                    if (paisAsignado == null) {
                        throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): No se encontró el país con ID " + idPais);
                    }
                    
                    Circuito c = new Circuito(nombre, longitud, paisAsignado);
                    circuitos.add(c);
                    nroLinea++;
                } catch (NumberFormatException e) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Longitud (debe ser un entero) o ID de país no son números válidos.");
                }
            }
        } catch (IOException e) {
            throw new LogicaException("Error  al leer el archivo " + path + ": " + e.getMessage());
        }
        return circuitos;
    }

    /**
     * Lee el archivo "DatosCarreras.csv".
     * @param path Ruta al archivo.
     * @param todosLosCircuitos Lista de circuitos para asignar la relación.
     * @return Lista de objetos Carrera.
     * @throws LogicaException Si el archivo no se encuentra o hay un error de formato.
     */
    public static List<Carrera> leerCarrerasDesdeCSV(String path, List<Circuito> todosLosCircuitos) throws LogicaException {
        List<Carrera> carreras = new ArrayList<>();
        int nroLinea = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Saltear encabezado
            nroLinea++;
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) { nroLinea++; continue; }
                String[] valores = linea.split(",");
                if (valores.length < 4) throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Línea incompleta.");

                try {
                    String fechaRealizacion = valores[0].trim();
                    int nroVueltas = Integer.parseInt(valores[1].trim());
                    String horaRealizacion = valores[2].trim();
                    String nombreCircuito = valores[3].trim();

                    Circuito circuitoAsignado = buscarCircuitoPorNombre(todosLosCircuitos, nombreCircuito);
                    if (circuitoAsignado == null) {
                        throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): No se encontró el circuito '" + nombreCircuito + "'.");
                    }

                    Pais paisAsignado = circuitoAsignado.getPais();
                    Carrera c = new Carrera(fechaRealizacion, nroVueltas, horaRealizacion, paisAsignado, circuitoAsignado);
                    carreras.add(c);
                    nroLinea++;
                } catch (NumberFormatException e) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Nro de vueltas no es un número.");
                }
            }
        } catch (IOException e) {
            throw new LogicaException("Error  al leer el archivo " + path + ": " + e.getMessage());
        }
        return carreras;
    }

    /**
     * Lee el archivo "DatosEscuderias.csv".
     * @param path Ruta al archivo.
     * @return Lista de objetos Escuderia.
     * @throws LogicaException Si el archivo no se encuentra o hay un error de formato.
     */
    public static List<Escuderia> leerEscuderiasDesdeCSV(String path) throws LogicaException {
        List<Escuderia> escuderias = new ArrayList<>();
        int nroLinea = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            br.readLine(); // Saltear encabezado
            nroLinea++;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) { nroLinea++; continue; }
                String[] valores = linea.split(",");
                if (valores.length < 1) throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Línea incompleta.");

                String nombre = valores[0].trim();
                Escuderia e = new Escuderia(nombre);
                escuderias.add(e);
                nroLinea++;
            }
        } catch (IOException e) {
            throw new LogicaException("Error  al leer el archivo " + path + ": " + e.getMessage());
        }
        return escuderias;
    }

    /**
     * Lee el archivo "DatosMecanicos.csv".
     * @param path Ruta al archivo.
     * @param paises Lista de países para asignar la relación.
     * @return Lista de objetos Mecanico.
     * @throws LogicaException Si el archivo no se encuentra o hay un error de formato.
     */
    public static List<Mecanico> leerMecanicosDesdeCSV(String path, List<Pais> paises) throws LogicaException {
        List<Mecanico> mecanicos = new ArrayList<>();
        int nroLinea = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            br.readLine(); // Saltear encabezado
            nroLinea++;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) { nroLinea++; continue; }
                String[] valores = linea.split(",");
                if (valores.length < 6) throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Línea incompleta.");

                try {
                    String dni = valores[0].trim();
                    String nombre = valores[1].trim();
                    String apellido = valores[2].trim();
                    int idPais = Integer.parseInt(valores[3].trim());
                    Especialidad esp = Especialidad.valueOf(valores[4].trim().toUpperCase()); // Convertir a mayúsculas
                    int aniosExperiencia = Integer.parseInt(valores[5].trim());

                    Pais paisAsignado = buscarPaisPorId(paises, idPais);
                    if (paisAsignado == null) {
                        throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): No se encontró el país con ID " + idPais);
                    }
                    
                    Mecanico m = new Mecanico(dni, nombre, apellido, paisAsignado, esp, aniosExperiencia, new ArrayList<>());
                    mecanicos.add(m);
                    nroLinea++;
                } catch (NumberFormatException e) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): ID de país o Años de exp. no son números.");
                } catch (IllegalArgumentException e) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Especialidad '" + valores[4].trim() + "' no válida.");
                }
            }
        } catch (IOException e) {
            throw new LogicaException("Error  al leer el archivo " + path + ": " + e.getMessage());
        }
        return mecanicos;
    }

    /**
     * Lee el archivo "DatosPilotos.csv".
     * @param path Ruta al archivo.
     * @param paises Lista de países para asignar la relación.
     * @return Lista de objetos Piloto.
     * @throws LogicaException Si el archivo no se encuentra o hay un error de formato.
     */
    public static List<Piloto> leerPilotosDesdeCSV(String path, List<Pais> paises) throws LogicaException {
        List<Piloto> pilotos = new ArrayList<>();
        int nroLinea = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            br.readLine(); // Saltear encabezado
            nroLinea++;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) { nroLinea++; continue; }
                String[] valores = linea.split(",");
                if (valores.length < 9) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): La línea está incompleta.");
                }

                try {
                    String dni = valores[0].trim();
                    String nombre = valores[1].trim();
                    String apellido = valores[2].trim();
                    int idPais = Integer.parseInt(valores[3].trim());
                    int numeroCompetencia = Integer.parseInt(valores[4].trim());
                    int victorias = Integer.parseInt(valores[5].trim());
                    int polePosition = Integer.parseInt(valores[6].trim());
                    int vueltasRapidas = Integer.parseInt(valores[7].trim());
                    int podios = Integer.parseInt(valores[8].trim());

                    Pais paisAsignado = buscarPaisPorId(paises, idPais);
                    if (paisAsignado == null) {
                        throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): No se encontró el país con ID " + idPais);
                    }
                    
                    Piloto p = new Piloto(dni, nombre, apellido, paisAsignado, numeroCompetencia, victorias, polePosition, vueltasRapidas, podios);
                    pilotos.add(p);
                    nroLinea++;
                } catch (NumberFormatException e) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Dato numérico (ID, stats) inválido.");
                }
            }
        } catch (IOException e) {
            throw new LogicaException("Error  al leer el archivo " + path + ": " + e.getMessage());
        }
        return pilotos;
    }

    /**
     * Lee el archivo de unión "DatosMecanicoEscuderia.csv" y vincula las entidades.
     * @param path Ruta al archivo.
     * @param todosLosMecanicos Lista de todos los mecánicos.
     * @param todasLasEscuderias Lista de todas las escuderías.
     * @throws LogicaException Si el archivo no se encuentra o hay un error de formato.
     */
    public static void vincularMecanicosAEscuderias(String path, List<Mecanico> todosLosMecanicos, List<Escuderia> todasLasEscuderias) throws LogicaException {
        int nroLinea = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            br.readLine(); // Saltear encabezado
            nroLinea++;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) { nroLinea++; continue; }
                String[] valores = linea.split(",");
                if (valores.length < 2) throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): Línea incompleta.");

                String dniMecanico = valores[0].trim();
                String nombreEscuderia = valores[1].trim();

                Mecanico mecanico = buscarMecanicoPorDNI(todosLosMecanicos, dniMecanico);
                Escuderia escuderia = buscarEscuderiaPorNombre(todasLasEscuderias, nombreEscuderia);

                if (mecanico == null) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): No se encontró el mecánico con DNI " + dniMecanico);
                }
                if (escuderia == null) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): No se encontró la escudería '" + nombreEscuderia + "'.");
                }
                
                mecanico.agregarEscuderia(escuderia);
                escuderia.agregarMecanico(mecanico);
                nroLinea++;
            }
        } catch (IOException e) {
            throw new LogicaException("Error  al leer el archivo " + path + ": " + e.getMessage());
        }
    }

    /**
     * Lee el archivo "DatosResultadoCarrera.csv".
     * @param path Ruta al archivo.
     * @param pilotos Lista de pilotos para asignar la relación.
     * @param carreras Lista de carreras para asignar la relación.
     * @return Lista de objetos ResultadoCarrera.
     * @throws LogicaException Si el archivo no se encuentra o hay un error de formato.
     */
    public static List<ResultadoCarrera> leerResultadosDesdeCSV(String path, List<Piloto> pilotos, List<Carrera> carreras)
            throws LogicaException {

        List<ResultadoCarrera> resultados = new ArrayList<>();
        int nroLinea = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            br.readLine(); // Saltear encabezado
            nroLinea++;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    nroLinea++;
                    continue;
                }

                String[] valores = linea.split(",");
                if (valores.length < 3) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): La línea está incompleta.");
                }

                String dniPiloto = valores[0].trim();
                String fechaCarrera = valores[1].trim();
                int posicion;

                try {
                    posicion = Integer.parseInt(valores[2].trim());
                } catch (NumberFormatException e) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): La posición '" + valores[2].trim() + "' no es un número.");
                }

                Piloto pilotoAsignado = buscarPilotoPorDNI(pilotos, dniPiloto);
                Carrera carreraAsignada = buscarCarreraPorFecha(carreras, fechaCarrera);

                if (pilotoAsignado == null) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): No se encontró el piloto con DNI " + dniPiloto);
                }
                if (carreraAsignada == null) {
                    throw new LogicaException("Error en " + path + " (Línea " + nroLinea + "): No se encontró la carrera con fecha " + fechaCarrera);
                }
                
                ResultadoCarrera res = new ResultadoCarrera(pilotoAsignado, posicion, carreraAsignada);
                resultados.add(res);
                nroLinea++;
            }
        } catch (IOException e) {
            throw new LogicaException("Error al leer el archivo " + path + ": " + e.getMessage());
        }
        return resultados;
    }
}