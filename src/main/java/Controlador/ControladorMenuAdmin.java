package Controlador;

import Vista.VistaLogin;
import Vista.VistaMenuAdmin;
import Vista.VistaCrearConcierto;
import Vista.VistaGestionVentas;

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

    private void cerrarSesion() {
        auth.cerrarSesion();
        vistaMenu.dispose();

        vistaLogin.txtDni.setText("");
        vistaLogin.txtContrasena.setText("");
        vistaLogin.setLocationRelativeTo(null);
        vistaLogin.setVisible(true);
    }
}
