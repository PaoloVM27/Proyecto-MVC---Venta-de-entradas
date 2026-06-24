package Controlador;

import Modelo.Cliente;
import Servicios.Autenticacion;
import Vista.VistaMenuCliente;
import Vista.VistaPerfilCliente;
import javax.swing.JOptionPane;

public class ControladorPerfilCliente {
    private VistaPerfilCliente vistaPerfil;
    private VistaMenuCliente vistaMenu;
    private Autenticacion auth;

    public ControladorPerfilCliente(VistaPerfilCliente vistaPerfil, VistaMenuCliente vistaMenu, Autenticacion auth) {
        this.vistaPerfil = vistaPerfil;
        this.vistaMenu = vistaMenu;
        this.auth = auth;

        this.vistaPerfil.btnGuardarContrasena.addActionListener(e -> guardarContrasena());
        this.vistaPerfil.btnVolver.addActionListener(e -> volverMenu());
    }

    public void iniciar() {
        cargarDatosCliente();

        vistaPerfil.setLocationRelativeTo(null);
        vistaPerfil.setVisible(true);
    }

    private void cargarDatosCliente() {
        Cliente cliente = auth.getClienteActual();

        if (cliente == null) {
            mostrarMensaje("No hay cliente logueado.");
            volverMenu();
            return;
        }

        vistaPerfil.txtNombres.setText(cliente.getNombres());
        vistaPerfil.txtApellidos.setText(cliente.getApellidos());
        vistaPerfil.txtDni.setText(cliente.getDni());
        vistaPerfil.txtPuntos.setText(String.valueOf(cliente.getPuntos()));

        vistaPerfil.txtNombres.setEditable(false);
        vistaPerfil.txtApellidos.setEditable(false);
        vistaPerfil.txtDni.setEditable(false);
        vistaPerfil.txtPuntos.setEditable(false);

        vistaPerfil.txtContrasenaNueva.setText("");
        vistaPerfil.txtConfirmarContrasena.setText("");
    }

    private void guardarContrasena() {
        Cliente cliente = auth.getClienteActual();

        if (cliente == null) {
            mostrarMensaje("No hay cliente logueado.");
            return;
        }

        String nuevaContrasena = new String(vistaPerfil.txtContrasenaNueva.getPassword()).trim();
        String confirmarContrasena = new String(vistaPerfil.txtConfirmarContrasena.getPassword()).trim();

        if (nuevaContrasena.isEmpty()) {
            mostrarMensaje("Ingresa una nueva contraseña.");
            return;
        }

        if (confirmarContrasena.isEmpty()) {
            mostrarMensaje("Confirma la nueva contraseña.");
            return;
        }

        if (!nuevaContrasena.equals(confirmarContrasena)) {
            mostrarMensaje("Las contraseñas no coinciden.");
            return;
        }

        cliente.setContrasena(nuevaContrasena);

        boolean guardado = auth.guardarClientes();

        if (!guardado) {
            mostrarMensaje("No se pudo guardar la nueva contraseña.");
            return;
        }

        mostrarMensaje("Contraseña actualizada correctamente.");

        vistaPerfil.txtContrasenaNueva.setText("");
        vistaPerfil.txtConfirmarContrasena.setText("");
    }

    private void volverMenu() {
        vistaPerfil.dispose();

        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setVisible(true);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vistaPerfil, mensaje);
    }
}