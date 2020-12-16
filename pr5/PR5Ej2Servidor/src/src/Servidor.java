package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	// Tabla usuario (id usuario, ref socket o flujos in and out mejor los flujos)
	// Tabla información (id usuario, lista ficheros)
	// Direccion op
	// Puerto
	// Server socket
	public static void main(String[] args) throws InterruptedException {
		if (args.length < 2) {
			System.err.println("Faltan argumentos: direccion local, puerto");
			return;
		}
		try {
			int _port = Integer.parseInt(args[1]);
			String _addr = args[0];
			MonitorData _data = new MonitorData(_addr);
			ServerSocket serverSocket = new ServerSocket(_port);
			Runtime.getRuntime().addShutdownHook(new Thread() {
		    	@Override
		    	public void run() {
		    		try {
						serverSocket.close();
					} catch (IOException e) {
						
						System.err.println(e.getMessage());
					}
		        	System.out.println("System was shutdown");
		    	}
			});
			//Socket socket = serverSocket.accept();
			//OyenteCliente oyente = new OyenteCliente(new ObjectOutputStream(socket.getOutputStream()), new ObjectInputStream(socket.getInputStream()), _data);
			//oyente.start();
			while (true) {

				Socket socket = serverSocket.accept();
				(new OyenteCliente(new ObjectOutputStream(socket.getOutputStream()), new ObjectInputStream(socket.getInputStream()), _data)).start();
			}
			//serverSocket.close();
			//oyente.join();
			//serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
