package factura;

import cliente.Cliente;
import reserva.Estadia;
import reserva.Reserva;
import reserva.Servicio;
import java.io.Serializable;

/**
 * Factura informativa generada a partir de una reserva y estadía.
 */
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    private int idFactura;
    private Reserva reserva;
    private Estadia estadia;
    private double descuentoAplicado;
    private double subTotalHabitacion;
    private double subTotalServicio;

    public Factura(int idFactura, Reserva reserva, Estadia estadia) {
        this.idFactura = idFactura;
        this.reserva = reserva;
        this.estadia = estadia;
        this.subTotalHabitacion = reserva.calcularSubTotal();
        this.subTotalServicio = estadia.getTotalServicios();
        this.descuentoAplicado = 0;
        aplicarDescuento();
    }

    public double aplicarDescuento() {
        Cliente cliente = reserva.getCliente();
        double base = subTotalHabitacion + subTotalServicio;
        if (cliente.isEsHabitual() && cliente.getDescuento() > 0) {
            descuentoAplicado = base * (cliente.getDescuento() / 100.0);
        } else {
            descuentoAplicado = 0;
        }
        return descuentoAplicado;
    }

    public double calcularTotal() {
        return subTotalHabitacion + subTotalServicio - descuentoAplicado;
    }

    public String generarFactura() {
        Cliente cliente = reserva.getCliente();
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("       FACTURA INFORMATIVA #").append(idFactura).append("\n");
        sb.append("========================================\n");
        sb.append("Cliente: ").append(cliente.getNombre()).append("\n");
        sb.append("Identificación: ").append(cliente.getTipoIdentificacion()).append("\n");
        sb.append("Reserva #").append(reserva.getIdReserva()).append("\n");
        sb.append("Noches: ").append(reserva.getDiasOcupacion()).append("\n\n");
        sb.append("--- Habitaciones ---\n");
        for (habitacion.Habitacion hab : reserva.getHabitaciones()) {
            sb.append(hab.getTipo()).append(" #").append(hab.getNumHabitacion())
              .append(" - $").append(String.format("%.2f", hab.getPrecioNoche()))
              .append("/noche\n");
        }
        sb.append("Subtotal habitaciones: $").append(String.format("%.2f", subTotalHabitacion)).append("\n\n");
        sb.append("--- Servicios ---\n");
        for (Servicio s : estadia.getListaServicios()) {
            sb.append(s.getNombre()).append(" - $").append(String.format("%.2f", s.getCosto())).append("\n");
        }
        sb.append("Subtotal servicios: $").append(String.format("%.2f", subTotalServicio)).append("\n\n");
        if (descuentoAplicado > 0) {
            sb.append("Descuento aplicado: -$").append(String.format("%.2f", descuentoAplicado)).append("\n");
        }
        sb.append("TOTAL: $").append(String.format("%.2f", calcularTotal())).append("\n");
        sb.append("========================================\n");
        return sb.toString();
    }

    public int getIdFactura() {
        return idFactura;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public Estadia getEstadia() {
        return estadia;
    }

    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public double getSubTotalHabitacion() {
        return subTotalHabitacion;
    }

    public double getSubTotalServicio() {
        return subTotalServicio;
    }
}
