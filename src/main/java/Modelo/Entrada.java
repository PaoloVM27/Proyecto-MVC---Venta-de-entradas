package Modelo;

public class Entrada {
    private int numero;
    private String estado;

    public Entrada() {
        this.numero = 0;
        this.estado = "Disponible";
    }

    public Entrada(int numero) {
        this.numero = numero;
        this.estado = "Disponible";
    }

    public boolean vender() {
        if (!estaDisponible()) {
            return false;
        }

        this.estado = "Vendida";
        return true;
    }

    public boolean liberar() {
        if (estaDisponible()) {
            return false;
        }

        this.estado = "Disponible";
        return true;
    }

    public boolean estaDisponible() {
        return this.estado.equals("Disponible");
    }

    public int getNumero() {
        return numero;
    }

    public String getEstado() {
        return estado;
    }
}