package Modelo;

import java.util.List;
import java.util.ArrayList;

public class Cliente extends Persona implements java.io.Serializable {
    private int puntos;
    private Tarjeta tarjeta;
    private boolean estado;

    public Cliente() {
        super();
        this.puntos = 0;
        this.tarjeta = null;
        this.estado = true;
    }

    public Cliente(String nombres, String apellidos, String dni, String contrasena) {
        super(nombres, apellidos, dni, contrasena);
        this.puntos = 0;
        this.tarjeta = null;
        this.estado = true;
    }

    public boolean ingresar(String usuario, String clave) {
        return this.estado && this.dni.equals(usuario) && validarContrasena(clave);
    }

    public boolean activar() {
        this.estado = true;
        return true;
    }

    public boolean desactivar() {
        this.estado = false;
        return true;
    }

    public boolean isEstado() {
        return estado;
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

    public boolean eliminarTarjeta() {
        if (this.tarjeta == null) {
            return false;
        }

        this.tarjeta = null;
        return true;
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
}