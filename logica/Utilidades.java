package logica;

import java.time.format.DateTimeFormatter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Clase de utilidad con métodos estáticos (static)
 * que pueden ser llamados desde cualquier parte del proyecto.
 */
public class Utilidades {
    /**
     * Convierte una fecha de formato "dd-MM-yyyy" a "yyyy-MM-dd".
     * Este formato ("yyyy-MM-dd") es estándar (ISO 8601) y permite
     * comparar fechas alfabéticamente de forma correcta.
     *
     * @param fechaDDMMAAAA La fecha en formato "dd-MM-yyyy".
     * @return La fecha en formato "yyyy-MM-dd", o null si la entrada es nula/vacía.
     */
    public static String formatearFecha(String fechaDDMMAAAA) {
        if (fechaDDMMAAAA == null || fechaDDMMAAAA.trim().isEmpty()) {
            return null;
        }
        try {
            String[] partes = fechaDDMMAAAA.split("-");
            if (partes.length == 3) {
                // Asegura que las partes tengan el padding correcto (ej. 01, 05)
                String dia = partes[0].length() == 1 ? "0" + partes[0] : partes[0];
                String mes = partes[1].length() == 1 ? "0" + partes[1] : partes[1];
                String anio = partes[2];
                return anio + "-" + mes + "-" + dia;
            }
            // Si ya está en formato YYYY-MM-DD, lo devuelve tal cual
            if (fechaDDMMAAAA.length() == 10 && partes[0].length() == 4) {
                return fechaDDMMAAAA;
            }
            
        } catch (Exception e) {
            // Si falla el split o parsing, devuelve la fecha original (o null)
            return fechaDDMMAAAA;
        }
        // Devuelve la fecha original si no pudo procesarla
        return fechaDDMMAAAA;
    }
    public static boolean esFechaValida(String fecha){
        if(fecha == null || fecha.isEmpty()){
            return false;
        }
        try{
            DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
            LocalDate.parse(fecha, fomatter);
            return true;
        } catch(DateTimeParseException e){
            return false;
        }
        }
    }
}