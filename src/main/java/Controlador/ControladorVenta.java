package Controlador;

import Modelo.Cliente;
import Modelo.Concierto;
import Modelo.Entrada;
import Modelo.Tarjeta;
import Modelo.Venta;
import Modelo.Zona;

import Persistencia.ArchivoCliente;
import Persistencia.ArchivoVenta;

import java.util.ArrayList;
import java.util.List;

public class ControladorVenta {
    private List<Venta> ventas;
    private List<Cliente> clientes;

    private ArchivoVenta archivoVenta;
    private ArchivoCliente archivoCliente;

    public ControladorVenta() {
        this.ventas = new ArrayList<>();
        this.clientes = null;
        this.archivoVenta = new ArchivoVenta();
        this.archivoCliente = new ArchivoCliente();
    }

    public ControladorVenta(List<Cliente> clientes) {
        this.ventas = new ArrayList<>();
        this.clientes = clientes;
        this.archivoVenta = new ArchivoVenta();
        this.archivoCliente = new ArchivoCliente();
    }

    public Venta comprarEntradas(Cliente cliente, Concierto concierto, Zona zona, int cantidad) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }

        if (concierto == null) {
            throw new IllegalArgumentException("El concierto no puede ser nulo.");
        }

        if (zona == null) {
            throw new IllegalArgumentException("La zona no puede ser nula.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }

        if (cantidad > 4) {
            throw new IllegalArgumentException("Solo puedes comprar hasta 4 entradas por venta.");
        }

        Tarjeta tarjeta = cliente.getTarjeta();

        if (tarjeta == null) {
            throw new IllegalStateException("El cliente no tiene una tarjeta registrada.");
        }

        Entrada[] entradasVendidas = zona.venderEntrada(cantidad);

        Venta venta = new Venta();
        venta.setTarjeta(tarjeta);
        venta.setDniCliente(cliente.getDni());
        venta.setNombreConcierto(concierto.getNombre());
        venta.setNombreZona(zona.getNombre());

        for (int i = 0; i < entradasVendidas.length; i++) {
            venta.agregarEntrada(entradasVendidas[i], zona.getPrecio());
        }

        boolean pagoCorrecto = tarjeta.procesarCobro(venta.getMonto());

        if (!pagoCorrecto) {
            venta.anular();
            throw new IllegalStateException("No se pudo procesar el pago.");
        }

        cliente.agregarVenta(venta);
        ventas.add(venta);

        guardarCambios();

        return venta;
    }

    public Venta comprarEntradas(Cliente cliente, Zona zona, int cantidad) {
        throw new IllegalArgumentException("Para persistencia debes enviar también el concierto.");
    }

    public boolean anularVenta(Cliente cliente, Venta venta) {
        if (cliente == null) {
            return false;
        }

        if (venta == null) {
            return false;
        }

        boolean anulada = cliente.anularVenta(venta);

        if (anulada) {
            ventas.remove(venta);
            guardarCambios();
        }

        return anulada;
    }

    public boolean cargarVentas(List<Concierto> conciertos) {
        if (clientes == null) {
            return false;
        }

        ventas = archivoVenta.cargarVentas(clientes, conciertos);
        return true;
    }

    private boolean guardarCambios() {
        if (clientes == null) {
            return true;
        }

        boolean clientesGuardados = archivoCliente.guardarClientes(clientes);
        boolean ventasGuardadas = archivoVenta.guardarVentas(clientes);

        return clientesGuardados && ventasGuardadas;
    }

    public List<Venta> listarVentas() {
        return ventas;
    }

    public double calcularMonto(Zona zona, int cantidad) {
        if (zona == null) {
            return 0.0;
        }

        if (cantidad <= 0) {
            return 0.0;
        }

        if (cantidad > 4) {
            return 0.0;
        }

        return zona.getPrecio() * cantidad;
    }
}