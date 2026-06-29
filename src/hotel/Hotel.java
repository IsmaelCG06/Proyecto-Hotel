package hotel;

import cliente.Cliente;
import cliente.Empresario;
import cliente.Natural;
import factura.Factura;
import habitacion.Doble;
import habitacion.Habitacion;
import habitacion.Matrimonial;
import habitacion.Sencilla;
import persistencia.GestorPersistencia;
import reserva.Estadia;
import reserva.Reserva;
import reserva.Servicio;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Clase principal que administra habitaciones, clientes, reservas y facturas.
 */
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private ArrayList<Reserva> reservas;
    private ArrayList<Habitacion> habitaciones;
    private ArrayList<Cliente> clientes;
    private ArrayList<Estadia> estadias;
    private ArrayList<Factura> facturas;
    private ArrayList<Servicio> serviciosDisponibles;
    private int contadorReserva;
    private int contadorFactura;
    private int contadorServicio;

    public Hotel(String nombre) {
        this.nombre = nombre;
        //--Cursor IA: sin inicializar las listas, al registrar algo sale NullPointerException
        this.reservas = new ArrayList<>();
        this.habitaciones = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.estadias = new ArrayList<>();
        this.facturas = new ArrayList<>();
        this.serviciosDisponibles = new ArrayList<>();
        this.contadorReserva = 1;
        this.contadorFactura = 1;
        this.contadorServicio = 1;
    }

    public static Hotel cargarDesdeArchivo() {
        Hotel hotel = GestorPersistencia.cargar(new Hotel("Hotel Sol del Mar"));
        return hotel;
    }

    public void guardarEnArchivo() {
        GestorPersistencia.guardar(this);
    }

    public boolean registrarHabitacion(Habitacion habitacion) {
        for (Habitacion h : habitaciones) {
            if (h.getNumHabitacion() == habitacion.getNumHabitacion()) {
                return false;
            }
        }
        habitaciones.add(habitacion);
        guardarEnArchivo();
        return true;
    }

    public boolean modificarHabitacion(int numHabitacion, Habitacion nueva) {
        for (int i = 0; i < habitaciones.size(); i++) {
            if (habitaciones.get(i).getNumHabitacion() == numHabitacion) {
                habitaciones.set(i, nueva);
                guardarEnArchivo();
                return true;
            }
        }
        return false;
    }

    public boolean eliminarHabitacion(int numHabitacion) {
        for (int i = 0; i < habitaciones.size(); i++) {
            if (habitaciones.get(i).getNumHabitacion() == numHabitacion) {
                habitaciones.remove(i);
                guardarEnArchivo();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Habitacion> consultarHabitacionesDisponiblesPorTipo(String tipo) {
        ArrayList<Habitacion> resultado = new ArrayList<>();
        for (Habitacion h : habitaciones) {
            if (h.estaDisponible() && h.getTipo().equalsIgnoreCase(tipo)) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    public ArrayList<Habitacion> listarHabitacionesDisponibles() {
        ArrayList<Habitacion> resultado = new ArrayList<>();
        for (Habitacion h : habitaciones) {
            if (h.estaDisponible()) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    public ArrayList<Habitacion> listarHabitacionesOcupadas() {
        ArrayList<Habitacion> resultado = new ArrayList<>();
        for (Habitacion h : habitaciones) {
            if (!h.estaDisponible()) {
                resultado.add(h);
            }
        }
        return resultado;
    }

    public ArrayList<Habitacion> listarTodasHabitaciones() {
        return habitaciones;
    }

    public Habitacion buscarHabitacionPorNumero(int num) {
        for (Habitacion h : habitaciones) {
            if (h.getNumHabitacion() == num) {
                return h;
            }
        }
        return null;
    }

    public String listarPreciosPorTipo() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Precios por tipo de habitación ===\n");
        for (Habitacion h : habitaciones) {
            sb.append(h.getTipo()).append(" #").append(h.getNumHabitacion())
              .append(": $").append(String.format("%.2f", h.getPrecioNoche()))
              .append("/noche - ").append(h.estaDisponible() ? "Disponible" : "Ocupada")
              .append("\n");
        }
        return sb.toString();
    }

    public String registrarCliente(Cliente cliente) {
        if (cliente instanceof Natural natural) {
            for (Cliente c : clientes) {
                if (c instanceof Natural n && n.getCedula() == natural.getCedula()) {
                    return "Ya existe un cliente natural con cédula " + natural.getCedula() + ".";
                }
            }
        }
        if (!cliente.isEsHabitual()) {
            cliente.setDescuento(0);
        }
        clientes.add(cliente);
        guardarEnArchivo();
        return "Cliente registrado correctamente.";
    }

    public boolean modificarCliente(int indice, Cliente cliente) {
        if (indice >= 0 && indice < clientes.size()) {
            clientes.set(indice, cliente);
            guardarEnArchivo();
            return true;
        }
        return false;
    }

    public boolean eliminarCliente(int indice) {
        if (indice >= 0 && indice < clientes.size()) {
            clientes.remove(indice);
            guardarEnArchivo();
            return true;
        }
        return false;
    }

    public ArrayList<Cliente> listarClientesHabituales() {
        ArrayList<Cliente> habituales = new ArrayList<>();
        for (Cliente c : clientes) {
            if (c.isEsHabitual()) {
                habituales.add(c);
            }
        }
        return habituales;
    }

    public ArrayList<Cliente> listarClientes() {
        return clientes;
    }

    public boolean registrarServicio(Servicio servicio) {
        serviciosDisponibles.add(servicio);
        guardarEnArchivo();
        return true;
    }

    public String crearServicio(String nombre, String descripcion, double costo) {
        String nombreNorm = nombre.trim();
        int repetidos = 0;
        for (Servicio s : serviciosDisponibles) {
            if (s.getNombre().equalsIgnoreCase(nombreNorm)) {
                repetidos++;
            }
        }
        if (repetidos >= 2) {
            return "Ya hay 2 servicios con el nombre '" + nombreNorm + "'. No se permiten más.";
        }
        Servicio servicio = new Servicio(contadorServicio++, nombreNorm, descripcion.trim(), costo);
        serviciosDisponibles.add(servicio);
        guardarEnArchivo();
        return "Servicio registrado correctamente.";
    }

    public boolean modificarServicio(int id, String nombre, String descripcion, double costo) {
        for (Servicio s : serviciosDisponibles) {
            if (s.getId() == id) {
                s.setNombre(nombre);
                s.setDescripcion(descripcion);
                s.setCosto(costo);
                guardarEnArchivo();
                return true;
            }
        }
        return false;
    }

    public boolean eliminarServicio(int id) {
        for (int i = 0; i < serviciosDisponibles.size(); i++) {
            if (serviciosDisponibles.get(i).getId() == id) {
                serviciosDisponibles.remove(i);
                guardarEnArchivo();
                return true;
            }
        }
        return false;
    }

    public ArrayList<Servicio> listarServicios() {
        return serviciosDisponibles;
    }

    public Servicio buscarServicioPorId(int id) {
        for (Servicio s : serviciosDisponibles) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public String crearReserva(Cliente cliente, ArrayList<Habitacion> habs,
                               LocalDate fechaInicio, int dias) {
        if (dias <= 0) {
            return "Los días de ocupación deben ser mayores a cero.";
        }
        for (Habitacion hab : habs) {
            if (hayConflictoFechas(hab, fechaInicio, dias, null)) {
                //--Cursor IA: sin validar solapamiento de fechas, dos clientes reservan la misma hab al mismo tiempo
                return "La habitación " + hab.getNumHabitacion()
                        + " ya está reservada en esas fechas.";
            }
        }
        Reserva reserva = new Reserva(contadorReserva++, cliente, habs, fechaInicio, dias);
        reserva.habitacionesOcupadas();
        reservas.add(reserva);
        Estadia estadia = new Estadia(reserva);
        estadias.add(estadia);
        guardarEnArchivo();
        return "Reserva #" + reserva.getIdReserva() + " creada correctamente.";
    }

    private boolean hayConflictoFechas(Habitacion hab, LocalDate inicio, int dias, Reserva excluir) {
        LocalDate fin = inicio.plusDays(dias);
        for (Reserva r : reservas) {
            if (!r.isActiva()) {
                continue;
            }
            if (excluir != null && r.getIdReserva() == excluir.getIdReserva()) {
                continue;
            }
            for (Habitacion hRes : r.getHabitaciones()) {
                if (hRes.getNumHabitacion() == hab.getNumHabitacion()) {
                    LocalDate finReserva = r.getFechaFin();
                    if (inicio.isBefore(finReserva) && fin.isAfter(r.getFechaInicio())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean eliminarReservaPorId(int idReserva) {
        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getIdReserva() == idReserva) {
                reservas.get(i).liberarHabitaciones();
                reservas.get(i).setActiva(false);
                reservas.remove(i);
                guardarEnArchivo();
                return true;
            }
        }
        return false;
    }

    public boolean eliminarReservaPorHabitacion(int numHabitacion) {
        for (int i = 0; i < reservas.size(); i++) {
            for (Habitacion h : reservas.get(i).getHabitaciones()) {
                if (h.getNumHabitacion() == numHabitacion) {
                    reservas.get(i).liberarHabitaciones();
                    reservas.get(i).setActiva(false);
                    reservas.remove(i);
                    guardarEnArchivo();
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<Reserva> listarReservasActivas() {
        ArrayList<Reserva> activas = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.isActiva()) {
                activas.add(r);
            }
        }
        return activas;
    }

    public Reserva buscarReservaPorId(int id) {
        for (Reserva r : reservas) {
            if (r.getIdReserva() == id) {
                return r;
            }
        }
        return null;
    }

    public Estadia buscarEstadiaPorReserva(int idReserva) {
        for (Estadia e : estadias) {
            if (e.getReserva().getIdReserva() == idReserva) {
                return e;
            }
        }
        return null;
    }

    public String agregarServicioAReserva(int idReserva, int idServicio) {
        Estadia estadia = buscarEstadiaPorReserva(idReserva);
        Servicio servicio = buscarServicioPorId(idServicio);
        if (estadia == null) {
            return "No se encontró estadía para la reserva #" + idReserva;
        }
        if (servicio == null) {
            return "Servicio no encontrado.";
        }
        estadia.agregarServicio(servicio);
        guardarEnArchivo();
        return "Servicio '" + servicio.getNombre() + "' agregado a la reserva #" + idReserva;
    }

    public Factura generarFactura(int idReserva) {
        Reserva reserva = buscarReservaPorId(idReserva);
        Estadia estadia = buscarEstadiaPorReserva(idReserva);
        if (reserva == null || estadia == null) {
            return null;
        }
        Factura factura = new Factura(contadorFactura++, reserva, estadia);
        facturas.add(factura);
        guardarEnArchivo();
        return factura;
    }

    public ArrayList<Factura> listarFacturas() {
        return facturas;
    }

    public void cargarDatosIniciales() {
        if (!habitaciones.isEmpty()) {
            return;
        }
        registrarHabitacion(new Sencilla(101, "Habitación acogedora", 85000,
                true, "WiFi, TV", "sencilla_default.jpg", 1, true));
        registrarHabitacion(new Doble(201, "Amplia con balcón", 120000,
                true, "WiFi, minibar", "doble_default.jpg", 2, false));
        registrarHabitacion(new Matrimonial(301, "Suite romántica", 180000,
                true, "WiFi, jacuzzi, vista", "matrimonial_default.jpg", 1, true, true));

        registrarCliente(new Natural("Ana García", "Cédula", "3001234567", true, 123456789));
        clientes.get(0).setDescuento(10);

        registrarCliente(new Empresario("Carlos López", "NIT", "3109876543",
                false, 900123456, "Tech Solutions SAS"));

        crearServicio("Desayuno buffet", "Desayuno completo en restaurante", 35000);
        crearServicio("Lavandería", "Lavado y planchado de ropa", 25000);
        crearServicio("Minibar", "Consumo de minibar", 45000);
        crearServicio("Room service", "Servicio a la habitación", 20000);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    public ArrayList<Estadia> getEstadias() {
        return estadias;
    }
}
