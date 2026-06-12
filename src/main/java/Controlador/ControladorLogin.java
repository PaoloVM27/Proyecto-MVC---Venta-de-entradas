package Controlador;

import Vista.VistaLogin;

public class ControladorLogin {
    private VistaLogin vista;
    private ControladorAutenticacion auth;

    public ControladorLogin(VistaLogin vista, ControladorAutenticacion auth) {
        this.vista = vista;
        this.auth = auth;

        this.vista.agregarEventoIngresar(e -> iniciarSesion());
        this.vista.agregarEventoRegistrarse(e -> irARegistro());
    }

    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void iniciarSesion() {
        String dni = vista.getDni();
        String contrasena = vista.getContrasena();
        String tipoUsuario = vista.getTipoUsuario();

        if (dni.isEmpty() || contrasena.isEmpty()) {
            vista.mostrarMensaje("Completa el DNI y la contraseña.");
            return;
        }

        if (tipoUsuario.equalsIgnoreCase("Cliente")) {
            iniciarSesionCliente(dni, contrasena);
        } else if (tipoUsuario.equalsIgnoreCase("Administrador")) {
            iniciarSesionAdministrador(dni, contrasena);
        } else {
            vista.mostrarMensaje("Tipo de usuario no válido.");
        }
    }

    private void iniciarSesionCliente(String dni, String contrasena) {
    boolean loginCorrecto = auth.iniciarSesionCliente(dni, contrasena);

    if (loginCorrecto) {
        vista.mostrarMensaje("Bienvenido cliente: " + auth.getClienteActual().getNombres());

        vista.setVisible(false);

        Vista.VistaMenuCliente vistaMenuCliente = new Vista.VistaMenuCliente();

        ControladorMenuCliente controladorMenuCliente = new ControladorMenuCliente(
                vistaMenuCliente,
                vista,
                auth
        );

        controladorMenuCliente.iniciar();

    } else {
        vista.mostrarMensaje("DNI o contraseña de cliente incorrectos.");
        }
    }

    private void iniciarSesionAdministrador(String dni, String contrasena) {
        boolean loginCorrecto = auth.iniciarSesionUsuario(dni, contrasena);

        if (loginCorrecto) {
            vista.mostrarMensaje("Bienvenido administrador: " + auth.getUsuarioActual().getNombres());
            vista.limpiarCampos();

            /*
             * Aquí después abriremos VistaMenuAdmin.
             */

        } else {
            vista.mostrarMensaje("DNI o contraseña de administrador incorrectos.");
        }
    }

    private void irARegistro() {
    vista.setVisible(false);

    Vista.VistaRegistroCliente vistaRegistro = new Vista.VistaRegistroCliente();

    ControladorRegistroCliente controladorRegistro = new ControladorRegistroCliente(
            vistaRegistro,
            vista,
            auth
    );

    controladorRegistro.iniciar();
    }
}