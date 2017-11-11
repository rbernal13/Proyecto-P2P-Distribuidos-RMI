package negocio;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Cliente {
	final public static int BUF_SIZE = 1024 * 64;
	private static String clientName;
	static boolean estado;


	public static boolean copy(InputStream in, OutputStream out) throws IOException {

		if (in == null || out == null) {
			estado = false;
			out.close();
			return false;
		} else {
			byte[] b = new byte[BUF_SIZE];
			int len;
			while ((len = in.read(b)) >= 0) {
				out.write(b, 0, len);
			}
			in.close();
			out.close();
			estado = true;
			return true;
		}
	}

	public static void subir(InterfazS server, File src, File dest, String clientName) throws IOException {

		
		boolean state = copy(new FileInputStream(src), server.getOutputStream(dest, clientName));
	}

	public static void descargarLec(InterfazS server, File src, File dest, String clientName)  {
		
		try {
			boolean state = copy(server.getInputStream(src, clientName), new FileOutputStream(dest));

			if (state == false) {

				File file1 = new File(dest.getName());
				file1.delete();
				
			} else {

			}

		} catch (IOException | NullPointerException e) {
			System.out.println("El archivo que intenta descargar esta siendo editado por otro usuario.");
			e.printStackTrace();
		}
	 
	}
public static void descargar(InterfazS server, File src, File dest, String clientName) throws InterruptedException  {
		
	if(!cancelarDescarga()){
		try {
			boolean state = copy(server.getInputStream(src, clientName), new FileOutputStream(dest));

			if (state == false) {

				File file1 = new File(dest.getName());
				file1.delete();
				
			} else {

			}

		} catch (IOException | NullPointerException e) {
			System.out.println("El archivo que intenta descargar esta siendo editado por otro usuario.");
			e.printStackTrace();
		}
	}
	}

	public static void update(InterfazS server, File src, File dest, String clientName) throws IOException {
		copy(server.getInputStream2(src, clientName), new FileOutputStream(dest));
	}

	public static void main(String arg[]) throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		clientName = addr.getHostName();

		System.out.println("cliente: " + clientName);

		ResourceBundle properties = PropertyResourceBundle.getBundle("Simple");
		int port = Registry.REGISTRY_PORT;
		try {
			System.out.println("Porfavor digite el puerto para iniciar la conexion:  ");
			port = Integer.parseInt(arg[0]);
			//port = Integer.parseInt(properties.getString("server.port"));

		} catch (Exception e) {
			port = Registry.REGISTRY_PORT;
		}
		String command = null;
		if (arg.length > 0) {
			command = arg[0];
		}

		Scanner sc = new Scanner(System.in);
		System.out.println(" *Recuerda primero subir un archivo * \n");
		System.out.println("Escriba la accion deseada:  (subir,descargar,leer,editar,ayuda) : \n");
		while (sc.hasNext()) {
		  	
			
			command = sc.next();
			System.out.println(command);

			StringTokenizer tokens = new StringTokenizer(command, "_");
			command = tokens.nextToken().trim();
			try {
				String serverIP = System.getProperty("server.ip");

				if (null == serverIP) {
					try {
						serverIP = properties.getString("server.ip");
					} catch (MissingResourceException e) {
						throw new Exception(
								"Undefined server IP.  Please define 'server.ip' as system property (ex. java -Dserver.ip=xxx) or in the Simple.properties file.");
					}
				}

				InterfazS server = (InterfazS) Naming.lookup("//" + serverIP + ":" + port + "/SimpleServer");

				if (command.equalsIgnoreCase("ping")) {
					System.out.println(server.ping());

				} else if (command.equalsIgnoreCase("subir")) {

					Servidor.addTrans(clientName, command);
					String srcFilename = tokens.nextToken().trim();
					String destFilename = tokens.nextToken().trim();

					subir(server, new File(srcFilename), new File(destFilename), clientName);
				}else if(command.equalsIgnoreCase("ayuda")){
					
					System.out.println("Ejemplo: subir_archivodeseado.ext_nombrenuevo.ext \n");
					System.out.println("Ejemplo: descargar_archivodeseado.ext_nombrenuevo.ext \n");
					System.out.println("Ejemplo: editar_archivodeseado.ext  \n");
					System.out.println("Ejemplo: leer_archivodeseado.ext \n");

				} else if (command.equalsIgnoreCase("descargar")) {
					
					Servidor.addTrans(clientName, command);
					String srcFilename = tokens.nextToken().trim();
					String destFilename = tokens.nextToken().trim();

					descargar(server, new File(srcFilename), new File(destFilename), clientName);

				} else if (command.equalsIgnoreCase("leer")) {
					
					Servidor.addTrans(clientName, command);
					String srcFilename = tokens.nextToken().trim();

					descargarLec(server, new File(srcFilename), new File(clientName + srcFilename), clientName);
					if (estado == true) {
						boolean cerro = false;

						Desktop.getDesktop().open(new File(clientName + srcFilename));

						Thread.sleep(15000);
						File file1 = new File(clientName + srcFilename);
						boolean isDeleteSuccess;
						do {
							
							isDeleteSuccess = file1.delete();
							if (isDeleteSuccess)
								System.out.println(file1.getName() +"  " + " ha sido borrado de la memoria cache!");
							else {
								
								System.out.println("La operacion de borrado fallo.");

							}
						} while (!isDeleteSuccess);
					}

				} else if (command.equalsIgnoreCase("editar")) {

					Servidor.addTrans(clientName, command);
					String srcFilename = tokens.nextToken().trim();
					update(server, new File(srcFilename), new File(clientName + srcFilename), clientName);

					Desktop.getDesktop().open(new File(clientName + srcFilename));

					System.out.print("Usuario: " + clientName + "�Ya a terminado de editar el archivo? S/N : ");
					Scanner sc1 = new Scanner(System.in);

					while (sc.hasNext()) {
						String s1 = sc.next();
						if (s1.equalsIgnoreCase("s")) {
							break;
						}
						System.out.print("Usuario: " + clientName + "�Ya a terminado de editar el archivo? S/N : ");
					}

					subir(server, new File(clientName + srcFilename), new File(srcFilename), clientName);

					File file1 = new File(clientName + srcFilename);
					boolean isDeleteSuccess;
					do {
						
						isDeleteSuccess = file1.delete();
						if (isDeleteSuccess)
							System.out.println(file1.getName() +    " ha sido borrado con exito!");
						else {
							
							System.out.println("La operacion de borrado fallo.");

						}
					} while (!isDeleteSuccess);

				}

				else {
					System.out.println(server.runCommand(command, null));
				}

				System.out.println("Escriba la accion deseada:  (subir,descargar,leer,editar) : \n");
			} catch (Exception e) {
				System.out.println("SimpleClient exception: " + e.getMessage());
				e.printStackTrace();
			}
		}  
	}
	
	private static boolean cancelarDescarga() throws InterruptedException{
		Thread.sleep(5000);
		System.out.println("�Cancelar la descarga? (S/N)");
		Scanner sc = new Scanner(System.in);
		String res = sc.nextLine();
		if(res.equalsIgnoreCase("N")){
			return false;
		}else{
			System.out.println("Descarga cancelada");
			return true;
		}
	}
}