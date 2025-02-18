package Ensamblaje;

public class productor extends Thread {
    private buzonDeReproceso buzonDeReproceso;
    private buzonDeRevision buzonDeRevision;

    public productor(buzonDeReproceso buzonDeReproceso, buzonDeRevision buzonDeRevision) {
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
    }

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

            buzonDeRevision.agregarProducto(p);
            System.out.println("[productor-" + getId() + "] Reprocesó producto: " + p.getId() + " [ESTADO: REPROCESADO]");
            buzonDeRevision.notifyAll();
        }
    }

    public void generar() {
        synchronized (buzonDeRevision) {
            while (!buzonDeReproceso.estaVacio()) {
                try {
                    System.out.println("[productor-" + getId() + "] Esperando reprocesos antes de generar...");
                    buzonDeRevision.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (!buzonDeReproceso.seguirGenerando()) {
                System.out.println("[productor-" + getId() + "] FIN detectado. Finalizando...");
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

        while (!equipoDeCalidad.produccionFinalizada) {
            if (!buzonDeReproceso.estaVacio()) {
                reprocesar();
            } else {
                generar();
            }
        }

        System.out.println("[productor-" + getId() + "] Finalizó correctamente.");
    }
}
