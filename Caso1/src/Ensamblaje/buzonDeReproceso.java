package Ensamblaje;

import java.util.ArrayList;

public class buzonDeReproceso {
    private ArrayList<producto> buzonReproceso;
    private boolean finRecibido = false;

    public buzonDeReproceso() {
        this.buzonReproceso = new ArrayList<>();
    }

    public synchronized void almacenar(producto i) {
        long threadId = Thread.currentThread().getId();
        System.out.println("[buzonDeReproceso-" + threadId + "] Recibiendo producto: " + i.getEstado());

        if ("FIN".equals(i.getEstado())) {
            finRecibido = true;
            System.out.println("[buzonDeReproceso-" + threadId + "] Producto FIN recibido. Notificando productores...");
        } else {
            buzonReproceso.add(i);
        }
        notifyAll();
    }

    public synchronized producto retirar() {
        long threadId = Thread.currentThread().getId();
        
        while (buzonReproceso.isEmpty() && !finRecibido) {
            try {
                System.out.println("[buzonDeReproceso-" + threadId + "] Esperando productos...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (finRecibido) {
            System.out.println("[buzonDeReproceso-" + threadId + "] Retornando NULL porque FIN ya fue entregado.");
            return null;
        }

        producto p = buzonReproceso.remove(0);
        System.out.println("[buzonDeReproceso-" + threadId + "] Producto retirado: " + p.getId());
        return p;
    }

    public synchronized boolean estaVacio() {
        return buzonReproceso.isEmpty();
    }

    public synchronized boolean seguirGenerando() {
        return !finRecibido;
    }
}
