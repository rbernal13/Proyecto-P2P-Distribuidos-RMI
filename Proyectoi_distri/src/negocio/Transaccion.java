package negocio;

public class Transaccion {

	public static int idTrans;
	public String duenio;
	public String accion;
	
	
	public Transaccion(String duenio, String accion) {
		super();
		this.duenio = duenio;
		this.accion = accion;
		this.idTrans = idTrans ++;
	}
	public static int getIdTrans() {
		return idTrans;
	}
	public static void setIdTrans(int idTrans) {
		Transaccion.idTrans = idTrans;
	}
	public String getDuenio() {
		return duenio;
	}
	public void setDuenio(String duenio) {
		this.duenio = duenio;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	
}
