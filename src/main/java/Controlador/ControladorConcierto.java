package Controlador;

import Modelo.Concierto;
import Modelo.Usuario;
import Modelo.Zona;
import Persistencia.ArchivoConcierto;
import java.util.Date;
import java.util.List;

public class ControladorConcierto {
    private List<Concierto> conciertos;
    private ArchivoConcierto archivoConcierto;

    public ControladorConcierto() {
        this.archivoConcierto = new ArchivoConcierto();
        this.conciertos = archivoConcierto.cargarConciertos();
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

        Concierto nuevoConcierto = new Concierto(nombre, fecha);
        conciertos.add(nuevoConcierto);

        return archivoConcierto.guardarConciertos(conciertos);
    }

    public boolean crearConciertoUNMSM(Usuario usuario) {
        if (usuario == null) {
            return false;
        }

        String nombreConcierto = "Aniversario UNMSM";

        Concierto concierto = buscarConcierto(nombreConcierto);

        if (concierto == null) {
            concierto = new Concierto(nombreConcierto, new Date());
            conciertos.add(concierto);
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

        return archivoConcierto.guardarConciertos(conciertos);
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
            return archivoConcierto.guardarConciertos(conciertos);
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
            return archivoConcierto.guardarConciertos(conciertos);
        }

        return false;
    }

    public Concierto buscarConcierto(String nombre) {
        if (nombre == null) {
            return null;
        }

        for (Concierto concierto : conciertos) {
            if (concierto.getNombre().equalsIgnoreCase(nombre)) {
                return concierto;
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
        return archivoConcierto.guardarConciertos(conciertos);
    }

    public List<Concierto> listarConciertos() {
        return conciertos;
    }
}