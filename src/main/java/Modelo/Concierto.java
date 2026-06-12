package Modelo;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Concierto {
    private String nombre;
    private Date fecha;
    private List<Zona> zonas;

    public Concierto() {
        this.nombre = "";
        this.fecha = new Date();
        this.zonas = new ArrayList<>();
    }

    public Concierto(String nombre, Date fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.zonas = new ArrayList<>();
    }

    public boolean agregarZona(String nombre, int capacidad, double precio) {
        if (nombre == null || nombre.isEmpty()) {
            return false;
        }

        if (capacidad <= 0) {
            return false;
        }

        if (precio <= 0) {
            return false;
        }

        if (buscarZona(nombre) != null) {
            return false;
        }

        Zona nuevaZona = new Zona(nombre, capacidad, precio);
        nuevaZona.generarEntradas();

        zonas.add(nuevaZona);
        return true;
    }

    public boolean eliminarZona(String nombre) {
        Zona zonaEncontrada = buscarZona(nombre);

        if (zonaEncontrada == null) {
            return false;
        }

        zonas.remove(zonaEncontrada);
        return true;
    }

    public Zona buscarZona(String nombre) {
        if (nombre == null) {
            return null;
        }

        for (Zona zona : zonas) {
            if (zona.getNombre().equalsIgnoreCase(nombre)) {
                return zona;
            }
        }

        return null;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}