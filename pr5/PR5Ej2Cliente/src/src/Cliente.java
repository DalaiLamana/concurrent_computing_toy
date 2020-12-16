package src;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import mensajes.Mensaje_Cerrar_Conexion;
import mensajes.Mensaje_Conexion;
import mensajes.Mensaje_Lista_Usuarios;
import mensajes.Mensaje_Pedir_Fichero;

enum MenuOptions{
	CONSULTA_LISTA_USUARIOS,
	PEDIR_FICHERO,
	/*MODIFICAR_INFO,*/
	SALIR
}
class KBManager{
	BufferedReader _br;
	KBManager(){
		InputStreamReader _kbInput = new InputStreamReader(System.in);
		_br = new BufferedReader(_kbInput);
	}
	
	protected MenuOptions DisplayMenu() throws IOException {
		int opcion;
		
		System.out.println("Selecciona una opción");
		System.out.println("********************************");
		System.out.println("Consultar lista de usuarios -> 0");
		System.out.println("********************************");
		System.out.println("Pedir fichero ---------------> 1");
		System.out.println("********************************");
		/*System.out.println("Modificar informacion -------> 2");
		System.out.println("********************************");*/
		System.out.println("Salir -----------------------> 2");
		System.out.println("********************************");
		do {
			try {
			opcion = Integer.parseInt(this._br.readLine());
			}catch (NumberFormatException e) {
				System.err.println("Opcion no valida");
				opcion = -1;
			}
		}while(opcion > 2 || opcion <0);
		switch(opcion) {
		case 0:
			return MenuOptions.CONSULTA_LISTA_USUARIOS;
		case 1:
			return MenuOptions.PEDIR_FICHERO;
		/*case 2:
			return MenuOptions.MODIFICAR_INFO;*/
		case 2:
			return MenuOptions.SALIR;
		}
		//nunca llega aqui
		return null;
	}
	protected String get_id() throws IOException {
		System.out.println("Escriba su id: ");
		return _br.readLine();
	}
	protected String readLine() throws IOException {
		return this._br.readLine();
	}
	
}

public class Cliente {
	
	public static void load_files(ArrayList<String> _files) throws IOException {
		String current = new java.io.File( "." ).getCanonicalPath();
		System.out.println(current);
		File f = new File(current);
		File[] files = f.listFiles();
		System.out.println("Files are:");
		String aux;
		// Display the names of the files
		for (int i = 0; i < files.length; i++) {
			aux = files[i].getName();
			if(aux.split("\\.").length >= 2 && !aux.split("\\.")[0].equals("")) {
				_files.add(aux);
				System.out.println(aux);
			}
		}
	}
	
	public static void main(String[] args) {
		
		if (args.length < 3) {
			System.err.println("Faltan argumentos: direccion local, direccion de servidor, puerto");
			return;
		}
		KBManager _kb = new KBManager();
		ArrayList<String> _files = new ArrayList<String>();
		try {
			String _serverAddr = args[1];
			String _id = _kb.get_id();
			int _port = Integer.parseInt(args[2]);
			String _myIp = args[0];
			Socket _socket = new Socket(_serverAddr, _port);
			
			ObjectOutputStream _fout = new ObjectOutputStream(_socket.getOutputStream());
			OyenteServidor _oyente = new OyenteServidor(_fout, new ObjectInputStream(_socket.getInputStream()), _port, _serverAddr, _myIp);
			load_files(_files);
			_oyente.start();
			_fout.writeObject(new Mensaje_Conexion(_id, _serverAddr, _files));
			boolean working = true;
			while(working) {
				MenuOptions opcion = _kb.DisplayMenu();
				_fout.reset();
				//parse_option(opcion,_fout, _id, serverAddr, _kb);
				switch(opcion) {
				case CONSULTA_LISTA_USUARIOS:
					System.out.println("Solicitando lista usuarios...");
					_fout.writeObject(new Mensaje_Lista_Usuarios(_id, _serverAddr));
					break;
				case PEDIR_FICHERO:
					System.out.println("Introduzca el nombre del fichero que desea: ");
					_fout.writeObject(new Mensaje_Pedir_Fichero(_id, _serverAddr, _kb.readLine()));
					break;
				case SALIR:
					System.out.println("Saliendo... ");
					_fout.writeObject(new Mensaje_Cerrar_Conexion(_id, _serverAddr));
					_oyente.join();
					_socket.close();
					working = false;
					break;
				}
			}
			
			
		} catch (IOException | InterruptedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
