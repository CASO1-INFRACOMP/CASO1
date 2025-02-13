package Ensamblaje;

//los operarios son los threads

public class operario extends Thread {
	
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
	
	//asociacion clase producto
	private producto producto;
	
	//constructor
	public operario(boolean esProductor, buzonDeReproceso buzonReproceso, buzonDeRevision buzonRevision, deposito deposito, double numProductos) {
        this.esProductor = esProductor;
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
        this.deposito = deposito;
        this.numProductos = numProductos
    }
	
	//señalamiento entre reprocesar y generar
	public void reprocesar() {synchronized (buzonDeReproceso) { 
		//tiene que estar vacio (mientras NO haya productos en reproceso)
		while(!hayProductosEnReproceso) {
			try {
				
				buzonDereproceso.almacenar(producto);
				notify();
				
			}	
           catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
		
		wait();
		
	}}

	
	public void generar() {synchronized (buzonDeRevision) { 
		//si hay productos en reproceso tiene que esperar
		while (!hayProductosEnReproceso) {
			try {
				wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
			//crear producto y mandar al buzon de revision 
			producto producto = new producto(generarId(), Math.random());
			buzonDeRevision.almacenar(producto);
			notifyAll();
		
			//mandamos señal
            //hayProductosEnReproceso = true;
            buzonDeReproceso.notifyAll();
            
		
		
	}}
	
	//sincronizado con la propia clase
	private synchronized static int generarId() {
		return ++id;
	}

	//sincronizado partiendo de la idea de que no se puede interactuar con 
	//el bloque de revision al mismo tiempo
	public synchronized boolean inspeccionar() {
		
		numPiso = Math.floor(numRechazados * 0.1);
		
		while(numRechazados == numPiso) {
			notiftAll();
			return aprobado = true;
		}
		
		numRechazados++;
		hayProductosEnReproceso = true;
		reprocesar();

	}

        
	
	@Override
	public void run() {
		
		while (true) { // espera semi activa
            synchronized (buzonDeRevision) { 
            	if(hayProductosEnReproceso) {
            		Thread.yield();
            	}else {
            		//logica
            	}
	}
}
