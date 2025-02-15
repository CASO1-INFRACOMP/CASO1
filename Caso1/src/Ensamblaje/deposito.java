package Ensamblaje;

public class deposito {
    private int productosAprobados;

    public deposito() {
        this.productosAprobados = 0;
    }

    // Recibe productos aprobados
    public synchronized void recibirProducto() {
        productosAprobados++;
        System.out.println("Producto aprobado recibido en el depósito. Total almacenados: " + productosAprobados);
    }

    // Retorna la cantidad de productos aprobados
    public synchronized int getCantidadProductos() {
        return productosAprobados;
    }
}
