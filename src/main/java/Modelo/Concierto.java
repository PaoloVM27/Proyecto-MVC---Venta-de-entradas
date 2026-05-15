package Modelo;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Concierto {
    private String nombre;
    private Date fecha;
    private List<Zona> zonas;
    
    public Concierto(){
        this.zonas = new ArrayList<>();
    }
    
    public boolean agregarZona(String nombre){
        Zona nuevaZona = new Zona();
        zonas.add(nuevaZona);
        return true;
    }
    
    public boolean eliminarZona(String nombre){
        return true;
    }
}
