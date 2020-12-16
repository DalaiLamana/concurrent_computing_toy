package src;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Receptor extends Thread {
	Socket _socket;
	String _file;
	public Receptor(String _addr, int port) throws UnknownHostException, IOException {
		_socket = new Socket(_addr, port);
	}
	
	public void run() {
		try {
			ObjectInputStream fin = new ObjectInputStream(_socket.getInputStream());
			FileClass file = (FileClass) fin.readObject();
			file.save_file();
			this._file = file.get_name(); 
			System.out.println("Archivo recibido");
			_socket.close();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public String get_file() {
		return this._file;
	}
}
