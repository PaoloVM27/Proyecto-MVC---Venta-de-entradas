package Controlador;

import Modelo.Cliente;
import Modelo.Usuario;
import Persistencia.ArchivoCliente;
import Persistencia.ArchivoUsuario;
import java.util.List;

public class ControladorAutenticacion {
    private List<Cliente> clientes;
    private List<Usuario> usuarios;
    private Cliente clienteActual;
    private Usuario usuarioActual;
    private ArchivoCliente archivoCliente;
    private ArchivoUsuario archivoUsuario;

    public ControladorAutenticacion() {
        this.archivoCliente = new ArchivoCliente();
        this.archivoUsuario = new ArchivoUsuario();

        this.clientes = archivoCliente.cargarClientes();
        this.usuarios = archivoUsuario.cargarUsuarios();

        this.clienteActual = null;
        this.usuarioActual = null;
    }

    public boolean registrarCliente(String nombres, String apellidos, String dni, String contrasena) {
        if (nombres == null || nombres.isEmpty()) {
            return false;
        }

        if (apellidos == null || apellidos.isEmpty()) {
            return false;
        }

        if (dni == null || dni.isEmpty()) {
            return false;
        }

        if (contrasena == null || contrasena.isEmpty()) {
            return false;
        }

        if (buscarClientePorDni(dni) != null) {
            return false;
        }

        Cliente nuevoCliente = new Cliente(nombres, apellidos, dni, contrasena);
        clientes.add(nuevoCliente);

        boolean guardado = archivoCliente.guardarClientes(clientes);

        if (!guardado) {
            clientes.remove(nuevoCliente);
            return false;
        }

        return true;
    }

    public boolean registrarUsuario(String nombres, String apellidos, String dni, String contrasena) {
        if (nombres == null || nombres.isEmpty()) {
            return false;
        }

        if (apellidos == null || apellidos.isEmpty()) {
            return false;
        }

        if (dni == null || dni.isEmpty()) {
            return false;
        }

        if (contrasena == null || contrasena.isEmpty()) {
            return false;
        }

        if (buscarUsuarioPorDni(dni) != null) {
            return false;
        }

        Usuario nuevoUsuario = new Usuario(nombres, apellidos, dni, contrasena);
        usuarios.add(nuevoUsuario);

        boolean guardado = archivoUsuario.guardarUsuarios(usuarios);

        if (!guardado) {
            usuarios.remove(nuevoUsuario);
            return false;
        }

        return true;
    }

    public boolean iniciarSesionCliente(String dni, String contrasena) {
        Cliente cliente = buscarClientePorDni(dni);

        if (cliente == null) {
            return false;
        }

        if (cliente.ingresar(dni, contrasena)) {
            clienteActual = cliente;
            usuarioActual = null;
            return true;
        }

        return false;
    }

    public boolean iniciarSesionUsuario(String dni, String contrasena) {
        Usuario usuario = buscarUsuarioPorDni(dni);

        if (usuario == null) {
            return false;
        }

        if (usuario.ingresar(dni, contrasena)) {
            usuarioActual = usuario;
            clienteActual = null;
            return true;
        }

        return false;
    }

    public void cerrarSesion() {
        clienteActual = null;
        usuarioActual = null;
    }

    public Cliente buscarClientePorDni(String dni) {
        for (Cliente cliente : clientes) {
            if (cliente.getDni().equals(dni)) {
                return cliente;
            }
        }

        return null;
    }

    public Usuario buscarUsuarioPorDni(String dni) {
        for (Usuario usuario : usuarios) {
            if (usuario.getDni().equals(dni)) {
                return usuario;
            }
        }

        return null;
    }

    public boolean guardarClientes() {
        return archivoCliente.guardarClientes(clientes);
    }

    public boolean guardarUsuarios() {
        return archivoUsuario.guardarUsuarios(usuarios);
    }

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }
}