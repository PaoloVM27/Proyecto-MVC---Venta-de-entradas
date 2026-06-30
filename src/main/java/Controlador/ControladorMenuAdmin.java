package Controlador;

import Vista.VistaLogin;
import Vista.VistaMenuAdmin;
import Vista.VistaCrearConcierto;
import Vista.VistaGestionVentas;
import Vista.VistaGestionUsuarios;

public class ControladorMenuAdmin {
    private VistaMenuAdmin vistaMenu;
    private VistaLogin vistaLogin;
    private Servicios.Autenticacion auth;

    public ControladorMenuAdmin(VistaMenuAdmin vistaMenu, VistaLogin vistaLogin, Servicios.Autenticacion auth) {
        this.vistaMenu = vistaMenu;
        this.vistaLogin = vistaLogin;
        this.auth = auth;

        this.vistaMenu.btnGenerarConcierto.addActionListener(e -> irAGenerarConcierto());
        this.vistaMenu.btnGestionVentas.addActionListener(e -> irAGestionVentas());
        this.vistaMenu.btnGestionUsuarios.addActionListener(e -> irAGestionUsuarios());
        this.vistaMenu.btnCerrarSesion.addActionListener(e -> cerrarSesion());

        javax.swing.JButton btnMostrar = obtenerBtnMostrarConciertos();
        if (btnMostrar != null) {
            btnMostrar.addActionListener(e -> irAMostrarConciertos());
        }
    }

    public void iniciar() {
        javax.swing.JButton btnMostrar = obtenerBtnMostrarConciertos();
        if (btnMostrar != null && btnMostrar.getActionListeners().length == 0) {
            btnMostrar.addActionListener(e -> irAMostrarConciertos());
        }

        if (auth.getUsuarioActual() != null) {
            vistaMenu.lblBienvenida.setText("Bienvenido, " + auth.getUsuarioActual().getNombres());
        }

        vistaMenu.setSize(700, 520);
        vistaMenu.setResizable(false);
        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setVisible(true);
    }

    private void irAMostrarConciertos() {
        vistaMenu.setVisible(false);

        try {
            Vista.VistaMostrarConciertos vistaMostrar = new Vista.VistaMostrarConciertos();
            ControladorMostrarConciertos controlador = new ControladorMostrarConciertos(
                    vistaMostrar,
                    vistaMenu,
                    auth
            );
            controlador.iniciar();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(vistaMenu, "Error al abrir la vista de mostrar conciertos: " + ex.getMessage());
            vistaMenu.setVisible(true);
        }
    }

    private void irAGenerarConcierto() {
        vistaMenu.setVisible(false);

        VistaCrearConcierto vistaCrear = new VistaCrearConcierto();
        ControladorCrearConcierto controladorCrear = new ControladorCrearConcierto(
                vistaCrear,
                vistaMenu,
                auth
        );
        controladorCrear.iniciar();
    }

    private void irAGestionVentas() {
        vistaMenu.setVisible(false);

        VistaGestionVentas vistaGestion = new VistaGestionVentas();
        ControladorGestionVentas controlador = new ControladorGestionVentas(
                vistaGestion,
                vistaMenu,
                auth
        );
        controlador.iniciar();
    }

    private void irAGestionUsuarios() {
        vistaMenu.setVisible(false);

        VistaGestionUsuarios vistaGestion = new VistaGestionUsuarios();
        ControladorGestionUsuarios controlador = new ControladorGestionUsuarios(
                vistaGestion,
                vistaMenu,
                auth
        );
        controlador.iniciar();
    }

    private void cerrarSesion() {
        auth.cerrarSesion();
        vistaMenu.dispose();

        vistaLogin.txtDni.setText("");
        vistaLogin.txtContrasena.setText("");
        vistaLogin.setSize(700, 520);
        vistaLogin.setResizable(false);
        vistaLogin.setLocationRelativeTo(null);
        vistaLogin.setVisible(true);
    }

    private javax.swing.JButton obtenerBtnMostrarConciertos() {
        try {
            java.lang.reflect.Field field = vistaMenu.getClass().getDeclaredField("btnMostrarConcierto");
            field.setAccessible(true);
            return (javax.swing.JButton) field.get(vistaMenu);
        } catch (Exception ex1) {
            try {
                java.lang.reflect.Field field = vistaMenu.getClass().getDeclaredField("jButton1");
                field.setAccessible(true);
                return (javax.swing.JButton) field.get(vistaMenu);
            } catch (Exception ex2) {
                for (java.awt.Component comp : vistaMenu.getContentPane().getComponents()) {
                    if (comp instanceof javax.swing.JButton) {
                        javax.swing.JButton btn = (javax.swing.JButton) comp;
                        if (btn != vistaMenu.btnCerrarSesion && btn != vistaMenu.btnGenerarConcierto 
                            && btn != vistaMenu.btnGestionUsuarios && btn != vistaMenu.btnGestionVentas) {
                            return btn;
                        }
                    }
                }
            }
        }
        return null;
    }
}
