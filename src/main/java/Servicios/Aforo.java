package Servicios;

import Modelo.Concierto;
import Modelo.Entrada;
import Modelo.Zona;


public class Aforo {

    public Aforo() {
    }

    public int obtenerCapacidadZona(Zona zona) {
        if (zona == null) {
            return 0;
        }

        return zona.getCapacidad();
    }

    public int obtenerEntradasDisponibles(Zona zona) {
        if (zona == null) {
            return 0;
        }

        return zona.contarDisponibles();
    }

    public int obtenerEntradasVendidas(Zona zona) {
        if (zona == null) {
            return 0;
        }

        return zona.contarVendidas();
    }

    public Entrada[] obtenerEntradasZona(Zona zona) {
        if (zona == null) {
            return new Entrada[0];
        }

        return zona.mostrarEntrada();
    }

    public Zona buscarZona(Concierto concierto, String nombreZona) {
        if (concierto == null) {
            return null;
        }

        if (nombreZona == null || nombreZona.isEmpty()) {
            return null;
        }

        return concierto.buscarZona(nombreZona);
    }

    public String obtenerResumenZona(Zona zona) {
        if (zona == null) {
            return "Zona no encontrada.";
        }

        String resumen = "";
        resumen += "Zona: " + zona.getNombre() + "\n";
        resumen += "Capacidad: " + zona.getCapacidad() + "\n";
        resumen += "Precio: " + zona.getPrecio() + "\n";
        resumen += "Disponibles: " + zona.contarDisponibles() + "\n";
        resumen += "Vendidas: " + zona.contarVendidas() + "\n";

        return resumen;
    }

    public String obtenerResumenConcierto(Concierto concierto) {
        if (concierto == null) {
            return "Concierto no encontrado.";
        }

        String resumen = "";
        resumen += "Concierto: " + concierto.getNombre() + "\n";
        resumen += "Fecha: " + concierto.getFecha() + "\n";
        resumen += "Zonas registradas: " + concierto.getZonas().length + "\n";
        resumen += "-----------------------------\n";

        Zona[] zonas = concierto.getZonas();

        for (Zona zona : zonas) {
            resumen += obtenerResumenZona(zona);
            resumen += "-----------------------------\n";
        }

        return resumen;
    }
}