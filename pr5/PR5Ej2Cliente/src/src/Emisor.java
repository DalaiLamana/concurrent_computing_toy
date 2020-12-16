package src;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Emisor extends Thread {
	ServerSocket _serverSocket;
	String _file;
	int _port;

	public Emisor(int port, String file) throws UnknownHostException, IOException {
		_serverSocket = new ServerSocket(port);
		_file = file;
		do {
			_port = (int) (Math.random() * 10248) + 1024;
		} while (_port != port);
	}

	public void run() {
		try {
			Socket socket = _serverSocket.accept();
			ObjectOutputStream fout = new ObjectOutputStream(socket.getOutputStream());
			FileClass file = new FileClass(_file);
			fout.writeObject(file);
			System.out.println("Archivo enviado");
			fout.close();
			socket.close();
			_serverSocket.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
