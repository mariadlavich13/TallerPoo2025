package gui; // Declaración del paquete

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
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;

// Nuevas importaciones para la Fuente Personalizada y Gráficos 2D
import java.io.File;
import java.io.IOException;

/**
 * Clase principal de la Interfaz Gráfica de Usuario (GUI) para el sistema de
 * Gestión de F1 "Escuderías Unidas".
 * <p>
 * Extiende {@link JFrame} y utiliza un {@link CardLayout} para navegar entre
 * los diferentes módulos del sistema (Bienvenida, Registro, Gestión, Informes).
 * <p>
 * Esta clase también se encarga de:
 * <ul>
 * <li>Cargar las fuentes personalizadas (F1.ttf, racing-sans-one).</li>
 * <li>Inicializar las capas de lógica ({@link SistemaGestion}, {@link LogicaRegistro}, etc.).</li>
 * <li>Cargar los datos iniciales desde los CSV.</li>
 * <li>Definir y aplicar el estilo visual (tema oscuro F1).</li>
 * <li>Manejar todas las interacciones del usuario y mostrar diálogos (información, error, selección).</li>
 * </ul>
 *
 * @see Main
 * @see archivos.SistemaGestion
 * @see logica.LogicaGestion
 * @see logica.LogicaRegistro
 * @see logica.LogicaInformes
 */
public class Gui extends JFrame {

    // --- Capas de Lógica y Persistencia ---
    private SistemaGestion sistema;
    private LogicaRegistro logicaRegistro;
    private LogicaGestion logicaGestion;
    private LogicaInformes logicaInformes;

    // --- Componentes GUI Principales ---
    private CardLayout cardLayout;
    private JPanel panelPrincipal; // El panel que contiene las pantallas

    // --- Paleta de Colores ---
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(0x15151e); // Azul oscuro 
    private static final Color COLOR_FONDO_SECUNDARIO = new Color(0x1F1F2B); // Un gris levemente más claro
    private static final Color COLOR_ROJO = new Color(0xD00000); // Rojo
    private static final Color COLOR_ROJO_OSCURO = new Color(0xA60000); // Rojo oscuro
    private static final Color COLOR_TEXTO_PRINCIPAL = Color.WHITE;
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(0xb0b0b0); // Gris claro

    // --- Tipografías (Versión 4: Cargando F1.ttf y rancing-sans-one.regular.ttf) ---

    // 1. Variables base para las fuentes personalizadas
    private static Font F1_FONT_BASE;
    private static Font RACING_SANS_ONE_FONT_BASE;

    /**
     * Bloque estático: Carga las fuentes personalizadas desde la carpeta 'fonts'.
     * Se ejecuta una sola vez cuando la clase es cargada por la JVM.
     * Registra las fuentes en el {@link GraphicsEnvironment} local.
     */
    static {
        // Carga F1.ttf
        String f1FontPath = "fonts/F1.ttf";
        try {
            File fontFile = new File(f1FontPath);
            if (fontFile.exists()) {
                F1_FONT_BASE = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(F1_FONT_BASE);
                System.out.println("Fuente F1.ttf cargada exitosamente.");
            } else {
                System.err.println("Advertencia: No se encontró el archivo de fuente: " + f1FontPath);
                throw new IOException("Archivo de fuente F1.ttf no encontrado");
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Error al cargar F1.ttf. Usando 'SansSerif' por defecto.");
            e.printStackTrace();
            F1_FONT_BASE = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        }

        // Carga racing-sans-one.regular.ttf
        String racingSansOnePath = "fonts/racing-sans-one.regular.ttf";
        try {
            File fontFile = new File(racingSansOnePath);
            if (fontFile.exists()) {
                RACING_SANS_ONE_FONT_BASE = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(RACING_SANS_ONE_FONT_BASE);
                System.out.println("Fuente racing-sans-one.regular.ttf cargada exitosamente.");
            } else {
                System.err.println("Advertencia: No se encontró el archivo de fuente: " + racingSansOnePath);
                throw new IOException("Archivo de fuente racing-sans-one.regular.ttf no encontrado");
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Error al cargar racing-sans-one.regular.ttf. Usando 'SansSerif' por defecto.");
            e.printStackTrace();
            RACING_SANS_ONE_FONT_BASE = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        }
    }

    // 2. Definición de las constantes de fuente (derivadas de las bases)
    private static final Font FONT_TITULO_APP = RACING_SANS_ONE_FONT_BASE.deriveFont(Font.BOLD, 36f);
    private static final Font FONT_BIENVENIDA = RACING_SANS_ONE_FONT_BASE.deriveFont(Font.PLAIN, 28f);

    private static final Font FONT_NAV_BOTONES = F1_FONT_BASE.deriveFont(Font.BOLD, 20f);
    private static final Font FONT_NAV_TITULO = F1_FONT_BASE.deriveFont(Font.BOLD, 17f);

    // 3. Fuentes SANS_SERIF para el resto de la UI
    private static final Font FONT_BOTON_ACCION = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private static final Font FONT_TITULO_PANEL_ACCION = new Font(Font.SANS_SERIF, Font.BOLD, 20);


    /**
     * Constructor de la GUI.
     * <ul>
     * <li>Inicializa las capas de lógica (SistemaGestion, LogicaRegistro, etc.).</li>
     * <li>Invoca la carga de datos desde CSV ({@link SistemaGestion#cargarDatos()}).</li>
     * <li>Configura las propiedades principales del JFrame (título, tamaño, etc.).</li>
     * <li>Llama a {@link #construirGUIMain()} para ensamblar la interfaz.</li>
     * </ul>
     * En caso de un error crítico durante la carga de datos, muestra un error
     * y termina la aplicación (System.exit(1)).
     */
    public Gui() {
        // 1. Inicializar las capas
        sistema = new SistemaGestion();
        logicaRegistro = new LogicaRegistro();
        logicaGestion = new LogicaGestion();
        logicaInformes = new LogicaInformes();

        // 2. Cargar datos
        try {
            sistema.cargarDatos();
        } catch (LogicaException e) {
            mostrarError("Error crítico al cargar los datos:\n" + e.getMessage() +
                    "\nLa aplicación se cerrará.");
            System.exit(1);
        }

        // 3. Configurar la Ventana Principal (JFrame)
        setTitle("Sistema de Gestión F1 - Escuderías Unidas");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO_PRINCIPAL);
        setLayout(new BorderLayout(10, 10));

        // 4. Construir la GUI principal
        construirGUIMain();
    }

    /**
     * Construye los componentes principales de la GUI (Header, Navegación, Contenido).
     * Utiliza un {@link BorderLayout} para organizar los paneles:
     * <ul>
     * <li>NORTE: {@link #crearPanelHeader()}</li>
     * <li>OESTE: {@link #crearPanelNavegacion()}</li>
     * <li>CENTRO: Panel principal con CardLayout (conteniendo Bienvenida, Registro, etc.)</li>
     * </ul>
     */
    private void construirGUIMain() {
        // --- PANEL HEADER (NORTE) Fondo de "Escuderías Unidas" ---
        add(crearPanelHeader(), BorderLayout.NORTH);

        // --- PANEL NAVEGACIÓN (OESTE) Fondo de "Registrar, Gestionar, Informes" ---
        add(crearPanelNavegacion(), BorderLayout.WEST);

        // --- PANEL PRINCIPAL (CENTRO) ---
        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.setBackground(COLOR_FONDO_PRINCIPAL);
        panelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Añadir las "pantallas" o "cartas" al panel principal
        panelPrincipal.add(crearPanelBienvenida(), "BIENVENIDA");
        panelPrincipal.add(crearPanelRegistro(), "REGISTRO");
        panelPrincipal.add(crearPanelGestion(), "GESTION");
        panelPrincipal.add(crearPanelInformes(), "INFORMES");

        add(panelPrincipal, BorderLayout.CENTER);

        // Mostrar la bienvenida primero
        cardLayout.show(panelPrincipal, "BIENVENIDA");
    }

    /**
     * Crea el panel de cabecera (Header) en la posición NORTE.
     * Contiene el título de la aplicación con la fuente personalizada.
     *
     * @return Un {@link JPanel} configurado como cabecera.
     */
    private JPanel crearPanelHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_FONDO_SECUNDARIO);
        panelHeader.setBorder(new EmptyBorder(15, 25, 15, 25));

        // Título de la App (con fuente RACING_SANS_ONE)
        JLabel lblTitulo = new JLabel("Escuderías Unidas");
        lblTitulo.setFont(FONT_TITULO_APP);
        lblTitulo.setForeground(COLOR_TEXTO_PRINCIPAL);
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
        panelHeader.add(lblTitulo, BorderLayout.CENTER);

        return panelHeader;
    }

    /**
     * Crea el panel de navegación lateral en la posición OESTE.
     * Contiene los botones ({@link BotonNavegacionRedondeado}) para cambiar
     * entre las "cartas" (paneles) del {@link CardLayout}.
     *
     * @return Un {@link JPanel} configurado como barra de navegación.
     */
    private JPanel crearPanelNavegacion() {
        JPanel panelNav = new JPanel();
        panelNav.setLayout(new BoxLayout(panelNav, BoxLayout.Y_AXIS));
        panelNav.setBackground(COLOR_FONDO_SECUNDARIO);
        panelNav.setBorder(new EmptyBorder(20, 10, 20, 10));
        panelNav.setPreferredSize(new Dimension(240, 0));

        // Título de Navegación
        JButton btnMenuPrincipal = new JButton("MENÚ PRINCIPAL");
        btnMenuPrincipal.setFont(FONT_NAV_TITULO); // Fuente F1
        btnMenuPrincipal.setForeground(COLOR_TEXTO_SECUNDARIO);
        btnMenuPrincipal.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMenuPrincipal.setContentAreaFilled(false);
        btnMenuPrincipal.setBorderPainted(false);
        btnMenuPrincipal.setFocusPainted(false);
        btnMenuPrincipal.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Acción para volver a Bienvenida
        btnMenuPrincipal.addActionListener(e -> cardLayout.show(panelPrincipal, "BIENVENIDA"));

        // Efecto Hover para el botón/título (cambio visual en un elemento cuando el curso pasa sobre él)
        btnMenuPrincipal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnMenuPrincipal.setForeground(COLOR_ROJO);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnMenuPrincipal.setForeground(COLOR_TEXTO_SECUNDARIO);
            }
        });

        panelNav.add(btnMenuPrincipal);
        panelNav.add(Box.createRigidArea(new Dimension(0, 25))); // Espaciador

        // Botones de Navegación (Redondeados y con fuente F1)
        panelNav.add(new BotonNavegacionRedondeado("Registrar", "REGISTRO"));
        panelNav.add(Box.createRigidArea(new Dimension(0, 15)));
        panelNav.add(new BotonNavegacionRedondeado("Gestionar", "GESTION"));
        panelNav.add(Box.createRigidArea(new Dimension(0, 15)));
        panelNav.add(new BotonNavegacionRedondeado("Informes", "INFORMES"));

        panelNav.add(Box.createVerticalGlue()); // Empuja todo hacia arriba

        return panelNav;
    }

    /**
     * Crea la pantalla de Bienvenida en el panel principal.
     * Muestra un texto de bienvenida y estadísticas básicas de los datos cargados.
     *
     * @return Un {@link JPanel} configurado como panel de bienvenida.
     */
    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_FONDO_PRINCIPAL);

        String texto = "Bienvenido al Sistema de Gestión de F1 'Escuderías Unidas'.\n\n" +
                "Use el menú de la izquierda para navegar por los módulos:\n\n" +
                " • Registrar: Dar de alta nuevos pilotos, escuderías, circuitos, etc.\n" +
                " • Gestionar: Realizar asociaciones, registrar contratos y resultados de carreras.\n" +
                " • Informes: Visualizar rankings, históricos y estadísticas.\n\n" +
                "Datos cargados exitosamente:\n" +
                " > " + sistema.getPilotos().size() + " pilotos\n" +
                " > " + sistema.getEscuderias().size() + " escuderías\n" +
                " > " + sistema.getCarreras().size() + " carreras\n";

        JTextArea bienvenida = new JTextArea(texto);
        bienvenida.setEditable(false);
        bienvenida.setFont(FONT_BIENVENIDA); // Fuente RACING_SANS_ONE
        bienvenida.setForeground(COLOR_TEXTO_SECUNDARIO);
        bienvenida.setBackground(COLOR_FONDO_PRINCIPAL);
        bienvenida.setMargin(new Insets(20, 20, 20, 20));
        bienvenida.setLineWrap(true);
        bienvenida.setWrapStyleWord(true);

        panel.add(bienvenida, BorderLayout.CENTER);
        return panel;
    }

    // -----------------------------------------------------------------
    // --- Paneles de Módulo (Registro, Gestión, Informes) ---
    // --- (MODIFICADOS PARA APARECER EN 1 COLUMNA) ---
    // -----------------------------------------------------------------

    /**
     * Método de fábrica para crear los paneles de acción (Registro, Gestión, Informes).
     * Crea un {@link JScrollPane} que contiene un panel con un {@link GridLayout} de 1 columna.
     * Esto permite que todos los botones de acción estén en una sola columna vertical
     * y que el panel sea desplazable si el contenido excede la altura de la ventana.
     *
     * @param titulo  El título para el borde del panel (ej. "Módulo de Registros").
     * @param botones La lista de {@link BotonAccionRedondeado} a agregar al panel.
     * @return Un {@link JScrollPane} configurado que contiene los botones.
     */
    private JScrollPane crearPanelDeAccionUnicaColumna(String titulo, List<JButton> botones) {
        // Panel principal que envuelve todo, con BorderLayout
        JPanel panelWrapper = new JPanel(new BorderLayout());
        panelWrapper.setBackground(COLOR_FONDO_PRINCIPAL);

        // Panel que contendrá los botones, con GridLayout de 1 columna
        JPanel panelGrid = new JPanel(new GridLayout(0, 1, 15, 15)); // 0 filas, 1 col, 15px gap
        panelGrid.setBackground(COLOR_FONDO_PRINCIPAL);

        // Borde con título estilizado
        Border bordeTitulo = BorderFactory.createTitledBorder(
                new EmptyBorder(20, 10, 10, 10),
                titulo,
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                FONT_TITULO_PANEL_ACCION,
                COLOR_TEXTO_PRINCIPAL
        );
        panelGrid.setBorder(bordeTitulo);

        // Agregar todos los botones al panel grid
        for (JButton boton : botones) {
            panelGrid.add(boton);
        }

        // Añadir el panelGrid al NORTE del wrapper
        // Esto evita que los botones se estiren verticalmente
        panelWrapper.add(panelGrid, BorderLayout.NORTH);

        // Crear el JScrollPane y añadir el wrapper
        JScrollPane scrollPane = new JScrollPane(panelWrapper);
        scrollPane.setBackground(COLOR_FONDO_PRINCIPAL);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setBackground(COLOR_FONDO_SECUNDARIO);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    /**
     * Crea la pantalla (Card) para el módulo de Registros.
     * Utiliza {@link #crearPanelDeAccionUnicaColumna} para generar el layout.
     *
     * @return Un {@link JScrollPane} con los botones de registro.
     */
    private JScrollPane crearPanelRegistro() {
        List<JButton> botones = new ArrayList<>();
        botones.add(new BotonAccionRedondeado("Registrar Piloto", e -> testRegistrarPiloto()));
        botones.add(new BotonAccionRedondeado("Registrar Mecánico", e -> testRegistrarMecanico()));
        botones.add(new BotonAccionRedondeado("Registrar Auto", e -> testRegistrarAuto()));
        botones.add(new BotonAccionRedondeado("Registrar Escudería", e -> testRegistrarEscuderia()));
        botones.add(new BotonAccionRedondeado("Registrar País", e -> registrarPais()));
        botones.add(new BotonAccionRedondeado("Registrar Circuito", e -> testRegistrarCircuito()));
        botones.add(new BotonAccionRedondeado("Registrar Carrera", e -> testRegistrarCarrera()));



        return crearPanelDeAccionUnicaColumna("Módulo de Registros", botones);
    }

    /**
     * Crea la pantalla (Card) para el módulo de Gestión.
     * Utiliza {@link #crearPanelDeAccionUnicaColumna} para generar el layout.
     *
     * @return Un {@link JScrollPane} con los botones de gestión.
     */
    private JScrollPane crearPanelGestion() {
        List<JButton> botones = new ArrayList<>();
        botones.add(new BotonAccionRedondeado("Asociar Piloto a Escudería (Contrato)", e -> testAsociarPilotoEscuderia()));
        botones.add(new BotonAccionRedondeado("Desvincular Piloto (Fin Contrato)", e -> testDesvincularPilotoEscuderia()));
        botones.add(new BotonAccionRedondeado("Asociar Auto a Escudería", e -> testAsociarAutoAEscuderia()));
        botones.add(new BotonAccionRedondeado("Asociar Mecánico a Escudería", e -> testAsociarMecanicoAEscuderia()));
        botones.add(new BotonAccionRedondeado("Asociar Piloto a Auto en Carrera", e -> testAsociarPilotoAutoCarrera()));
        botones.add(new BotonAccionRedondeado("Registrar Resultado de Carrera", e -> testRegistrarResultado()));
        botones.add(new BotonAccionRedondeado("Asignar Pole Position", e -> testAsignarPolePosition()));


        return crearPanelDeAccionUnicaColumna("Módulo de Gestión", botones);
    }

    /**
     * Crea la pantalla (Card) para el módulo de Informes.
     * Utiliza {@link #crearPanelDeAccionUnicaColumna} para generar el layout.
     *
     * @return Un {@link JScrollPane} con los botones de informes.
     */
    private JScrollPane crearPanelInformes() {
        List<JButton> botones = new ArrayList<>();
        botones.add(new BotonAccionRedondeado("Ranking de Pilotos", e -> testRankingPilotos()));
        botones.add(new BotonAccionRedondeado("Resultados de Carreras por Fechas", e -> testReporteResultadosPorFecha()));
        botones.add(new BotonAccionRedondeado("Histórico de un Piloto", e -> testHistoricoPiloto()));
        botones.add(new BotonAccionRedondeado("Histórico de todos los Pilotos", e -> testHistoricoTodosLosPilotos()));
        botones.add(new BotonAccionRedondeado("Reporte de Autos por Escudería", e -> testReporteAutosPorEscuderia()));
        botones.add(new BotonAccionRedondeado("Reporte de Mecánicos por Escudería", e -> testReporteMecanicosPorEscuderia()));
        botones.add(new BotonAccionRedondeado("Contador de veces que un Piloto corrió en un Circuito", e -> testContadorPilotoEnCircuito()));
        botones.add(new BotonAccionRedondeado("Contador de Carreras por Circuito", e -> testContadorCarrerasEnCircuito()));

        return crearPanelDeAccionUnicaColumna("Módulo de Informes", botones);
    }

    // -----------------------------------------------------------------
    // --- NUEVAS CLASES INTERNAS: Botones Personalizados ---
    // -----------------------------------------------------------------

    /**
     * Clase interna que define un botón de navegación personalizado.
     * Usado en el panel de navegación (izquierda).
     * <ul>
     * <li>Es redondeado ({@code fillRoundRect}).</li>
     * <li>Usa la fuente F1 ({@code FONT_NAV_BOTONES}).</li>
     * <li>Cambia a color {@code COLOR_ROJO} durante el hover.</li>
     * </ul>
     */
    private class BotonNavegacionRedondeado extends JButton {

        /**
         * Crea un nuevo botón de navegación.
         *
         * @param texto    El texto que se mostrará en el botón.
         * @param cardName El nombre de la "carta" (panel) que debe mostrar
         * el {@link CardLayout} al hacer clic.
         */
        public BotonNavegacionRedondeado(String texto, String cardName) {
            super(texto);
            setFont(FONT_NAV_BOTONES); // Fuente F1
            setForeground(COLOR_TEXTO_PRINCIPAL);
            setBackground(COLOR_FONDO_SECUNDARIO);

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setBorder(new EmptyBorder(12, 25, 12, 25));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, getMinimumSize().height));
            setAlignmentX(Component.CENTER_ALIGNMENT);

            addActionListener(e -> cardLayout.show(panelPrincipal, cardName));
        }

        /**
         * Sobrescribe el método de pintado para dibujar el fondo redondeado
         * y gestionar los efectos de hover y click.
         *
         * @param g El contexto gráfico.
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            ButtonModel model = getModel();
            if (model.isPressed()) {
                g2.setColor(COLOR_ROJO.darker());
            } else if (model.isRollover()) {
                g2.setColor(COLOR_ROJO); // Hover es Rojo
            } else {
                g2.setColor(getBackground()); // Normal es Gris
            }
            // Dibuja el rectángulo redondeado (30x30)
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Clase interna que define un botón de acción personalizado.
     * Usado en los paneles centrales de módulos (Registro, Gestión, Informes).
     * <ul>
     * <li>Es redondeado ({@code fillRoundRect}).</li>
     * <li>Usa la fuente SANS_SERIF ({@code FONT_BOTON_ACCION}).</li>
     * <li>Es de color {@code COLOR_ROJO_OSCURO}, se aclara en hover.</li>
     * </ul>
     */
    private class BotonAccionRedondeado extends JButton {

        /**
         * Crea un nuevo botón de acción.
         *
         * @param titulo   El texto que se mostrará en el botón.
         * @param listener El {@link ActionListener} que se ejecutará al hacer clic.
         */
        public BotonAccionRedondeado(String titulo, ActionListener listener) {
            super(titulo);
            setFont(FONT_BOTON_ACCION); // Fuente SANS_SERIF
            setForeground(COLOR_TEXTO_PRINCIPAL);
            setBackground(COLOR_ROJO_OSCURO);

            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setBorder(new EmptyBorder(15, 20, 15, 20)); // Padding
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Establecer un tamaño preferido para que no se estiren
            setPreferredSize(new Dimension(350, 60));

            addActionListener(listener);
        }

        /**
         * Sobrescribe el método de pintado para dibujar el fondo redondeado
         * y gestionar los efectos de hover y click.
         *
         * @param g El contexto gráfico.
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            ButtonModel model = getModel();
            if (model.isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (model.isRollover()) {
                g2.setColor(getBackground().brighter()); // Hover se aclara
            } else {
                g2.setColor(getBackground()); // Normal es Rojo
            }
            // Dibuja el rectángulo redondeado (20x20)
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // -----------------------------------------------------------------
    // --- MÉTODOS DE REGISTRO (LogicaRegistro) ---
    // -----------------------------------------------------------------

    /**
     * Manejador de GUI para registrar un nuevo País.
     * Pide los datos al usuario y llama a {@link LogicaRegistro#registrarPais(SistemaGestion, int, String)}.
     */
    private void registrarPais() {
        try {
            String idInput = getDesdeUsuario("Ingrese el ID del País:");
            if (idInput == null) return;
            int idPais = Integer.parseInt(idInput);
            String descripcion = getDesdeUsuario("Ingrese el nombre del País:");
            if (descripcion == null) return;
            logicaRegistro.registrarPais(sistema, idPais, descripcion);
            mostrarInfo("País registrado con éxito!");
        } catch (NumberFormatException e) {
            mostrarError("Error de Entrada: El ID debe ser un número entero válido.");
        } catch (LogicaException e) {
            mostrarError("Error al registrar país:\n" + e.getMessage());
        }
    }

    /**
     * Manejador de GUI para registrar una nueva Escudería.
     * Pide los datos y llama a {@link LogicaRegistro#registrarEscuderia(SistemaGestion, String, Pais)}.
     */
    private void testRegistrarEscuderia() {
        try {
            Pais p = seleccionarPais();
            if (p == null) return;
            String nombre = getDesdeUsuario("Ingrese el nombre de la nueva escudería:");
            if (nombre == null) return;
            logicaRegistro.registrarEscuderia(sistema, nombre, p);
            mostrarInfo("¡Escudería '" + nombre + "' registrada con éxito!");
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para registrar un nuevo Circuito.
     * Pide los datos y llama a {@link LogicaRegistro#registrarCircuito(SistemaGestion, String, int, Pais)}.
     */
    private void testRegistrarCircuito() {
        try {
            Pais p = seleccionarPais();
            if (p == null) return;
            String nombre = getDesdeUsuario("Nombre del nuevo circuito:");
            int longitud = Integer.parseInt(getDesdeUsuario("Longitud (en metros, ej: 5000):"));
            logicaRegistro.registrarCircuito(sistema, nombre, longitud, p);
            mostrarInfo("¡Circuito '" + nombre + "' registrado con éxito!");
        } catch (LogicaException | NumberFormatException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para registrar una nueva Carrera.
     * Pide los datos y llama a {@link LogicaRegistro#registrarCarrera(SistemaGestion, String, int, String, Pais, Circuito)}.
     */
    private void testRegistrarCarrera() {
        try {
            Circuito c = seleccionarCircuito();
            if (c == null) return;
            String fecha = getDesdeUsuario("Fecha de la carrera (dd-MM-yyyy):");
            int vueltas = Integer.parseInt(getDesdeUsuario("Número de vueltas:"));
            String hora = getDesdeUsuario("Hora de la carrera (ej: 15:00):");
            logicaRegistro.registrarCarrera(sistema, fecha, vueltas, hora, c.getPais(), c);
            mostrarInfo("¡Carrera registrada en " + c.getNombre() + " para el " + fecha + "!");
        } catch (LogicaException | NumberFormatException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para registrar un nuevo Piloto.
     * Pide los datos y llama a {@link LogicaRegistro#registrarPiloto(SistemaGestion, String, String, String, Pais, int, int, int, int, int)}.
     */
    private void testRegistrarPiloto() {
        try {
            String dni = getDesdeUsuario("DNI del piloto:");
            String nom = getDesdeUsuario("Nombre:");
            String ape = getDesdeUsuario("Apellido:");
            Pais p = seleccionarPais();
            if (p == null) return;
            int nro = Integer.parseInt(getDesdeUsuario("Nro Competencia:"));
            logicaRegistro.registrarPiloto(sistema, dni, nom, ape, p, nro, 0, 0, 0, 0);
            mostrarInfo("¡Piloto '" + nom + " " + ape + "' registrado con éxito!");
        } catch (LogicaException | NumberFormatException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para registrar un nuevo Mecánico.
     * Pide los datos y llama a {@link LogicaRegistro#registrarMecanico(SistemaGestion, String, String, String, Pais, Especialidad, int)}.
     */
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
            logicaRegistro.registrarMecanico(sistema, dni, nom, ape, p, esp, anios);
            mostrarInfo("¡Mecánico '" + nom + " " + ape + "' registrado con éxito!");
        } catch (LogicaException | NumberFormatException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para registrar un nuevo Auto.
     * Pide los datos y llama a {@link LogicaRegistro#registrarAuto(SistemaGestion, String, String)}.
     */
    private void testRegistrarAuto() {
        try {
            String modelo = getDesdeUsuario("Modelo del auto (ej: W16):");
            String motor = getDesdeUsuario("Motor del auto (ej: Mercedes-AMG F1 M16):");
            logicaRegistro.registrarAuto(sistema, modelo, motor);
            mostrarInfo("¡Auto '" + modelo + "' registrado con éxito!");
            mostrarInfo("Recuerde asociar este auto a una escudería desde el menú 'Gestionar'.");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    // -----------------------------------------------------------------
    // --- MÉTODOS DE GESTIÓN (LogicaGestion) ---
    // -----------------------------------------------------------------

    /**
     * Manejador de GUI para asociar un Piloto y un Auto a una Carrera.
     * Llama a {@link LogicaGestion#asociarPilotoAutoACarrera(Carrera, Piloto, Auto, String)}.
     */
    private void testAsociarPilotoAutoCarrera() {
        try {
            Piloto p = seleccionarPiloto();
            Auto a = seleccionarAuto();
            Carrera c = seleccionarCarrera();
            if (p == null || a == null || c == null) return;
            logicaGestion.asociarPilotoAutoACarrera(c, p, a, c.getFechaRealizacion());
            mostrarInfo("¡ASIGNACIÓN EXITOSA!\nPiloto: " + p.getNombre() + "\nAuto: " + a.getModelo() + "\nCarrera: " + c.getCircuito().getNombre());
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para registrar el resultado de un Piloto en una Carrera.
     * Llama a {@link LogicaGestion#registrarResultado(SistemaGestion, Carrera, Piloto, int, boolean)}.
     */
    private void testRegistrarResultado() {
        try {
            Carrera c = seleccionarCarrera();
            if (c == null) return;
            Piloto p = seleccionarParticipante(c);
            if (p == null) return;
            int pos = Integer.parseInt(getDesdeUsuario("Posición final para " + p.getNombre() + ":"));
            boolean vr = getConfirmacion("¿Obtuvo la vuelta rápida?");
            logicaGestion.registrarResultado(sistema, c, p, pos, vr);
            mostrarInfo("¡Resultado registrado!\nPiloto: " + p.getNombre() + "\nPosición: " + pos + "\n(Revise el ranking)");
        } catch (LogicaException | NumberFormatException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para asociar un Piloto a una Escudería (crear contrato).
     * Llama a {@link LogicaGestion#asociarPilotoAEscuderia(Piloto, Escuderia, String)}.
     */
    private void testAsociarPilotoEscuderia() {
        try {
            Piloto p = seleccionarPiloto();
            Escuderia e = seleccionarEscuderia();
            String fecha = getDesdeUsuario("Fecha de inicio de contrato (dd-MM-yyyy):");
            if (p == null || e == null || fecha == null) return;
            logicaGestion.asociarPilotoAEscuderia(p, e, fecha);
            mostrarInfo("¡CONTRATO EXITOSO!\n" + p.getNombre() + " -> " + e.getNombre() + " (desde " + fecha + ")");
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para desvincular un Piloto de una Escudería (finalizar contrato).
     * Llama a {@link LogicaGestion#desvincularPilotoDeEscuderia(Piloto, Escuderia, String)}.
     */
    private void testDesvincularPilotoEscuderia() {
        try {
            Piloto p = seleccionarPiloto();
            if (p == null) return;
            Escuderia e = seleccionarContratoActivo(p);
            if (e == null) return;
            String fecha = getDesdeUsuario("Fecha de FIN de contrato (dd-MM-yyyy):");
            if (fecha == null) return;
            logicaGestion.desvincularPilotoDeEscuderia(p, e, fecha);
            mostrarInfo("¡CONTRATO FINALIZADO!\n" + p.getNombre() + " y " + e.getNombre() + " (hasta " + fecha + ")");
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para asociar un Auto a una Escudería.
     * Llama a {@link LogicaGestion#asociarAutoAEscuderia(Auto, Escuderia)}.
     */
    private void testAsociarAutoAEscuderia() {
        try {
            Auto a = seleccionarAuto();
            Escuderia e = seleccionarEscuderia();
            if (a == null || e == null) return;
            logicaGestion.asociarAutoAEscuderia(a, e);
            mostrarInfo("¡Auto " + a.getModelo() + " ahora pertenece a " + e.getNombre() + "!");
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para asociar un Mecánico a una Escudería.
     * Llama a {@link LogicaGestion#asociarMecanicoAEscuderia(Mecanico, Escuderia)}.
     */
    private void testAsociarMecanicoAEscuderia() {
        try {
            Mecanico m = seleccionarMecanico();
            Escuderia e = seleccionarEscuderia();
            if (m == null || e == null) return;
            logicaGestion.asociarMecanicoAEscuderia(m, e);
            mostrarInfo("¡Mecánico " + m.getNombre() + " ahora trabaja para " + e.getNombre() + "!");
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para asignar una Pole Position a un Piloto.
     * Llama a {@link LogicaGestion#asignarPolePosition(Piloto)}.
     */
    private void testAsignarPolePosition() {
        try {
            Piloto p = seleccionarPiloto();
            if (p == null) return;
            int polesAntes = p.getPolePosition();
            logicaGestion.asignarPolePosition(p);
            mostrarInfo("¡Pole position asignada a " + p.getNombre() + " " + p.getApellido() + "!\n" +
                    "Total anterior: " + polesAntes + "\n" +
                    "Total nuevo: " + p.getPolePosition());
        } catch (LogicaException | NullPointerException ex) {
            mostrarError(ex.getMessage());
        }
    }

    // -----------------------------------------------------------------
    // --- MÉTODOS DE INFORMES (LogicaInformes) ---
    // -----------------------------------------------------------------

    /**
     * Manejador de GUI para mostrar el Ranking de Pilotos.
     * Llama a {@link LogicaInformes#getRankingPilotos(SistemaGestion, LogicaGestion)}.
     */
    private void testRankingPilotos() {
        try {
            List<PilotoPuntaje> ranking = logicaInformes.getRankingPilotos(sistema, logicaGestion);
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

    /**
     * Manejador de GUI para mostrar los resultados de carreras en un rango de fechas.
     * Llama a {@link LogicaInformes#getReporteResultadosPorFechas(SistemaGestion, String, String)}.
     */
    private void testReporteResultadosPorFecha() {
        try {
            String fDesde = getDesdeUsuario("Fecha desde (formato dd-MM-yyyy):");
            if (fDesde == null) return;
            String fHasta = getDesdeUsuario("Fecha hasta (formato dd-MM-yyyy):");
            if (fHasta == null) return;
            List<ResultadoCarrera> resultados = logicaInformes.getReporteResultadosPorFechas(sistema, fDesde, fHasta);
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

    /**
     * Manejador de GUI para mostrar el histórico de estadísticas de un Piloto.
     * Llama a {@link LogicaInformes#getEstadisticasPiloto(SistemaGestion, String)}.
     */
    private void testHistoricoPiloto() {
        try {
            Piloto p = seleccionarPiloto();
            if (p == null) return;
            Piloto pStats = logicaInformes.getEstadisticasPiloto(sistema, p.getDni());
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

    /**
     * Manejador de GUI para mostrar el histórico de estadísticas de todos los Pilotos.
     * Llama a {@link LogicaInformes#getEstadisticasTodosLosPilotos(SistemaGestion)}.
     */
    private void testHistoricoTodosLosPilotos() {
        try {
            List<Piloto> pilotos = logicaInformes.getEstadisticasTodosLosPilotos(sistema);
            StringBuilder sb = new StringBuilder("--- Histórico de Todos los Pilotos ---\n\n");
            for (Piloto p : pilotos) {
                sb.append(String.format("%-25s (Victorias:%s, Podios:%s, PolePosition:%s, VueltasRapidas:%s)\n",
                        p.getNombre() + " " + p.getApellido(),
                        p.getVictorias(), p.getPodios(), p.getPolePosition(), p.getVueltasRapidas()));
            }
            mostrarReporte(sb.toString(), "Histórico de Pilotos");
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para mostrar el reporte de Autos usados por Escudería en Carreras.
     * Llama a {@link LogicaInformes#getReporteAutosPorEscuderia(SistemaGestion)}.
     */
    private void testReporteAutosPorEscuderia() {
        try {
            List<AutoPiloto> informe = logicaInformes.getReporteAutosPorEscuderia(sistema);
            StringBuilder sb = new StringBuilder("--- Reporte Autos por Escudería y Carrera ---\n");
            String escuderiaActual = "";
            for (AutoPiloto ap : informe) {
                String nombreEscuderia = (ap.getAuto().getEscuderia() != null) ? ap.getAuto().getEscuderia().getNombre() : "SIN ESCUDERIA";
                if (!nombreEscuderia.equals(escuderiaActual)) {
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

    /**
     * Manejador de GUI para mostrar el reporte de Mecánicos por Escudería.
     * Llama a {@link LogicaInformes#getReporteMecanicosPorEscuderia(SistemaGestion)}.
     */
    private void testReporteMecanicosPorEscuderia() {
        try {
            List<Escuderia> escuderias = logicaInformes.getReporteMecanicosPorEscuderia(sistema);
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

    /**
     * Manejador de GUI para contar cuántas veces un Piloto corrió en un Circuito.
     * Llama a {@link LogicaInformes#getContadorPilotoEnCircuito(SistemaGestion, Piloto, Circuito)}.
     */
    private void testContadorPilotoEnCircuito() {
        try {
            Piloto p = seleccionarPiloto();
            Circuito c = seleccionarCircuito();
            if (p == null || c == null) return;
            int cont = logicaInformes.getContadorPilotoEnCircuito(sistema, p, c);
            String veces = (cont == 1) ? "vez" : "veces";
            mostrarInfo("El piloto " + p.getNombre() + " corrió " + cont + " " + veces + " en " + c.getNombre());
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Manejador de GUI para contar cuántas carreras se corrieron en un Circuito.
     * Llama a {@link LogicaInformes#getContadorCarrerasEnCircuito(SistemaGestion, Circuito)}.
     */
    private void testContadorCarrerasEnCircuito() {
        try {
            Circuito c = seleccionarCircuito();
            if (c == null) return;
            int cont = logicaInformes.getContadorCarrerasEnCircuito(sistema, c);
            mostrarInfo("Se corrieron " + cont + " carreras en " + c.getNombre());
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    // -----------------------------------------------------------------
    // --- UTILIDADES GUI (JOptionPanes) ---
    // -----------------------------------------------------------------

    /**
     * Bloque estático para configurar el "Look and Feel" de todos los
     * {@link JOptionPane} para que coincidan con el tema oscuro de la aplicación.
     * Se ejecuta una sola vez al cargar la clase.
     */
    static {
        // Ajuste de fuentes para los JOptionPane (Usando SANS_SERIF)
        Font optionPaneFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
        Font optionPaneButtonFont = new Font(Font.SANS_SERIF, Font.BOLD, 12);

        UIManager.put("Panel.background", COLOR_FONDO_SECUNDARIO);
        UIManager.put("OptionPane.background", COLOR_FONDO_SECUNDARIO);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXTO_PRINCIPAL);
        UIManager.put("OptionPane.messageFont", optionPaneFont);
        UIManager.put("Label.foreground", COLOR_TEXTO_PRINCIPAL);
        UIManager.put("Label.font", optionPaneFont);
        UIManager.put("Button.background", COLOR_ROJO);
        UIManager.put("Button.foreground", COLOR_TEXTO_PRINCIPAL);
        UIManager.put("Button.font", optionPaneButtonFont);
        UIManager.put("TextField.background", COLOR_TEXTO_PRINCIPAL);
        UIManager.put("TextField.foreground", COLOR_FONDO_PRINCIPAL);
        UIManager.put("TextField.caretForeground", COLOR_FONDO_PRINCIPAL);
        UIManager.put("TextField.font", optionPaneFont);
        UIManager.put("ComboBox.font", optionPaneFont);
        UIManager.put("ComboBox.background", COLOR_TEXTO_PRINCIPAL);
        UIManager.put("ComboBox.foreground", COLOR_FONDO_PRINCIPAL);
    }

    /**
     * Muestra un diálogo de error estilizado.
     * Acepta saltos de línea (\n) y los convierte a HTML (<br>) para
     * un formato correcto en JOptionPane.
     *
     * @param mensaje El mensaje de error a mostrar.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, "<html>" + mensaje.replaceAll("\n", "<br>") + "</html>",
                "Error de Lógica", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un diálogo de información estilizado.
     * Acepta saltos de línea (\n) y los convierte a HTML (<br>).
     *
     * @param mensaje El mensaje de información a mostrar.
     */
    private void mostrarInfo(String mensaje) {
        JOptionPane.showMessageDialog(this, "<html>" + mensaje.replaceAll("\n", "<br>") + "</html>",
                "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un diálogo de entrada (input) estilizado.
     *
     * @param mensaje La pregunta o instrucción para el usuario (ej. "Ingrese nombre:").
     * @return El String ingresado por el usuario, o {@code null} si cancela.
     */
    private String getDesdeUsuario(String mensaje) {
        return (String) JOptionPane.showInputDialog(this, mensaje, "Entrada Requerida",
                JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Muestra un diálogo de confirmación (Sí/No) estilizado.
     *
     * @param mensaje La pregunta de confirmación (ej. "¿Está seguro?").
     * @return {@code true} si el usuario presiona "Sí", {@code false} en caso contrario.
     */
    private boolean getConfirmacion(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /**
     * Muestra un diálogo grande con un {@link JScrollPane} para reportes extensos.
     * Utiliza una fuente monoespaciada para alinear correctamente los reportes
     * formateados como texto plano.
     *
     * @param textoReporte El contenido completo del reporte (puede tener \n).
     * @param titulo       El título de la ventana del reporte.
     */
    private void mostrarReporte(String textoReporte, String titulo) {
        JTextArea textArea = new JTextArea(25, 70);
        textArea.setText(textoReporte);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        textArea.setBackground(COLOR_FONDO_SECUNDARIO);
        textArea.setForeground(COLOR_TEXTO_PRINCIPAL);
        textArea.setCaretColor(COLOR_TEXTO_PRINCIPAL);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(new LineBorder(COLOR_ROJO));
        scrollPane.setPreferredSize(new Dimension(800, 500));

        JOptionPane.showMessageDialog(this, scrollPane, titulo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    // -----------------------------------------------------------------
    // --- UTILIDADES DE SELECCIÓN (JComboBoxes en JOptionPanes) ---
    // -----------------------------------------------------------------

    /**
     * Muestra un diálogo para que el usuario seleccione un {@link Pais} de una lista.
     *
     * @return El {@link Pais} seleccionado, o {@code null} si cancela o no hay países.
     */
    private Pais seleccionarPais() {
        if (sistema.getPaises().isEmpty()) {
            mostrarError("No hay países registrados. Por favor, registre un país primero.");
            return null;
        }
        return (Pais) JOptionPane.showInputDialog(this, "Seleccione un País:", "Seleccionar País",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getPaises().toArray(), sistema.getPaises().get(0));
    }

    /**
     * Muestra un diálogo para que el usuario seleccione un {@link Piloto} de una lista.
     *
     * @return El {@link Piloto} seleccionado, o {@code null} si cancela o no hay pilotos.
     */
    private Piloto seleccionarPiloto() {
        if (sistema.getPilotos().isEmpty()) {
            mostrarError("No hay pilotos registrados. Por favor, registre un piloto primero.");
            return null;
        }
        return (Piloto) JOptionPane.showInputDialog(this, "Seleccione un Piloto:", "Seleccionar Piloto",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getPilotos().toArray(), sistema.getPilotos().get(0));
    }

    /**
     * Muestra un diálogo para que el usuario seleccione un {@link Piloto}
     * que haya participado en una {@link Carrera} específica.
     *
     * @param c La {@link Carrera} de la cual obtener los participantes.
     * @return El {@link Piloto} seleccionado, o {@code null} si cancela o no hay participantes.
     */
    private Piloto seleccionarParticipante(Carrera c) {
        if (c.getParticipantes().isEmpty()) {
            mostrarError("Esta carrera no tiene pilotos participantes registrados.");
            return null;
        }
        List<Piloto> participantes = new ArrayList<>();
        for (AutoPiloto ap : c.getParticipantes()) {
            participantes.add(ap.getPiloto());
        }
        return (Piloto) JOptionPane.showInputDialog(this, "Seleccione un Piloto (que haya participado):", "Seleccionar Participante",
                JOptionPane.QUESTION_MESSAGE, null,
                participantes.toArray(), participantes.get(0));
    }

    /**
     * Muestra un diálogo para que el usuario seleccione un {@link Auto} de una lista.
     *
     * @return El {@link Auto} seleccionado, o {@code null} si cancela o no hay autos.
     */
    private Auto seleccionarAuto() {
        if (sistema.getAutos().isEmpty()) {
            mostrarError("No hay autos registrados. Por favor, registre un auto primero.");
            return null;
        }
        return (Auto) JOptionPane.showInputDialog(this, "Seleccione un Auto:", "Seleccionar Auto",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getAutos().toArray(), sistema.getAutos().get(0));
    }

    /**
     * Muestra un diálogo para que el usuario seleccione una {@link Carrera} de una lista.
     *
     * @return La {@link Carrera} seleccionada, o {@code null} si cancela o no hay carreras.
     */
    private Carrera seleccionarCarrera() {
        if (sistema.getCarreras().isEmpty()) {
            mostrarError("No hay carreras registradas. Por favor, registre una carrera primero.");
            return null;
        }
        return (Carrera) JOptionPane.showInputDialog(this, "Seleccione una Carrera (por fecha y circuito):", "Seleccionar Carrera",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getCarreras().toArray(), sistema.getCarreras().get(0));
    }

    /**
     * Muestra un diálogo para que el usuario seleccione una {@link Escuderia} de una lista.
     *
     * @return La {@link Escuderia} seleccionada, o {@code null} si cancela o no hay escuderías.
     */
    private Escuderia seleccionarEscuderia() {
        if (sistema.getEscuderias().isEmpty()) {
            mostrarError("No hay escuderías registradas. Por favor, registre una escudería primero.");
            return null;
        }
        return (Escuderia) JOptionPane.showInputDialog(this, "Seleccione una Escudería:", "Seleccionar Escudería",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getEscuderias().toArray(), sistema.getEscuderias().get(0));
    }

    /**
     * Muestra un diálogo para que el usuario seleccione un {@link Circuito} de una lista.
     *
     * @return El {@link Circuito} seleccionado, o {@code null} si cancela o no hay circuitos.
     */
    private Circuito seleccionarCircuito() {
        if (sistema.getCircuitos().isEmpty()) {
            mostrarError("No hay circuitos registrados. Por favor, registre un circuito primero.");
            return null;
        }
        return (Circuito) JOptionPane.showInputDialog(this, "Seleccione un Circuito:", "Seleccionar Circuito",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getCircuitos().toArray(), sistema.getCircuitos().get(0));
    }

    /**
     * Muestra un diálogo para que el usuario seleccione un {@link Mecanico} de una lista.
     *
     * @return El {@link Mecanico} seleccionado, o {@code null} si cancela o no hay mecánicos.
     */
    private Mecanico seleccionarMecanico() {
        if (sistema.getMecanicos().isEmpty()) {
            mostrarError("No hay mecánicos registrados. Por favor, registre un mecánico primero.");
            return null;
        }
        return (Mecanico) JOptionPane.showInputDialog(this, "Seleccione un Mecánico:", "Seleccionar Mecánico",
                JOptionPane.QUESTION_MESSAGE, null,
                sistema.getMecanicos().toArray(), sistema.getMecanicos().get(0));
    }

    /**
     * Muestra un diálogo para que el usuario seleccione una {@link Especialidad} (Enum).
     *
     * @return La {@link Especialidad} seleccionada, o {@code null} si cancela.
     */
    private Especialidad seleccionarEspecialidad() {
        return (Especialidad) JOptionPane.showInputDialog(this, "Seleccione una Especialidad:", "Seleccionar Especialidad",
                JOptionPane.QUESTION_MESSAGE, null,
                Especialidad.values(), Especialidad.values()[0]);
    }

    /**
     * Muestra un diálogo para que el usuario seleccione una {@link Escuderia}
     * con la que un {@link Piloto} tiene un contrato activo.
     *
     * @param p El {@link Piloto} del cual se buscarán los contratos activos.
     * @return La {@link Escuderia} seleccionada, o {@code null} si cancela.
     * @throws LogicaException Si el piloto no tiene ningún contrato activo para finalizar.
     */
    private Escuderia seleccionarContratoActivo(Piloto p) throws LogicaException {
        List<Escuderia> activas = new ArrayList<>();
        for (PilotoEscuderia pe : p.getPilotosEscuderias()) {
            if (pe.getHastaFecha() == null || pe.getHastaFecha().trim().isEmpty()) {
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