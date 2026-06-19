package Controlador;

import Vista.VistaLogin;
import Vista.VistaMenuCliente;

public class ControladorMenuCliente {
    private VistaMenuCliente vistaMenu;
    private VistaLogin vistaLogin;
    private Servicios.Autenticacion auth;

    public ControladorMenuCliente(VistaMenuCliente vistaMenu, VistaLogin vistaLogin, Servicios.Autenticacion auth) {
        this.vistaMenu = vistaMenu;
        this.vistaLogin = vistaLogin;
        this.auth = auth;

        this.vistaMenu.btnComprar.addActionListener(e -> irAComprar());
        this.vistaMenu.btnMisEntradas.addActionListener(e -> irAMisEntradas());
        this.vistaMenu.btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    public void iniciar() {
        if (auth.getClienteActual() != null) {
            vistaMenu.lblBienvenida.setText("Bienvenido, " + auth.getClienteActual().getNombres());
        }

        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setVisible(true);
    }

   private void irAComprar() {
    vistaMenu.setVisible(false);

    Vista.VistaCompra vistaCompra = new Vista.VistaCompra();

    ControladorCompra controladorCompra = new ControladorCompra(
            vistaCompra,
            vistaMenu,
            auth
    );

    controladorCompra.iniciar();
    }
    private void irAMisEntradas() {
        javax.swing.JOptionPane.showMessageDialog(vistaMenu, "Luego abriremos la vista de mis entradas.");
    }

    private void cerrarSesion() {
        auth.cerrarSesion();

        vistaMenu.dispose();

        vistaLogin.txtDni.setText("");
        vistaLogin.txtContrasena.setText("");
        vistaLogin.setLocationRelativeTo(null);
        vistaLogin.setVisible(true);
    }
}