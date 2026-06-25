package Controlador;

import Modelo.Cliente;
import Modelo.Usuario;
import Servicios.Autenticacion;
import Vista.VistaGestionUsuarios;
import Vista.VistaMenuAdmin;
import javax.swing.table.DefaultTableModel;

public class ControladorGestionUsuarios {
    private VistaGestionUsuarios vista;
    private VistaMenuAdmin vistaMenuAdmin;
    private Autenticacion auth;

    private static final String DNI_SUPER_ADMIN = "11223344";

    public ControladorGestionUsuarios(VistaGestionUsuarios vista, VistaMenuAdmin vistaMenuAdmin, Autenticacion auth) {
        this.vista = vista;
        this.vistaMenuAdmin = vistaMenuAdmin;
        this.auth = auth;

        this.vista.btnBuscar.addActionListener(e -> buscar());
        this.vista.btnMostrarTodo.addActionListener(e -> cargarUsuarios());
        this.vista.btnSuspender.addActionListener(e -> suspenderUsuario());
        this.vista.btnActivar.addActionListener(e -> activarUsuario());
        this.vista.btnRegistrarAdmin.addActionListener(e -> irARegistrarAdmin());
        this.vista.btnModificarDatos.addActionListener(e -> irAModificarDatos());
        this.vista.btnVolver.addActionListener(e -> volver());

        boolean esSuperAdmin = auth.getUsuarioActual() != null &&
                auth.getUsuarioActual().getDni().equals(DNI_SUPER_ADMIN);

        vista.btnRegistrarAdmin.setEnabled(esSuperAdmin);
        vista.btnModificarDatos.setEnabled(true);
    }

    public void iniciar() {
        configurarTabla();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void configurarTabla() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"DNI", "Nombres", "Apellidos", "Puntos", "Tipo", "Estado"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        vista.tblUsuarios.setModel(model);
    }

    private void cargarUsuarios() {
        mostrarFiltrado("", "Todos");
    }

    private void buscar() {
        String dni = vista.txtFiltroDni.getText().trim();
        String estado = vista.cboEstado.getSelectedItem().toString();
        mostrarFiltrado(dni, estado);
    }

    private void mostrarFiltrado(String filtroDni, String filtroEstado) {
        DefaultTableModel model = (DefaultTableModel) vista.tblUsuarios.getModel();
        model.setRowCount(0);

        for (Cliente c : auth.getClientes()) {
            boolean matchDni = filtroDni.isEmpty() || c.getDni().contains(filtroDni);
            boolean matchEstado = filtroEstado.equals("Todos") || filtroEstado.equals("Activo");
            if (matchDni && matchEstado) {
                model.addRow(new Object[]{
                    c.getDni(),
                    c.getNombres(),
                    c.getApellidos(),
                    c.getPuntos(),
                    "Cliente",
                    c.isEstado() ? "Activo" : "Suspendido"
                });
            }
        }

        for (Usuario u : auth.getUsuarios()) {
            String estadoU = u.isEstado() ? "Activo" : "Suspendido";
            boolean matchDni = filtroDni.isEmpty() || u.getDni().contains(filtroDni);
            boolean matchEstado = filtroEstado.equals("Todos") || filtroEstado.equals(estadoU);
            if (matchDni && matchEstado) {
                model.addRow(new Object[]{
                    u.getDni(),
                    u.getNombres(),
                    u.getApellidos(),
                    "-",
                    "Admin",
                    estadoU
                });
            }
        }
    }

    private void suspenderUsuario() {
        int fila = vista.tblUsuarios.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Selecciona un usuario de la tabla.");
            return;
        }

        String dni = vista.tblUsuarios.getValueAt(fila, 0).toString();
        String tipo = vista.tblUsuarios.getValueAt(fila, 4).toString();

        if (dni.equals(DNI_SUPER_ADMIN)) {
            javax.swing.JOptionPane.showMessageDialog(vista, "No se puede suspender al administrador principal.");
            return;
        }

        if (dni.equals(auth.getUsuarioActual().getDni())) {
            javax.swing.JOptionPane.showMessageDialog(vista, "No puedes suspenderte a ti mismo.");
            return;
        }

        if (tipo.equals("Admin")) {
            Usuario u = auth.buscarUsuarioPorDni(dni);
            if (u != null) {
                u.desactivar();
                auth.guardarUsuarios();
                cargarUsuarios();
                javax.swing.JOptionPane.showMessageDialog(vista, "Administrador suspendido.");
            }
        } else {
            Modelo.Cliente c = auth.buscarClientePorDni(dni);
            if (c != null) {
                c.desactivar();
                auth.guardarClientes();
                cargarUsuarios();
                javax.swing.JOptionPane.showMessageDialog(vista, "Cliente suspendido.");
            }
        }
    }

    private void activarUsuario() {
        int fila = vista.tblUsuarios.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Selecciona un usuario de la tabla.");
            return;
        }

        String dni = vista.tblUsuarios.getValueAt(fila, 0).toString();
        String tipo = vista.tblUsuarios.getValueAt(fila, 4).toString();

        if (dni.equals(DNI_SUPER_ADMIN)) {
            javax.swing.JOptionPane.showMessageDialog(vista, "No se puede modificar al administrador principal.");
            return;
        }

        if (tipo.equals("Admin")) {
            Usuario u = auth.buscarUsuarioPorDni(dni);
            if (u != null) {
                u.activar();
                auth.guardarUsuarios();
                cargarUsuarios();
                javax.swing.JOptionPane.showMessageDialog(vista, "Administrador activado.");
            }
        } else {
            Modelo.Cliente c = auth.buscarClientePorDni(dni);
            if (c != null) {
                c.activar();
                auth.guardarClientes();
                cargarUsuarios();
                javax.swing.JOptionPane.showMessageDialog(vista, "Cliente activado.");
            }
        }
    }

    private void irARegistrarAdmin() {
        vista.setVisible(false);
        Vista.VistaRegistrarAdmin vistaRegistrar = new Vista.VistaRegistrarAdmin();
        ControladorRegistrarAdmin controlador = new ControladorRegistrarAdmin(vistaRegistrar, vista, auth);
        controlador.iniciar();
    }

    private void irAModificarDatos() {
        int fila = vista.tblUsuarios.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Selecciona un usuario de la tabla.");
            return;
        }

        String dni = vista.tblUsuarios.getValueAt(fila, 0).toString();
        String tipo = vista.tblUsuarios.getValueAt(fila, 4).toString();

        if (dni.equals(DNI_SUPER_ADMIN)) {
            javax.swing.JOptionPane.showMessageDialog(vista, "No se pueden modificar los datos del administrador principal.");
            return;
        }

        vista.setVisible(false);
        Vista.VistaModificarDatos vistaModificar = new Vista.VistaModificarDatos();
        ControladorModificarDatos controlador = new ControladorModificarDatos(vistaModificar, vista, auth, dni, tipo);
        controlador.iniciar();
    }

    private void volver() {
        vista.dispose();
        vistaMenuAdmin.setLocationRelativeTo(null);
        vistaMenuAdmin.setVisible(true);
    }
}
