import gui.Gui;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada principal de la aplicación "Escuderías Unidas".
 * <p>
 * Su única responsabilidad es instanciar e iniciar la Interfaz Gráfica de
 * Usuario (GUI) de forma segura en el hilo de despacho de eventos de Swing.
 *
 * @see gui.Gui
 */
public class Main {

    /**
     * Método principal que inicia la aplicación.
     * <p>
     * Utiliza {@link SwingUtilities#invokeLater(Runnable)} para asegurar que la
     * GUI se cree y se muestre en el Hilo de Despacho de Eventos (EDT),
     * lo cual es la práctica estándar para la concurrencia segura en Swing.
     *
     * @param args Argumentos de línea de comandos (actualmente no se utilizan).
     */
    public static void main(String[] args) {
        // Llama a la GUI para que se ejecute de forma segura en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            new Gui().setVisible(true);
        });
    }
}