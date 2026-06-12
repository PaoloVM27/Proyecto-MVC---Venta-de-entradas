package Controlador;

import Vista.VistaLogin;
import Vista.VistaMenuCliente;

public class ControladorMenuCliente {
    private VistaMenuCliente vistaMenu;
    private VistaLogin vistaLogin;
    private ControladorAutenticacion auth;

    public ControladorMenuCliente(VistaMenuCliente vistaMenu, VistaLogin vistaLogin, ControladorAutenticacion auth) {
        this.vistaMenu = vistaMenu;
        this.vistaLogin = vistaLogin;
        this.auth = auth;

        this.vistaMenu.agregarEventoComprar(e -> irAComprar());
        this.vistaMenu.agregarEventoMisEntradas(e -> irAMisEntradas());
        this.vistaMenu.agregarEventoCerrarSesion(e -> cerrarSesion());
    }

    public void iniciar() {
        if (auth.getClienteActual() != null) {
            vistaMenu.setNombreCliente(auth.getClienteActual().getNombres());
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
        vistaMenu.mostrarMensaje("Luego abriremos la vista de mis entradas.");
    }

    private void cerrarSesion() {
        auth.cerrarSesion();

        vistaMenu.dispose();

        vistaLogin.limpiarCampos();
        vistaLogin.setLocationRelativeTo(null);
        vistaLogin.setVisible(true);
    }
}