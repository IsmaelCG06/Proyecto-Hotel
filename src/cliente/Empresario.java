package cliente;

/**
 * Representa un cliente empresarial del hotel.
 * Hereda de Cliente.
 */
public class Empresario extends Cliente {

    private static final long serialVersionUID = 1L;

    private int nit;
    private String razonSocial;

    public Empresario(String nombre, String tipoIdentificacion, String contacto,
                      boolean esHabitual, int nit, String razonSocial) {
        super(nombre, tipoIdentificacion, contacto, esHabitual);
        this.nit = nit;
        this.razonSocial = razonSocial;
    }

    public int getNit() {
        return nit;
    }

    public void setNit(int nit) {
        this.nit = nit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * Implementación del método abstracto.
     */
    @Override
    public String getDatos() {
        return "=== Cliente Empresarial ===" +
               "\nRazón Social : " + razonSocial +
               "\nNIT          : " + nit +
               "\nContacto     : " + contacto +
               "\nHabitual     : " + (esHabitual ? "Sí" : "No") +
               "\nDescuento    : " + descuento + "%";
    }

    @Override
    public String toString() {
        return razonSocial + " (NIT: " + nit + ")";
    }
}