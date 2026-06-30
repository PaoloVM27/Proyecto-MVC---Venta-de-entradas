package Controlador;

import Modelo.ArregloConcierto;
import Modelo.Concierto;
import Modelo.Zona;
import Modelo.Entrada;
import Vista.VistaMenuAdmin;
import Vista.VistaMostrarConciertos;
import javax.swing.JOptionPane;

public class ControladorMostrarConciertos {
    private VistaMostrarConciertos vista;
    private VistaMenuAdmin vistaMenuAdmin;
    private Servicios.Autenticacion auth;
    private ArregloConcierto arregloConcierto;
    private Concierto conciertoSeleccionado;
    private Zona zonaSeleccionada;
    private int indiceZonaSeleccionada = -1;

    public ControladorMostrarConciertos(VistaMostrarConciertos vista, VistaMenuAdmin vistaMenuAdmin, Servicios.Autenticacion auth) {
        this.vista = vista;
        this.vistaMenuAdmin = vistaMenuAdmin;
        this.auth = auth;
        this.arregloConcierto = new ArregloConcierto();
    }

    public void iniciar() {
        cargarComboConciertos();

        javax.swing.JButton btnVolver = obtenerBtnVolver();
        if (btnVolver != null) {
            btnVolver.addActionListener(e -> volver());
        }

        javax.swing.JButton btnEliminar = obtenerBtnEliminar();
        if (btnEliminar != null) {
            btnEliminar.addActionListener(e -> eliminarConcierto());
        }

        javax.swing.JButton btnGuardar = obtenerBtnGuardar();
        if (btnGuardar != null) {
            btnGuardar.addActionListener(e -> guardarZona());
        }

        javax.swing.JComboBox<String> combo = obtenerComboConciertos();
        if (combo != null) {
            combo.addActionListener(e -> conciertoSeleccionadoModificado());
        }
        
        javax.swing.JTable tabla = obtenerTablaZonas();
        if (tabla != null) {
            tabla.getSelectionModel().addListSelectionListener(listEvent -> {
                if (!listEvent.getValueIsAdjusting()) {
                    zonaSeleccionadaModificada();
                }
            });

            tabla.addMouseListener(new java.awt.event.MouseAdapter() {
                private int lastSelectedRow = -2;

                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    int row = tabla.rowAtPoint(e.getPoint());
                    if (row == -1) {
                        tabla.clearSelection();
                        lastSelectedRow = -2;
                    } else {
                        if (row == lastSelectedRow) {
                            tabla.clearSelection();
                            lastSelectedRow = -2;
                        } else {
                            lastSelectedRow = row;
                        }
                    }
                }
            });
        }

        conciertoSeleccionadoModificado();

        vista.setSize(700, 520);
        vista.setResizable(false);
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void cargarComboConciertos() {
        javax.swing.JComboBox<String> combo = obtenerComboConciertos();
        if (combo == null) return;

        java.awt.event.ActionListener[] listeners = combo.getActionListeners();
        for (java.awt.event.ActionListener al : listeners) {
            combo.removeActionListener(al);
        }

        combo.removeAllItems();
        Concierto[] conciertos = arregloConcierto.listarConciertos();
        if (conciertos != null) {
            for (Concierto c : conciertos) {
                if (c != null) {
                    combo.addItem(c.getNombre());
                }
            }
        }

        for (java.awt.event.ActionListener al : listeners) {
            combo.addActionListener(al);
        }
    }

    private void conciertoSeleccionadoModificado() {
        javax.swing.JComboBox<String> combo = obtenerComboConciertos();
        if (combo == null) return;

        String nombreConcierto = (combo.getSelectedItem() != null) ? combo.getSelectedItem().toString() : null;
        if (nombreConcierto == null) {
            this.conciertoSeleccionado = null;
            limpiarCamposConcierto();
            limpiarCamposZona();
            cargarTablaZonas();
            return;
        }

        this.conciertoSeleccionado = arregloConcierto.buscarConcierto(nombreConcierto);
        if (this.conciertoSeleccionado != null) {
            javax.swing.JTextField txtNombre = obtenerTxtNombreConcierto();
            javax.swing.JTextField txtFecha = obtenerTxtFechaConcierto();

            if (txtNombre != null) {
                txtNombre.setText(conciertoSeleccionado.getNombre());
                txtNombre.setEditable(false);
            }
            if (txtFecha != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                txtFecha.setText(sdf.format(conciertoSeleccionado.getFecha()));
                txtFecha.setEditable(false);
            }

            cargarTablaZonas();
            limpiarCamposZona();
        } else {
            limpiarCamposConcierto();
            limpiarCamposZona();
            cargarTablaZonas();
        }
    }

    private void cargarTablaZonas() {
        javax.swing.JTable tabla = obtenerTablaZonas();
        if (tabla == null) return;

        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(
            new String[]{"Nombre de Zona", "Precio (S/)", "Capacidad", "Disponibles"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (conciertoSeleccionado != null) {
            Zona[] zonas = conciertoSeleccionado.getZonas();
            if (zonas != null) {
                for (Zona z : zonas) {
                    if (z != null) {
                        modelo.addRow(new Object[]{
                            z.getNombre(),
                            z.getPrecio(),
                            z.getCapacidad(),
                            z.contarDisponibles()
                        });
                    }
                }
            }
        }

        tabla.setModel(modelo);
    }

    private void zonaSeleccionadaModificada() {
        javax.swing.JTable tabla = obtenerTablaZonas();
        if (tabla == null) return;

        int fila = tabla.getSelectedRow();
        this.indiceZonaSeleccionada = fila;

        if (fila == -1 || conciertoSeleccionado == null) {
            this.zonaSeleccionada = null;
            limpiarCamposZona();
            return;
        }

        Zona[] zonas = conciertoSeleccionado.getZonas();
        if (zonas != null && fila < zonas.length) {
            this.zonaSeleccionada = zonas[fila];
            if (this.zonaSeleccionada != null) {
                javax.swing.JTextField txtNombre = obtenerTxtNombreZona();
                javax.swing.JTextField txtPrecio = obtenerTxtPrecioZona();
                javax.swing.JTextField txtCapacidad = obtenerTxtCapacidadZona();

                if (txtNombre != null) txtNombre.setText(zonaSeleccionada.getNombre());
                if (txtPrecio != null) txtPrecio.setText(String.valueOf(zonaSeleccionada.getPrecio()));
                if (txtCapacidad != null) txtCapacidad.setText(String.valueOf(zonaSeleccionada.getCapacidad()));
            }
        }
    }

    private void guardarZona() {
        if (conciertoSeleccionado == null || zonaSeleccionada == null) {
            JOptionPane.showMessageDialog(vista, "Selecciona una zona de la tabla para modificar.");
            return;
        }

        javax.swing.JTextField txtNombre = obtenerTxtNombreZona();
        javax.swing.JTextField txtPrecio = obtenerTxtPrecioZona();
        javax.swing.JTextField txtCapacidad = obtenerTxtCapacidadZona();

        if (txtNombre == null || txtPrecio == null || txtCapacidad == null) {
            return;
        }

        String nuevoNombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String capacidadStr = txtCapacidad.getText().trim();

        if (nuevoNombre.isEmpty() || precioStr.isEmpty() || capacidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Completa todos los campos de la zona.");
            return;
        }

        try {
            double nuevoPrecio = Double.parseDouble(precioStr);
            int nuevaCapacidad = Integer.parseInt(capacidadStr);

            if (nuevoPrecio <= 0 || nuevaCapacidad <= 0) {
                JOptionPane.showMessageDialog(vista, "Precio y capacidad deben ser valores positivos.");
                return;
            }

            Zona[] zonas = conciertoSeleccionado.getZonas();
            if (zonas != null) {
                for (int i = 0; i < zonas.length; i++) {
                    if (i != indiceZonaSeleccionada && zonas[i] != null && zonas[i].getNombre().equalsIgnoreCase(nuevoNombre)) {
                        JOptionPane.showMessageDialog(vista, "Ya existe otra zona con ese nombre en este concierto.");
                        return;
                    }
                }
            }

            try {
                java.lang.reflect.Field fieldNombre = zonaSeleccionada.getClass().getDeclaredField("nombre");
                fieldNombre.setAccessible(true);
                fieldNombre.set(zonaSeleccionada, nuevoNombre);

                java.lang.reflect.Field fieldPrecio = zonaSeleccionada.getClass().getDeclaredField("precio");
                fieldPrecio.setAccessible(true);
                fieldPrecio.set(zonaSeleccionada, nuevoPrecio);

                java.lang.reflect.Field fieldCapacidad = zonaSeleccionada.getClass().getDeclaredField("capacidad");
                fieldCapacidad.setAccessible(true);
                int capacidadAnterior = (int) fieldCapacidad.get(zonaSeleccionada);

                if (nuevaCapacidad != capacidadAnterior) {
                    fieldCapacidad.set(zonaSeleccionada, nuevaCapacidad);
                    java.lang.reflect.Field fieldEntradas = zonaSeleccionada.getClass().getDeclaredField("entradasAsignadas");
                    fieldEntradas.setAccessible(true);

                    Entrada[] nuevasEntradas = new Entrada[nuevaCapacidad];
                    for (int i = 0; i < nuevaCapacidad; i++) {
                        nuevasEntradas[i] = new Entrada(i + 1);
                    }
                    fieldEntradas.set(zonaSeleccionada, nuevasEntradas);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al modificar la zona: " + ex.getMessage());
                return;
            }

            boolean guardado = arregloConcierto.guardarConciertos();
            if (guardado) {
                JOptionPane.showMessageDialog(vista, "Zona modificada con éxito.");

                javax.swing.JTable tabla = obtenerTablaZonas();
                if (tabla != null) {
                    tabla.clearSelection();
                }
                limpiarCamposZona();
                cargarTablaZonas();
            } else {
                JOptionPane.showMessageDialog(vista, "No se pudo guardar la modificación.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Precio y capacidad deben ser valores numéricos.");
        }
    }

    private void eliminarConcierto() {
        if (conciertoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, "No hay ningún concierto seleccionado.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Estas seguro de eliminar este concierto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = arregloConcierto.eliminarConcierto(conciertoSeleccionado.getNombre());
        if (eliminado) {
            JOptionPane.showMessageDialog(vista, "Concierto eliminado correctamente.");

            this.arregloConcierto = new ArregloConcierto();
            cargarComboConciertos();

            limpiarCamposConcierto();
            limpiarCamposZona();
            
            javax.swing.JComboBox<String> combo = obtenerComboConciertos();
            if (combo != null && combo.getItemCount() > 0) {
                combo.setSelectedIndex(0);
            } else {
                this.conciertoSeleccionado = null;
                cargarTablaZonas();
            }
        } else {
            JOptionPane.showMessageDialog(vista, "No se pudo eliminar el concierto.");
        }
    }

    private void volver() {
        vista.dispose();

        vistaMenuAdmin.setSize(700, 520);
        vistaMenuAdmin.setResizable(false);
        vistaMenuAdmin.setLocationRelativeTo(null);
        vistaMenuAdmin.setVisible(true);
    }

    private void limpiarCamposConcierto() {
        javax.swing.JTextField txtNombre = obtenerTxtNombreConcierto();
        javax.swing.JTextField txtFecha = obtenerTxtFechaConcierto();
        if (txtNombre != null) txtNombre.setText("");
        if (txtFecha != null) txtFecha.setText("");
    }

    private void limpiarCamposZona() {
        javax.swing.JTextField txtNombre = obtenerTxtNombreZona();
        javax.swing.JTextField txtPrecio = obtenerTxtPrecioZona();
        javax.swing.JTextField txtCapacidad = obtenerTxtCapacidadZona();
        if (txtNombre != null) txtNombre.setText("");
        if (txtPrecio != null) txtPrecio.setText("");
        if (txtCapacidad != null) txtCapacidad.setText("");
    }

    // Resolutores dinámicos por reflexión y tipo de componentes
    private javax.swing.JComboBox<String> obtenerComboConciertos() {
        for (java.awt.Component comp : vista.getContentPane().getComponents()) {
            if (comp instanceof javax.swing.JComboBox) {
                return (javax.swing.JComboBox<String>) comp;
            }
        }
        return null;
    }

    private javax.swing.JTextField obtenerTxtNombreConcierto() {
        try {
            java.lang.reflect.Field field = vista.getClass().getDeclaredField("jTextField1");
            field.setAccessible(true);
            return (javax.swing.JTextField) field.get(vista);
        } catch (Exception ex) {
            return null;
        }
    }

    private javax.swing.JTextField obtenerTxtFechaConcierto() {
        try {
            java.lang.reflect.Field field = vista.getClass().getDeclaredField("jTextField2");
            field.setAccessible(true);
            return (javax.swing.JTextField) field.get(vista);
        } catch (Exception ex) {
            return null;
        }
    }

    private javax.swing.JTextField obtenerTxtNombreZona() {
        try {
            java.lang.reflect.Field field = vista.getClass().getDeclaredField("jTextField3");
            field.setAccessible(true);
            return (javax.swing.JTextField) field.get(vista);
        } catch (Exception ex) {
            return null;
        }
    }

    private javax.swing.JTextField obtenerTxtPrecioZona() {
        try {
            java.lang.reflect.Field field = vista.getClass().getDeclaredField("jTextField4");
            field.setAccessible(true);
            return (javax.swing.JTextField) field.get(vista);
        } catch (Exception ex) {
            return null;
        }
    }

    private javax.swing.JTextField obtenerTxtCapacidadZona() {
        try {
            java.lang.reflect.Field field = vista.getClass().getDeclaredField("jTextField5");
            field.setAccessible(true);
            return (javax.swing.JTextField) field.get(vista);
        } catch (Exception ex) {
            return null;
        }
    }

    private javax.swing.JTable obtenerTablaZonas() {
        try {
            java.lang.reflect.Field field = vista.getClass().getDeclaredField("jTable1");
            field.setAccessible(true);
            return (javax.swing.JTable) field.get(vista);
        } catch (Exception ex) {
            for (java.awt.Component comp : vista.getContentPane().getComponents()) {
                if (comp instanceof javax.swing.JScrollPane) {
                    javax.swing.JScrollPane sp = (javax.swing.JScrollPane) comp;
                    if (sp.getViewport().getView() instanceof javax.swing.JTable) {
                        return (javax.swing.JTable) sp.getViewport().getView();
                    }
                }
            }
        }
        return null;
    }

    private javax.swing.JButton obtenerBtnGuardar() {
        for (java.awt.Component comp : vista.getContentPane().getComponents()) {
            if (comp instanceof javax.swing.JButton) {
                javax.swing.JButton btn = (javax.swing.JButton) comp;
                if (btn.getText() != null && btn.getText().toLowerCase().contains("guardar")) {
                    return btn;
                }
            }
        }
        try {
            java.lang.reflect.Field field = vista.getClass().getDeclaredField("jButton1");
            field.setAccessible(true);
            return (javax.swing.JButton) field.get(vista);
        } catch (Exception ex) {
            return null;
        }
    }

    private javax.swing.JButton obtenerBtnEliminar() {
        for (java.awt.Component comp : vista.getContentPane().getComponents()) {
            if (comp instanceof javax.swing.JButton) {
                javax.swing.JButton btn = (javax.swing.JButton) comp;
                if (btn.getText() != null && btn.getText().toLowerCase().contains("eliminar")) {
                    return btn;
                }
            }
        }
        try {
            java.lang.reflect.Field field = vista.getClass().getDeclaredField("jButton2");
            field.setAccessible(true);
            return (javax.swing.JButton) field.get(vista);
        } catch (Exception ex) {
            return null;
        }
    }

    private javax.swing.JButton obtenerBtnVolver() {
        for (java.awt.Component comp : vista.getContentPane().getComponents()) {
            if (comp instanceof javax.swing.JButton) {
                javax.swing.JButton btn = (javax.swing.JButton) comp;
                if (btn.getText() != null && btn.getText().toLowerCase().contains("volver")) {
                    return btn;
                }
            }
        }
        try {
            java.lang.reflect.Field field = vista.getClass().getDeclaredField("jButton3");
            field.setAccessible(true);
            return (javax.swing.JButton) field.get(vista);
        } catch (Exception ex) {
            return null;
        }
    }
}
