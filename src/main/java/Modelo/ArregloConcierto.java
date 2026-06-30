package Modelo;

import Modelo.Concierto;
import Modelo.Usuario;
import Modelo.Zona;
import Persistencia.ArchivoConcierto;
import java.util.Date;

public class ArregloConcierto {
    private Concierto[] conciertos;
    private int numConciertos;
    private ArchivoConcierto archivoConcierto;

    public ArregloConcierto() {
        this.archivoConcierto = new ArchivoConcierto();
        Concierto[] cargados = archivoConcierto.cargarConciertos();
        
        if (cargados != null) {
            this.conciertos = new Concierto[Math.max(10, cargados.length * 2)];
            this.numConciertos = cargados.length;
            for (int i = 0; i < cargados.length; i++) {
                this.conciertos[i] = cargados[i];
            }
        } else {
            this.conciertos = new Concierto[10];
            this.numConciertos = 0;
        }
    }

    public boolean crearConcierto(String nombre, Date fecha) {
        if (nombre == null || nombre.isEmpty()) {
            return false;
        }

        if (fecha == null) {
            return false;
        }

        if (buscarConcierto(nombre) != null) {
            return false;
        }

        if (numConciertos >= conciertos.length) {
            redimensionarArreglo();
        }

        Concierto nuevoConcierto = new Concierto(nombre, fecha);
        conciertos[numConciertos] = nuevoConcierto;
        numConciertos++;

        return archivoConcierto.guardarConciertos(conciertos, numConciertos);
    }
    
    private void redimensionarArreglo() {
        Concierto[] nuevoArreglo = new Concierto[conciertos.length * 2];
        for (int i = 0; i < numConciertos; i++) {
            nuevoArreglo[i] = conciertos[i];
        }
        conciertos = nuevoArreglo;
    }

    public boolean crearConciertoUNMSM(Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        String nombreConcierto = "Aniversario UNMSM";

        Concierto concierto = buscarConcierto(nombreConcierto);

        if (concierto == null) {
            concierto = new Concierto(nombreConcierto, new Date());
            if (numConciertos >= conciertos.length) {
                redimensionarArreglo();
            }
            conciertos[numConciertos] = concierto;
            numConciertos++;
        }

        if (concierto.buscarZona("VIP") == null) {
            usuario.registrarZonas(concierto, "VIP", 50, 200.0);
        }

        if (concierto.buscarZona("Preferencial") == null) {
            usuario.registrarZonas(concierto, "Preferencial", 100, 150.0);
        }

        if (concierto.buscarZona("General") == null) {
            usuario.registrarZonas(concierto, "General", 200, 80.0);
        }

        if (concierto.buscarZona("Tribuna") == null) {
            usuario.registrarZonas(concierto, "Tribuna", 150, 50.0);
        }

        return archivoConcierto.guardarConciertos(conciertos, numConciertos);
    }

    public boolean agregarZona(Usuario usuario, String nombreConcierto, String nombreZona, int capacidad, double precio) {
        if (usuario == null) {
            return false;
        }

        Concierto concierto = buscarConcierto(nombreConcierto);

        if (concierto == null) {
            return false;
        }

        boolean agregada = usuario.registrarZonas(concierto, nombreZona, capacidad, precio);

        if (agregada) {
            return archivoConcierto.guardarConciertos(conciertos, numConciertos);
        }

        return false;
    }

    public boolean eliminarZona(Usuario usuario, String nombreConcierto, String nombreZona) {
        if (usuario == null) {
            return false;
        }

        Concierto concierto = buscarConcierto(nombreConcierto);

        if (concierto == null) {
            return false;
        }

        boolean eliminada = usuario.eliminarZona(concierto, nombreZona);

        if (eliminada) {
            return archivoConcierto.guardarConciertos(conciertos, numConciertos);
        }

        return false;
    }

    public Concierto buscarConcierto(String nombre) {
        if (nombre == null) {
            return null;
        }

        for (int i = 0; i < numConciertos; i++) {
            if (conciertos[i].getNombre().equalsIgnoreCase(nombre)) {
                return conciertos[i];
            }
        }

        return null;
    }

    public Zona buscarZona(String nombreConcierto, String nombreZona) {
        Concierto concierto = buscarConcierto(nombreConcierto);

        if (concierto == null) {
            return null;
        }

        return concierto.buscarZona(nombreZona);
    }

    public boolean guardarConciertos() {
        return archivoConcierto.guardarConciertos(conciertos, numConciertos);
    }

    public Concierto[] listarConciertos() {
        Concierto[] resultado = new Concierto[numConciertos];
        for (int i = 0; i < numConciertos; i++) {
            resultado[i] = conciertos[i];
        }
        return resultado;
    }

    public boolean eliminarConcierto(String nombre) {
        int indice = -1;
        for (int i = 0; i < numConciertos; i++) {
            if (conciertos[i].getNombre().equalsIgnoreCase(nombre)) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            return false;
        }

        for (int i = indice; i < numConciertos - 1; i++) {
            conciertos[i] = conciertos[i + 1];
        }
        conciertos[numConciertos - 1] = null;
        numConciertos--;

        return archivoConcierto.guardarConciertos(conciertos, numConciertos);
    }
}