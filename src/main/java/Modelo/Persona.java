package Modelo;

public abstract class Persona {
    protected String nombres;
    protected String apellidos;
    protected String dni;
    protected String contrasena;

    public Persona() {
        this.nombres = "";
        this.apellidos = "";
        this.dni = "";
        this.contrasena = "";
    }

    public Persona(String nombres, String apellidos, String dni, String contrasena) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.contrasena = contrasena;
    }

    public boolean registrarTarjeta() {
        return false;
    }

    public boolean eliminarTarjeta() {
        return false;
    }

    public boolean anularVenta() {
        return false;
    }

    public boolean validarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}