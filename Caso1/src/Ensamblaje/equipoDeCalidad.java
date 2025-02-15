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
    private static boolean finGenerado = false; // Hacer estático para compartir entre hilos
    private static final Random random = new Random();

    public equipoDeCalidad(buzonDeRevision buzonDeRevision, buzonDeReproceso buzonDeReproceso, deposito deposito, int numProductos) {
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
        this.deposito = deposito;
        this.numProductos = numProductos;
        this.maxRechazados = (int) Math.floor(numProductos * 0.1); // función piso
    }

    public synchronized boolean inspeccionar(producto p) {
        System.out.println("Equipo de calidad verifica el producto: " + p.getId());
        
        int valorAleatorio = random.nextInt(100) + 1;
        if (valorAleatorio % 7 == 0 && numRechazados < maxRechazados) { // múltiplo de 7
            numRechazados++;
            buzonDeReproceso.almacenar(p);
            System.out.println("Producto " + p.getId() + " rechazado y enviado a buzón de reproceso.");
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
    while (productosProcesados < numProductos) {
        synchronized (buzonDeRevision) {
            while (buzonDeRevision.estaVacio()) {
                try {
                    System.out.println("Equipo de calidad en espera... espera pasiva");
                    buzonDeRevision.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        aprobar();
    }

    synchronized (equipoDeCalidad.class) {
        if (!finGenerado) {
            System.out.println("Generando producto FIN");
            buzonDeReproceso.almacenar(new producto(0, "FIN"));
            finGenerado = true;
        }
    }

    System.out.println("Equipo de calidad finaliza ejecución.");
}}
