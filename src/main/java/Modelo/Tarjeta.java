package Modelo;

public class Tarjeta implements java.io.Serializable {
    private long numero;
    private String nombre;
    private String fecha;
    private int cvv;
    private double saldo;

    public Tarjeta(long numero, String nombre, String fecha, int cvv, double saldo) {
        this.numero = numero;
        this.nombre = nombre;
        this.fecha = fecha;
        this.cvv = cvv;
        this.saldo = saldo;
    }

    public boolean validarDatos() {
        if (numero <= 0) {
            return false;
        }

        if (nombre == null || nombre.isEmpty()) {
            return false;
        }

        if (fecha == null || fecha.isEmpty()) {
            return false;
        }

        if (cvv < 100 || cvv > 9999) {
            return false;
        }

        if (saldo < 0) {
            return false;
        }

        return true;
    }

    public boolean verificarFondo(double monto) {
        if (monto <= 0) {
            return false;
        }
        return true;
    }

    public boolean procesarCobro(double monto) {
        if (!validarDatos()) {
            return false;
        }

        if (!verificarFondo(monto)) {
            return false;
        }

        return true;
    }

    public long getNumero() {
        return numero;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public int getCvv() {
        return cvv;
    }

    public double getSaldo() {
        return saldo;
    }
}