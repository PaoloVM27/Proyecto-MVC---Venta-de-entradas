package Controlador;

import Modelo.ArregloConcierto;
import Vista.VistaMenuAdmin;
import Vista.VistaCrearConcierto;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class ControladorCrearConcierto {
    private VistaCrearConcierto vista;
    private VistaMenuAdmin vistaMenuAdmin;
    private Servicios.Autenticacion auth;
    private ArregloConcierto arregloConcierto;

    public ControladorCrearConcierto(VistaCrearConcierto vista, VistaMenuAdmin vistaMenuAdmin, Servicios.Autenticacion auth) {
        this.vista = vista;
        this.vistaMenuAdmin = vistaMenuAdmin;
        this.auth = auth;
        this.arregloConcierto = new ArregloConcierto();

        this.vista.btnCrear.addActionListener(e -> crearConcierto());
        this.vista.btnVolver.addActionListener(e -> volver());
    }

    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void crearConcierto() {
        String nombre = vista.txtNombreConcierto.getText().trim();
        String fechaTexto = vista.txtFechaConcierto.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingresa el nombre del concierto.");
            return;
        }

        if (fechaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingresa la fecha del concierto.");
            return;
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.setLenient(false);
            Date fecha = format.parse(fechaTexto);

            boolean creado = arregloConcierto.crearConcierto(nombre, fecha);

            if (creado) {
                Modelo.Usuario admin = auth.getUsuarioActual();
                if (admin != null) {
                    arregloConcierto.agregarZona(admin, nombre, "VIP", 50, 200.0);
                    arregloConcierto.agregarZona(admin, nombre, "Preferencial", 100, 150.0);
                    arregloConcierto.agregarZona(admin, nombre, "General", 200, 80.0);
                    arregloConcierto.agregarZona(admin, nombre, "Tribuna", 150, 50.0);
                }
                JOptionPane.showMessageDialog(vista, "Concierto creado con éxito.");
                vista.txtNombreConcierto.setText("");
                vista.txtFechaConcierto.setText("");
            } else {
                JOptionPane.showMessageDialog(vista, "No se pudo crear el concierto. Es posible que ya exista uno con el mismo nombre.");
            }

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(vista, "Formato de fecha inválido. Usa el formato dd/MM/yyyy (ej: 25/12/2026).");
        }
    }

    private void volver() {
        vista.dispose();
        vistaMenuAdmin.setLocationRelativeTo(null);
        vistaMenuAdmin.setVisible(true);
    }
}
