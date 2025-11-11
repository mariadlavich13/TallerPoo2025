
// Importaciones de la Lógica y Persistencia
import archivos.SistemaGestion;
import logica.LogicaException;
import logica.LogicaGestion;
import logica.LogicaInformes;
import logica.LogicaRegistro;
import logica.PilotoPuntaje;

// Importaciones de Clases del Modelo (todas)
import clases.*;

// Importaciones de Java Swing (GUI)
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList; // Necesario para la lista de participantes

/**
 * PANEL DE PRUEBAS DE GUI (Test Harness)
 * * Esta clase es una GUI de Swing (JFrame) que carga todo el sistema
 * y proporciona un menú para probar CADA UNA de las funciones de la capa de Lógica.
 * Atrapa y muestra las 'LogicaException' usando JOptionPane.
 * * Es la base funcional sobre la cual se puede construir la GUI estética.
 */
public class MainGUI extends JFrame {

    // --- Capas de Lógica y Persistencia ---
    private SistemaGestion sistema;
    private LogicaRegistro logicaRegistro;
    private LogicaGestion logicaGestion;
    private LogicaInformes logicaInformes;

    /**
     * Método principal que inicia la aplicación GUI.
     */
    public static void main (String[] args) {
        // Asegura que la GUI se ejecute en el Hilo de Despacho de Eventos (EDT)
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }

    /**
     * Constructor. Carga el sistema y construye la GUI.
     */
    public MainGUI() {
        // 1. Inicializar las capas
        sistema = new SistemaGestion();
        logicaRegistro = new LogicaRegistro();
        logicaGestion = new LogicaGestion();
        logicaInformes = new LogicaInformes();

        // 2. Cargar datos (¡El paso más crítico!)
        try {
            // Intenta cargar todos los CSV
            sistema.cargarDatos(); //
            mostrarInfo("Sistema cargado exitosamente.\n" +
                        sistema.getPilotos().size() + " pilotos, " +
                        sistema.getCarreras().size() + " carreras, " +
                        sistema.getResultadosCarreras().size() + " resultados cargados.");
        } catch (LogicaException e) {
            // Si algo falla (CSV faltante, dato corrupto, ID no encontrado),
            // la GUI lo captura y lo muestra.
            mostrarError("Error crítico al cargar los datos:\n" + e.getMessage() +
                        "\nLa aplicación se cerrará.");
            System.exit(1); // Cierra la app si no se pueden cargar los datos
        }

        // 3. Configurar la Ventana Principal (JFrame)
        setTitle("Panel de Pruebas Funcional - Taller F1");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar
        setLayout(new BorderLayout());
        
        // Mensaje de bienvenida
        JTextArea bienvenida = new JTextArea(
            "Bienvenido al Panel de Pruebas Funcional.\n\n" +
            "Esta GUI no es estética, pero es funcional.\n" +
            "Usa los menús de arriba para probar cada método de tu capa de lógica.\n\n" +
            "1. Carga de datos: Exitosa.\n" +
            "2. Manejo de excepciones: Activo.\n" +
            "3. Lógica de negocio: Conectada.\n\n" +
            "¡Listo para probar!"
        );
        bienvenida.setEditable(false);
        bienvenida.setFont(new Font("Arial", Font.PLAIN, 16));
        bienvenida.setMargin(new Insets(20, 20, 20, 20));
        add(bienvenida, BorderLayout.CENTER);

        // 4. Construir la Barra de Menús
        construirMenu();
    }

    /**
     * Crea y añade la barra de menús principal al JFrame.
     */
    private void construirMenu() {
        JMenuBar menuBar = new JMenuBar();

        // --- Menú REGISTRAR (LógicaRegistro) ---
        JMenu menuRegistro = new JMenu("Registrar (LógicaRegistro)");
        menuRegistro.add(crearItemMenu("Registrar País...", e -> registrarPais()));
        menuRegistro.add(crearItemMenu("Registrar Escudería...", e -> testRegistrarEscuderia()));
        menuRegistro.add(crearItemMenu("Registrar Circuito...", e -> testRegistrarCircuito()));
        menuRegistro.add(crearItemMenu("Registrar Carrera...", e -> testRegistrarCarrera()));
        menuRegistro.add(crearItemMenu("Registrar Piloto...", e -> testRegistrarPiloto()));
        menuRegistro.add(crearItemMenu("Registrar Mecánico...", e -> testRegistrarMecanico()));
        menuRegistro.add(crearItemMenu("Registrar Auto...", e -> testRegistrarAuto()));
        menuBar.add(menuRegistro);

        // --- Menú GESTIONAR (LógicaGestion) ---
        JMenu menuGestion = new JMenu("Gestionar (LógicaGestion)");
        menuGestion.add(crearItemMenu("Asociar Piloto a Auto en Carrera...", e -> testAsociarPilotoAutoCarrera()));
        menuGestion.add(crearItemMenu("Registrar Resultado de Carrera...", e -> testRegistrarResultado()));
        menuGestion.add(crearItemMenu("Asociar Piloto a Escudería (Contrato)...", e -> testAsociarPilotoEscuderia()));
        menuGestion.add(crearItemMenu("Desvincular Piloto de Escudería (Fin Contrato)...", e -> testDesvincularPilotoEscuderia()));
        menuGestion.add(crearItemMenu("Asociar Auto a Escudería...", e -> testAsociarAutoAEscuderia()));
        menuGestion.add(crearItemMenu("Asociar Mecánico a Escudería...", e -> testAsociarMecanicoAEscuderia()));
        menuGestion.add(crearItemMenu("Asignar Pole Position a Piloto...", e -> testAsignarPolePosition()));
        menuBar.add(menuGestion);
        
        // --- Menú INFORMES (LogicaInformes) ---
        JMenu menuInformes = new JMenu("Informes (LógicaInformes)");
        menuInformes.add(crearItemMenu("1. Ranking de Pilotos (Puntajes)", e -> testRankingPilotos()));
        menuInformes.add(crearItemMenu("2. Resultados por Rango de Fechas", e -> testReporteResultadosPorFecha()));
        menuInformes.add(crearItemMenu("3. Histórico de un Piloto", e -> testHistoricoPiloto()));
        menuInformes.add(crearItemMenu("4. Histórico de todos los Pilotos", e -> testHistoricoTodosLosPilotos()));
        menuInformes.add(crearItemMenu("5. Reporte: Autos por Escudería", e -> testReporteAutosPorEscuderia()));
        menuInformes.add(crearItemMenu("6. Reporte: Mecánicos por Escudería", e -> testReporteMecanicosPorEscuderia()));
        menuInformes.add(crearItemMenu("7. Contador: Piloto en Circuito", e -> testContadorPilotoEnCircuito()));
        menuInformes.add(crearItemMenu("8. Contador: Carreras en Circuito", e -> testContadorCarrerasEnCircuito()));
        menuBar.add(menuInformes);

        // Establece la barra de menús en la ventana
        setJMenuBar(menuBar);
    }
    
    // -----------------------------------------------------------------
    // --- MÉTODOS DE PRUEBA: REGISTRO (LogicaRegistro) ---
    // -----------------------------------------------------------------
    
private void registrarPais() {
    try {
        // 1. Obtener ID y Descripción del País
        String idInput = JOptionPane.showInputDialog(this, "Ingrese el ID del País:", "Registro de País", JOptionPane.QUESTION_MESSAGE);
        
        // Manejar cancelación
        if (idInput == null) return; 
        
        int idPais = Integer.parseInt(idInput);
        
        String descripcion = JOptionPane.showInputDialog(this, "Ingrese el nombre del País:", "Registro de País", JOptionPane.QUESTION_MESSAGE);

        // Manejar cancelación
        if (descripcion == null) return; 

        // 2. Llamada a la lógica que puede lanzar LogicaException
        logicaRegistro.registrarPais(sistema, idPais, descripcion);

        // 3. Mostrar mensaje de éxito si todo salió bien
        JOptionPane.showMessageDialog(this, 
            "País registrado con éxito!", 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);

    } catch (NumberFormatException e) {
        // Captura errores si el usuario ingresa un ID no numérico
        JOptionPane.showMessageDialog(this, 
            "Error de Entrada: El ID debe ser un número entero válido.", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    } catch (LogicaException e) {
        // 4. Captura la LogicaException (por ID o nombre duplicado)
        // Muestra el mensaje de error definido en LogicaRegistro
        JOptionPane.showMessageDialog(this, 
            "Error al registrar país:\n" + e.getMessage(), 
            "Error de Registro", 
            JOptionPane.ERROR_MESSAGE);
    }
}

    private void testRegistrarEscuderia() {
        try {
            Pais p = seleccionarPais();
            if (p == null) return;
            String nombre = getDesdeUsuario("Ingrese el nombre de la nueva escudería:");
            if (nombre == null) return;
            logicaRegistro.registrarEscuderia(sistema, nombre, p); //
            mostrarInfo("¡Escudería '" + nombre + "' registrada con éxito!");
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testRegistrarCircuito() {
        try {
            Pais p = seleccionarPais();
            if (p == null) return;
            String nombre = getDesdeUsuario("Nombre del nuevo circuito:");
            int longitud = Integer.parseInt(getDesdeUsuario("Longitud (km, ej: 5):"));
            logicaRegistro.registrarCircuito(sistema, nombre, longitud, p); //
            mostrarInfo("¡Circuito '" + nombre + "' registrado con éxito!");
        } catch (LogicaException | NumberFormatException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testRegistrarCarrera() {
        try {
            Circuito c = seleccionarCircuito();
            if (c == null) return;
            String fecha = getDesdeUsuario("Fecha de la carrera (dd-MM-yyyy):");
            int vueltas = Integer.parseInt(getDesdeUsuario("Número de vueltas:"));
            String hora = getDesdeUsuario("Hora de la carrera (ej: 15:00):");
            logicaRegistro.registrarCarrera(sistema, fecha, vueltas, hora, c.getPais(), c); //
            mostrarInfo("¡Carrera registrada en " + c.getNombre() + " para el " + fecha + "!");
        } catch (LogicaException | NumberFormatException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void testRegistrarPiloto() {
        try {
            String dni = getDesdeUsuario("DNI del piloto:");
            String nom = getDesdeUsuario("Nombre:");
            String ape = getDesdeUsuario("Apellido:");
            Pais p = seleccionarPais();
            if (p == null) return;
            int nro = Integer.parseInt(getDesdeUsuario("Nro Competencia:"));
            logicaRegistro.registrarPiloto(sistema, dni, nom, ape, p, nro, 0, 0, 0, 0); //
            mostrarInfo("¡Piloto '" + nom + " " + ape + "' registrado con éxito!");
        } catch (LogicaException | NumberFormatException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testRegistrarMecanico() {
        try {
            String dni = getDesdeUsuario("DNI del mecánico:");
            String nom = getDesdeUsuario("Nombre:");
            String ape = getDesdeUsuario("Apellido:");
            Pais p = seleccionarPais();
            if (p == null) return;
            Especialidad esp = seleccionarEspecialidad();
            if (esp == null) return;
            int anios = Integer.parseInt(getDesdeUsuario("Años de experiencia:"));
            logicaRegistro.registrarMecanico(sistema, dni, nom, ape, p, esp, anios); //
            mostrarInfo("¡Mecánico '" + nom + " " + ape + "' registrado con éxito!");
        } catch (LogicaException | NumberFormatException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testRegistrarAuto() {
        try {
            String modelo = getDesdeUsuario("Modelo del auto (ej: W16):");
            String motor = getDesdeUsuario("Motor del auto (ej: Mercedes-AMG F1 M16):");
            logicaRegistro.registrarAuto(sistema, modelo, motor); //
            mostrarInfo("¡Auto '" + modelo + "' registrado con éxito!");
            mostrarInfo("Recuerde asociar este auto a una escudería desde el menú 'Gestionar'.");
        } catch (Exception ex) { // No lanza LogicaException, pero por si acaso
            mostrarError(ex.getMessage());
        }
    }

    // -----------------------------------------------------------------
    // --- MÉTODOS DE PRUEBA: GESTIÓN (LogicaGestion) ---
    // -----------------------------------------------------------------
    
    private void testAsociarPilotoAutoCarrera() {
        try {
            Piloto p = seleccionarPiloto();
            Auto a = seleccionarAuto();
            Carrera c = seleccionarCarrera();
            if (p == null || a == null || c == null) return;
            logicaGestion.asociarPilotoAutoACarrera(c, p, a, c.getFechaRealizacion()); //
            mostrarInfo("¡ASIGNACIÓN EXITOSA!\nPiloto: " + p.getNombre() + "\nAuto: " + a.getModelo() + "\nCarrera: " + c.getCircuito().getNombre());
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testRegistrarResultado() {
        try {
            Carrera c = seleccionarCarrera();
            if (c == null) return;
            Piloto p = seleccionarParticipante(c);
            if (p == null) return;
            int pos = Integer.parseInt(getDesdeUsuario("Posición final para " + p.getNombre() + ":"));
            boolean vr = getConfirmacion("¿Obtuvo la vuelta rápida?");
            logicaGestion.registrarResultado(sistema, c, p, pos, vr); //
            mostrarInfo("¡Resultado registrado!\nPiloto: " + p.getNombre() + "\nPosición: " + pos + "\n(Revise el ranking)");
        } catch (LogicaException | NumberFormatException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testAsociarPilotoEscuderia() {
        try {
            Piloto p = seleccionarPiloto();
            Escuderia e = seleccionarEscuderia();
            String fecha = getDesdeUsuario("Fecha de inicio de contrato (dd-MM-yyyy):");
            if (p == null || e == null || fecha == null) return;
            logicaGestion.asociarPilotoAEscuderia(p, e, fecha); //
            mostrarInfo("¡CONTRATO EXITOSO!\n" + p.getNombre() + " -> " + e.getNombre() + " (desde " + fecha + ")");
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testDesvincularPilotoEscuderia() {
        try {
            Piloto p = seleccionarPiloto();
            if (p == null) return;
            Escuderia e = seleccionarContratoActivo(p);
            if (e == null) return;
            String fecha = getDesdeUsuario("Fecha de FIN de contrato (dd-MM-yyyy):");
            if (fecha == null) return;
            logicaGestion.desvincularPilotoDeEscuderia(p, e, fecha); //
            mostrarInfo("¡CONTRATO FINALIZADO!\n" + p.getNombre() + " y " + e.getNombre() + " (hasta " + fecha + ")");
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testAsociarAutoAEscuderia() {
        try {
            Auto a = seleccionarAuto();
            Escuderia e = seleccionarEscuderia();
            if (a == null || e == null) return;
            logicaGestion.asociarAutoAEscuderia(a, e); //
            mostrarInfo("¡Auto " + a.getModelo() + " ahora pertenece a " + e.getNombre() + "!");
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testAsociarMecanicoAEscuderia() {
        try {
            Mecanico m = seleccionarMecanico();
            Escuderia e = seleccionarEscuderia();
            if (m == null || e == null) return;
            logicaGestion.asociarMecanicoAEscuderia(m, e); //
            mostrarInfo("¡Mecánico " + m.getNombre() + " ahora trabaja para " + e.getNombre() + "!");
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testAsignarPolePosition() {
        try {
            Piloto p = seleccionarPiloto();
            if (p == null) return;
            int polesAntes = p.getPolePosition();
            logicaGestion.asignarPolePosition(p); //
            mostrarInfo("¡Pole position asignada a " + p.getNombre() + " " + p.getApellido() + "!\n" +
                        "Total anterior: " + polesAntes + "\n" +
                        "Total nuevo: " + p.getPolePosition());
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    // -----------------------------------------------------------------
    // --- MÉTODOS DE PRUEBA: INFORMES (LogicaInformes) ---
    // -----------------------------------------------------------------
    
    private void testRankingPilotos() {
        try {
            List<PilotoPuntaje> ranking = logicaInformes.getRankingPilotos(sistema, logicaGestion); //
            StringBuilder sb = new StringBuilder("--- Ranking de Pilotos ---\n\n");
            int i = 1;
            for (PilotoPuntaje pp : ranking) {
                sb.append(String.format("%-3s %-25s %s puntos\n",
                    i + ".",
                    pp.getPiloto().getNombre() + " " + pp.getPiloto().getApellido(),
                    pp.getPuntajeTotal()));
                i++;
            }
            mostrarReporte(sb.toString(), "Ranking de Pilotos");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testReporteResultadosPorFecha() {
        try {
            String fDesde = getDesdeUsuario("Fecha desde (formato dd-MM-yyyy):");
            if (fDesde == null) return;
            String fHasta = getDesdeUsuario("Fecha hasta (formato dd-MM-yyyy):");
            if (fHasta == null) return;

            List<ResultadoCarrera> resultados = logicaInformes.getReporteResultadosPorFechas(sistema, fDesde, fHasta); //
            StringBuilder sb = new StringBuilder("--- Resultados del " + fDesde + " al " + fHasta + " ---\n\n");
            if (resultados.isEmpty()) {
                sb.append("No se encontraron resultados en ese rango.");
            }
            String carreraActual = "";
            for (ResultadoCarrera r : resultados) {
                String nombreCarrera = r.getCarrera().getCircuito().getNombre() + " (" + r.getCarrera().getFechaRealizacion() + ")";
                if (!nombreCarrera.equals(carreraActual)) {
                    sb.append("\n--- ").append(nombreCarrera).append(" ---\n");
                    carreraActual = nombreCarrera;
                }
                sb.append(String.format("  %-3s %s\n",
                    r.getPosicion() + "º",
                    r.getPiloto().getNombre() + " " + r.getPiloto().getApellido()));
            }
            mostrarReporte(sb.toString(), "Reporte de Resultados");
        } catch (Exception ex) {
            mostrarError("Error al formatear fechas. Use el formato dd-MM-yyyy.\n" + ex.getMessage());
        }
    }
    
    private void testHistoricoPiloto() {
        try {
            Piloto p = seleccionarPiloto();
            if (p == null) return;
            Piloto pStats = logicaInformes.getEstadisticasPiloto(sistema, p.getDni()); //
            String reporte = "--- Estadísticas de " + pStats.getNombre() + " " + pStats.getApellido() + " ---\n" +
                            "Victorias: " + pStats.getVictorias() + "\n" +
                            "Podios: " + pStats.getPodios() + "\n" +
                            "Pole Positions: " + pStats.getPolePosition() + "\n" +
                            "Vueltas Rápidas: " + pStats.getVueltasRapidas() + "\n";
            mostrarReporte(reporte, "Histórico de Piloto");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testHistoricoTodosLosPilotos() {
        try {
            List<Piloto> pilotos = logicaInformes.getEstadisticasTodosLosPilotos(sistema); //
            StringBuilder sb = new StringBuilder("--- Histórico de Todos los Pilotos ---\n\n");
            for(Piloto p : pilotos) {
                sb.append(String.format("%-25s (Victorias:%s, Podios:%s, PolePosition:%s, VueltasRapidas:%s)\n",
                    p.getNombre() + " " + p.getApellido(),
                    p.getVictorias(), p.getPodios(), p.getPolePosition(), p.getVueltasRapidas()));
            }
            mostrarReporte(sb.toString(), "Histórico de Pilotos");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testReporteAutosPorEscuderia() {
        try {
            List<AutoPiloto> informe = logicaInformes.getReporteAutosPorEscuderia(sistema); //
            StringBuilder sb = new StringBuilder("--- Reporte Autos por Escudería y Carrera ---\n");
            String escuderiaActual = "";
            for (AutoPiloto ap : informe) {
                String nombreEscuderia = (ap.getAuto().getEscuderia() != null) ? ap.getAuto().getEscuderia().getNombre() : "SIN ESCUDERIA";
                if(!nombreEscuderia.equals(escuderiaActual)) {
                    sb.append("\n--- ").append(nombreEscuderia.toUpperCase()).append(" ---\n");
                    escuderiaActual = nombreEscuderia;
                }
                sb.append(String.format("  Carrera: %s (%s)\n", ap.getCarrera().getCircuito().getNombre(), ap.getCarrera().getFechaRealizacion()));
                sb.append(String.format("     Piloto: %s\n", ap.getPiloto().getNombre() + " " + ap.getPiloto().getApellido()));
                sb.append(String.format("     Auto: %s (%s)\n", ap.getAuto().getModelo(), ap.getAuto().getMotor()));
            }
            mostrarReporte(sb.toString(), "Reporte de Autos");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testReporteMecanicosPorEscuderia() {
        try {
            List<Escuderia> escuderias = logicaInformes.getReporteMecanicosPorEscuderia(sistema); //
            StringBuilder sb = new StringBuilder("--- Reporte de Mecánicos por Escudería ---\n");
            for (Escuderia e : escuderias) {
                sb.append("\n--- ").append(e.getNombre().toUpperCase()).append(" ---\n");
                if (e.getMecanicos().isEmpty()) {
                    sb.append("  (Sin mecánicos asignados)\n");
                }
                for (Mecanico m : e.getMecanicos()) {
                    sb.append(String.format("  - %-20s (%s - %d años exp.)\n",
                        m.getNombre() + " " + m.getApellido(),
                        m.getEspecialidad(),
                        m.getAniosExperiencia()));
                }
            }
            mostrarReporte(sb.toString(), "Reporte de Mecánicos");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testContadorPilotoEnCircuito() {
        try {
            Piloto p = seleccionarPiloto();
            Circuito c = seleccionarCircuito();
            if (p == null || c == null) return;
            int cont = logicaInformes.getContadorPilotoEnCircuito(sistema, p, c); //
            
            String veces = (cont == 1) ? "vez" : "veces";
            
            mostrarInfo("El piloto " + p.getNombre() + " corrió " + cont + " " + veces + " en " + c.getNombre());
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }
    
    private void testContadorCarrerasEnCircuito() {
        try {
            Circuito c = seleccionarCircuito();
            if (c == null) return;
            int cont = logicaInformes.getContadorCarrerasEnCircuito(sistema, c); //
            mostrarInfo("Se corrieron " + cont + " carreras en " + c.getNombre());
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    // -----------------------------------------------------------------
    // --- UTILIDADES GUI ---
    // -----------------------------------------------------------------

    /** Muestra un diálogo de error. */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error de Lógica", JOptionPane.ERROR_MESSAGE);
    }
    
    /** Muestra un diálogo de información. */
    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /** Muestra un diálogo de entrada de texto. */
    private String getDesdeUsuario(String mensaje) {
        return JOptionPane.showInputDialog(this, mensaje);
    }
    
    /** Muestra un diálogo de confirmación (Si/No). */
    private boolean getConfirmacion(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    /** Muestra un reporte largo en un JTextArea con scroll. */
    private void mostrarReporte(String textoReporte, String titulo) {
        JTextArea textArea = new JTextArea(25, 70);
        textArea.setText(textoReporte);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Fuente monoespaciada para alinear texto
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    /** Crea un JMenuItem y le asigna un ActionListener. */
    private JMenuItem crearItemMenu(String titulo, ActionListener listener) {
        JMenuItem item = new JMenuItem(titulo);
        item.addActionListener(listener);
        return item;
    }
    
    // -----------------------------------------------------------------
    // --- UTILIDADES DE SELECCIÓN (Simulan JComboBoxes) ---
    // -----------------------------------------------------------------
    
    private Pais seleccionarPais() {
        return (Pais) JOptionPane.showInputDialog(this, "Seleccione un País:", "Seleccionar País",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getPaises().toArray(), sistema.getPaises().get(0));
    }
    
    private Piloto seleccionarPiloto() {
        return (Piloto) JOptionPane.showInputDialog(this, "Seleccione un Piloto:", "Seleccionar Piloto",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getPilotos().toArray(), sistema.getPilotos().get(0));
    }
    
    private Piloto seleccionarParticipante(Carrera c) {
        if(c.getParticipantes().isEmpty()) {
            mostrarError("Esta carrera no tiene pilotos participantes registrados.");
            return null;
        }
        List<Piloto> participantes = new ArrayList<>();
        for(AutoPiloto ap : c.getParticipantes()) { participantes.add(ap.getPiloto()); }
        return (Piloto) JOptionPane.showInputDialog(this, "Seleccione un Piloto (que haya participado):", "Seleccionar Participante",
                JOptionPane.QUESTION_MESSAGE, null,
                participantes.toArray(), participantes.get(0));
    }

    private Auto seleccionarAuto() {
        return (Auto) JOptionPane.showInputDialog(this, "Seleccione un Auto:", "Seleccionar Auto",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getAutos().toArray(), sistema.getAutos().get(0));
    }
    
    private Carrera seleccionarCarrera() {
        return (Carrera) JOptionPane.showInputDialog(this, "Seleccione una Carrera (por fecha y circuito):", "Seleccionar Carrera",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getCarreras().toArray(), sistema.getCarreras().get(0));
    }
    
    private Escuderia seleccionarEscuderia() {
        return (Escuderia) JOptionPane.showInputDialog(this, "Seleccione una Escudería:", "Seleccionar Escudería",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getEscuderias().toArray(), sistema.getEscuderias().get(0));
    }
    
    private Circuito seleccionarCircuito() {
        return (Circuito) JOptionPane.showInputDialog(this, "Seleccione un Circuito:", "Seleccionar Circuito",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getCircuitos().toArray(), sistema.getCircuitos().get(0));
    }
    
    private Mecanico seleccionarMecanico() {
        return (Mecanico) JOptionPane.showInputDialog(this, "Seleccione un Mecánico:", "Seleccionar Mecánico",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getMecanicos().toArray(), sistema.getMecanicos().get(0));
    }
    
    private Especialidad seleccionarEspecialidad() {
        return (Especialidad) JOptionPane.showInputDialog(this, "Seleccione una Especialidad:", "Seleccionar Especialidad",
                JOptionPane.QUESTION_MESSAGE, null,
                Especialidad.values(), Especialidad.values()[0]);
    }
    
    private Escuderia seleccionarContratoActivo(Piloto p) throws LogicaException {
        List<Escuderia> activas = new ArrayList<>();
        for(PilotoEscuderia pe : p.getPilotosEscuderias()) {
            if(pe.getHastaFecha() == null || pe.getHastaFecha().trim().isEmpty()) {
                activas.add(pe.getEscuderia());
            }
        }
        if (activas.isEmpty()) {
            throw new LogicaException("El piloto " + p.getNombre() + " no tiene contratos activos para finalizar.");
        }
        return (Escuderia) JOptionPane.showInputDialog(this, "Seleccione el contrato activo a finalizar:", "Finalizar Contrato",
                JOptionPane.QUESTION_MESSAGE, null,
                activas.toArray(), activas.get(0));
    }
}