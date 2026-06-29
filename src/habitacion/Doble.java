package habitacion;

/**
 * Habitación doble con vista opcional al mar.
 */
public class Doble extends Habitacion {

    private static final long serialVersionUID = 1L;

    private int numCamas;
    private boolean vistaMar;

    public Doble(int numHabitacion, String descripcion, double precioNoche,
                 boolean disponibilidad, String caracteristica, String imagen,
                 int numCamas, boolean vistaMar) {
        super(numHabitacion, descripcion, precioNoche, disponibilidad, caracteristica, imagen);
        this.numCamas = numCamas;
        this.vistaMar = vistaMar;
    }

    public int getNumCamas() {
        return numCamas;
    }

    public void setNumCamas(int numCamas) {
        this.numCamas = numCamas;
    }

    public boolean isVistaMar() {
        return vistaMar;
    }

    public void setVistaMar(boolean vistaMar) {
        this.vistaMar = vistaMar;
    }

    @Override
    public double getPrecioNoche() {
        return precioNoche;
    }

    @Override
    public String getDescripcion() {
        return descripcion + " | Doble | " + numCamas + " cama(s)"
                + (vistaMar ? " | Vista al mar" : "");
    }
}
