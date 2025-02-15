package Ensamblaje;

import java.util.Scanner;

public class lineaDeProduccion {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

        // Pedir parámetros al usuario
        System.out.print("Ingrese el tamaño del buzón de revisión: ");
        int tamanoBuzon = scanner.nextInt();

        System.out.print("Ingrese el número de productores: ");
        int numProductores = scanner.nextInt();

        System.out.print("Ingrese el número total de productos a producir: ");
        int numProductos = scanner.nextInt();

        // Crear los buzones y el depósito
        buzonDeRevision buzonDeRevision = new buzonDeRevision(tamanoBuzon);
        buzonDeReproceso buzonDeReproceso = new buzonDeReproceso();
        deposito deposito = new deposito();

        // Crear productores
        productor[] productores = new productor[numProductores];
        for (int i = 0; i < numProductores; i++) {
            productores[i] = new productor(buzonDeReproceso, buzonDeRevision); // los productores no saben cuantos productos son 
            productores[i].start();
        }

        // Crear equipo de control de calidad
        equipoDeCalidad[] equipoCalidad = new equipoDeCalidad[numProductores];
        for (int i = 0; i < numProductores; i++) {
            equipoCalidad[i] = new equipoDeCalidad(buzonDeRevision, buzonDeReproceso, deposito, numProductos);
            equipoCalidad[i].start();
        }
    }
}
