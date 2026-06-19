package Servicios;

import Modelo.Cliente;
import Modelo.Usuario;
import Persistencia.ArchivoCliente;
import Persistencia.ArchivoUsuario;

public class Autenticacion {
    private Cliente[] clientes;
    private int numClientes;
    private Usuario[] usuarios;
    private int numUsuarios;
    
    private Cliente clienteActual;
    private Usuario usuarioActual;
    private ArchivoCliente archivoCliente;
    private ArchivoUsuario archivoUsuario;

    public Autenticacion() {
        this.archivoCliente = new ArchivoCliente();
        this.archivoUsuario = new ArchivoUsuario();

        Cliente[] clientesCargados = archivoCliente.cargarClientes();
        if (clientesCargados != null) {
            this.clientes = new Cliente[Math.max(10, clientesCargados.length * 2)];
            this.numClientes = clientesCargados.length;
            for (int i = 0; i < clientesCargados.length; i++) {
                this.clientes[i] = clientesCargados[i];
            }
        } else {
            this.clientes = new Cliente[10];
            this.numClientes = 0;
        }

        Usuario[] usuariosCargados = archivoUsuario.cargarUsuarios();
        if (usuariosCargados != null) {
            this.usuarios = new Usuario[Math.max(10, usuariosCargados.length * 2)];
            this.numUsuarios = usuariosCargados.length;
            for (int i = 0; i < usuariosCargados.length; i++) {
                this.usuarios[i] = usuariosCargados[i];
            }
        } else {
            this.usuarios = new Usuario[10];
            this.numUsuarios = 0;
        }

        this.clienteActual = null;
        this.usuarioActual = null;
    }
    
    private void redimensionarClientes() {
        Cliente[] nuevoArreglo = new Cliente[clientes.length * 2];
        for (int i = 0; i < numClientes; i++) {
            nuevoArreglo[i] = clientes[i];
        }
        clientes = nuevoArreglo;
    }
    
    private void redimensionarUsuarios() {
        Usuario[] nuevoArreglo = new Usuario[usuarios.length * 2];
        for (int i = 0; i < numUsuarios; i++) {
            nuevoArreglo[i] = usuarios[i];
        }
        usuarios = nuevoArreglo;
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

        if (numClientes >= clientes.length) {
            redimensionarClientes();
        }

        Cliente nuevoCliente = new Cliente(nombres, apellidos, dni, contrasena);
        clientes[numClientes] = nuevoCliente;
        numClientes++;

        boolean guardado = archivoCliente.guardarClientes(clientes, numClientes);

        if (!guardado) {
            clientes[numClientes - 1] = null;
            numClientes--;
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

        if (numUsuarios >= usuarios.length) {
            redimensionarUsuarios();
        }

        Usuario nuevoUsuario = new Usuario(nombres, apellidos, dni, contrasena);
        usuarios[numUsuarios] = nuevoUsuario;
        numUsuarios++;

        boolean guardado = archivoUsuario.guardarUsuarios(usuarios, numUsuarios);

        if (!guardado) {
            usuarios[numUsuarios - 1] = null;
            numUsuarios--;
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
        for (int i = 0; i < numClientes; i++) {
            if (clientes[i].getDni().equals(dni)) {
                return clientes[i];
            }
        }
        return null;
    }

    public Usuario buscarUsuarioPorDni(String dni) {
        for (int i = 0; i < numUsuarios; i++) {
            if (usuarios[i].getDni().equals(dni)) {
                return usuarios[i];
            }
        }
        return null;
    }

    public boolean guardarClientes() {
        return archivoCliente.guardarClientes(clientes, numClientes);
    }

    public boolean guardarUsuarios() {
        return archivoUsuario.guardarUsuarios(usuarios, numUsuarios);
    }

    public Cliente getClienteActual() {
        return clienteActual;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public Cliente[] getClientes() {
        Cliente[] resultado = new Cliente[numClientes];
        for (int i = 0; i < numClientes; i++) {
            resultado[i] = clientes[i];
        }
        return resultado;
    }

    public int getNumClientes() {
        return numClientes;
    }

    public Usuario[] getUsuarios() {
        Usuario[] resultado = new Usuario[numUsuarios];
        for (int i = 0; i < numUsuarios; i++) {
            resultado[i] = usuarios[i];
        }
        return resultado;
    }

    public int getNumUsuarios() {
        return numUsuarios;
    }
}