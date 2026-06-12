package Persistencia;

import Modelo.Usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ArchivoUsuario {
    private String ruta;

    public ArchivoUsuario() {
        this.ruta = "datos/usuarios.txt";
        crearCarpetaDatos();
    }

    private void crearCarpetaDatos() {
        File carpeta = new File("datos");

        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }

    public boolean guardarUsuarios(List<Usuario> usuarios) {
        try {
            PrintWriter escritor = new PrintWriter(new FileWriter(ruta));

            for (Usuario usuario : usuarios) {
                escritor.println(
                        usuario.getNombres() + ";" +
                        usuario.getApellidos() + ";" +
                        usuario.getDni() + ";" +
                        usuario.getContrasena() + ";" +
                        usuario.isEstado()
                );
            }

            escritor.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();

        try {
            File archivo = new File(ruta);

            if (!archivo.exists()) {
                return usuarios;
            }

            BufferedReader lector = new BufferedReader(new FileReader(ruta));
            String linea;

            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(";");

                if (datos.length >= 4) {
                    String nombres = datos[0];
                    String apellidos = datos[1];
                    String dni = datos[2];
                    String contrasena = datos[3];

                    Usuario usuario = new Usuario(nombres, apellidos, dni, contrasena);

                    if (datos.length == 5) {
                        boolean estado = Boolean.parseBoolean(datos[4]);

                        if (estado) {
                            usuario.activar();
                        } else {
                            usuario.desactivar();
                        }
                    }

                    usuarios.add(usuario);
                }
            }

            lector.close();

        } catch (Exception e) {
            return usuarios;
        }

        return usuarios;
    }
}