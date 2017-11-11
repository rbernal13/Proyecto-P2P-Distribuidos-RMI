package negocio;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ArchivoCache {
	
	
	
	
	private static final String ROOT_DIR = "\\tmp\\";
	private LinkedList<String> lectores;
	private String dueno;
	private File storedFile;
	private byte[] data;
	
	
	public ArchivoCache(String fileName) throws IOException {
		dueno = null;
		lectores = new LinkedList<String>();
		storedFile = new File(ROOT_DIR + fileName);
		data = new byte[(int) storedFile.length()];
		
	}
	
	
	
    public String getStoredFile() {
		return storedFile.getName();
	}



	public void setStoredFile(File storedFile) {
		this.storedFile = storedFile;
	}



	public void agregarLector(String client) {
        if (!lectores.contains(client))
        	lectores.add(client);
    }
	
    public List<String> getReaders() {
		return Collections.unmodifiableList(lectores);
	}
    
	public String getDueno() {
		return dueno;
	}

	public void setDueno(String dueno) {
		this.dueno = dueno;
	}
	
	public void sinDueno(){
		dueno =null;
	}
    
    
    


}
