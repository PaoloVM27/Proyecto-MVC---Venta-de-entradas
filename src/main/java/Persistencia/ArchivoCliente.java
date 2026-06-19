package Persistencia;

import Librerias.SerializadoraGen;
import Modelo.Cliente;
import java.io.File;

public class ArchivoCliente {
    private String ruta;

    public ArchivoCliente() {
        this.ruta = "datos/clientes.dat";
        crearCarpetaDatos();
    }

    private void crearCarpetaDatos() {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }

    public boolean guardarClientes(Cliente[] clientes, int numClientes) {
        try {
            // Creamos un arreglo ajustado al tamaño real para no guardar nulls innecesarios
            Cliente[] arregloExacto = new Cliente[numClientes];
            for (int i = 0; i < numClientes; i++) {
                arregloExacto[i] = clientes[i];
            }
            
            SerializadoraGen.serializar(ruta, arregloExacto);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public Cliente[] cargarClientes() {
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                return new Cliente[0];
            }

            Object obj = SerializadoraGen.deserializar(ruta);
            
            if (obj instanceof Cliente[]) {
                return (Cliente[]) obj;
            }
            
        } catch (Exception e) {
            // Ignorar errores de carga inicial
        }

        return new Cliente[0];
    }
}