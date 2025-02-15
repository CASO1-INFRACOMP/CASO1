package Ensamblaje;

//los operarios son los threads

public class productor extends Thread {
	
	private double numRechazados = 0;
	private double numPiso;
	
	//sirve para activar el señalamiento
	private static boolean hayProductosEnReproceso = false;
	
	//existen operarios productores y operarios del equipo de calidad
	// hay que discriminarlos a traves de un parametro
	private boolean esProductor;
	
	
	//llegan por parametro desde el main
	private buzonDeReproceso buzonDeReproceso;
	private buzonDeRevision buzonDeRevision;
	private deposito deposito;
	private static int id = 0;
	private double numProductos;
	
	
	
	//constructor
	public productor(buzonDeReproceso buzonDeReproceso, buzonDeRevision buzonDeRevision) {
        if (buzonDeReproceso == null || buzonDeRevision == null) {
            throw new IllegalArgumentException("Los buzones no pueden ser null");
        }
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
    }
	
	//señalamiento entre reprocesar y generar
	public void reprocesar() {
        synchronized (buzonDeReproceso) {
			//tiene que estar vacio (mientras NO haya productos en reproceso)

            while (!hayProductosEnReproceso) {
                try {
                    buzonDeReproceso.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            producto p = buzonDeReproceso.retirar();
            buzonDeRevision.agregarProducto(p);
            buzonDeRevision.notifyAll();
        }
    }


	
	public void generar() {
        synchronized (buzonDeRevision) {
            while (hayProductosEnReproceso) {
                try {
                    buzonDeRevision.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            producto p = new producto();
            buzonDeRevision.agregarProducto(p);
            buzonDeRevision.notifyAll();
        }
    }

	@Override
    public void run() {
        while (true) {
            if (hayProductosEnReproceso) {
                reprocesar();
            } else {
                generar();
            }
            Thread.yield();
        }
    }
}
