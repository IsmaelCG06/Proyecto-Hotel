package cliente;

import java.io.Serializable;

/**
 * Clase abstracta que representa un cliente del hotel.
 * Puede ser una persona natural o un empresario.
 */
public abstract class Cliente implements Serializable {

    // Atributos protegidos (accesibles por subclases)
    protected String nombre;
    protected String tipoIdentificacion;
    protected String contacto;
    protected double descuento;
    protected boolean esHabitual;

    // Constructor
    public Cliente(String nombre, String tipoIdentificacion, String contacto, boolean esHabitual) {
        this.nombre = nombre;
        this.tipoIdentificacion = tipoIdentificacion;
        this.contacto = contacto;
        this.esHabitual = esHabitual;
        this.descuento = 0.0;
    }

    // Método abstracto que cada subclase implementa a su manera
    public abstract String getDatos();

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public String getContacto() {
        return contacto;
    }

    public double getDescuento() {
        return descuento;
    }

    public boolean isEsHabitual() {
        return esHabitual;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public void setDescuento(double descuento) {
        if (descuento >= 0 && descuento <= 100) {
            this.descuento = descuento;
        }
    }

    public void setEsHabitual(boolean esHabitual) {
        this.esHabitual = esHabitual;
    }

    /**
     * Aplica un descuento solo si el cliente es habitual.
     * Retorna el valor con descuento aplicado.
     */
    public double aplicarDescuento(double valorBase) {
        if (esHabitual && descuento > 0) {
            return valorBase - (valorBase * descuento / 100);
        }
        return valorBase;
    }

    @Override
    public String toString() {
        return getDatos();
    }
}
