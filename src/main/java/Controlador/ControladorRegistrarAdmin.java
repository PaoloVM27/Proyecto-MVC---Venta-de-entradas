package Controlador;

import Servicios.Autenticacion;
import Vista.VistaGestionUsuarios;
import Vista.VistaRegistrarAdmin;

public class ControladorRegistrarAdmin {
    private VistaRegistrarAdmin vista;
    private VistaGestionUsuarios vistaGestion;
    private Autenticacion auth;

    public ControladorRegistrarAdmin(VistaRegistrarAdmin vista, VistaGestionUsuarios vistaGestion, Autenticacion auth) {
        this.vista = vista;
        this.vistaGestion = vistaGestion;
        this.auth = auth;

        this.vista.btnRegistrar.addActionListener(e -> registrar());
        this.vista.btnVolver.addActionListener(e -> volver());
        this.vista.chkMostrarContrasena.addActionListener(e -> toggleContrasena());
    }

    public void iniciar() {
        vista.setSize(700, 520);
        vista.setResizable(false);
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void toggleContrasena() {
        if (vista.chkMostrarContrasena.isSelected()) {
            vista.txtContrasena.setEchoChar((char) 0);
        } else {
            vista.txtContrasena.setEchoChar('*');
        }
    }

    private void registrar() {
        String nombres = vista.txtNombres.getText().trim();
        String apellidos = vista.txtApellidos.getText().trim();
        String dni = vista.txtDni.getText().trim();
        String contrasena = new String(vista.txtContrasena.getPassword()).trim();

        if (nombres.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || contrasena.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Completa todos los campos.");
            return;
        }

        if (!nombres.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Nombres no debe contener números ni caracteres especiales.");
            return;
        }

        if (!apellidos.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Apellidos no debe contener números ni caracteres especiales.");
            return;
        }

        if (!dni.matches("\\d{8}")) {
            javax.swing.JOptionPane.showMessageDialog(vista, "El DNI debe tener exactamente 8 dígitos numéricos.");
            return;
        }

        boolean registrado = auth.registrarUsuario(nombres, apellidos, dni, contrasena);

        if (registrado) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Administrador registrado correctamente.");
            limpiarCampos();
        } else {
            javax.swing.JOptionPane.showMessageDialog(vista, "No se pudo registrar. El DNI ya existe.");
        }
    }

    private void limpiarCampos() {
        vista.txtNombres.setText("");
        vista.txtApellidos.setText("");
        vista.txtDni.setText("");
        vista.txtContrasena.setText("");
        vista.chkMostrarContrasena.setSelected(false);
        vista.txtContrasena.setEchoChar('*');
    }

    private void volver() {
        vista.dispose();
        vistaGestion.setSize(700, 520);
        vistaGestion.setResizable(false);
        vistaGestion.setLocationRelativeTo(null);
        vistaGestion.setVisible(true);
    }
}
