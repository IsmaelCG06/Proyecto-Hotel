package habitacion;

/**
 * Habitación matrimonial con jacuzzi opcional.
 */
public class Matrimonial extends Habitacion {

    private static final long serialVersionUID = 1L;

    private int numCamas;
    private boolean vistaMar;
    private boolean tieneJacuzzi;

    public Matrimonial(int numHabitacion, String descripcion, double precioNoche,
                       boolean disponibilidad, String caracteristica, String imagen,
                       int numCamas, boolean vistaMar, boolean tieneJacuzzi) {
        super(numHabitacion, descripcion, precioNoche, disponibilidad, caracteristica, imagen);
        this.numCamas = numCamas;
        this.vistaMar = vistaMar;
        this.tieneJacuzzi = tieneJacuzzi;
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

    public boolean isTieneJacuzzi() {
        return tieneJacuzzi;
    }

    public void setTieneJacuzzi(boolean tieneJacuzzi) {
        this.tieneJacuzzi = tieneJacuzzi;
    }

    @Override
    public double getPrecioNoche() {
        return precioNoche;
    }

    @Override
    public String getDescripcion() {
        return descripcion + " | Matrimonial | " + numCamas + " cama(s)"
                + (vistaMar ? " | Vista al mar" : "")
                + (tieneJacuzzi ? " | Jacuzzi" : "");
    }
}
