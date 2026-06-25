package Main;

import Servicios.Autenticacion;
import Controlador.ControladorLogin;
import Vista.VistaLogin;

public class Main {
    public static void main(String[] args) {
        Autenticacion auth = new Autenticacion();

        javax.swing.SwingUtilities.invokeLater(() -> {
            VistaLogin vistaLogin = new VistaLogin();
            ControladorLogin controladorLogin = new ControladorLogin(vistaLogin, auth);
            controladorLogin.iniciar();
        });
    }
}