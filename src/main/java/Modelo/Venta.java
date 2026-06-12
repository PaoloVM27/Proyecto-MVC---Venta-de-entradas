package Modelo;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Venta {
    private Date fecha;
    private double monto;
    private List<Entrada> entradas;
    private Tarjeta tarjeta;

    // Datos auxiliares para persistencia
    private String dniCliente;
    private String nombreConcierto;
    private String nombreZona;

    public Venta() {
        this.fecha = new Date();
        this.monto = 0.0;
        this.entradas = new ArrayList<>();
        this.tarjeta = null;
        this.dniCliente = "";
        this.nombreConcierto = "";
        this.nombreZona = "";
    }

    public boolean anular() {
        if (entradas.isEmpty()) {
            return false;
        }

        for (Entrada entrada : entradas) {
            entrada.liberar();
        }

        entradas.clear();
        monto = 0.0;

        return true;
    }

    public boolean agregarEntrada(Entrada e, double precio) {
        if (e == null) {
            throw new IllegalArgumentException("La entrada no puede ser nula.");
        }

        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }

        if (entradas.size() >= 4) {
            throw new IllegalArgumentException("No puedes comprar más de 4 entradas por venta.");
        }

        if (entradas.contains(e)) {
            throw new IllegalArgumentException("La entrada ya fue agregada a esta venta.");
        }

        entradas.add(e);
        monto += precio;

        return true;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        if (fecha != null) {
            this.fecha = fecha;
        }
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        if (monto >= 0) {
            this.monto = monto;
        }
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public int getCantidadEntradas() {
        return entradas.size();
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getNombreConcierto() {
        return nombreConcierto;
    }

    public void setNombreConcierto(String nombreConcierto) {
        this.nombreConcierto = nombreConcierto;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }
}