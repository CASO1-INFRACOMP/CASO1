package Ensamblaje;

//los operarios son los threads

public class operario extends Thread {
	
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
	
	//constructor
	public operario(boolean esProductor, buzonDeReproceso buzonReproceso, buzonDeRevision buzonRevision, deposito deposito) {
        this.esProductor = esProductor;
        this.buzonDeReproceso = buzonReproceso;
        this.buzonDeRevision = buzonRevision;
        this.deposito = deposito;
    }
	
	//señalamiento entre reprocesar y generar
	public void reprocesar() {synchronized (buzonDeReproceso) { 
		//tiene que estar vacio (mientras NO haya productos en reproceso)
		if (!hayProductosEnReproceso) {
			try {
				buzonDeReproceso.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
			//mandamos señal
			hayProductosEnReproceso = false;
			buzonDeReproceso.notifyAll();
		}
		
	}}

	
	public void generar() {synchronized (buzonDeRevision) { 
		//si hay productos en reproceso tiene que esperar
		while (hayProductosEnReproceso) {
			try {
				buzonDeRevision.wait();
			
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
			//crear producto y mandar al buzon de revision 
			producto producto = new producto(generarId(), "Nuevo");
			buzonDeRevision.almacenar(producto);
		
			//mandamos señal
            hayProductosEnReproceso = true;
            buzonDeReproceso.notifyAll();
            
		
		
	}}
	
	//sincronizado con la propia clase
	private synchronized static int generarId() {
		return ++id;
	}

	//sincronizado partiendo de la idea de que no se puede interactuar con 
	//el bloque de revision al mismo tiempo
	public void inspeccionar() {
        while (true) { // espera semi activa
            synchronized (buzonDeRevision) { 
            	if(hayProductosEnReproceso) {
            		Thread.yield();
            	}else {
            		//logica
            	}
                
            }}
        
        
	
	}
	
	
	@Override
	public void run() {
		
		
		
		Integer i = new ();
		buff.almacenar(i);
		
	}
	
}
