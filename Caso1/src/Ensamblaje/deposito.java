package Ensamblaje;

public class deposito {
    
    private int productosAprobados;

    public deposito() {
        this.productosAprobados = 0;
    }


    //un producto a la vez
    public synchronized void recibirProducto() {
    	
        productosAprobados++;
        
        System.out.println("Producto aprobado recibido en el depósito. Total almacenados: " + productosAprobados);
    }

    public synchronized int getProductosAprobados() {
        return productosAprobados;
    }
}
