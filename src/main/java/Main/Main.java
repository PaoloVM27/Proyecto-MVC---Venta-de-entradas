package Main;

import Controlador.ControladorAutenticacion;
import Controlador.ControladorLogin;
import Vista.VistaLogin;

public class Main {
    public static void main(String[] args) {
        ControladorAutenticacion auth = new ControladorAutenticacion();

        VistaLogin vistaLogin = new VistaLogin();

        ControladorLogin controladorLogin = new ControladorLogin(vistaLogin, auth);

        controladorLogin.iniciar();
    }
}