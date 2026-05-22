package Modelo;

public class Entrada {
    private int numero;
    private String estado = "Disponible";
    
    public boolean vender(){
        this.estado = "Vendida";
        return true;
    }
    
    public boolean liberar(){
        this.estado = "Disponible";
        return true;
    }
    
    public boolean estaDisponible() {
        return this.estado.equals("Disponible");
    }
}
