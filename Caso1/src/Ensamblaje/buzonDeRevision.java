package Ensamblaje;

import java.util.ArrayList;

//buffer de tamaño limitado
public class buzonDeRevision {
	
	//n limitar la cantidad de productos que pueden ser
	//almacenados en este buzón
	private ArrayList buzonRevision;
	private int limiteCantidadProductos;

	
	
	public buzonDeRevision( int limiteCantidadProductos) {
		this.buzonRevision = new ArrayList();
		this.limiteCantidadProductos = limiteCantidadProductos;
	}


	//no se puede almacenar ni retirar productos al mismo tiempo
	//exlusion mutua
	public synchronized void almacenar(producto i) {
		
		while (buzonRevision.size()==limiteCantidadProductos) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buzonRevision.add(i);
		notify();	
	}
	
	public synchronized producto retirar() {
		while (buzonRevision.size()==0) {
			try {
				yield();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		producto i = (producto)buzonRevision.remove(0);
		notify();
		return i;
		
	}
	
	public ArrayList<buzonDeRevision> getRevision(){
		
		return buzonRevision;
	}
}
