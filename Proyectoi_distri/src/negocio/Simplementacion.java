package negocio;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import RMI.RMIInputStream;
import RMI.RMIInputStreamImpl;
import RMI.RMIOutputStream;
import RMI.RMIOutputStreamImpl;


public class Simplementacion extends UnicastRemoteObject implements InterfazS {

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_MAX_THREAD_COUNT = 5;

	private static Vector<Thread> pendingCommandThreads = new Vector<Thread>();
	private static Vector<Thread> runningCommandThreads = new Vector<Thread>();

	List<ArchivoCache> archivos = new ArrayList<ArchivoCache>();

	public Simplementacion() throws RemoteException {
	}

	@Override
	public String ping() {
		return "Se ha conectado!";
	}

	@Override
	public String runCommand(String command, String[] envp) throws RemoteException {

		CommandThread t = new CommandThread(command, envp);
		try {

			if (getActiveThreadCount() < getMaxThreadCount()) {
				runningCommandThreads.add(t);
				t.start();
			} else {
				pendingCommandThreads.add(t);
				System.out.println("Queued (thread: " + t.getName() + "): " + command);
			}
			t.join();

		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}

		return t.getResults();
	}

	public OutputStream getOutputStream(File f, String clientName) throws IOException {
		System.out.println("Subiendo el archivo: " + f.getName() + " por el cliente: " + clientName);

		ArchivoCache archivoCache = new ArchivoCache(f.getName());

		archivoCache.agregarLector(clientName);

		Iterator<ArchivoCache> it = archivos.iterator();
		while (it.hasNext()) {
			ArchivoCache mi = it.next();
			if (mi.getStoredFile().equals(f.getName())) {

				if (mi.getDueno() != null) {

					it.remove();

					archivos.add(archivoCache);

					return new RMIOutputStream(new RMIOutputStreamImpl(new FileOutputStream(f)));

				}

			}

		}

		archivos.add(archivoCache);

		return new RMIOutputStream(new RMIOutputStreamImpl(new FileOutputStream(f)));

	}

	public InputStream getInputStream(File f, String clientName) throws IOException {

		System.out.println("Descarga de archivo:  " + f.getName());

		Iterator<ArchivoCache> it = archivos.iterator();

		while (it.hasNext()) {

			ArchivoCache mi = it.next();
			if (mi.getStoredFile().equals(f.getName())) {

				if (mi.getDueno() == null) {

					return new RMIInputStream(new RMIInputStreamImpl(new FileInputStream(f)));

				} else if (mi.getDueno() != null) {
					return null;
				}

			}

		}
		return new RMIInputStream(new RMIInputStreamImpl(new FileInputStream(f)));

	}

	public InputStream getInputStream2(File f, String clientName) throws IOException {

		System.out.println("Editar archivo:  " + f.getName());

		Iterator<ArchivoCache> it = archivos.iterator();
		while (it.hasNext()) {

			ArchivoCache mi = it.next();
			if (mi.getStoredFile().equals(f.getName())) {

				if (mi.getDueno() == null) {
					System.out.println("Cliente nulo");
					mi.setDueno(clientName);
					it.remove();

					archivos.add(mi);

					return new RMIInputStream(new RMIInputStreamImpl(new FileInputStream(f)));
				}

			}

		}

		return new RMIInputStream(new RMIInputStreamImpl(new FileInputStream(f)));
	}

	public static int getPendingThreadCount() {
		return pendingCommandThreads.size();
	}

	public static int getActiveThreadCount() {
		return runningCommandThreads.size();
	}

	protected int getMaxThreadCount() {
		return DEFAULT_MAX_THREAD_COUNT;
	}

	class CommandThread extends Thread {
		private String command = null;
		private String[] envp = null;
		private StringBuffer results = new StringBuffer();

		public CommandThread(String command, String[] envp) {
			super();
			this.command = command;
			this.envp = envp;
		}

		public void run() {
			long startTime = System.currentTimeMillis();

			if (Simplementacion.getActiveThreadCount() > 1) {
				try {
					sleep((long) (Math.random() * 2000));
				} catch (InterruptedException e1) {
				}
			}

			try {
				System.out.println("Corriendo (Hilo: " + this.getName() + ") : " + command);

				Process cmdProcess = Runtime.getRuntime().exec(command, envp);

				BufferedReader stdInput = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));

				BufferedReader stdError = new BufferedReader(new InputStreamReader(cmdProcess.getErrorStream()));

				String s = null;

				while ((s = stdInput.readLine()) != null) {
					results.append(s);
				}

				while ((s = stdError.readLine()) != null) {
					results.append(s);
				}

			} catch (IOException e) {
				results.append(e.getMessage());
			} finally {
				long endTime = System.currentTimeMillis();
				runningCommandThreads.remove(this);
				System.out.println("Completed (thread: " + this.getName() + ") in " + (endTime - startTime) + " ms");

				if (getPendingThreadCount() > 0 && getActiveThreadCount() < getMaxThreadCount()) {
					Thread t = pendingCommandThreads.remove(0);
					runningCommandThreads.add(t);
					t.start();
				}
			}
		}

		public String getResults() {
			return results.toString();
		}

	}
}
