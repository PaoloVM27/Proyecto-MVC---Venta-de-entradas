package Modelo;

import Modelo.Zona;

public class ArregloZonas {
    private Zona[] zonas;
    private int numZonas;

    public ArregloZonas() {
        this.zonas = new Zona[10];
        this.numZonas = 0;
    }

    public boolean agregarZona(String nombre, double precio, int capacidad) {
        if (nombre == null || nombre.isEmpty() || precio <= 0 || capacidad <= 0) {
            return false;
        }

        for (int i = 0; i < numZonas; i++) {
            if (zonas[i] != null && zonas[i].getNombre().equalsIgnoreCase(nombre)) {
                return false;
            }
        }

        if (numZonas >= zonas.length) {
            redimensionar();
        }

        zonas[numZonas++] = new Zona(nombre, capacidad, precio);
        return true;
    }

    private void redimensionar() {
        Zona[] nuevo = new Zona[zonas.length * 2];
        for (int i = 0; i < numZonas; i++) {
            nuevo[i] = zonas[i];
        }
        zonas = nuevo;
    }

    public Zona[] obtenerZonas() {
        Zona[] activas = new Zona[numZonas];
        for (int i = 0; i < numZonas; i++) {
            activas[i] = zonas[i];
        }
        return activas;
    }

    public int getNumZonas() {
        return numZonas;
    }

    public void limpiar() {
        this.zonas = new Zona[10];
        this.numZonas = 0;
    }
}
