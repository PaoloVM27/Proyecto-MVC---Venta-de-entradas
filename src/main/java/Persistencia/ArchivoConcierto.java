package Persistencia;

import Modelo.Concierto;
import Modelo.Zona;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

public class ArchivoConcierto {
    private String ruta;

    public ArchivoConcierto() {
        this.ruta = "datos/conciertos.txt";
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
            PrintWriter escritor = new PrintWriter(new FileWriter(ruta));

            for (int i = 0; i < numConciertos; i++) {
                Concierto concierto = conciertos[i];
                Zona[] zonas = concierto.getZonas();
                for (int j = 0; j < zonas.length; j++) {
                    Zona zona = zonas[j];
                    if (zona != null) {
                        escritor.println(
                                concierto.getNombre() + ";" +
                                concierto.getFecha().getTime() + ";" +
                                zona.getNombre() + ";" +
                                zona.getCapacidad() + ";" +
                                zona.getPrecio()
                        );
                    }
                }
            }

            escritor.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public Concierto[] cargarConciertos() {
        Concierto[] conciertos = new Concierto[10];
        int numConciertos = 0;

        try {
            File archivo = new File(ruta);

            if (!archivo.exists()) {
                return new Concierto[0];
            }

            BufferedReader lector = new BufferedReader(new FileReader(ruta));
            String linea;

            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(";");

                if (datos.length == 5) {
                    String nombreConcierto = datos[0];
                    long fechaMillis = Long.parseLong(datos[1]);
                    String nombreZona = datos[2];
                    int capacidad = Integer.parseInt(datos[3]);
                    double precio = Double.parseDouble(datos[4]);

                    Concierto concierto = buscarConciertoEnLista(conciertos, numConciertos, nombreConcierto);

                    if (concierto == null) {
                        concierto = new Concierto(nombreConcierto, new Date(fechaMillis));
                        if (numConciertos >= conciertos.length) {
                            Concierto[] nuevoArreglo = new Concierto[conciertos.length * 2];
                            for (int i = 0; i < numConciertos; i++) {
                                nuevoArreglo[i] = conciertos[i];
                            }
                            conciertos = nuevoArreglo;
                        }
                        conciertos[numConciertos] = concierto;
                        numConciertos++;
                    }

                    concierto.agregarZona(nombreZona, capacidad, precio);
                }
            }

            lector.close();

        } catch (Exception e) {
            // Devuelve lo que pudo cargar
        }

        Concierto[] resultado = new Concierto[numConciertos];
        for (int i = 0; i < numConciertos; i++) {
            resultado[i] = conciertos[i];
        }
        return resultado;
    }

    private Concierto buscarConciertoEnLista(Concierto[] conciertos, int numConciertos, String nombre) {
        for (int i = 0; i < numConciertos; i++) {
            if (conciertos[i].getNombre().equalsIgnoreCase(nombre)) {
                return conciertos[i];
            }
        }

        return null;
    }
}