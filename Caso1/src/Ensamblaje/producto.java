package Ensamblaje;

public class producto {
    private static int contadorId = 1;
    private int id;
    private String estado;

    public producto() {
        this.id = generarId();
        this.estado = "Nuevo";
    }

    public producto(int id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    private static synchronized int generarId() {
        return contadorId++;
    }

    public int getId() { return id; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
