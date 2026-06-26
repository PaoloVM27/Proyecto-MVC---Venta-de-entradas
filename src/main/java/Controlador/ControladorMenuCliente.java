package Controlador;

import Vista.VistaLogin;
import Vista.VistaMenuCliente;
import Vista.VistaMetodoPago;
import Vista.VistaMisEntradas;
import Vista.VistaPerfilCliente;

public class ControladorMenuCliente {
    private VistaMenuCliente vistaMenu;
    private VistaLogin vistaLogin;
    private Servicios.Autenticacion auth;

    public ControladorMenuCliente(VistaMenuCliente vistaMenu, VistaLogin vistaLogin, Servicios.Autenticacion auth) {
        this.vistaMenu = vistaMenu;
        this.vistaLogin = vistaLogin;
        this.auth = auth;

        this.vistaMenu.btnComprar.addActionListener(e -> irAComprar());
        this.vistaMenu.btnMetodoPago.addActionListener(e -> irAMetodoPago());
        this.vistaMenu.btnMisEntradas.addActionListener(e -> irAMisEntradas());
        this.vistaMenu.btnCerrarSesion.addActionListener(e -> cerrarSesion());
        this.vistaMenu.btnPerfilCliente.addActionListener(e -> irAPerfilCliente());
    }

    public void iniciar() {
        if (auth.getClienteActual() != null) {
            vistaMenu.lblBienvenida.setText("Bienvenido, " + auth.getClienteActual().getNombres());
        }

        vistaMenu.setSize(700, 520);
        vistaMenu.setResizable(false);
        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setVisible(true);
    }

   private void irAComprar() {
    vistaMenu.setVisible(false);

    Vista.VistaCompra vistaCompra = new Vista.VistaCompra();

    ControladorCompra controladorCompra = new ControladorCompra(
            vistaCompra,
            vistaMenu,
            auth
    );

    controladorCompra.iniciar();
    }
    private void irAMisEntradas() {
        vistaMenu.setVisible(false);
        VistaMisEntradas vistaMisEntradas = new VistaMisEntradas();
        ControladorMisEntradas controladorMisEntradas = new ControladorMisEntradas(
                vistaMisEntradas,
                vistaMenu,
                auth
        );

        controladorMisEntradas.iniciar();
    }
    private void irAPerfilCliente() {
        vistaMenu.setVisible(false);

        VistaPerfilCliente vistaPerfilCliente = new VistaPerfilCliente();

        ControladorPerfilCliente controladorPerfilCliente = new ControladorPerfilCliente(
                vistaPerfilCliente,
                vistaMenu,
                auth
        );

        controladorPerfilCliente.iniciar();
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
    
    private void irAMetodoPago() {
        vistaMenu.setVisible(false);
        VistaMetodoPago vistaMetodoPago = new VistaMetodoPago();
        ControladorMetodoPago controladorMetodoPago = new ControladorMetodoPago(
                vistaMetodoPago,
                vistaMenu,
                auth
        );
        controladorMetodoPago.iniciar();
        }
    }