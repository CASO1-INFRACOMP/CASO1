package Ensamblaje;

// Los operarios son los threads
public class productor extends Thread {
    
    private static boolean hayProductosEnReproceso = false;
    
    // Llegan por parámetro desde el main
    private buzonDeReproceso buzonDeReproceso;
    private buzonDeRevision buzonDeRevision;

    private static boolean finDetectado = false;
    
    // Constructor
    public productor(buzonDeReproceso buzonDeReproceso, buzonDeRevision buzonDeRevision) {
        if (buzonDeReproceso == null || buzonDeRevision == null) {
            throw new IllegalArgumentException("Los buzones no pueden ser null");
        }
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
    }
    
    // Señalamiento entre reprocesar y generar
    public void reprocesar() {
        synchronized (buzonDeReproceso) {
            // Esperar hasta que haya productos en el buzón de reproceso
            while (!hayProductosEnReproceso) {
                try {
                    buzonDeReproceso.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // Retirar producto del buzón de reproceso
            producto p = buzonDeReproceso.retirar();
            
            // Si el producto es FIN, el productor finaliza
            if ("FIN".equals(p.getEstado())) {
                System.out.println("Productor recibe producto FIN y finaliza ejecución.");
                return;
            }
            
            // Enviar producto reprocesado al buzón de revisión
            buzonDeRevision.agregarProducto(p);
            System.out.println("Productor reprocesa producto: " + p.getId());
            hayProductosEnReproceso = !buzonDeReproceso.estaVacio();
            buzonDeRevision.notifyAll();
        }
    }

    public void generar() {
        synchronized (buzonDeRevision) {
            // Esperar hasta que haya espacio en el buzón de revisión
            while (hayProductosEnReproceso || buzonDeRevision.estaLleno()) {
                try {
                    buzonDeRevision.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (finDetectado) {
                return;
            }
            // Crear y almacenar un nuevo producto
            producto nuevo = new producto();
            buzonDeRevision.agregarProducto(nuevo);
            System.out.println("Productor genera nuevo producto: " + nuevo.getId());
            buzonDeRevision.notifyAll();
        }
    }
    
    @Override
    public void run() {
        while (!finDetectado) {
            synchronized (buzonDeRevision) {
                while (buzonDeRevision.estaLleno()) {
                    try {
                        System.out.println("Productor en espera... espera pasiva");
                        buzonDeRevision.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            
            // Si hay productos en reproceso, priorizarlos
            if (hayProductosEnReproceso) {
                reprocesar();
            } else {
                generar();
            }
        }
    }
}
