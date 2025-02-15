package Ensamblaje;
//no tienen problemas con la cantidad de productos que lleguen desde la línea de producción.
public class deposito {
    private int productosAprobados;

    public deposito() {
        this.productosAprobados = 0;
    }

    // Recibe productos aprobados
    public synchronized void recibirProducto() {
        productosAprobados++;
        System.out.println("[deposito] Producto aprobado recibido en el depósito. Total almacenados: " + productosAprobados + " [ESTADO: FINALIZADO]");
    }
    
    // Retorna la cantidad de productos aprobados
    public synchronized int getCantidadProductos() {
        return productosAprobados;
    }
}
