package negocio;

public class Transaccion {

	private static int idTrans;
	private String duenio;
	private String accion;
	private String nombreArch;
	private boolean estado;
	
	
	public Transaccion(String duenio, String accion, String nombreArch) {
		super();
		this.duenio = duenio;
		this.accion = accion;
		this.nombreArch = nombreArch;
		this.idTrans = idTrans ++;
		this.estado = false;
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
	public String getNombreArch() {
		return nombreArch;
	}
	public void setNombreArch(String nombreArch) {
		this.nombreArch = nombreArch;
	}
	public boolean getEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
	
}
