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
    private Servicios.Autenticacion auth;

    private Modelo.ArregloConcierto arregloConcierto;
    private Modelo.ArregloVentas arregloVentas;
    private Modelo.ArregloCliente arregloCliente;

    private Concierto concierto;

    public ControladorCompra(VistaCompra vistaCompra, VistaMenuCliente vistaMenuCliente, Servicios.Autenticacion auth) {
        this.vistaCompra = vistaCompra;
        this.vistaMenuCliente = vistaMenuCliente;
        this.auth = auth;

        this.arregloConcierto = new Modelo.ArregloConcierto();
        this.arregloVentas = new Modelo.ArregloVentas(auth.getClientes(), auth.getNumClientes());
        this.arregloCliente = new Modelo.ArregloCliente(auth.getClienteActual(), auth.getClientes(), auth.getNumClientes());

        this.vistaCompra.btnComprar.addActionListener(e -> comprarEntradas());
        this.vistaCompra.btnVolver.addActionListener(e -> volverMenu());
        this.vistaCompra.cboZona.addActionListener(e -> calcularMonto());
        this.vistaCompra.spnCantidad.addChangeListener(e -> calcularMonto());
    }

    public void iniciar() {
        Concierto[] listaConciertos = arregloConcierto.listarConciertos();
        
        vistaCompra.cboConcierto.removeAllItems();
        for (Concierto c : listaConciertos) {
            if (c != null) {
                vistaCompra.cboConcierto.addItem(c.getNombre());
            }
        }

        vistaCompra.cboConcierto.addActionListener(e -> actualizarConciertoSeleccionado());

        arregloVentas.cargarVentas(listaConciertos, listaConciertos.length);

        cargarTarjetaCliente();

        actualizarConciertoSeleccionado();
        
        vistaCompra.setSize(700, 520);
        vistaCompra.setResizable(false);
        vistaCompra.setLocationRelativeTo(null);
        vistaCompra.setVisible(true);
    }

    private void actualizarConciertoSeleccionado() {
        Object seleccionado = vistaCompra.cboConcierto.getSelectedItem();
        if (seleccionado == null) {
            return;
        }

        String nombreConcierto = seleccionado.toString();
        concierto = arregloConcierto.buscarConcierto(nombreConcierto);

        if (concierto != null) {
            vistaCompra.cboZona.removeAllItems();
            for (Zona z : concierto.getZonas()) {
                if (z != null) {
                    vistaCompra.cboZona.addItem(z.getNombre());
                }
            }
            calcularMonto();
        }
    }

    private void cargarTarjetaCliente() {
        Cliente cliente = auth.getClienteActual();

        if (cliente == null) {
            javax.swing.JOptionPane.showMessageDialog(vistaCompra, "No hay cliente logueado.");
            return;
        }

        Tarjeta tarjeta = cliente.getTarjeta();

        if (tarjeta != null) {
            vistaCompra.txtNumeroTarjeta.setText(String.valueOf(tarjeta.getNumero()));
            vistaCompra.txtNombreTarjeta.setText(tarjeta.getNombre());
            vistaCompra.txtFechaTarjeta.setText(tarjeta.getFecha());
            vistaCompra.txtCvv.setText(String.valueOf(tarjeta.getCvv()));

            vistaCompra.txtNumeroTarjeta.setEditable(false);
            vistaCompra.txtNombreTarjeta.setEditable(false);
            vistaCompra.txtFechaTarjeta.setEditable(false);
            vistaCompra.txtCvv.setEditable(false);
            vistaCompra.txtResumen.setText("Tarjeta cargada correctamente.\nPuedes comprar tus entradas.");
        } else {
            vistaCompra.txtNumeroTarjeta.setEditable(true);
            vistaCompra.txtNombreTarjeta.setEditable(true);
            vistaCompra.txtFechaTarjeta.setEditable(true);
            vistaCompra.txtCvv.setEditable(true);
            vistaCompra.txtResumen.setText("No tienes tarjeta registrada.\nIngresa los datos de tu tarjeta para comprar.");
        }
    }

    private Zona obtenerZonaSeleccionada() {
        Object seleccionado = vistaCompra.cboZona.getSelectedItem();
        if (seleccionado == null) {
            return null;
        }

        String nombreZona = seleccionado.toString();

        if (concierto == null) {
            return null;
        }

        return concierto.buscarZona(nombreZona);
    }

    private void calcularMonto() {
        Zona zona = obtenerZonaSeleccionada();

        if (zona == null) {
            return;
        }

        int cantidad = Integer.parseInt(vistaCompra.spnCantidad.getValue().toString());
        double monto = arregloVentas.calcularMonto(zona, cantidad);

        vistaCompra.lblMonto.setText("Monto: S/ " + monto);

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String resumen = "";
        resumen += "Concierto: " + concierto.getNombre() + "\n";
        resumen += "Fecha: " + sdf.format(concierto.getFecha()) + "\n";
        resumen += "Zona seleccionada: " + zona.getNombre() + "\n";
        resumen += "Precio por entrada: S/ " + zona.getPrecio() + "\n";
        resumen += "Cantidad: " + cantidad + "\n";
        resumen += "Monto total: S/ " + monto + "\n";
        resumen += "Entradas disponibles: " + zona.contarDisponibles() + "\n";

        vistaCompra.txtResumen.setText(resumen);
    }

    private void comprarEntradas() {
        try {
            Cliente cliente = auth.getClienteActual();

            if (cliente == null) {
                javax.swing.JOptionPane.showMessageDialog(vistaCompra, "No hay cliente logueado.");
                return;
            }

            Zona zona = obtenerZonaSeleccionada();

            if (zona == null) {
                javax.swing.JOptionPane.showMessageDialog(vistaCompra, "Zona no encontrada.");
                return;
            }

            if (cliente.getTarjeta() == null) {
                boolean tarjetaRegistrada = registrarTarjetaDesdeVista();

                if (!tarjetaRegistrada) {
                    return;
                }
            }

            int cantidad = Integer.parseInt(vistaCompra.spnCantidad.getValue().toString());

            Venta venta = arregloVentas.comprarEntradas(
                    cliente,
                    concierto,
                    zona,
                    cantidad
            );

            arregloConcierto.guardarConciertos();

            Tarjeta tarjeta = cliente.getTarjeta();

            vistaCompra.txtNumeroTarjeta.setText(String.valueOf(tarjeta.getNumero()));
            vistaCompra.txtNombreTarjeta.setText(tarjeta.getNombre());
            vistaCompra.txtFechaTarjeta.setText(tarjeta.getFecha());
            vistaCompra.txtCvv.setText(String.valueOf(tarjeta.getCvv()));

            vistaCompra.txtNumeroTarjeta.setEditable(false);
            vistaCompra.txtNombreTarjeta.setEditable(false);
            vistaCompra.txtFechaTarjeta.setEditable(false);
            vistaCompra.txtCvv.setEditable(false);

            String resumen = "";
            resumen += "COMPRA REALIZADA CORRECTAMENTE\n";
            resumen += "Cliente: " + cliente.getNombres() + " " + cliente.getApellidos() + "\n";
            resumen += "Concierto: " + venta.getNombreConcierto() + "\n";
            resumen += "Zona: " + venta.getNombreZona() + "\n";
            resumen += "Cantidad de entradas: " + venta.getCantidadEntradas() + "\n";
            resumen += "Monto pagado: S/ " + venta.getMonto() + "\n";
            resumen += "Saldo restante: S/ " + tarjeta.getSaldo() + "\n";
            resumen += "Entradas disponibles en zona: " + zona.contarDisponibles() + "\n";

            vistaCompra.txtResumen.setText(resumen);
            vistaCompra.lblMonto.setText("Monto: S/ " + venta.getMonto());

            javax.swing.JOptionPane.showMessageDialog(vistaCompra, "Compra realizada correctamente.");

        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(vistaCompra, "Revisa los datos numéricos de la tarjeta, CVV o saldo.");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(vistaCompra, e.getMessage());
        }
    }

    private boolean registrarTarjetaDesdeVista() {
        int numero = Integer.parseInt(vistaCompra.txtNumeroTarjeta.getText().trim());
        String nombre = vistaCompra.txtNombreTarjeta.getText().trim();
        String fecha = vistaCompra.txtFechaTarjeta.getText().trim();
        int cvv = Integer.parseInt(vistaCompra.txtCvv.getText().trim());

        boolean registrada = arregloCliente.registrarTarjeta(
                numero,
                nombre,
                fecha,
                cvv,
                0.0
        );

        if (!registrada) {
            javax.swing.JOptionPane.showMessageDialog(vistaCompra, "No se pudo registrar la tarjeta. Revisa los datos.");
            return false;
        }

        return true;
    }

    private void volverMenu() {
        vistaCompra.dispose();

        vistaMenuCliente.setSize(700, 520);
        vistaMenuCliente.setResizable(false);
        vistaMenuCliente.setLocationRelativeTo(null);
        vistaMenuCliente.setVisible(true);
    }
}