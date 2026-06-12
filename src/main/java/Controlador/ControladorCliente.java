package Controlador;

import Modelo.Cliente;
import Modelo.Tarjeta;
import Modelo.Venta;
import Persistencia.ArchivoCliente;
import java.util.List;

public class ControladorCliente {
    private Cliente clienteActual;
    private List<Cliente> clientes;
    private ArchivoCliente archivoCliente;

    public ControladorCliente() {
        this.clienteActual = null;
        this.clientes = null;
        this.archivoCliente = new ArchivoCliente();
    }

    public ControladorCliente(Cliente clienteActual) {
        this.clienteActual = clienteActual;
        this.clientes = null;
        this.archivoCliente = new ArchivoCliente();
    }

    public ControladorCliente(Cliente clienteActual, List<Cliente> clientes) {
        this.clienteActual = clienteActual;
        this.clientes = clientes;
        this.archivoCliente = new ArchivoCliente();
    }

    public boolean asignarClienteActual(Cliente cliente) {
        if (cliente == null) {
            return false;
        }

        this.clienteActual = cliente;
        return true;
    }

    public boolean registrarTarjeta(int numero, String nombre, String fecha, int cvv, double saldo) {
        if (clienteActual == null) {
            return false;
        }

        Tarjeta tarjeta = new Tarjeta(numero, nombre, fecha, cvv, saldo);

        boolean registrada = clienteActual.registrarTarjeta(tarjeta);

        if (registrada) {
            guardarCambios();
        }

        return registrada;
    }

    public boolean eliminarTarjeta() {
        if (clienteActual == null) {
            return false;
        }

        boolean eliminada = clienteActual.eliminarTarjeta();

        if (eliminada) {
            guardarCambios();
        }

        return eliminada;
    }

    private boolean guardarCambios() {
        if (clientes == null) {
            return true;
        }

        return archivoCliente.guardarClientes(clientes);
    }

    public Tarjeta obtenerTarjeta() {
        if (clienteActual == null) {
            return null;
        }

        return clienteActual.getTarjeta();
    }

    public boolean tieneTarjeta() {
        if (clienteActual == null) {
            return false;
        }

        return clienteActual.getTarjeta() != null;
    }

    public List<Venta> obtenerVentas() {
        if (clienteActual == null) {
            return null;
        }

        return clienteActual.getVentas();
    }

    public boolean anularVenta(Venta venta) {
        if (clienteActual == null) {
            return false;
        }

        boolean anulada = clienteActual.anularVenta(venta);

        if (anulada) {
            guardarCambios();
        }

        return anulada;
    }

    public int obtenerPuntos() {
        if (clienteActual == null) {
            return 0;
        }

        return clienteActual.getPuntos();
    }

    public String obtenerNombreCompleto() {
        if (clienteActual == null) {
            return "";
        }

        return clienteActual.getNombres() + " " + clienteActual.getApellidos();
    }

    public Cliente getClienteActual() {
        return clienteActual;
    }
}