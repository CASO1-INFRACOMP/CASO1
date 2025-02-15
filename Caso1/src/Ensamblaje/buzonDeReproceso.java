package Ensamblaje;

import java.util.ArrayList;


//no tiene limite de capacidad
public class buzonDeReproceso {
    private ArrayList<producto> buzonReproceso;

    public buzonDeReproceso() {
        this.buzonReproceso = new ArrayList<>();
    }

    public synchronized void almacenar(producto i) {
        buzonReproceso.add(i);
        notifyAll(); //revisar
    }


    //tambien se puede retirar del buzon de revision pero ese es otro metodo
    public synchronized producto retirar() {
        while (buzonReproceso.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return buzonReproceso.remove(0);
    }

    public synchronized boolean estaVacio() {
        return buzonReproceso.isEmpty();
    }

	
}
