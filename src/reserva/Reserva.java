package reserva;

import cliente.Cliente;
import habitacion.Habitacion;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Reserva de una o varias habitaciones para un cliente.
 */
public class Reserva implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idReserva;
    private Cliente cliente;
    private ArrayList<Habitacion> habitaciones;
    private LocalDate fechaInicio;
    private int diasOcupacion;
    private boolean activa;

    public Reserva(int idReserva, Cliente cliente, ArrayList<Habitacion> habitaciones,
                   LocalDate fechaInicio, int diasOcupacion) {
        this.idReserva = idReserva;
        this.cliente = cliente;
        this.habitaciones = habitaciones;
        this.fechaInicio = fechaInicio;
        this.diasOcupacion = diasOcupacion;
        this.activa = true;
    }

    public void habitacionesOcupadas() {
        for (Habitacion hab : habitaciones) {
            hab.setDisponibilidad(false);
        }
    }

    public void liberarHabitaciones() {
        for (Habitacion hab : habitaciones) {
            hab.setDisponibilidad(true);
        }
    }

    public double calcularSubTotal() {
        double subtotal = 0;
        for (Habitacion hab : habitaciones) {
            subtotal += hab.getPrecioNoche() * diasOcupacion;
        }
        return subtotal;
    }

    public String resumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Reserva #").append(idReserva).append(" ===\n");
        sb.append("Cliente: ").append(cliente.getNombre()).append("\n");
        sb.append("Inicio: ").append(fechaInicio).append("\n");
        sb.append("Días: ").append(diasOcupacion).append("\n");
        sb.append("Habitaciones:\n");
        for (Habitacion hab : habitaciones) {
            sb.append("  - ").append(hab.getDescripcion()).append("\n");
        }
        sb.append("Subtotal habitaciones: $").append(String.format("%.2f", calcularSubTotal()));
        return sb.toString();
    }

    public LocalDate getFechaFin() {
        return fechaInicio.plusDays(diasOcupacion);
    }

    public String getNumerosHabitaciones() {
        StringBuilder sb = new StringBuilder();
        for (Habitacion h : habitaciones) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(h.getNumHabitacion());
        }
        return sb.toString();
    }

    public String getHabitacionesDetalle() {
        StringBuilder sb = new StringBuilder();
        for (Habitacion h : habitaciones) {
            if (sb.length() > 0) {
                sb.append(" | ");
            }
            sb.append("#").append(h.getNumHabitacion())
              .append(" ").append(h.getTipo());
        }
        return sb.toString();
    }

    public int getIdReserva() {
        return idReserva;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<Habitacion> getHabitaciones() {
        return habitaciones;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getDiasOcupacion() {
        return diasOcupacion;
    }

    public void setDiasOcupacion(int diasOcupacion) {
        this.diasOcupacion = diasOcupacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "Reserva #" + idReserva + " - " + cliente.getNombre()
                + " (" + fechaInicio + ", " + diasOcupacion + " días)";
    }
}
