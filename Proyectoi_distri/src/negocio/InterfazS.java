package negocio;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;


public interface InterfazS extends java.rmi.Remote {

	String ping() throws RemoteException;
	

	String runCommand(String command, String[] envp) throws RemoteException;
	

	OutputStream getOutputStream(File f, String clientName) throws IOException;
	

	InputStream getInputStream(File f, String clientName) throws IOException;
	
	
	
	
	InputStream getInputStream2(File f,String clientName) throws IOException;
}