package Ensamblaje;

import java.util.Scanner;

public class lineaDeProduccion {
	
	
	public static void main(String[]args) {
		
		Scanner scanner = new Scanner(System.in);

        // Pedir parámetros al usuario
        System.out.print("Ingrese el tamaño del buzón de revisión: ");
        int tamanoBuzon = scanner.nextInt();

        System.out.print("Ingrese el número de operarios en cada equipo: ");
        int numOperarios = scanner.nextInt();

        System.out.print("Ingrese el número total de productos a producir: ");
        int numProductos = scanner.nextInt();

        // Crear el buzón de revisión
        buzonDeRevision buzonDeRevision = new buzonDeRevision(tamanoBuzon);
        
        buzonDeReproceso buzonDeReproceso = new buzonDeReproceso();
        deposito deposito = new deposito();
		
		
        // Crear operarios productores
        operario[] productores = new operario[numOperarios];
        for (int i = 0; i < numOperarios; i++) {
            productores[i] = new operario(true, buzonDeReproceso, buzonDeRevision, deposito, numProductos);
            productores[i].start();
        }

        // Crear operarios del equipo de calidad
        operario[] equipoDeCalidad = new operario[numOperarios];
        for (int i = 0; i < numOperarios; i++) {
            equipoDeCalidad[i] = new operario(false, buzonDeReproceso, buzonDeRevision, deposito, numProductos);
            equipoDeCalidad[i].start();
        }

        scanner.close();
    }

}
