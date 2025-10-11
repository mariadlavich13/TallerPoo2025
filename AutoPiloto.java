package TallerPoo2025;

import java.util.List;

public class AutoPiloto {
    private String fechaAsignacion;
    private List<Piloto> pilotos; // Relación con Piloto

    public AutoPiloto() {
        this.fechaAsignacion="";
    }

    public AutoPiloto(String fechaAsignacion) {
        this.fechaAsignacion=fechaAsignacion;
    }

    public String getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(String fechaAsignacion) {
        this.fechaAsignacion=fechaAsignacion;
    }
}