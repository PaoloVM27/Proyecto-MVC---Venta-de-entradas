package Controlador;

import Vista.VistaLogin;
import Vista.VistaRegistroCliente;

public class ControladorRegistroCliente {
    private VistaRegistroCliente vistaRegistro;
    private VistaLogin vistaLogin;
    private ControladorAutenticacion auth;

    public ControladorRegistroCliente(VistaRegistroCliente vistaRegistro, VistaLogin vistaLogin, ControladorAutenticacion auth) {
        this.vistaRegistro = vistaRegistro;
        this.vistaLogin = vistaLogin;
        this.auth = auth;

        this.vistaRegistro.agregarEventoRegistrar(e -> registrarCliente());
        this.vistaRegistro.agregarEventoVolver(e -> volverLogin());
    }

    public void iniciar() {
        vistaRegistro.setLocationRelativeTo(null);
        vistaRegistro.setVisible(true);
    }

    private void registrarCliente() {
        String nombres = vistaRegistro.getNombres();
        String apellidos = vistaRegistro.getApellidos();
        String dni = vistaRegistro.getDni();
        String contrasena = vistaRegistro.getContrasena();
        String confirmarContrasena = vistaRegistro.getConfirmarContrasena();

        if (nombres.isEmpty()) {
            vistaRegistro.mostrarMensaje("Ingresa tus nombres.");
            return;
        }

        if (apellidos.isEmpty()) {
            vistaRegistro.mostrarMensaje("Ingresa tus apellidos.");
            return;
        }

        if (dni.isEmpty()) {
            vistaRegistro.mostrarMensaje("Ingresa tu DNI.");
            return;
        }

        if (contrasena.isEmpty()) {
            vistaRegistro.mostrarMensaje("Ingresa una contraseña.");
            return;
        }

        if (confirmarContrasena.isEmpty()) {
            vistaRegistro.mostrarMensaje("Confirma tu contraseña.");
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            vistaRegistro.mostrarMensaje("Las contraseñas no coinciden.");
            return;
        }

        boolean registrado = auth.registrarCliente(nombres, apellidos, dni, contrasena);

        if (registrado) {
            vistaRegistro.mostrarMensaje("Cliente registrado correctamente.");
            vistaRegistro.limpiarCampos();
            volverLogin();
        } else {
            vistaRegistro.mostrarMensaje("No se pudo registrar. Puede que el DNI ya exista.");
        }
    }

    private void volverLogin() {
        vistaRegistro.dispose();
        vistaLogin.setLocationRelativeTo(null);
        vistaLogin.setVisible(true);
    }
}