package Controlador;

import Vista.VistaLogin;
import Vista.VistaMenuAdmin;

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

        if (dni.isEmpty() || contrasena.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Completa el DNI y la contraseña.");
            return;
        }

        boolean esAdmin = auth.iniciarSesionUsuario(dni, contrasena);
        if (esAdmin) {
            iniciarSesionAdministrador();
            return;
        }

        boolean esCliente = auth.iniciarSesionCliente(dni, contrasena);
        if (esCliente) {
            iniciarSesionCliente();
            return;
        }

        Modelo.Usuario usuarioSuspendido = auth.buscarUsuarioPorDni(dni);
        if (usuarioSuspendido != null && !usuarioSuspendido.isEstado()) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Tu cuenta ha sido suspendida. Contacta al administrador.");
            return;
        }

        Modelo.Cliente clienteSuspendido = auth.buscarClientePorDni(dni);
        if (clienteSuspendido != null && !clienteSuspendido.isEstado()) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Tu cuenta ha sido suspendida. Contacta al administrador.");
            return;
        }

        javax.swing.JOptionPane.showMessageDialog(vista, "DNI o contraseña incorrectos.");
    }

    private void iniciarSesionCliente() {
        javax.swing.JOptionPane.showMessageDialog(vista, "Bienvenido cliente: " + auth.getClienteActual().getNombres());

        vista.setVisible(false);

        Vista.VistaMenuCliente vistaMenuCliente = new Vista.VistaMenuCliente();

        ControladorMenuCliente controladorMenuCliente = new ControladorMenuCliente(
                vistaMenuCliente,
                vista,
                auth
        );

        controladorMenuCliente.iniciar();
    }

    private void iniciarSesionAdministrador() {
        javax.swing.JOptionPane.showMessageDialog(vista, "Bienvenido administrador: " + auth.getUsuarioActual().getNombres());
        
        vista.setVisible(false);

        VistaMenuAdmin vistaMenuAdmin = new VistaMenuAdmin();
        ControladorMenuAdmin controladorMenuAdmin = new ControladorMenuAdmin(
                vistaMenuAdmin,
                vista,
                auth
        );

        controladorMenuAdmin.iniciar();
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