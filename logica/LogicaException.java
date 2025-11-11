package logica;

/**
 * Excepción personalizada para la capa de Lógica.
 * Se utiliza para comunicar errores de reglas de negocio (ej. DNI duplicado,
 * asignación inválida) a la capa de GUI de una manera controlada.
 */
public class LogicaException extends Exception {

    /**
     * Constructor que acepta un mensaje de error.
     * Este mensaje es el que se mostrará al usuario final en la GUI.
     *
     * @param mensaje El mensaje de error descriptivo.
     */
    public LogicaException(String mensaje) {
        super(mensaje);
    }
}