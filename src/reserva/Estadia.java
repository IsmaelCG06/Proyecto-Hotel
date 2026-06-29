package reserva;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Estadía asociada a una reserva, con servicios consumidos.
 */
public class Estadia implements Serializable {

    private static final long serialVersionUID = 1L;

    private Reserva reserva;
    private double totalServicios;
    private LocalDate fechaFin;
    private ArrayList<Servicio> listaServicios;

    public Estadia(Reserva reserva) {
        this.reserva = reserva;
        this.fechaFin = reserva.getFechaFin();
        this.listaServicios = new ArrayList<>();
        this.totalServicios = 0;
    }

    public void agregarServicio(Servicio s) {
        listaServicios.add(s);
        totalServicios += s.getCosto();
    }

    public double calcularTotal() {
        return reserva.calcularSubTotal() + totalServicios;
    }

    public ArrayList<Servicio> getListaServicios() {
        return listaServicios;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public double getTotalServicios() {
        return totalServicios;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
