package persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Gestiona la persistencia del hotel en archivos binarios (.dat).
 */
public class GestorPersistencia {

    private static final String CARPETA_DATOS = "datos";
    private static final String ARCHIVO_HOTEL = CARPETA_DATOS + File.separator + "hotel.dat";

    private GestorPersistencia() {
    }

    public static void guardar(Object objeto) {
        guardarEn(ARCHIVO_HOTEL, objeto);
    }

    public static void guardarEn(String archivo, Object objeto) {
        ObjectOutputStream oos = null;
        try {
            asegurarCarpetaDatos();
            oos = new ObjectOutputStream(new FileOutputStream(archivo));
            oos.writeObject(objeto);
            oos.flush();
        } catch (Exception e) {
            //--Cursor IA
            System.err.println("Error al guardar datos: " + e.getMessage());
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                System.err.println("Error al cerrar archivo de escritura: " + e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T cargar(T objetoPorDefecto) {
        return cargarDesde(ARCHIVO_HOTEL, objetoPorDefecto);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cargarDesde(String archivo, T objetoPorDefecto) {
        ObjectInputStream ois = null;
        try {
            File archivoDatos = new File(archivo);
            if (!archivoDatos.exists()) {
                return objetoPorDefecto;
            }
            ois = new ObjectInputStream(new FileInputStream(archivoDatos));
            return (T) ois.readObject();
        } catch (Exception e) {
            //--Cursor IA: si el .dat no existe o está corrupto, se devuelve el valor por defecto
            System.err.println("Error al cargar datos: " + e.getMessage());
            return objetoPorDefecto;
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                System.err.println("Error al cerrar archivo de lectura: " + e.getMessage());
            }
        }
    }

    private static void asegurarCarpetaDatos() {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }
}
