package Controlador;

import Vista.VistaLogin;
import Vista.VistaRegistroCliente;

public class ControladorRegistroCliente {
    private VistaRegistroCliente vistaRegistro;
    private VistaLogin vistaLogin;
    private Servicios.Autenticacion auth;

    public ControladorRegistroCliente(VistaRegistroCliente vistaRegistro, VistaLogin vistaLogin, Servicios.Autenticacion auth) {
        this.vistaRegistro = vistaRegistro;
        this.vistaLogin = vistaLogin;
        this.auth = auth;

        this.vistaRegistro.btnRegistrar.addActionListener(e -> registrarCliente());
        this.vistaRegistro.btnVolver.addActionListener(e -> volverLogin());
    }

    public void iniciar() {
        vistaRegistro.setSize(700, 520);
        vistaRegistro.setResizable(false);
        vistaRegistro.setLocationRelativeTo(null);
        vistaRegistro.setVisible(true);
    }

    private void registrarCliente() {
        String nombres = vistaRegistro.txtNombres.getText().trim();
        String apellidos = vistaRegistro.txtApellidos.getText().trim();
        String dni = vistaRegistro.txtDni.getText().trim();
        String contrasena = new String(vistaRegistro.txtContrasena.getPassword()).trim();
        String confirmarContrasena = new String(vistaRegistro.txtConfirmarContrasena.getPassword()).trim();

        if (nombres.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "Ingresa tus nombres.");
            return;
        }

        if (apellidos.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "Ingresa tus apellidos.");
            return;
        }

        if (dni.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "Ingresa tu DNI.");
            return;
        }

        if (contrasena.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "Ingresa una contraseña.");
            return;
        }

        if (confirmarContrasena.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "Confirma tu contraseña.");
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "Las contraseñas no coinciden.");
            return;
        }

        boolean registrado = auth.registrarCliente(nombres, apellidos, dni, contrasena);

        if (registrado) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "Cliente registrado correctamente.");
            vistaRegistro.txtNombres.setText("");
            vistaRegistro.txtApellidos.setText("");
            vistaRegistro.txtDni.setText("");
            vistaRegistro.txtContrasena.setText("");
            vistaRegistro.txtConfirmarContrasena.setText("");
            volverLogin();
        } else {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "No se pudo registrar. Puede que el DNI ya exista.");
        }
    }

    private void volverLogin() {
        vistaRegistro.dispose();
        vistaLogin.setSize(700, 520);
        vistaLogin.setResizable(false);
        vistaLogin.setLocationRelativeTo(null);
        vistaLogin.setVisible(true);
    }
}