package Ensamblaje;

public class productor extends Thread {
    private buzonDeReproceso buzonDeReproceso;
    private buzonDeRevision buzonDeRevision;
    private static boolean hayProductosEnReproceso = false;

    public productor(buzonDeReproceso buzonDeReproceso, buzonDeRevision buzonDeRevision) {
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
    }

    public void generar() {
        synchronized (buzonDeRevision) {
            while (buzonDeRevision.estaLleno()) {
                try {
                    System.out.println("[productor-" + Thread.currentThread().getId() + "] Buzón de revisión lleno. Esperando...");
                    buzonDeRevision.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
    
            if (!buzonDeReproceso.seguirGenerando()) {
                System.out.println("[productor-" + Thread.currentThread().getId() + "] FIN detectado en generación. Finalizando...");
                return;
            }
    
            producto nuevo = new producto();
            buzonDeRevision.agregarProducto(nuevo);
            System.out.println("[productor-" + Thread.currentThread().getId() + "] Generó producto: " + nuevo.getId());
        }
    }

    @Override
    public void run() {
        System.out.println("[productor-" + Thread.currentThread().getId() + "] Iniciado.");
        while (true) {
            if (!buzonDeReproceso.seguirGenerando()) {
                System.out.println("[productor-" + Thread.currentThread().getId() + "] FIN detectado en run(). Finalizando...");
                break;
            }
    
            if (hayProductosEnReproceso) {
                producto p = buzonDeReproceso.retirar();
                if (p == null || "FIN".equals(p.getEstado())) {
                    System.out.println("[productor-" + Thread.currentThread().getId() + "] Detectó FIN al retirar. Finalizando...");
                    break;
                }
                buzonDeRevision.agregarProducto(p);
                System.out.println("[productor-" + Thread.currentThread().getId() + "] Reprocesa producto: " + p.getId());
            } else {
                generar();
            }
        }
        System.out.println("[productor-" + Thread.currentThread().getId() + "] Finalizó correctamente.");
    }
}
