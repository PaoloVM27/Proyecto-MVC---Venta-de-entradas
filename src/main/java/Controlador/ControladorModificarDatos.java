package Controlador;

import Modelo.Cliente;
import Modelo.Usuario;
import Servicios.Autenticacion;
import Vista.VistaGestionUsuarios;
import Vista.VistaModificarDatos;

public class ControladorModificarDatos {
    private VistaModificarDatos vista;
    private VistaGestionUsuarios vistaGestion;
    private Autenticacion auth;

    private static final String DNI_SUPER_ADMIN = "11223344";

    private String dniOriginal;
    private String tipoUsuario;

    public ControladorModificarDatos(VistaModificarDatos vista, VistaGestionUsuarios vistaGestion,
            Autenticacion auth, String dniOriginal, String tipoUsuario) {
        this.vista = vista;
        this.vistaGestion = vistaGestion;
        this.auth = auth;
        this.dniOriginal = dniOriginal;
        this.tipoUsuario = tipoUsuario;

        this.vista.btnGuardar.addActionListener(e -> guardar());
        this.vista.btnVolver.addActionListener(e -> volver());
        this.vista.chkMostrarContrasena.addActionListener(e -> toggleContrasena());
    }

    public void iniciar() {
        cargarDatosActuales();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void cargarDatosActuales() {
        if (tipoUsuario.equals("Cliente")) {
            Cliente c = auth.buscarClientePorDni(dniOriginal);
            if (c != null) {
                vista.txtNombres.setText(c.getNombres());
                vista.txtApellidos.setText(c.getApellidos());
                vista.txtDni.setText(c.getDni());
                vista.txtContrasena.setText("");
            }
        } else {
            Usuario u = auth.buscarUsuarioPorDni(dniOriginal);
            if (u != null) {
                vista.txtNombres.setText(u.getNombres());
                vista.txtApellidos.setText(u.getApellidos());
                vista.txtDni.setText(u.getDni());
                vista.txtContrasena.setText("");
            }
        }
    }

    private void toggleContrasena() {
        if (vista.chkMostrarContrasena.isSelected()) {
            vista.txtContrasena.setEchoChar((char) 0);
        } else {
            vista.txtContrasena.setEchoChar('*');
        }
    }

    private void guardar() {
        String nombres = vista.txtNombres.getText().trim();
        String apellidos = vista.txtApellidos.getText().trim();
        String dni = vista.txtDni.getText().trim();
        String contrasena = new String(vista.txtContrasena.getPassword()).trim();

        if (nombres.isEmpty() || apellidos.isEmpty() || dni.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Nombres, apellidos y DNI no pueden estar vacíos.");
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
            javax.swing.JOptionPane.showMessageDialog(vista, "El DNI debe tener exactamente 8 dígitos.");
            return;
        }

        if (tipoUsuario.equals("Cliente")) {
            Cliente c = auth.buscarClientePorDni(dniOriginal);
            if (c != null) {
                c.setNombres(nombres);
                c.setApellidos(apellidos);
                c.setDni(dni);
                if (!contrasena.isEmpty()) {
                    c.setContrasena(contrasena);
                }
                auth.guardarClientes();
            }
        } else {
            Usuario u = auth.buscarUsuarioPorDni(dniOriginal);
            if (u != null) {
                u.setNombres(nombres);
                u.setApellidos(apellidos);
                u.setDni(dni);
                if (!contrasena.isEmpty()) {
                    u.setContrasena(contrasena);
                }
                auth.guardarUsuarios();
            }
        }

        javax.swing.JOptionPane.showMessageDialog(vista, "Datos modificados correctamente.");
        volver();
    }

    private void volver() {
        vista.dispose();
        vistaGestion.setLocationRelativeTo(null);
        vistaGestion.setVisible(true);
    }
}
