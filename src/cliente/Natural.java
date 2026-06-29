package cliente;

/**
 * Representa un cliente persona natural del hotel.
 * Hereda de Cliente.
 */
public class Natural extends Cliente {

    private static final long serialVersionUID = 1L;

    private int cedula;

    public Natural(String nombre, String tipoIdentificacion, String contacto,
                   boolean esHabitual, int cedula) {
        super(nombre, tipoIdentificacion, contacto, esHabitual);
        this.cedula = cedula;
    }

    public int getCedula() {
        return cedula;
    }

    public void setCedula(int cedula) {
        this.cedula = cedula;
    }

    /**
     * Implementación del método abstracto.
     * Retorna un resumen de los datos del cliente natural.
     */
    @Override
    public String getDatos() {
        return "=== Cliente Natural ===" +
               "\nNombre       : " + nombre +
               "\nCédula       : " + cedula +
               "\nContacto     : " + contacto +
               "\nHabitual     : " + (esHabitual ? "Sí" : "No") +
               "\nDescuento    : " + descuento + "%";
    }

    @Override
    public String toString() {
        return nombre + " (Cédula: " + cedula + ")";
    }
}
