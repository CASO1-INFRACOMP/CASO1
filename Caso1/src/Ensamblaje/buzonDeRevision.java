package Ensamblaje;

import java.util.ArrayList;

//buffer de tamaño limitado
public class buzonDeRevision {
	
	//n limitar la cantidad de productos que pueden ser
	//almacenados en este buzón
	private ArrayList buff;
	private int limiteCantidadProductos;

	
	
	public buzonDeRevision( int limiteCantidadProductos) {
		super();
		this.buff = new ArrayList();
		this.limiteCantidadProductos = limiteCantidadProductos;
	}


	//no se puede almacenar ni retirar productos al mismo tiempo
	//exlusion mutua
	public synchronized void almacenar(producto i) {
		
		while (buff.size()==limiteCantidadProductos) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buff.add(i);
		notify();	
	}
	
	public synchronized Integer retirar() {
		while (buff.size()==0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		producto i = (producto)buff.remove(0);
		notify();
		return i;
		
	}
}
