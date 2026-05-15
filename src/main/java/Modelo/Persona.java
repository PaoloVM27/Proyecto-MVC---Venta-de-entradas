package Modelo;

public abstract class Persona {
    protected String nombres;
    protected String apellidos;
    protected String dni;
    protected String contrasena;
    
    public boolean registrarTarjeta(){
        return true;
    }
    
    public boolean eliminarTarjeta(){
        return true;
    }
    
    public boolean anularVenta(){
        return true;
    }
}   