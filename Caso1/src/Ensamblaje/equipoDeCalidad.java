package Ensamblaje;

import java.util.Random;

public class equipoDeCalidad extends Thread {
    private buzonDeReproceso buzonDeReproceso;
    private buzonDeRevision buzonDeRevision;
    private deposito deposito;
    private int numProductos;
    private int productosProcesados = 0;
    private static boolean finGenerado = false;
    private static final Random random = new Random();

    public equipoDeCalidad(buzonDeRevision buzonDeRevision, buzonDeReproceso buzonDeReproceso, deposito deposito, int numProductos) {
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
        this.deposito = deposito;
        this.numProductos = numProductos;
    }

    public void aprobar() {
        long threadId = Thread.currentThread().getId();
        System.out.println("[equipoDeCalidad-" + threadId + "] Intentando retirar producto...");

        producto p = buzonDeRevision.retirarProducto();
        System.out.println("[equipoDeCalidad-" + threadId + "] Revisando producto: " + p.getId());

        if (random.nextInt(100) % 7 == 0) {
            buzonDeReproceso.almacenar(p);
            System.out.println("[equipoDeCalidad-" + threadId + "] Producto " + p.getId() + " rechazado. Enviado a buzón de reproceso.");
        } else {
            deposito.recibirProducto();
            System.out.println("[equipoDeCalidad-" + threadId + "] Producto " + p.getId() + " aprobado y enviado a depósito.");
        }

        productosProcesados++;
        System.out.println("[equipoDeCalidad-" + threadId + "] Productos procesados: " + productosProcesados + "/" + numProductos);

        synchronized (buzonDeRevision) {
            buzonDeRevision.notifyAll();
        }

        if (productosProcesados == numProductos && !finGenerado) {
            System.out.println("[equipoDeCalidad-" + threadId + "] Generando producto FIN...");
            producto fin = new producto(0, "FIN");
            buzonDeReproceso.almacenar(fin);
            finGenerado = true;
        }
    }

    @Override
    public void run() {
        long threadId = Thread.currentThread().getId();
        System.out.println("[equipoDeCalidad-" + threadId + "] Iniciado.");

        while (true) {
            if (finGenerado && buzonDeRevision.estaVacio()) {
                System.out.println("[equipoDeCalidad-" + threadId + "] Finalizando correctamente.");
                break;
            }

            if (!buzonDeRevision.estaVacio()) {
                aprobar();
            } else {
                try {
                    System.out.println("[equipoDeCalidad-" + threadId + "] Esperando productos...");
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
