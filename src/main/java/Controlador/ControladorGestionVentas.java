package Controlador;

import Modelo.ArregloConcierto;
import Modelo.ArregloVentas;
import Modelo.Concierto;
import Modelo.Venta;
import Servicios.Autenticacion;
import Vista.VistaGestionVentas;
import Vista.VistaMenuAdmin;
import javax.swing.table.DefaultTableModel;

public class ControladorGestionVentas {
    private VistaGestionVentas vista;
    private VistaMenuAdmin vistaMenuAdmin;
    private Autenticacion auth;

    private ArregloConcierto arregloConcierto;
    private ArregloVentas arregloVentas;

    public ControladorGestionVentas(VistaGestionVentas vista, VistaMenuAdmin vistaMenuAdmin, Autenticacion auth) {
        this.vista = vista;
        this.vistaMenuAdmin = vistaMenuAdmin;
        this.auth = auth;

        this.arregloConcierto = new ArregloConcierto();
        this.arregloVentas = new ArregloVentas(auth.getClientes(), auth.getNumClientes());

        this.vista.btnBuscar.addActionListener(e -> buscar());
        this.vista.btnMostrarTodo.addActionListener(e -> cargarTodasLasVentas());
        this.vista.btnAnularVenta.addActionListener(e -> anularVenta());
        this.vista.btnVolver.addActionListener(e -> volver());
    }

    public void iniciar() {
        Concierto[] conciertos = arregloConcierto.listarConciertos();
        arregloVentas.cargarVentas(conciertos, conciertos.length);
        configurarTabla();
        vista.setSize(700, 520);
        vista.setResizable(false);
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void configurarTabla() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"DNI Cliente", "Concierto", "Zona", "Entradas", "Monto (S/)"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        vista.tblVentas.setModel(model);
    }

    private void cargarTodasLasVentas() {
        String filtroDni = "";
        String filtroConcierto = "";
        mostrarVentasFiltradas(filtroDni, filtroConcierto);
    }

    private void buscar() {
        String filtroDni = vista.txtFiltroNombre.getText().trim().toLowerCase();
        String filtroConcierto = vista.txtFiltroConcierto.getText().trim().toLowerCase();
        mostrarVentasFiltradas(filtroDni, filtroConcierto);
    }

    private void mostrarVentasFiltradas(String filtroDni, String filtroConcierto) {
        DefaultTableModel model = (DefaultTableModel) vista.tblVentas.getModel();
        model.setRowCount(0);

        double total = 0;
        for (Concierto c : arregloConcierto.listarConciertos()) {
            for (Venta v : c.getVentas()) {
                boolean matchDni = filtroDni.isEmpty() || v.getDniCliente().toLowerCase().contains(filtroDni);
                boolean matchConcierto = filtroConcierto.isEmpty() || v.getNombreConcierto().toLowerCase().contains(filtroConcierto);
                if (matchDni && matchConcierto) {
                    model.addRow(new Object[]{
                        v.getDniCliente(),
                        v.getNombreConcierto(),
                        v.getNombreZona(),
                        v.getCantidadEntradas(),
                        v.getMonto()
                    });
                    total += v.getMonto();
                }
            }
        }
        vista.lblRecaudacion.setText(String.valueOf(total));
    }

    private void anularVenta() {
        int fila = vista.tblVentas.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Selecciona una venta de la tabla.");
            return;
        }

        String dniCliente = vista.tblVentas.getValueAt(fila, 0).toString();
        String nombreConcierto = vista.tblVentas.getValueAt(fila, 1).toString();

        Modelo.Cliente cliente = auth.buscarClientePorDni(dniCliente);
        Concierto concierto = arregloConcierto.buscarConcierto(nombreConcierto);

        if (cliente == null || concierto == null) {
            javax.swing.JOptionPane.showMessageDialog(vista, "No se encontró el cliente o el concierto.");
            return;
        }

        Venta ventaAAnular = null;
        for (Venta v : concierto.getVentas()) {
            if (v.getDniCliente().equals(dniCliente)) {
                ventaAAnular = v;
                break;
            }
        }

        if (ventaAAnular == null) {
            javax.swing.JOptionPane.showMessageDialog(vista, "No se encontró la venta.");
            return;
        }

        boolean anulada = arregloVentas.anularVenta(concierto, cliente, ventaAAnular);
        if (anulada) {
            arregloConcierto.guardarConciertos();
            javax.swing.JOptionPane.showMessageDialog(vista, "Venta anulada correctamente.");
            cargarTodasLasVentas();
        } else {
            javax.swing.JOptionPane.showMessageDialog(vista, "No se pudo anular la venta.");
        }
    }

    private void volver() {
        vista.dispose();
        vistaMenuAdmin.setSize(700, 520);
        vistaMenuAdmin.setResizable(false);
        vistaMenuAdmin.setLocationRelativeTo(null);
        vistaMenuAdmin.setVisible(true);
    }
}
