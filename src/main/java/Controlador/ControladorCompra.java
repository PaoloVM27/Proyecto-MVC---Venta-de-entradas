package Controlador;

import Modelo.Cliente;
import Modelo.Concierto;
import Modelo.Tarjeta;
import Modelo.Venta;
import Modelo.Zona;
import Vista.VistaCompra;
import Vista.VistaMenuCliente;

public class ControladorCompra {
    private VistaCompra vistaCompra;
    private VistaMenuCliente vistaMenuCliente;
    private ControladorAutenticacion auth;

    private ControladorConcierto controladorConcierto;
    private ControladorVenta controladorVenta;
    private ControladorCliente controladorCliente;

    private Concierto concierto;

    public ControladorCompra(VistaCompra vistaCompra, VistaMenuCliente vistaMenuCliente, ControladorAutenticacion auth) {
        this.vistaCompra = vistaCompra;
        this.vistaMenuCliente = vistaMenuCliente;
        this.auth = auth;

        this.controladorConcierto = new ControladorConcierto();
        this.controladorVenta = new ControladorVenta(auth.getClientes());
        this.controladorCliente = new ControladorCliente(auth.getClienteActual(), auth.getClientes());

        this.vistaCompra.agregarEventoCalcularMonto(e -> calcularMonto());
        this.vistaCompra.agregarEventoComprar(e -> comprarEntradas());
        this.vistaCompra.agregarEventoVolver(e -> volverMenu());
    }

    public void iniciar() {
        concierto = controladorConcierto.buscarConcierto("Aniversario UNMSM");

        if (concierto == null) {
            vistaCompra.mostrarMensaje("No se encontró el concierto Aniversario UNMSM.");
            volverMenu();
            return;
        }

        controladorVenta.cargarVentas(controladorConcierto.listarConciertos());

        cargarTarjetaCliente();

        vistaCompra.setLocationRelativeTo(null);
        vistaCompra.setVisible(true);

        calcularMonto();
    }

    private void cargarTarjetaCliente() {
        Cliente cliente = auth.getClienteActual();

        if (cliente == null) {
            vistaCompra.mostrarMensaje("No hay cliente logueado.");
            return;
        }

        Tarjeta tarjeta = cliente.getTarjeta();

        if (tarjeta != null) {
            vistaCompra.mostrarDatosTarjeta(
                    tarjeta.getNumero(),
                    tarjeta.getNombre(),
                    tarjeta.getFecha(),
                    tarjeta.getCvv(),
                    tarjeta.getSaldo()
            );

            vistaCompra.bloquearDatosTarjeta();
            vistaCompra.setResumen("Tarjeta cargada correctamente.\nPuedes comprar tus entradas.");
        } else {
            vistaCompra.habilitarDatosTarjeta();
            vistaCompra.setResumen("No tienes tarjeta registrada.\nIngresa los datos de tu tarjeta para comprar.");
        }
    }

    private Zona obtenerZonaSeleccionada() {
        String nombreZona = vistaCompra.getZonaSeleccionada();

        if (concierto == null) {
            return null;
        }

        return concierto.buscarZona(nombreZona);
    }

    private void calcularMonto() {
        Zona zona = obtenerZonaSeleccionada();

        if (zona == null) {
            vistaCompra.mostrarMensaje("Zona no encontrada.");
            return;
        }

        int cantidad = vistaCompra.getCantidad();
        double monto = controladorVenta.calcularMonto(zona, cantidad);

        vistaCompra.setMonto(monto);

        String resumen = "";
        resumen += "Zona seleccionada: " + zona.getNombre() + "\n";
        resumen += "Precio por entrada: S/ " + zona.getPrecio() + "\n";
        resumen += "Cantidad: " + cantidad + "\n";
        resumen += "Monto total: S/ " + monto + "\n";
        resumen += "Entradas disponibles: " + zona.contarDisponibles() + "\n";

        vistaCompra.setResumen(resumen);
    }

    private void comprarEntradas() {
        try {
            Cliente cliente = auth.getClienteActual();

            if (cliente == null) {
                vistaCompra.mostrarMensaje("No hay cliente logueado.");
                return;
            }

            Zona zona = obtenerZonaSeleccionada();

            if (zona == null) {
                vistaCompra.mostrarMensaje("Zona no encontrada.");
                return;
            }

            if (cliente.getTarjeta() == null) {
                boolean tarjetaRegistrada = registrarTarjetaDesdeVista();

                if (!tarjetaRegistrada) {
                    return;
                }
            }

            int cantidad = vistaCompra.getCantidad();

            Venta venta = controladorVenta.comprarEntradas(
                    cliente,
                    concierto,
                    zona,
                    cantidad
            );

            Tarjeta tarjeta = cliente.getTarjeta();

            vistaCompra.mostrarDatosTarjeta(
                    tarjeta.getNumero(),
                    tarjeta.getNombre(),
                    tarjeta.getFecha(),
                    tarjeta.getCvv(),
                    tarjeta.getSaldo()
            );

            vistaCompra.bloquearDatosTarjeta();

            String resumen = "";
            resumen += "COMPRA REALIZADA CORRECTAMENTE\n";
            resumen += "Cliente: " + cliente.getNombres() + " " + cliente.getApellidos() + "\n";
            resumen += "Concierto: " + venta.getNombreConcierto() + "\n";
            resumen += "Zona: " + venta.getNombreZona() + "\n";
            resumen += "Cantidad de entradas: " + venta.getCantidadEntradas() + "\n";
            resumen += "Monto pagado: S/ " + venta.getMonto() + "\n";
            resumen += "Saldo restante: S/ " + tarjeta.getSaldo() + "\n";
            resumen += "Entradas disponibles en zona: " + zona.contarDisponibles() + "\n";

            vistaCompra.setResumen(resumen);
            vistaCompra.setMonto(venta.getMonto());

            vistaCompra.mostrarMensaje("Compra realizada correctamente.");

        } catch (NumberFormatException e) {
            vistaCompra.mostrarMensaje("Revisa los datos numéricos de la tarjeta, CVV o saldo.");
        } catch (Exception e) {
            vistaCompra.mostrarMensaje(e.getMessage());
        }
    }

    private boolean registrarTarjetaDesdeVista() {
        int numero = vistaCompra.getNumeroTarjeta();
        String nombre = vistaCompra.getNombreTarjeta();
        String fecha = vistaCompra.getFechaTarjeta();
        int cvv = vistaCompra.getCvv();
        double saldo = vistaCompra.getSaldo();

        boolean registrada = controladorCliente.registrarTarjeta(
                numero,
                nombre,
                fecha,
                cvv,
                saldo
        );

        if (!registrada) {
            vistaCompra.mostrarMensaje("No se pudo registrar la tarjeta. Revisa los datos.");
            return false;
        }

        return true;
    }

    private void volverMenu() {
        vistaCompra.dispose();

        vistaMenuCliente.setLocationRelativeTo(null);
        vistaMenuCliente.setVisible(true);
    }
}