package Ensamblaje;

import java.util.Scanner;

public class lineaDeProduccion {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Ingrese el tamaño del buzón de revisión: ");
        int tamanoBuzon = scanner.nextInt();

        System.out.print("Ingrese el número de productores: ");
        int numProductores = scanner.nextInt();

        System.out.print("Ingrese el número total de productos a producir: ");
        int numProductos = scanner.nextInt();

        buzonDeRevision buzonDeRevision = new buzonDeRevision(tamanoBuzon);
        buzonDeReproceso buzonDeReproceso = new buzonDeReproceso();
        deposito deposito = new deposito();
        
        for (int i = 0; i < numProductores; i++) {
            new productor(buzonDeReproceso, buzonDeRevision).start();
            new equipoDeCalidad(buzonDeRevision, buzonDeReproceso, deposito, numProductos).start();
        }
        scanner.close();
    }
}
