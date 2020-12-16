
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
	public static void main(String[] args) {
		if (args.length < 3) {
			System.err.println("Argumentos insuficientes");
			System.err.println("hostname puerto archivo");
			return;
		}
		String hostname = args[0];
		String archivo = args[2];
		int port = Integer.parseInt(args[1]);
		try {
		Socket socket = new Socket(hostname, port);
		
		//"Envoltorios" y flujos
		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

		BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
		objectOutput.writeObject(archivo);
		String contentOfFile = (String) objectInput.readObject();
		bw.write(contentOfFile);
		objectOutput.writeObject("1");
		bw.close();
		socket.close();
		}catch(IOException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

}
