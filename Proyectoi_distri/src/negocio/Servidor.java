package negocio;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;

 public class Servidor {
	
	private static Vector<Transaccion> trClientes = new Vector<Transaccion>();;

	
	public static void addTrans(String duenio, String comando, String nombreArch){
		System.out.println("Owner: "+duenio+" "+"Comando: "+comando);
		Transaccion tr = new Transaccion(duenio,comando,nombreArch);
		System.out.println("Transaccion adquirida:" +tr.getDuenio() + " "+tr.getAccion());
		tr.setEstado(true);
		Servidor.trClientes.add(tr);
		pat();
		
	}
	
	private static void pat(){
		
		FileWriter outFile = null;
		PrintWriter dataOutStream = null;
		
		try {
			outFile = new FileWriter("transacciones.txt");
			dataOutStream = new PrintWriter(outFile);
			
			for(Transaccion t: Servidor.trClientes){
				
				dataOutStream.println(t.getIdTrans());
				System.out.println(t.getIdTrans());
				dataOutStream.println(t.getDuenio());
				System.out.println(t.getDuenio());
				dataOutStream.print(t.getAccion());
				System.out.println(t.getAccion());
				dataOutStream.println(t.getNombreArch().trim());
				dataOutStream.println("------");
				
			
			}
			
			dataOutStream.close();
			outFile.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error en la ruta del archivO: "+ e.getMessage());
		}catch (IOException e3) {
			System.out.println("Error guardando el archivo: "+ e3.getMessage());
		}
		
	}

	public static Vector<Transaccion> getTransc(){
		
		return Servidor.trClientes;
	}
	public static void main(String args[]) {
		
		ResourceBundle properties = PropertyResourceBundle.getBundle("Simple");
		
		int port = Registry.REGISTRY_PORT;
		try {
			System.out.println("Porfavor digite el puerto para iniciar la conexion:  ");
			port = Integer.parseInt(args[0]);
			
			//port = Integer.parseInt(properties.getString("server.port"));
		} catch (Exception e) {

		}

		try {

			Registry registry = LocateRegistry.createRegistry(port);

			Simplementacion obj = new Simplementacion();

			registry.rebind("SimpleServer", obj);

			System.out.println("Servidor Inicio en el Puerto: " + port);
		} catch (Exception e) {
			System.out.println("Error al iniciar el servidor: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
}
