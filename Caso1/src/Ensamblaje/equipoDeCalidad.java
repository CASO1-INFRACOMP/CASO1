package Ensamblaje;

import java.util.Random;

public class equipoDeCalidad extends Thread {


    
    private buzonDeReproceso buzonDeReproceso;
    private buzonDeRevision buzonDeRevision;
    private deposito deposito;
    private int numProductos;
    private int productosProcesados = 0;
    private int numRechazados = 0;
    private int maxRechazados;
    private boolean finGenerado = false;
    private static final Random random = new Random();

    public equipoDeCalidad(buzonDeRevision buzonDeRevision, buzonDeReproceso buzonDeReproceso, deposito deposito, int numProductos) {
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
        this.deposito = deposito;
        this.numProductos = numProductos;
        this.maxRechazados = (int) Math.floor(numProductos * 0.1);
    }

    public synchronized boolean inspeccionar(producto p) {
        System.out.println("Equipo de calidad verifica el producto: " + p.getId());
        
        int valorAleatorio = random.nextInt(100) + 1;
        if (valorAleatorio % 7 == 0 && numRechazados < maxRechazados) {
            numRechazados++;
            buzonDeReproceso.almacenar(p);
            System.out.println("Producto " + p.getId() + " rechazado y enviado a buzon de reproceso.");
            return false;
        }
        return true;
    }

    public synchronized void aprobar() {
        producto p = buzonDeRevision.retirarProducto();
        if (inspeccionar(p)) {
            deposito.recibirProducto();
            System.out.println("Producto " + p.getId() + " aprobado y enviado a depósito.");
        }
        productosProcesados++;
    }

    @Override
    public void run() {
        while (productosProcesados < numProductos || !buzonDeRevision.estaLleno()) {
            aprobar();
        }
        
        if (!finGenerado) {
            System.out.println("Generando producto FIN");
            buzonDeReproceso.almacenar(new producto(0, "FIN"));
            finGenerado = true;
        }
        
        while (!buzonDeRevision.estaLleno()) {
            aprobar();
        }
        
        System.out.println("Equipo de calidad finaliza ejecución.");
    }
}
