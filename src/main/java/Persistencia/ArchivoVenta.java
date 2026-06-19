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
import java.util.Date;

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

    public boolean guardarVentas(Venta[] ventas, int numVentas) {
        try {
            PrintWriter escritor = new PrintWriter(new FileWriter(ruta));

            for (int i = 0; i < numVentas; i++) {
                Venta venta = ventas[i];
                if (venta != null) {
                    escritor.println(
                            venta.getDniCliente() + ";" +
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

    public Venta[] cargarVentas(Cliente[] clientes, int numClientes, Concierto[] conciertos, int numConciertos) {
        Venta[] ventasCargadas = new Venta[10];
        int numVentas = 0;

        try {
            File archivo = new File(ruta);

            if (!archivo.exists()) {
                return new Venta[0];
            }

            // No limpiamos los conciertos aquí, ya que limpiar sus arreglos puede ser complejo.
            // Es mejor asumir que se cargan las ventas en conciertos vacíos o manejarlos en el cargador.

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

                    Cliente cliente = buscarCliente(clientes, numClientes, dniCliente);
                    Concierto concierto = buscarConcierto(conciertos, numConciertos, nombreConcierto);

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
                                concierto.agregarVenta(venta);
                                
                                if (numVentas >= ventasCargadas.length) {
                                    Venta[] nuevoArreglo = new Venta[ventasCargadas.length * 2];
                                    for (int i = 0; i < numVentas; i++) {
                                        nuevoArreglo[i] = ventasCargadas[i];
                                    }
                                    ventasCargadas = nuevoArreglo;
                                }
                                
                                ventasCargadas[numVentas] = venta;
                                numVentas++;
                            }
                        }
                    }
                }
            }

            lector.close();

        } catch (Exception e) {
            // Retorna las cargadas hasta el error
        }

        Venta[] resultado = new Venta[numVentas];
        for (int i = 0; i < numVentas; i++) {
            resultado[i] = ventasCargadas[i];
        }
        return resultado;
    }

    private String obtenerNumerosEntradas(Venta venta) {
        String resultado = "";
        
        Entrada[] entradas = venta.getEntradas();
        int numEntradas = venta.getCantidadEntradas();

        for (int i = 0; i < numEntradas; i++) {
            if (entradas[i] != null) {
                resultado += entradas[i].getNumero();

                if (i < numEntradas - 1) {
                    resultado += ",";
                }
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

    private Cliente buscarCliente(Cliente[] clientes, int numClientes, String dni) {
        for (int i = 0; i < numClientes; i++) {
            if (clientes[i].getDni().equals(dni)) {
                return clientes[i];
            }
        }

        return null;
    }

    private Concierto buscarConcierto(Concierto[] conciertos, int numConciertos, String nombre) {
        for (int i = 0; i < numConciertos; i++) {
            if (conciertos[i].getNombre().equalsIgnoreCase(nombre)) {
                return conciertos[i];
            }
        }

        return null;
    }

    private Entrada buscarEntrada(Zona zona, int numero) {
        Entrada[] entradas = zona.mostrarEntrada();

        for (Entrada entrada : entradas) {
            if (entrada != null && entrada.getNumero() == numero) {
                return entrada;
            }
        }

        return null;
    }
}