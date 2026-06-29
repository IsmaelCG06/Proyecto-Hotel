package reserva;

import java.io.Serializable;

/**
 * Servicio del hotel que puede cargarse durante una estadía.
 */
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String descripcion;
    private double costo;

    public Servicio(int id, String nombre, String descripcion, double costo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return nombre + " - $" + costo;
    }
}
