package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import mensajes.Mensaje;
import mensajes.Mensaje_Confirmacion_Conexion;
import mensajes.Mensaje_Confirmacion_Cerrar_Conexion;
import mensajes.Mensaje_Anadir_Fichero;
import mensajes.Mensaje_Confirmacion_Lista_Usuarios;
import mensajes.Mensaje_Emitir_Fichero;
import mensajes.Mensaje_Preparado_ClienteServidor;
import mensajes.Mensaje_Preparado_ServidorCliente;

public class OyenteServidor extends Thread {
	ObjectInputStream _fin;
	ObjectOutputStream _fout;
	int _serverPort; // Podria hacer un getter en cliente, pero un int no cuesta mucha memoria y así
						// es más rápido.
	String _serverAddr;
	String _myIp;
	String _myId;

	OyenteServidor(ObjectOutputStream fout, ObjectInputStream fin, int serverPort, String serverAddr, String _myIp)
			throws IOException {
		this._serverPort = serverPort;
		this._serverAddr = serverAddr;
		this._fout = fout;
		this._fin = fin;
	}

	public void run() {
		Mensaje m;
		boolean working = true;
		try {
			while (working) {
				_fout.reset();
				_fout.flush();
				m = (Mensaje) _fin.readObject();
				switch (m.getType()) {
				case MENSAJE_CONFIRMACION_CONEXION:
					System.out.println("Conexion establecida");
					break;
				case MENSAJE_CONFIRMACION_LISTA_USARIOS:
					// Envio mensaje confirmacion lista de usuarios por fout
					System.out.println("Lista de usuarios entregada");
					HashMap<String, ArrayList<String>> usuarios = ((Mensaje_Confirmacion_Lista_Usuarios) m)
							.get_usuarios();
					for (HashMap.Entry<String, ArrayList<String>> entry : usuarios.entrySet()) {
						System.out.println("El usuario " + entry.getKey() + " dispone de los siguientes ficheros:");
						for (String file : entry.getValue()) {
							System.out.println(file);
						}
					}
					break;
				case MENSAJE_PREPARADO_SERVIDORCLIENTE:
					// Eliminar info de tablas de usuario
					((Mensaje_Preparado_ServidorCliente) m).get_addr();
					System.out.println("Creando receptor");
					Receptor r =new Receptor(((Mensaje_Preparado_ServidorCliente) m).get_addr(),
							((Mensaje_Preparado_ServidorCliente) m).get_port());
					r.start();
					r.join();
					_fout.writeObject(new Mensaje_Anadir_Fichero(m.get_destino(), _serverAddr, r.get_file()));
					break;
				case MENSAJE_CONFIRMACION_CERRAR_CONEXION:
					System.out.println("Cerrando conexion");
					working = false;
					break;
				case MENSAJE_EMITIR_FICHERO:
					int puerto;
					do {
						puerto = (int) (Math.random() * 10248) + 1024;
					} while (puerto == _serverPort);
					// Adquiero flujo por id de cliente, envio mensaje
					// Mensaje_Preparado_ServidorCliente
					System.out.println("Creando emisor");
					(new Emisor(puerto, ((Mensaje_Emitir_Fichero) m).get_file())).start();
					_fout.writeObject(
							new Mensaje_Preparado_ClienteServidor(m.get_destino(), m.get_origen(), _myIp, puerto));
					break;
				default:
					System.out.println("Mensaje no reconocido");
					break;
				}
			}

		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

	}
}
