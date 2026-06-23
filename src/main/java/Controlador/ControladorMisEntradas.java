package Controlador;

import Modelo.ArregloConcierto;
import Modelo.ArregloVentas;
import Modelo.Cliente;
import Modelo.Concierto;
import Modelo.Entrada;
import Modelo.Venta;
import Servicios.Autenticacion;
import Vista.VistaMenuCliente;
import Vista.VistaMisEntradas;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControladorMisEntradas {
    private VistaMisEntradas vistaMisEntradas;
    private VistaMenuCliente vistaMenu;
    private Autenticacion auth;

    private ArregloConcierto arregloConcierto;
    private ArregloVentas arregloVentas;

    public ControladorMisEntradas(VistaMisEntradas vistaMisEntradas, VistaMenuCliente vistaMenu, Autenticacion auth) {
        this.vistaMisEntradas = vistaMisEntradas;
        this.vistaMenu = vistaMenu;
        this.auth = auth;

        this.arregloConcierto = new ArregloConcierto();
        this.arregloVentas = new ArregloVentas(auth.getClientes(), auth.getNumClientes());

        this.vistaMisEntradas.btnActualizar.addActionListener(e -> cargarEntradas());
        this.vistaMisEntradas.btnVolver.addActionListener(e -> volverMenu());
    }

    public void iniciar() {
        prepararTabla();
        cargarEntradas();

        vistaMisEntradas.setLocationRelativeTo(null);
        vistaMisEntradas.setVisible(true);
    }

    private void prepararTabla() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        modelo.addColumn("Fecha");
        modelo.addColumn("Concierto");
        modelo.addColumn("Zona");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Entradas");
        modelo.addColumn("Monto");

        vistaMisEntradas.tblEntradas.setModel(modelo);
    }

    private void cargarEntradas() {
        Cliente cliente = auth.getClienteActual();

        if (cliente == null) {
            mostrarMensaje("No hay cliente logueado.");
            volverMenu();
            return;
        }

        Concierto[] conciertos = arregloConcierto.listarConciertos();
        arregloVentas.cargarVentas(conciertos, conciertos.length);

        Venta[] ventas = arregloVentas.listarVentas();

        DefaultTableModel modelo = (DefaultTableModel) vistaMisEntradas.tblEntradas.getModel();
        modelo.setRowCount(0);

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

        double totalGastado = 0.0;
        int cantidadCompras = 0;

        for (int i = 0; i < ventas.length; i++) {
            Venta venta = ventas[i];

            if (venta != null && cliente.getDni().equals(venta.getDniCliente())) {
                Object[] fila = new Object[6];

                fila[0] = formatoFecha.format(venta.getFecha());
                fila[1] = venta.getNombreConcierto();
                fila[2] = venta.getNombreZona();
                fila[3] = venta.getCantidadEntradas();
                fila[4] = obtenerNumerosEntradas(venta);
                fila[5] = "S/ " + String.format("%.2f", venta.getMonto());

                modelo.addRow(fila);

                totalGastado += venta.getMonto();
                cantidadCompras++;
            }
        }

        vistaMisEntradas.lblCantidadCompras.setText("Cantidad de compras: " + cantidadCompras);
        vistaMisEntradas.lblTotalGastado.setText("Total gastado: S/ " + String.format("%.2f", totalGastado));
    }

    private String obtenerNumerosEntradas(Venta venta) {
        Entrada[] entradas = venta.getEntradas();
        String texto = "";

        for (int i = 0; i < entradas.length; i++) {
            texto += entradas[i].getNumero();

            if (i < entradas.length - 1) {
                texto += ", ";
            }
        }

        return texto;
    }

    private void volverMenu() {
        vistaMisEntradas.dispose();

        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setVisible(true);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vistaMisEntradas, mensaje);
    }
}