package Modelo;

public class Usuario extends Persona implements java.io.Serializable {
    private boolean estado;

    public Usuario() {
        super();
        this.estado = true;
    }

    public Usuario(String nombres, String apellidos, String dni, String contrasena) {
        super(nombres, apellidos, dni, contrasena);
        this.estado = true;
    }

    public boolean ingresar(String usuario, String clave) {
        return this.estado && this.dni.equals(usuario) && validarContrasena(clave);
    }

    public void registrarZonas() {
    }

    public boolean registrarZonas(Concierto concierto, String nombreZona, int capacidad, double precio) {
        if (!estado) {
            return false;
        }

        if (concierto == null) {
            return false;
        }

        return concierto.agregarZona(nombreZona, capacidad, precio);
    }

    public boolean eliminarZona(Concierto concierto, String nombreZona) {
        if (!estado) {
            return false;
        }

        if (concierto == null) {
            return false;
        }

        return concierto.eliminarZona(nombreZona);
    }

    public boolean activar() {
        this.estado = true;
        return true;
    }

    public boolean desactivar() {
        this.estado = false;
        return true;
    }

    public boolean isEstado() {
        return estado;
    }
}