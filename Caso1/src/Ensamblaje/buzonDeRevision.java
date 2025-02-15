package Ensamblaje;

import java.util.ArrayList;

public class buzonDeRevision {
    private ArrayList<producto> buzonRevision;
    private int limiteCantidadProductos;

    public buzonDeRevision(int limiteCantidadProductos) {
        this.buzonRevision = new ArrayList<>();
        this.limiteCantidadProductos = limiteCantidadProductos;
    }

    public synchronized void agregarProducto(producto i) {
        while (buzonRevision.size() == limiteCantidadProductos) { //si no hay espacio disponible los
            //productores deben esperar hasta que puedan depositar el producto actual
            try {
                System.out.println("[buzonDeRevision] Buzón lleno. Esperando espacio...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buzonRevision.add(i);
        System.out.println("[buzonDeRevision] Producto agregado: " + i.getId() + " [ESTADO: EN REVISIÓN]");
        notifyAll();
    }
    
    public synchronized producto retirarProducto() {
        while (buzonRevision.isEmpty()) {
            try {
                System.out.println("[buzonDeRevision] Esperando productos...");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        producto i = buzonRevision.remove(0);
        System.out.println("[buzonDeRevision] Producto retirado: " + i.getId() + " [ESTADO: EN INSPECCIÓN]");
        notifyAll();
        return i;
    }
    

    public synchronized boolean estaLleno() {
        return buzonRevision.size() >= limiteCantidadProductos;
    }

    public synchronized boolean estaVacio() {
        return buzonRevision.isEmpty();
    }
}
