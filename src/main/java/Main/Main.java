package Main;

import Vista.VistaCompra;
import Controlador.ControladorVenta;

public class Main {
    public static void main(String[] args) {
        VistaCompra vista = new VistaCompra();
        
        ControladorVenta controlador = new ControladorVenta(vista);
        
        controlador.iniciar();
    }
}