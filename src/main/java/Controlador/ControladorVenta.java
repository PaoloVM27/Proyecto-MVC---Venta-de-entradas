package Controlador;

import Modelo.*;
import Vista.VistaCompra;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorVenta implements ActionListener {
    
    private VistaCompra vista;
    private Zona zonaVip;
    private Tarjeta tarjetaCliente;

    public ControladorVenta(VistaCompra vista) {
        this.vista = vista;
        
        this.zonaVip = new Zona("VIP", 50, 200.00);
        this.zonaVip.generarEntradas();
        
        this.tarjetaCliente = new Tarjeta(1234, "Juan Perez", "12/25", 123, 500.00);
        
        this.vista.btnComprar.addActionListener(this);
    }

    public void iniciar() {
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        vista.txtConsola.append("Sistema iniciado. Zona VIP lista con 50 entradas\n");
        vista.txtConsola.append("Tu saldo actual es: 500.00 soles\n");
        vista.txtConsola.append("--------------------------------------------------\n");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnComprar) {
            procesarCompra();
        }
    }

    private void procesarCompra() {
        try {
            Entrada nuevaEntrada = zonaVip.venderUnaEntrada();
            
            Venta venta = new Venta();
            venta.agregarEntrada(nuevaEntrada, zonaVip.getPrecio());
            
            vista.txtConsola.append("Intentando cobrar: $" + venta.getMonto() + "...\n");
            
            boolean cobroExitoso = tarjetaCliente.procesarCobro(venta.getMonto());
            
            if (cobroExitoso) {
                vista.txtConsola.append("¡Compra exitosa! Tienes tu entrada\n\n");
            } else {
                vista.txtConsola.append("Error: Tarjeta rechazada o fondos insuficientes\n\n");
                nuevaEntrada.liberar();
            }
            
        } catch (Exception ex) {
            vista.txtConsola.append("Problema: " + ex.getMessage() + "\n\n");
        }
    }
}