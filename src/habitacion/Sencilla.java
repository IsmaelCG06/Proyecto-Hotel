package habitacion;

/**
 * Habitación sencilla con camas y baño privado.
 */
public class Sencilla extends Habitacion {

    private static final long serialVersionUID = 1L;

    private int numCamas;
    private boolean tieneBanio;

    public Sencilla(int numHabitacion, String descripcion, double precioNoche,
                    boolean disponibilidad, String caracteristica, String imagen,
                    int numCamas, boolean tieneBanio) {
        super(numHabitacion, descripcion, precioNoche, disponibilidad, caracteristica, imagen);
        this.numCamas = numCamas;
        this.tieneBanio = tieneBanio;
    }

    public int getNumCamas() {
        return numCamas;
    }

    public void setNumCamas(int numCamas) {
        this.numCamas = numCamas;
    }

    public boolean isTieneBanio() {
        return tieneBanio;
    }

    public void setTieneBanio(boolean tieneBanio) {
        this.tieneBanio = tieneBanio;
    }

    @Override
    public double getPrecioNoche() {
        return precioNoche;
    }

    @Override
    public String getDescripcion() {
        return descripcion + " | Sencilla | " + numCamas + " cama(s)"
                + (tieneBanio ? " | Baño privado" : " | Baño compartido");
    }
}
