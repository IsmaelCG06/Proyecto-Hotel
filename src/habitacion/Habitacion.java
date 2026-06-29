package habitacion;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase abstracta que representa una habitación del hotel.
 */
public abstract class Habitacion implements Serializable {

    private static final long serialVersionUID = 1L;

    protected int numHabitacion;
    protected String descripcion;
    protected double precioNoche;
    protected boolean disponibilidad;
    protected String caracteristica;
    protected String imagen;

    public Habitacion(int numHabitacion, String descripcion, double precioNoche,
                      boolean disponibilidad, String caracteristica, String imagen) {
        this.numHabitacion = numHabitacion;
        this.descripcion = descripcion;
        this.precioNoche = precioNoche;
        this.disponibilidad = disponibilidad;
        this.caracteristica = caracteristica;
        this.imagen = imagen;
    }

    public abstract double getPrecioNoche();

    public abstract String getDescripcion();

    public boolean estaDisponible() {
        return disponibilidad;
    }

    public int getNumHabitacion() {
        return numHabitacion;
    }

    public void setNumHabitacion(int numHabitacion) {
        this.numHabitacion = numHabitacion;
    }

    public String getDescripcionBase() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /** Imágenes separadas por coma; compatible con datos antiguos de una sola foto. */
    public ArrayList<String> getImagenes() {
        ArrayList<String> lista = new ArrayList<>();
        if (imagen == null || imagen.trim().isEmpty()) {
            return lista;
        }
        for (String parte : imagen.split(",")) {
            String nombre = parte.trim();
            if (!nombre.isEmpty()) {
                lista.add(nombre);
            }
        }
        return lista;
    }

    public void setImagenes(ArrayList<String> lista) {
        if (lista == null || lista.isEmpty()) {
            imagen = "";
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(lista.get(i).trim());
        }
        imagen = sb.toString();
    }

    public String getImagenesTexto() {
        return imagen == null ? "" : imagen.replace(",", ", ");
    }

    public String getTipo() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "Hab. " + numHabitacion + " - " + getTipo() + " ($" + getPrecioNoche() + "/noche)";
    }
}
