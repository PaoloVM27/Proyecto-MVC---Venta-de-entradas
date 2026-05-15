package Modelo;
import java.util.List;
import java.util.ArrayList;

public class Cliente extends Persona{
    private int puntos;
    private Tarjeta tarjeta;
    private List<Venta> ventas;
    
    public Cliente(){
        this.ventas = new ArrayList<>();
    }
    private void ingresar(String usuario, String clave) {
        
    }
}
