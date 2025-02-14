package Ensamblaje;

//los operarios son los threads

public class controlcalidad extends Thread {
	
	private double numRechazados;
	private double numPiso;
	private boolean aprobado;
	private int numAprobados;
	
	
	//llegan por parametro desde el main
	private buzonDeReproceso buzonDeReproceso;
	private buzonDeRevision buzonDeRevision;
	private deposito deposito;
	private double numProductos;
	
	private Arraylist<buzondeReproceso> listica = new Arraylist<>();
	
	//asociacion clase producto
	private producto producto;
	
	//constructor
	public controlcalidad(buzonDeReproceso buzonReproceso, buzonDeRevision buzonRevision, deposito deposito, double numProductos) {
        this.buzonDeReproceso = buzonDeReproceso;
        this.buzonDeRevision = buzonDeRevision;
        this.deposito = deposito;
        this.numProductos = numProductos;
        this.aprobado = true;
        this.numAprobados = 0;
        this.numAprobados = 0;
    }


	public synchronized boolean inspeccionar() {
		
		numPiso = Math.floor(numRechazados * 0.1);
		producto producto = buzonDeRevision.retirar();
		System.out.println("Equipo de calidad verifica el producto: "+ producto.getId());
		
		if(numRechazados == numPiso) {
			numAprobados++;
			return aprobado = true;
		}
		
		if(proucto.getEstado % 7 == 0 ) {
			numRechazados++;
			buzonDeReproceso.almacenar(producto);
			System.out.println("Producto: "+ producto.getId() "enviado a buzon de reproceso")
			return aprobado = false;
		}
		numAprobados++;
		return aprobado = true;
		
	}
	
	public synchronized void aprobar() {
		if(aprobado == true) {
			if(numAprobados <= numProductos){
				System.out.println("Producto: "+ producto.getId() "enviado a buzon a deposito")
				deposito.recibirProducto();
			}
			else {
				buzonDeReproseco.almacenar(new producto(0, 0));
			}
		}	
	}

	@Override
	public void run() {
		listica = buzonDeRevision.getRevision();
		while(listica.size() != 0) {
			inspecionar();
			aprobar();
		}
		
	}
}
