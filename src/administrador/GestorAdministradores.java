package administrador;

import persistencia.GestorPersistencia;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Administra los usuarios del sistema y su persistencia en administradores.dat.
 */
public class GestorAdministradores implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String ARCHIVO = "datos" + File.separator + "administradores.dat";

    private ArrayList<Administrador> administradores;

    public GestorAdministradores() {
        this.administradores = new ArrayList<>();
    }

    public static GestorAdministradores cargar() {
        GestorAdministradores gestor = GestorPersistencia.cargarDesde(
                ARCHIVO, new GestorAdministradores());
        if (gestor.administradores.isEmpty()) {
            gestor.crearAdministradorPorDefecto();
        }
        return gestor;
    }

    private void crearAdministradorPorDefecto() {
        //--Cursor IA: primera ejecución sin .dat — usuario por defecto para poder entrar
        administradores.add(new Administrador("admin", "admin123", "Administrador del sistema"));
        guardar();
    }

    public void guardar() {
        GestorPersistencia.guardarEn(ARCHIVO, this);
    }

    public Administrador buscarPorCredenciales(String id, String contrasena) {
        for (Administrador admin : administradores) {
            if (admin.validarCredenciales(id, contrasena)) {
                return admin;
            }
        }
        return null;
    }

    public boolean registrarAdministrador(Administrador nuevo) {
        for (Administrador admin : administradores) {
            if (admin.getId().equals(nuevo.getId())) {
                return false;
            }
        }
        administradores.add(nuevo);
        guardar();
        return true;
    }

    public ArrayList<Administrador> getAdministradores() {
        return administradores;
    }
}
