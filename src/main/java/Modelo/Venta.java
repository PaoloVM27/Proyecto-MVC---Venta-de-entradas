package Modelo;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Venta {
    private Date fecha;
    private int monto;
    private List<Entrada> entradas;
    
    public Venta(){
        this.entradas = new ArrayList<>();
    }
    
    public boolean anular(){
        return true;
    }
    
    //Método para limitar entradas a 4 por venta
    public boolean agregarEntrada(Entrada e){
        if(entradas.size() < 4){
            entradas.add(e);
            return true;
        }
        return false;
    }
}
