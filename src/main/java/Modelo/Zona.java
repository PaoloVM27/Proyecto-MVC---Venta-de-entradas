package Modelo;

public class Zona {
    private String nombre;
    private int capacidad;
    private double precio;
    private Entrada[] entradasAsignadas;

    public Zona(String nombre, int capacidad, double precio) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precio = precio;
        this.entradasAsignadas = null;
    }

    public boolean generarEntradas() {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }

        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }

        if (entradasAsignadas != null) {
            return false;
        }

        entradasAsignadas = new Entrada[capacidad];

        for (int i = 0; i < capacidad; i++) {
            entradasAsignadas[i] = new Entrada(i + 1);
        }

        return true;
    }

    public Entrada[] mostrarEntrada() {
        if (entradasAsignadas == null) {
            return new Entrada[0];
        }

        return entradasAsignadas;
    }

    public Entrada[] venderEntrada(int numero) {
        if (entradasAsignadas == null) {
            throw new IllegalStateException("Primero debes generar las entradas.");
        }

        if (numero <= 0) {
            throw new IllegalArgumentException("La cantidad de entradas debe ser mayor que cero.");
        }

        if (numero > contarDisponibles()) {
            throw new IllegalStateException("No hay suficientes entradas disponibles en la zona " + nombre + ".");
        }

        Entrada[] entradasVendidas = new Entrada[numero];
        int contador = 0;

        for (int i = 0; i < entradasAsignadas.length && contador < numero; i++) {
            if (entradasAsignadas[i].estaDisponible()) {
                entradasAsignadas[i].vender();
                entradasVendidas[contador] = entradasAsignadas[i];
                contador++;
            }
        }

        return entradasVendidas;
    }

    public Entrada venderUnaEntrada() {
        Entrada[] entradas = venderEntrada(1);
        return entradas[0];
    }

    public int contarDisponibles() {
        if (entradasAsignadas == null) {
            return 0;
        }

        int contador = 0;

        for (int i = 0; i < entradasAsignadas.length; i++) {
            if (entradasAsignadas[i].estaDisponible()) {
                contador++;
            }
        }

        return contador;
    }

    public int contarVendidas() {
        if (entradasAsignadas == null) {
            return 0;
        }

        int contador = 0;

        for (int i = 0; i < entradasAsignadas.length; i++) {
            if (!entradasAsignadas[i].estaDisponible()) {
                contador++;
            }
        }

        return contador;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getPrecio() {
        return precio;
    }
}