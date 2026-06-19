package Controlador;

import Vista.VistaLogin;
import Vista.VistaMenuAdmin;
import Vista.VistaCrearConcierto;

public class ControladorMenuAdmin {
    private VistaMenuAdmin vistaMenu;
    private VistaLogin vistaLogin;
    private Servicios.Autenticacion auth;

    public ControladorMenuAdmin(VistaMenuAdmin vistaMenu, VistaLogin vistaLogin, Servicios.Autenticacion auth) {
        this.vistaMenu = vistaMenu;
        this.vistaLogin = vistaLogin;
        this.auth = auth;

        // Conectar el botón de abrir la ventana de generar conciertos
        this.vistaMenu.btnGenerarConcierto.addActionListener(e -> irAGenerarConcierto());
        
        // Conectar el botón de Cerrar Sesión
        this.vistaMenu.btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    public void iniciar() {
        if (auth.getUsuarioActual() != null) {
            vistaMenu.lblBienvenida.setText("Bienvenido, " + auth.getUsuarioActual().getNombres());
        }

        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setVisible(true);
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

    private void cerrarSesion() {
        auth.cerrarSesion();
        vistaMenu.dispose();

        vistaLogin.txtDni.setText("");
        vistaLogin.txtContrasena.setText("");
        vistaLogin.cboTipoUsuario.setSelectedIndex(0);
        vistaLogin.setLocationRelativeTo(null);
        vistaLogin.setVisible(true);
    }
}
