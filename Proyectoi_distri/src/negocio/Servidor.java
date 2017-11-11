package negocio;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;

 public class Servidor {
	
	private static Vector<Transaccion> trClientes = new Vector<Transaccion>();;

	
	public static void addTrans(String duenio, String comando){
		System.out.println("Owner: "+duenio+" "+"Comando: "+comando);
		Transaccion tr = new Transaccion(duenio,comando);
		System.out.println("Transaccion adquirida:" +tr.getDuenio() + " "+tr.getAccion());
		Servidor.trClientes.add(tr);
		
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
