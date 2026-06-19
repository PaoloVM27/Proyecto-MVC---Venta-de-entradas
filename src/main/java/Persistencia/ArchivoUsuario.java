package Persistencia;

import Modelo.Usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

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

    public boolean guardarUsuarios(Usuario[] usuarios, int numUsuarios) {
        try {
            PrintWriter escritor = new PrintWriter(new FileWriter(ruta));

            for (int i = 0; i < numUsuarios; i++) {
                Usuario usuario = usuarios[i];
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

    public Usuario[] cargarUsuarios() {
        Usuario[] usuarios = new Usuario[10];
        int numUsuarios = 0;

        try {
            File archivo = new File(ruta);

            if (!archivo.exists()) {
                return new Usuario[0];
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

                    if (numUsuarios >= usuarios.length) {
                        Usuario[] nuevoArreglo = new Usuario[usuarios.length * 2];
                        for (int i = 0; i < numUsuarios; i++) {
                            nuevoArreglo[i] = usuarios[i];
                        }
                        usuarios = nuevoArreglo;
                    }

                    usuarios[numUsuarios] = usuario;
                    numUsuarios++;
                }
            }

            lector.close();

        } catch (Exception e) {
            // Retorna lo cargado
        }

        Usuario[] resultado = new Usuario[numUsuarios];
        for (int i = 0; i < numUsuarios; i++) {
            resultado[i] = usuarios[i];
        }
        return resultado;
    }
}