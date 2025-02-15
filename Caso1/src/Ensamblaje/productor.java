package Ensamblaje;

public class productor extends Thread {
    private buzonDeReproceso buzonDeReproceso;
    private buzonDeRevision buzonDeRevision;

    public productor(buzonDeReproceso buzonDeReproceso, buzonDeRevision buzonDeRevision) {
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
    }

    /**
     * La tarea de reprocesar tiene prioridad sobre la de generar; solamente se
     * generan productos nuevos si no hay nada en el buzón de reproceso.
     */
    public void reprocesar() {
        producto p = buzonDeReproceso.retirar();

        if (p == null || "FIN".equals(p.getEstado())) {
            System.out.println("[productor-" + getId() + "] Detectó FIN al retirar. Finalizando...");
            return;
        }

        synchronized (buzonDeRevision) {
            while (buzonDeRevision.estaLleno()) {
                try {
                    System.out.println("[productor-" + getId() + "] Buzón de revisión lleno. Esperando...");
                    buzonDeRevision.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            buzonDeRevision.agregarProducto(p); // Después de reprocesar o generar,
            // almacenan el producto en el buzón de revisión.
            
            System.out.println("[productor-" + getId() + "] Reprocesó producto: " + p.getId() + " [ESTADO: REPROCESADO]");
            buzonDeRevision.notifyAll();
        }
    }

    /**
     * Los productores solamente pueden generar nuevos productos si el buzón de reproceso se
     * encuentra vacío.
     */
    public void generar() {
        synchronized (buzonDeRevision) {
            // Los productores solo generan productos nuevos si el buzón de reproceso está vacío
            while (!buzonDeReproceso.estaVacio()) { 
                // Los productores solamente pueden comenzar a producir un nuevo producto cuando el anterior ha sido almacenado
                try {
                    System.out.println("[productor-" + getId() + "] Buzón de revisión lleno. Esperando...");
                    buzonDeRevision.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (!buzonDeReproceso.seguirGenerando()) {
                System.out.println("[productor-" + getId() + "] FIN detectado en generación. Finalizando...");
                return;
            }

            producto nuevo = new producto();
            buzonDeRevision.agregarProducto(nuevo);
            System.out.println("[productor-" + getId() + "] Generó producto: " + nuevo.getId() + " [ESTADO: NUEVO]");
            buzonDeRevision.notifyAll();
        }
    }

    @Override
    public void run() {
        System.out.println("[productor-" + getId() + "] Iniciado.");
        
        while (true) {
            // Verifica si ya se ha detectado el fin del proceso
            if (!buzonDeReproceso.seguirGenerando()) {
                System.out.println("[productor-" + getId() + "] FIN detectado en run(). Finalizando...");
                break;
            }

            // **Prioridad: Intentar reprocesar primero**
            if (!buzonDeReproceso.estaVacio()) {
                reprocesar();
            } else {
                generar();
            }
        }

        System.out.println("[productor-" + getId() + "] Finalizó correctamente.");
    }
}
