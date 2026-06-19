package Modelo;

import java.util.Date;

public class Concierto implements java.io.Serializable {
    private String nombre;
    private Date fecha;
    
    private Zona[] zonas;
    private int numZonas;
    
    private Venta[] ventas;
    private int numVentas;

    public Concierto() {
        this.nombre = "";
        this.fecha = new Date();
        this.zonas = new Zona[10];
        this.numZonas = 0;
        this.ventas = new Venta[10];
        this.numVentas = 0;
    }

    public Concierto(String nombre, Date fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.zonas = new Zona[10];
        this.numZonas = 0;
        this.ventas = new Venta[10];
        this.numVentas = 0;
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

        if (numZonas >= zonas.length) {
            Zona[] nuevoArreglo = new Zona[zonas.length * 2];
            for (int i = 0; i < numZonas; i++) {
                nuevoArreglo[i] = zonas[i];
            }
            zonas = nuevoArreglo;
        }

        Zona nuevaZona = new Zona(nombre, capacidad, precio);
        nuevaZona.generarEntradas();

        zonas[numZonas] = nuevaZona;
        numZonas++;
        return true;
    }

    public boolean eliminarZona(String nombre) {
        for (int i = 0; i < numZonas; i++) {
            if (zonas[i].getNombre().equalsIgnoreCase(nombre)) {
                // Desplazar elementos a la izquierda
                for (int j = i; j < numZonas - 1; j++) {
                    zonas[j] = zonas[j + 1];
                }
                zonas[numZonas - 1] = null;
                numZonas--;
                return true;
            }
        }
        return false;
    }

    public Zona buscarZona(String nombre) {
        if (nombre == null) {
            return null;
        }

        for (int i = 0; i < numZonas; i++) {
            if (zonas[i].getNombre().equalsIgnoreCase(nombre)) {
                return zonas[i];
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

    public Zona[] getZonas() {
        Zona[] resultado = new Zona[numZonas];
        for (int i = 0; i < numZonas; i++) {
            resultado[i] = zonas[i];
        }
        return resultado;
    }

    public Venta[] getVentas() {
        Venta[] resultado = new Venta[numVentas];
        for (int i = 0; i < numVentas; i++) {
            resultado[i] = ventas[i];
        }
        return resultado;
    }
    
    public int getNumZonas() {
        return numZonas;
    }
    
    public int getNumVentas() {
        return numVentas;
    }

    public boolean agregarVenta(Venta venta) {
        if (venta == null) {
            return false;
        }
        
        if (numVentas >= ventas.length) {
            Venta[] nuevoArreglo = new Venta[ventas.length * 2];
            for (int i = 0; i < numVentas; i++) {
                nuevoArreglo[i] = ventas[i];
            }
            ventas = nuevoArreglo;
        }
        
        ventas[numVentas] = venta;
        numVentas++;
        return true;
    }

    public boolean anularVenta(Venta venta) {
        if (venta == null) {
            return false;
        }
        
        for (int i = 0; i < numVentas; i++) {
            if (ventas[i] == venta) { // Comparación por referencia
                boolean anulada = ventas[i].anular();
                if (anulada) {
                    // Desplazar elementos a la izquierda
                    for (int j = i; j < numVentas - 1; j++) {
                        ventas[j] = ventas[j + 1];
                    }
                    ventas[numVentas - 1] = null;
                    numVentas--;
                    return true;
                }
                return false;
            }
        }
        
        return false;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}