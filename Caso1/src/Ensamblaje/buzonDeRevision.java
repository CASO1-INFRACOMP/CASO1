package Ensamblaje;

import java.util.ArrayList;

//buffer de tamaño limitado
public class buzonDeRevision {
	
	//n limitar la cantidad de productos que pueden ser
	//almacenados en este buzón
	private ArrayList<producto> buzonRevision;
    private int limiteCantidadProductos;
	
	
	public buzonDeRevision(int limiteCantidadProductos) {
        this.buzonRevision = new ArrayList<>();
        this.limiteCantidadProductos = limiteCantidadProductos;
    }


	//no se puede almacenar ni retirar productos al mismo tiempo
	//exlusion mutua
	public synchronized void agregarProducto(producto i) {
        while (buzonRevision.size() == limiteCantidadProductos) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buzonRevision.add(i);
        notifyAll();
    }
	
	public synchronized producto retirarProducto() {
        while (buzonRevision.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        producto i = buzonRevision.remove(0);
        notifyAll();
        return i;
    }
	
	public synchronized boolean estaLleno() {
        return buzonRevision.size() >= limiteCantidadProductos;
    }
}
