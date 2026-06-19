package Persistencia;

import Librerias.SerializadoraGen;
import Modelo.Usuario;
import java.io.File;

public class ArchivoUsuario {
    private String ruta;

    public ArchivoUsuario() {
        this.ruta = "datos/usuarios.dat";
        crearCarpetaDatos();
    }

    private void crearCarpetaDatos() {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }

    public boolean guardarUsuarios(Usuario[] usuarios, int numUsuarios) {
        try {
            Usuario[] arregloExacto = new Usuario[numUsuarios];
            for (int i = 0; i < numUsuarios; i++) {
                arregloExacto[i] = usuarios[i];
            }
            
            SerializadoraGen.serializar(ruta, arregloExacto);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public Usuario[] cargarUsuarios() {
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                return new Usuario[0];
            }

            Object obj = SerializadoraGen.deserializar(ruta);
            
            if (obj instanceof Usuario[]) {
                return (Usuario[]) obj;
            }
            
        } catch (Exception e) {
        }

        return new Usuario[0];
    }
}