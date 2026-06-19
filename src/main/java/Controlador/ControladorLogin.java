package Controlador;

import Vista.VistaLogin;

public class ControladorLogin {
    private VistaLogin vista;
    private Servicios.Autenticacion auth;

    public ControladorLogin(VistaLogin vista, Servicios.Autenticacion auth) {
        this.vista = vista;
        this.auth = auth;

        this.vista.btnIngresar.addActionListener(e -> iniciarSesion());
        this.vista.btnRegistrarse.addActionListener(e -> irARegistro());
    }

    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void iniciarSesion() {
        String dni = vista.txtDni.getText().trim();
        String contrasena = new String(vista.txtContrasena.getPassword()).trim();
        String tipoUsuario = vista.cboTipoUsuario.getSelectedItem().toString().trim();

        if (dni.isEmpty() || contrasena.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Completa el DNI y la contraseña.");
            return;
        }

        if (tipoUsuario.equalsIgnoreCase("Cliente")) {
            iniciarSesionCliente(dni, contrasena);
        } else if (tipoUsuario.equalsIgnoreCase("Administrador")) {
            iniciarSesionAdministrador(dni, contrasena);
        } else {
            javax.swing.JOptionPane.showMessageDialog(vista, "Tipo de usuario no válido.");
        }
    }

    private void iniciarSesionCliente(String dni, String contrasena) {
    boolean loginCorrecto = auth.iniciarSesionCliente(dni, contrasena);

    if (loginCorrecto) {
        javax.swing.JOptionPane.showMessageDialog(vista, "Bienvenido cliente: " + auth.getClienteActual().getNombres());

        vista.setVisible(false);

        Vista.VistaMenuCliente vistaMenuCliente = new Vista.VistaMenuCliente();

        ControladorMenuCliente controladorMenuCliente = new ControladorMenuCliente(
                vistaMenuCliente,
                vista,
                auth
        );

        controladorMenuCliente.iniciar();

    } else {
        javax.swing.JOptionPane.showMessageDialog(vista, "DNI o contraseña de cliente incorrectos.");
        }
    }

    private void iniciarSesionAdministrador(String dni, String contrasena) {
        boolean loginCorrecto = auth.iniciarSesionUsuario(dni, contrasena);

        if (loginCorrecto) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Bienvenido administrador: " + auth.getUsuarioActual().getNombres());
            vista.txtDni.setText("");
            vista.txtContrasena.setText("");
            vista.cboTipoUsuario.setSelectedIndex(0);

        } else {
            javax.swing.JOptionPane.showMessageDialog(vista, "DNI o contraseña de administrador incorrectos.");
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