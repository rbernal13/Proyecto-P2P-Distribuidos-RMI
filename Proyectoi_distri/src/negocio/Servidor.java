package negocio;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Servidor {

	public static void main(String args[]) {
		ResourceBundle properties = PropertyResourceBundle.getBundle("Simple");
		int port = Registry.REGISTRY_PORT;
		try {
			port = Integer.parseInt(properties.getString("server.port"));
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
