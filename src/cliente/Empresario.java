package cliente;

/**
 * Representa un cliente empresarial del hotel.
 * Hereda de Cliente.
 */
public class Empresario extends Cliente {

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

    /**
     * Formato para guardar en archivo .txt
     */
    public String toArchivoTxt() {
        return "EMPRESARIO|" + nombre + "|" + nit + "|" + razonSocial
               + "|" + contacto + "|" + esHabitual + "|" + descuento;
    }

    /**
     * Reconstruye un Empresario desde una línea del archivo .txt
     */
    public static Empresario fromArchivoTxt(String linea) {
        String[] partes = linea.split("\\|");
        String nombre = partes[1];
        int nit = Integer.parseInt(partes[2]);
        String razonSocial = partes[3];
        String contacto = partes[4];
        boolean esHabitual = Boolean.parseBoolean(partes[5]);
        double descuento = Double.parseDouble(partes[6]);
        Empresario e = new Empresario(nombre, "NIT", contacto, esHabitual, nit, razonSocial);
        e.setDescuento(descuento);
        return e;
    }

    @Override
    public String toString() {
        return razonSocial + " (NIT: " + nit + ")";
    }
}