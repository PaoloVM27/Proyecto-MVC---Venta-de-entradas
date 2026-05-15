package Modelo;

public class Entrada {
    private int numero;
    private String estado;
    
    public boolean vender(){
        this.estado = "Vendida";
        return true;
    }
    
    public boolean liberar(){
        this.estado = "Disponible";
        return true;
    }
}
