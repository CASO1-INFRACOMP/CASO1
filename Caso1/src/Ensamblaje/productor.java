package Ensamblaje;

public class productor extends Thread {
    private buzonDeReproceso buzonDeReproceso;
    private buzonDeRevision buzonDeRevision;

    public productor(buzonDeReproceso buzonDeReproceso, buzonDeRevision buzonDeRevision) {
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
    }

    //La tarea de reprocesar tiene prioridad sobre la de generar; solamente se
//generan productos nuevos si no hay nada en el buzón de reproceso. Después de reprocesar o generar,
//almacenan el producto en el buzón de revisión.

    public void reprocesar() {
        producto p = buzonDeReproceso.retirar(); //Solo un productor puede retirar del buzón de reproceso es un momento dado.

        if (p == null || "FIN".equals(p.getEstado())) {
            System.out.println("[productor-" + getId() + "] Detectó FIN al retirar. Finalizando...");
            return;
        }

        synchronized (buzonDeRevision) {
            //si hay espacio disponible los productores seguirán generando
            //y depositando nuevos productos (o los productos reprocesados), pero si no hay espacio disponible los
            //productores deben esperar hasta que puedan depositar el producto actual, antes de empezar con el
            //siguiente.

            while (buzonDeRevision.estaLleno()) { //Si hay productos en el buzón de reproceso, los productores deberán intentar
                //reprocesar uno de los productos que allí se encuentren primero.
                try {
                    System.out.println("[productor-" + getId() + "] Buzón de revisión lleno. Esperando...");
                    buzonDeRevision.wait(); //Si el buzón está lleno deben esperar, de manera pasiva,
                    //hasta que haya espacio para almacenarlo.
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            buzonDeRevision.agregarProducto(p);
            System.out.println("[productor-" + getId() + "] Reprocesó producto: " + p.getId() + " [ESTADO: REPROCESADO]");
            buzonDeRevision.notifyAll();
        }
    }

    public void generar() {//Además, generan un producto a
        //la vez y solamente pueden comenzar a producir un nuevo producto cuando el anterior ha sido
        //almacenado en el buzón de revisión.
        synchronized (buzonDeRevision) {
            //si hay espacio disponible los productores seguirán generando
            //y depositando nuevos productos (o los productos reprocesados), pero si no hay espacio disponible los
            //productores deben esperar hasta que puedan depositar el producto actual, antes de empezar con el
            //siguiente.
            while (!buzonDeReproceso.estaVacio()) { //Los productores solamente pueden generar nuevos productos si el buzón de reproceso se
                //encuentra vacío.
                try {
                    System.out.println("[productor-" + getId() + "] Esperando reprocesos antes de generar...");
                    buzonDeRevision.wait();
                    //Si el buzón está lleno deben esperar, de manera pasiva,
                    //hasta que haya espacio para almacenarlo.
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
