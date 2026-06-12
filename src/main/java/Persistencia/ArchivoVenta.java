package Persistencia;

import Modelo.Cliente;
import Modelo.Concierto;
import Modelo.Entrada;
import Modelo.Tarjeta;
import Modelo.Venta;
import Modelo.Zona;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArchivoVenta {
    private String ruta;

    public ArchivoVenta() {
        this.ruta = "datos/ventas.txt";
        crearCarpetaDatos();
    }

    private void crearCarpetaDatos() {
        File carpeta = new File("datos");

        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }

    public boolean guardarVentas(List<Cliente> clientes) {
        try {
            PrintWriter escritor = new PrintWriter(new FileWriter(ruta));

            for (Cliente cliente : clientes) {
                for (Venta venta : cliente.getVentas()) {
                    escritor.println(
                            cliente.getDni() + ";" +
                            venta.getNombreConcierto() + ";" +
                            venta.getNombreZona() + ";" +
                            venta.getFecha().getTime() + ";" +
                            venta.getMonto() + ";" +
                            venta.getCantidadEntradas() + ";" +
                            obtenerNumerosEntradas(venta) + ";" +
                            obtenerNumeroTarjeta(venta)
                    );
                }
            }

            escritor.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public List<Venta> cargarVentas(List<Cliente> clientes, List<Concierto> conciertos) {
        List<Venta> ventasCargadas = new ArrayList<>();

        try {
            File archivo = new File(ruta);

            if (!archivo.exists()) {
                return ventasCargadas;
            }

            limpiarVentasClientes(clientes);

            BufferedReader lector = new BufferedReader(new FileReader(ruta));
            String linea;

            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(";");

                if (datos.length == 8) {
                    String dniCliente = datos[0];
                    String nombreConcierto = datos[1];
                    String nombreZona = datos[2];
                    long fechaMillis = Long.parseLong(datos[3]);
                    double monto = Double.parseDouble(datos[4]);
                    String numerosEntradas = datos[6];

                    Cliente cliente = buscarCliente(clientes, dniCliente);
                    Concierto concierto = buscarConcierto(conciertos, nombreConcierto);

                    if (cliente != null && concierto != null) {
                        Zona zona = concierto.buscarZona(nombreZona);

                        if (zona != null) {
                            Venta venta = new Venta();
                            venta.setDniCliente(dniCliente);
                            venta.setNombreConcierto(nombreConcierto);
                            venta.setNombreZona(nombreZona);
                            venta.setFecha(new Date(fechaMillis));
                            venta.setTarjeta(cliente.getTarjeta());

                            String[] numeros = numerosEntradas.split(",");

                            for (String numeroTexto : numeros) {
                                int numeroEntrada = Integer.parseInt(numeroTexto);
                                Entrada entrada = buscarEntrada(zona, numeroEntrada);

                                if (entrada != null && entrada.estaDisponible()) {
                                    entrada.vender();
                                    venta.agregarEntrada(entrada, zona.getPrecio());
                                }
                            }

                            venta.setMonto(monto);

                            if (venta.getCantidadEntradas() > 0) {
                                cliente.agregarVenta(venta);
                                ventasCargadas.add(venta);
                            }
                        }
                    }
                }
            }

            lector.close();

        } catch (Exception e) {
            return ventasCargadas;
        }

        return ventasCargadas;
    }

    private String obtenerNumerosEntradas(Venta venta) {
        String resultado = "";

        for (int i = 0; i < venta.getEntradas().size(); i++) {
            resultado += venta.getEntradas().get(i).getNumero();

            if (i < venta.getEntradas().size() - 1) {
                resultado += ",";
            }
        }

        return resultado;
    }

    private String obtenerNumeroTarjeta(Venta venta) {
        Tarjeta tarjeta = venta.getTarjeta();

        if (tarjeta == null) {
            return "0";
        }

        return String.valueOf(tarjeta.getNumero());
    }

    private void limpiarVentasClientes(List<Cliente> clientes) {
        for (Cliente cliente : clientes) {
            cliente.getVentas().clear();
            cliente.setPuntos(0);
        }
    }

    private Cliente buscarCliente(List<Cliente> clientes, String dni) {
        for (Cliente cliente : clientes) {
            if (cliente.getDni().equals(dni)) {
                return cliente;
            }
        }

        return null;
    }

    private Concierto buscarConcierto(List<Concierto> conciertos, String nombre) {
        for (Concierto concierto : conciertos) {
            if (concierto.getNombre().equalsIgnoreCase(nombre)) {
                return concierto;
            }
        }

        return null;
    }

    private Entrada buscarEntrada(Zona zona, int numero) {
        Entrada[] entradas = zona.mostrarEntrada();

        for (Entrada entrada : entradas) {
            if (entrada.getNumero() == numero) {
                return entrada;
            }
        }

        return null;
    }
}