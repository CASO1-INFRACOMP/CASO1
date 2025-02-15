package Ensamblaje;

import java.util.ArrayList;

public class buzonDeReproceso {
    private ArrayList<producto> buzonReproceso;
    private boolean finRecibido = false;

    public buzonDeReproceso() {
        this.buzonReproceso = new ArrayList<>();
    }

    public synchronized void almacenar(producto i) {
        System.out.println("[buzonDeReproceso] Recibiendo producto: " + i.getEstado());
    
        if ("FIN".equals(i.getEstado())) {
            finRecibido = true;
            System.out.println("[buzonDeReproceso] Producto FIN recibido. Notificando productores... [ESTADO: FINAL]");
        } else {
            buzonReproceso.add(i);
            System.out.println("[buzonDeReproceso] Producto agregado a reproceso: " + i.getId() + " [ESTADO: RECHAZADO]");
        }
        notifyAll();
    }
    
    public synchronized producto retirar() {
        while (buzonReproceso.isEmpty() && !finRecibido) {
            try {
                System.out.println("[buzonDeReproceso] Esperando productos...");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    
        if (finRecibido) {
            System.out.println("[buzonDeReproceso] Retornando NULL porque FIN ya fue entregado.");
            return null;
        }
    
        producto p = buzonReproceso.remove(0);
        System.out.println("[buzonDeReproceso] Producto retirado: " + p.getId() + " [ESTADO: LISTO PARA REPROCESO]");
        return p;
    }
    

    public synchronized boolean estaVacio() {
        return buzonReproceso.isEmpty();
    }

    public synchronized boolean seguirGenerando() {
        return !finRecibido;
    }
}
