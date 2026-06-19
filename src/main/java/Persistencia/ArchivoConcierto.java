package Persistencia;

import Librerias.SerializadoraGen;
import Modelo.Concierto;
import java.io.File;

public class ArchivoConcierto {
    private String ruta;

    public ArchivoConcierto() {
        this.ruta = "datos/conciertos.dat";
        crearCarpetaDatos();
    }

    private void crearCarpetaDatos() {
        File carpeta = new File("datos");
        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }

    public boolean guardarConciertos(Concierto[] conciertos, int numConciertos) {
        try {
            Concierto[] arregloExacto = new Concierto[numConciertos];
            for (int i = 0; i < numConciertos; i++) {
                arregloExacto[i] = conciertos[i];
            }
            
            SerializadoraGen.serializar(ruta, arregloExacto);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public Concierto[] cargarConciertos() {
        try {
            File archivo = new File(ruta);
            if (!archivo.exists()) {
                return new Concierto[0];
            }

            Object obj = SerializadoraGen.deserializar(ruta);
            
            if (obj instanceof Concierto[]) {
                return (Concierto[]) obj;
            }
            
        } catch (Exception e) {
        }

        return new Concierto[0];
    }
}