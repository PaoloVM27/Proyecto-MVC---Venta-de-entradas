package Modelo;

public class Tarjeta {
    private int numero;
    private String nombre;
    private String fecha;
    private int cvv;
    private double saldo;
    
    public Tarjeta(int numero, String nombre, String fecha, int cvv, double saldo) {
        this.numero = numero;
        this.nombre = nombre;
        this.fecha = fecha;
        this.cvv = cvv;
        this.saldo = saldo;
    }

    public boolean validarDatos() {
        if (this.cvv > 99 && this.fecha != null && !this.fecha.isEmpty()) {
            System.out.println("Tarjeta válida");
            return true;
        }
        System.out.println("Datos de tarjeta incorrectos");
        return false;
    }
    
    public boolean verificarFondo(double monto) {
        if (this.saldo >= monto) {
            System.out.println("Hay fondos suficientes.");
            return true;
        }
        System.out.println("Saldo insuficiente. Tienes: " + this.saldo + ", y necesitas: " + monto);
        return false;
    }
    
    public boolean procesarCobro(double monto) {
        if (validarDatos() && verificarFondo(monto)) {
            this.saldo = this.saldo - monto;
            System.out.println("Cobro exitoso. Tu nuevo saldo es: " + this.saldo);
            return true;
        }
        System.out.println("El cobro falló");
        return false;
    }
}
