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

    public synchronized boolean estaVacio() {
        return buzonRevision.isEmpty();
    }
}
