package Modelo;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Venta {
    private Date fecha;
    private double monto;
    private List<Entrada> entradas;
    
    public Venta(){
        this.entradas = new ArrayList<>();
        this.fecha = new Date();
        this.monto = 0.0;
    }
    
    public boolean anular(){
        return true;
    }
    
    public boolean agregarEntrada(Entrada e, double precio){
        if (entradas.size() >= 4) {
            throw new IllegalArgumentException("No puedes comprar más de 4 entradas por venta");
        }
        entradas.add(e);
        this.monto += precio;
        
        return true;
    }
    
    public double getMonto() {
        return monto;
    }
}
