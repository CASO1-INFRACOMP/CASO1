package Ensamblaje;

import java.util.ArrayList;

public class buzonDeRevision {
    private ArrayList<producto> buzonRevision;
    private int limiteCantidadProductos; //decidieron limitar la cantidad de productos que pueden ser
    //almacenados en este buzón.

    public buzonDeRevision(int limiteCantidadProductos) {
        this.buzonRevision = new ArrayList<>();
        this.limiteCantidadProductos = limiteCantidadProductos;
    }

    public synchronized void agregarProducto(producto i) {
        while (buzonRevision.size() >= limiteCantidadProductos) { //si no hay espacio disponible los
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
            // Si no hay productos disponibles, los consumidores deben esperar
            try {
                System.out.println("[buzonDeRevision] Esperando productos...");
                wait(); // El hilo se bloquea hasta que otro hilo llame a notifyAll()
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restablecer el estado de interrupción
                System.out.println("[buzonDeRevision] Hilo interrumpido al esperar productos.");
                return null; // Retorna null para evitar problemas en la ejecución
            }
        }
        producto i = buzonRevision.remove(0);
        System.out.println("[buzonDeRevision] Producto retirado: " + i.getId() + " [ESTADO: EN INSPECCIÓN]");
        notifyAll(); // Notificar a los hilos en espera de un cambio en el buzón
        return i;
    }
    

    public synchronized boolean estaLleno() {
        return buzonRevision.size() >= limiteCantidadProductos;
    }

    public synchronized boolean estaVacio() {
        return buzonRevision.isEmpty();
    }
}
