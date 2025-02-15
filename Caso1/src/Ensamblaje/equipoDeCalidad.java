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
        System.out.println("[equipoDeCalidad-" + getId() + "] Intentando retirar producto...");
        producto p = buzonDeRevision.retirarProducto();
        System.out.println("[equipoDeCalidad-" + getId() + "] Revisando producto: " + p.getId() + " [ESTADO: EN REVISIÓN]");
    
        if (random.nextInt(100) % 7 == 0) {
            buzonDeReproceso.almacenar(p);
            System.out.println("[equipoDeCalidad-" + getId() + "] Producto " + p.getId() + " rechazado. Enviado a buzón de reproceso. [ESTADO: RECHAZADO]");
        } else {
            deposito.recibirProducto();
            System.out.println("[equipoDeCalidad-" + getId() + "] Producto " + p.getId() + " aprobado y enviado a depósito. [ESTADO: APROBADO]");
        }
    
        productosProcesados++;
        System.out.println("[equipoDeCalidad-" + getId() + "] Productos procesados: " + productosProcesados + "/" + numProductos);
    
        synchronized (buzonDeRevision) {
            buzonDeRevision.notifyAll();
        }
    
        if (productosProcesados == numProductos && !finGenerado) {
            synchronized (equipoDeCalidad.class) {
                if (!finGenerado) {
                    System.out.println("[equipoDeCalidad-" + getId() + "] Generando producto FIN...");
                    producto fin = new producto(0, "FIN");
                    buzonDeReproceso.almacenar(fin);
                    finGenerado = true;
                }
            }
        }
    }
    

    @Override
    public void run() {
        long threadId = Thread.currentThread().getId();
        System.out.println("[equipoDeCalidad-" + threadId + "] Iniciado.");

        while (true) {
            if (finGenerado && buzonDeRevision.estaVacio()) { //Después de depositado el producto FIN, debe revisar si aún hay productos en el buzón de revisión y si hay debe procesarlos. 
                System.out.println("[equipoDeCalidad-" + threadId + "] No hay mas productos - Finalizando correctamente.");
                break;
            }

            if (!buzonDeRevision.estaVacio()) {
                aprobar();
            } else {
                try {
                    System.out.println("[equipoDeCalidad-" + threadId + "] Esperando productos...");
                    Thread.sleep(50); //espera semi aciva sin thread.yield() porque sino genera un bucle infinito
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
