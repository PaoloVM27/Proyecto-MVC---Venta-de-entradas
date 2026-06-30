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
        this.vistaRegistro.jCheckBox1.addActionListener(e -> toggleContrasena());
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

        String fechaNacText = vistaRegistro.jTextField1.getText().trim();
        if (fechaNacText.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "Ingresa tu fecha de nacimiento.");
            return;
        }

        java.time.LocalDate fechaNac;
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fechaNac = java.time.LocalDate.parse(fechaNacText, formatter);
        } catch (java.time.format.DateTimeParseException e) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "El formato de fecha de nacimiento debe ser dd/mm/aaaa.");
            return;
        }

        java.time.LocalDate ahora = java.time.LocalDate.now();
        int edad = java.time.Period.between(fechaNac, ahora).getYears();
        if (edad < 18) {
            javax.swing.JOptionPane.showMessageDialog(vistaRegistro, "Debes ser mayor de edad (18 años o más) para registrarte.");
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
            vistaRegistro.jTextField1.setText("");
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

    private void toggleContrasena() {
        if (vistaRegistro.jCheckBox1.isSelected()) {
            vistaRegistro.txtContrasena.setEchoChar((char) 0);
            vistaRegistro.txtConfirmarContrasena.setEchoChar((char) 0);
        } else {
            vistaRegistro.txtContrasena.setEchoChar('*');
            vistaRegistro.txtConfirmarContrasena.setEchoChar('*');
        }
    }
}