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
    }
    
    public boolean generarEntradas(){
        entradasAsignadas = new Entrada[capacidad];
        for (int i = 0; i < capacidad; i++) {
            Entrada e = new Entrada();
            e.liberar();
            entradasAsignadas[i] = e;
        }
        return true;
    }
    
    public Entrada[] mostrarEntrada(){
        return entradasAsignadas;
    }
    
    public Entrada[] venderEntrada(int numero){
        return new Entrada[0];
    }
    
    public Entrada venderUnaEntrada() {
        if (entradasAsignadas == null) throw new IllegalStateException("Primero debes generar las entradas.");
        
        for (Entrada e : entradasAsignadas) {
            if (e.estaDisponible()) {
                e.vender();
                return e;
            }
        }
        throw new IllegalStateException("Ya no hay entradas disponibles en la zona: " + nombre);
    }
    
    public double getPrecio() {
        return precio;
    }
}
