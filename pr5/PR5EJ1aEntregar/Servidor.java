import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Servidor {
	public static void main(String[] args){
		if (args.length < 1) {
			System.err.println("No hay argumento: puerto");
			return;
		}
		try {
		int port = Integer.parseInt(args[0]);
		ServerSocket serverSocket = new ServerSocket(port);
		Socket socket = serverSocket.accept();
		ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

		String clientRequest = (String) objectInput.readObject();

		readFileAndSend(clientRequest, objectOutput);
		clientRequest = (String) objectInput.readObject();
		if (!clientRequest.equals("1")) {
			System.err.println("Todo mal");
		}
		serverSocket.close();
		}catch(IOException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void readFileAndSend(String file, ObjectOutputStream objectOutput) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		Scanner sc = new Scanner(new File(file));
		String contentOfFile = sc.useDelimiter("\\Z").next();
		objectOutput.writeObject(contentOfFile);
		sc.close();
		br.close();
	}
}
