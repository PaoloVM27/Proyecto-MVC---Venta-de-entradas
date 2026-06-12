package Persistencia;

import Modelo.Concierto;
import Modelo.Zona;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public boolean guardarConciertos(List<Concierto> conciertos) {
        try {
            PrintWriter escritor = new PrintWriter(new FileWriter(ruta));

            for (Concierto concierto : conciertos) {
                for (Zona zona : concierto.getZonas()) {
                    escritor.println(
                            concierto.getNombre() + ";" +
                            concierto.getFecha().getTime() + ";" +
                            zona.getNombre() + ";" +
                            zona.getCapacidad() + ";" +
                            zona.getPrecio()
                    );
                }
            }

            escritor.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public List<Concierto> cargarConciertos() {
        List<Concierto> conciertos = new ArrayList<>();

        try {
            File archivo = new File(ruta);

            if (!archivo.exists()) {
                return conciertos;
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

                    Concierto concierto = buscarConciertoEnLista(conciertos, nombreConcierto);

                    if (concierto == null) {
                        concierto = new Concierto(nombreConcierto, new Date(fechaMillis));
                        conciertos.add(concierto);
                    }

                    concierto.agregarZona(nombreZona, capacidad, precio);
                }
            }

            lector.close();

        } catch (Exception e) {
            return conciertos;
        }

        return conciertos;
    }

    private Concierto buscarConciertoEnLista(List<Concierto> conciertos, String nombre) {
        for (Concierto concierto : conciertos) {
            if (concierto.getNombre().equalsIgnoreCase(nombre)) {
                return concierto;
            }
        }

        return null;
    }
}