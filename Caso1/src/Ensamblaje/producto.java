package Ensamblaje;

public class producto {
	
	private int id;
	private String estado;
	
	
	
	public producto(int id, int estado) {
		this.id = id;
		this.estado = estado;
	}
	
	
	public int getId() { return id; }
	
	public String getEstado() { return estado; }
	
    public void setEstado(int estado) { this.estado = estado; }
	

}
