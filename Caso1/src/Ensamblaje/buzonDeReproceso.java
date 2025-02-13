package Ensamblaje;

import java.util.ArrayList;

public class buzonDeReproceso {
	
	private ArrayList buzonReproceso;

	public buzonDeReproceso() {
		this.buzonReproceso = new Arraylist();
	}

	
	//no tiene limite de capacidad
	
	public synchronized void almacenar(producto i) {
		
		while(buzonReproceso != null) {
			buzonReproceso.add(i);
			notify();
		}
		wait();
		
	}
	

	public synchronized producto retirar() {
		
		while (buzonReproceso.size()==0) {
			try {
				yield();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		producto i = (producto)buzonReproceso.remove(0);
		return i;
		
	}
}
	
	
}
