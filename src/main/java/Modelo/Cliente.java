package Modelo;

import java.util.List;
import java.util.ArrayList;

public class Cliente extends Persona {
    private int puntos;
    private Tarjeta tarjeta;
    private List<Venta> ventas;

    public Cliente() {
        super();
        this.puntos = 0;
        this.tarjeta = null;
        this.ventas = new ArrayList<>();
    }

    public Cliente(String nombres, String apellidos, String dni, String contrasena) {
        super(nombres, apellidos, dni, contrasena);
        this.puntos = 0;
        this.tarjeta = null;
        this.ventas = new ArrayList<>();
    }

    public boolean ingresar(String usuario, String clave) {
        return this.dni.equals(usuario) && validarContrasena(clave);
    }

    public boolean registrarTarjeta(Tarjeta tarjeta) {
        if (tarjeta == null) {
            return false;
        }

        if (!tarjeta.validarDatos()) {
            return false;
        }

        this.tarjeta = tarjeta;
        return true;
    }

    @Override
    public boolean registrarTarjeta() {
        return this.tarjeta != null && this.tarjeta.validarDatos();
    }

    @Override
    public boolean eliminarTarjeta() {
        if (this.tarjeta == null) {
            return false;
        }

        this.tarjeta = null;
        return true;
    }

    public boolean agregarVenta(Venta venta) {
        if (venta == null) {
            return false;
        }

        ventas.add(venta);
        puntos = puntos + venta.getCantidadEntradas();

        return true;
    }

    public boolean anularVenta(Venta venta) {
        if (venta == null) {
            return false;
        }

        if (!ventas.contains(venta)) {
            return false;
        }

        int puntosARestar = venta.getCantidadEntradas();

        boolean anulada = venta.anular();

        if (anulada) {
            ventas.remove(venta);
            puntos = puntos - puntosARestar;

            if (puntos < 0) {
                puntos = 0;
            }
        }

        return anulada;
    }

    @Override
    public boolean anularVenta() {
        if (ventas.isEmpty()) {
            return false;
        }

        Venta ultimaVenta = ventas.get(ventas.size() - 1);
        return anularVenta(ultimaVenta);
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        if (puntos < 0) {
            this.puntos = 0;
        } else {
            this.puntos = puntos;
        }
    }

    public Tarjeta getTarjeta() {
        return tarjeta;
    }

    public List<Venta> getVentas() {
        return ventas;
    }
}