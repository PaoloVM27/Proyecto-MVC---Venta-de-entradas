package Persistencia;

import Librerias.SerializadoraGen;
import Modelo.Cliente;
import Modelo.Concierto;
import Modelo.Venta;
import java.io.File;

public class ArchivoVenta {
    private String ruta;

    public ArchivoVenta() {
        this.ruta = "datos/ventas.dat";
        crearCarpetaDatos();
    }

    private void crearCarpetaDatos() {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }

    public boolean guardarVentas(Venta[] ventas, int numVentas) {
        try {
            Venta[] arregloExacto = new Venta[numVentas];
            for (int i = 0; i < numVentas; i++) {
                arregloExacto[i] = ventas[i];
            }
            
            SerializadoraGen.serializar(ruta, arregloExacto);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public Venta[] cargarVentas(Cliente[] clientes, int numClientes, Concierto[] conciertos, int numConciertos) {
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                return new Venta[0];
            }

            Object obj = SerializadoraGen.deserializar(ruta);
            
            if (obj instanceof Venta[]) {
                return (Venta[]) obj;
            }
            
        } catch (Exception e) {
        }

        return new Venta[0];
    }
}