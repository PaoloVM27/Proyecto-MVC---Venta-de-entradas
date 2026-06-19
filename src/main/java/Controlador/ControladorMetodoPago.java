package Controlador;

import Modelo.ArregloCliente;
import Modelo.Cliente;
import Modelo.Tarjeta;
import Servicios.Autenticacion;
import Vista.VistaMenuCliente;
import Vista.VistaMetodoPago;
import javax.swing.JOptionPane;

public class ControladorMetodoPago {
    private VistaMetodoPago vistaMetodoPago;
    private VistaMenuCliente vistaMenu;
    private Autenticacion auth;
    private ArregloCliente arregloCliente;

    public ControladorMetodoPago(VistaMetodoPago vistaMetodoPago, VistaMenuCliente vistaMenu, Autenticacion auth) {
        this.vistaMetodoPago = vistaMetodoPago;
        this.vistaMenu = vistaMenu;
        this.auth = auth;

        this.arregloCliente = new ArregloCliente(
                auth.getClienteActual(),
                auth.getClientes(),
                auth.getNumClientes()
        );

        this.vistaMetodoPago.btnGuardarTarjeta.addActionListener(e -> guardarTarjeta());
        this.vistaMetodoPago.btnEliminarTarjeta.addActionListener(e -> eliminarTarjeta());
        this.vistaMetodoPago.btnVolver.addActionListener(e -> volverMenu());
    }

    public void iniciar() {
        cargarTarjetaGuardada();

        vistaMetodoPago.setLocationRelativeTo(null);
        vistaMetodoPago.setVisible(true);
    }

    private void cargarTarjetaGuardada() {
        Cliente cliente = auth.getClienteActual();

        if (cliente == null) {
            mostrarMensaje("No hay cliente logueado.");
            volverMenu();
            return;
        }

        Tarjeta tarjeta = cliente.getTarjeta();

        if (tarjeta == null) {
            limpiarCampos();
            vistaMetodoPago.lblEstado.setText("Estado: No tienes tarjeta guardada.");
            return;
        }

        vistaMetodoPago.txtNumeroTarjeta.setText(String.valueOf(tarjeta.getNumero()));
        vistaMetodoPago.txtNombreTarjeta.setText(tarjeta.getNombre());
        vistaMetodoPago.txtFechaTarjeta.setText(tarjeta.getFecha());
        vistaMetodoPago.txtCvv.setText(String.valueOf(tarjeta.getCvv()));
        vistaMetodoPago.txtSaldo.setText(String.valueOf(tarjeta.getSaldo()));

        vistaMetodoPago.lblEstado.setText("Estado: Tarjeta guardada.");
    }

    private void guardarTarjeta() {
        try {
            Cliente cliente = auth.getClienteActual();

            if (cliente == null) {
                mostrarMensaje("No hay cliente logueado.");
                return;
            }

            String numeroTexto = vistaMetodoPago.txtNumeroTarjeta.getText().trim();
            String nombre = vistaMetodoPago.txtNombreTarjeta.getText().trim();
            String fecha = vistaMetodoPago.txtFechaTarjeta.getText().trim();
            String cvvTexto = vistaMetodoPago.txtCvv.getText().trim();
            String saldoTexto = vistaMetodoPago.txtSaldo.getText().trim();

            if (numeroTexto.isEmpty() || nombre.isEmpty() || fecha.isEmpty()
                    || cvvTexto.isEmpty() || saldoTexto.isEmpty()) {
                mostrarMensaje("Completa todos los datos de la tarjeta.");
                return;
            }

            int numero = Integer.parseInt(numeroTexto);
            int cvv = Integer.parseInt(cvvTexto);
            double saldo = Double.parseDouble(saldoTexto);

            boolean registrada = arregloCliente.registrarTarjeta(
                    numero,
                    nombre,
                    fecha,
                    cvv,
                    saldo
            );

            if (!registrada) {
                mostrarMensaje("No se pudo guardar la tarjeta. Revisa los datos.");
                return;
            }

            mostrarMensaje("Tarjeta guardada correctamente.");
            cargarTarjetaGuardada();

        } catch (NumberFormatException e) {
            mostrarMensaje("Número de tarjeta, CVV y saldo deben ser valores numéricos.");
        } catch (Exception e) {
            mostrarMensaje(e.getMessage());
        }
    }

    private void eliminarTarjeta() {
        Cliente cliente = auth.getClienteActual();

        if (cliente == null) {
            mostrarMensaje("No hay cliente logueado.");
            return;
        }

        if (cliente.getTarjeta() == null) {
            mostrarMensaje("No tienes tarjeta guardada.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                vistaMetodoPago,
                "¿Seguro que deseas eliminar tu tarjeta guardada?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminada = arregloCliente.eliminarTarjeta();

        if (!eliminada) {
            mostrarMensaje("No se pudo eliminar la tarjeta.");
            return;
        }

        limpiarCampos();
        vistaMetodoPago.lblEstado.setText("Estado: No tienes tarjeta guardada.");
        mostrarMensaje("Tarjeta eliminada correctamente.");
    }

    private void limpiarCampos() {
        vistaMetodoPago.txtNumeroTarjeta.setText("");
        vistaMetodoPago.txtNombreTarjeta.setText("");
        vistaMetodoPago.txtFechaTarjeta.setText("");
        vistaMetodoPago.txtCvv.setText("");
        vistaMetodoPago.txtSaldo.setText("");
    }

    private void volverMenu() {
        vistaMetodoPago.dispose();

        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setVisible(true);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vistaMetodoPago, mensaje);
    }
}