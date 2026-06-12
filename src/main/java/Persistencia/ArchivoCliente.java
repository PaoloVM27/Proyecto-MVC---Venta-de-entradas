package Persistencia;

import Modelo.Cliente;
import Modelo.Tarjeta;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ArchivoCliente {
    private String ruta;

    public ArchivoCliente() {
        this.ruta = "datos/clientes.txt";
        crearCarpetaDatos();
    }

    private void crearCarpetaDatos() {
        File carpeta = new File("datos");

        if (!carpeta.exists()) {
            carpeta.mkdir();
        }
    }

    public boolean guardarClientes(List<Cliente> clientes) {
        try {
            PrintWriter escritor = new PrintWriter(new FileWriter(ruta));

            for (Cliente cliente : clientes) {
                escritor.print(
                        cliente.getNombres() + ";" +
                        cliente.getApellidos() + ";" +
                        cliente.getDni() + ";" +
                        cliente.getContrasena() + ";" +
                        cliente.getPuntos() + ";"
                );

                Tarjeta tarjeta = cliente.getTarjeta();

                if (tarjeta == null) {
                    escritor.println("false");
                } else {
                    escritor.println(
                            "true;" +
                            tarjeta.getNumero() + ";" +
                            tarjeta.getNombre() + ";" +
                            tarjeta.getFecha() + ";" +
                            tarjeta.getCvv() + ";" +
                            tarjeta.getSaldo()
                    );
                }
            }

            escritor.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public List<Cliente> cargarClientes() {
    List<Cliente> clientes = new ArrayList<>();

    try {
        File archivo = new File(ruta);

        if (!archivo.exists()) {
            return clientes;
        }

        BufferedReader lector = new BufferedReader(new FileReader(ruta));
        String linea;

        while ((linea = lector.readLine()) != null) {
            String[] datos = linea.split(";");

            if (datos.length >= 6) {
                String nombres = datos[0];
                String apellidos = datos[1];
                String dni = datos[2];
                String contrasena = datos[3];
                int puntos = Integer.parseInt(datos[4]);
                boolean tieneTarjeta = Boolean.parseBoolean(datos[5]);

                Cliente cliente = new Cliente(nombres, apellidos, dni, contrasena);
                cliente.setPuntos(puntos);

                if (tieneTarjeta && datos.length == 11) {
                    int numero = Integer.parseInt(datos[6]);
                    String nombreTarjeta = datos[7];
                    String fecha = datos[8];
                    int cvv = Integer.parseInt(datos[9]);
                    double saldo = Double.parseDouble(datos[10]);

                    Tarjeta tarjeta = new Tarjeta(numero, nombreTarjeta, fecha, cvv, saldo);
                    cliente.registrarTarjeta(tarjeta);
                }

                clientes.add(cliente);
            }
        }

        lector.close();

    } catch (Exception e) {
        return clientes;
    }

    return clientes;
    }
}