package administrador;

import java.io.Serializable;

/**
 * Usuario administrador que puede acceder al sistema de gestión del hotel.
 */
public class Administrador implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String contrasena;
    private String nombre;

    public Administrador(String id, String contrasena, String nombre) {
        this.id = id;
        this.contrasena = contrasena;
        this.nombre = nombre;
    }

    public boolean validarCredenciales(String idIngresado, String contrasenaIngresada) {
        //--Cursor IA: comparar sin trim en contraseña por si el usuario incluye espacios a propósito
        return id.equals(idIngresado) && contrasena.equals(contrasenaIngresada);
    }

    public String getId() {
        return id;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
