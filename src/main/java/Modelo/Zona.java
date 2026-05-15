package Modelo;

public class Zona {
    private String nombre;
    private int capacidad;
    private double precio;
    private Entrada[] entradasAsignadas;
    
    public boolean generarEntradas(){
        entradasAsignadas = new Entrada[capacidad];
        return true;
    }
    
    public Entrada[] mostrarEntrada(){
        return entradasAsignadas;
    }
    
    public Entrada[] venderEntrada(int numero){
        return new Entrada[0];
    }
}
