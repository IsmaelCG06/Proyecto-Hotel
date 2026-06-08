package cliente;

/**
 * Representa un cliente persona natural del hotel.
 * Hereda de Cliente.
 */
public class Natural extends Cliente {

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

    /**
     * Formato para guardar en archivo .txt
     * Separado por | para fácil lectura al cargar
     */
    public String toArchivoTxt() {
        return "NATURAL|" + nombre + "|" + cedula + "|" + contacto
               + "|" + esHabitual + "|" + descuento;
    }

    /**
     * Reconstruye un Natural desde una línea del archivo .txt
     */
    public static Natural fromArchivoTxt(String linea) {
        String[] partes = linea.split("\\|");
        String nombre = partes[1];
        int cedula = Integer.parseInt(partes[2]);
        String contacto = partes[3];
        boolean esHabitual = Boolean.parseBoolean(partes[4]);
        double descuento = Double.parseDouble(partes[5]);
        Natural n = new Natural(nombre, "Cédula", contacto, esHabitual, cedula);
        n.setDescuento(descuento);
        return n;
    }

    @Override
    public String toString() {
        return nombre + " (Cédula: " + cedula + ")";
    }
}
