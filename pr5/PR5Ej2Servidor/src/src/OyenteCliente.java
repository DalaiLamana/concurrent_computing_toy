package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mensajes.Mensaje;
import mensajes.Mensaje_Anadir_Fichero;
import mensajes.Mensaje_Conexion;
import mensajes.Mensaje_Confirmacion_Cerrar_Conexion;
import mensajes.Mensaje_Confirmacion_Conexion;
import mensajes.Mensaje_Confirmacion_Lista_Usuarios;
import mensajes.Mensaje_Emitir_Fichero;
import mensajes.Mensaje_Pedir_Fichero;
import mensajes.Mensaje_Cerrar_Conexion;
import mensajes.Mensaje_Lista_Usuarios;
import mensajes.Mensaje_Preparado_ClienteServidor;
import mensajes.Mensaje_Preparado_ServidorCliente;

public class OyenteCliente extends Thread {
	private ObjectInputStream _fin;
	private ObjectOutputStream _fout;
	private static MonitorData _monitor;

	OyenteCliente(ObjectOutputStream fout, ObjectInputStream fin, MonitorData monitor) throws IOException {
		this._fout = fout;
		this._fin = fin;
		_monitor = monitor;
	}

	public void run() {
		Mensaje m; boolean working = true;
		while (working) {
			try {
				_fout.reset();
				_fout.flush();
				m = (Mensaje) _fin.readObject();
				switch (m.getType()) {
				case MENSAJE_CONEXION:
					System.out.println("Cliente " + m.get_origen() + " conectado.");
					_monitor.putFlujos(m.get_origen(), new Flujos(this._fout, this._fin));
					_monitor.putInfo(m.get_origen(), ((Mensaje_Conexion) m).get_ficheros());
					this._fout.writeObject(
							new Mensaje_Confirmacion_Conexion(this._monitor.get_server_ip(), m.get_origen()));
					break;
				case MENSAJE_LISTA_USARIOS:
					System.out.println("Entregando lista usuarios...");
					// Envio mensaje confirmacion lista de usuarios por fout
					this._fout.writeObject(new Mensaje_Confirmacion_Lista_Usuarios(this._monitor.get_server_ip(),
							m.get_origen(), this._monitor.get_info()));
					break;
				case MENSAJE_CERRAR_CONEXION:
					System.out.println("Cerrando conexion...");
					// Eliminar info de tablas de usuario
					_monitor.remove_flujos(m.get_origen());
					_monitor.remove_info(m.get_origen());
					this._fout.writeObject(
							new Mensaje_Confirmacion_Cerrar_Conexion(_monitor.get_server_ip(), m.get_origen()));
					working = false;
					break;
				case MENSAJE_PEDIR_FICHERO:
					System.out.println("Solicitando fichero...");
					String fichero = ((Mensaje_Pedir_Fichero) m).get_file();
					// Busco id por fichero
					String id = _monitor.get_client_by_file(fichero);
					// Mando Mensaje_Pedir_Fichero
					_monitor.get_fout_by_client(id)
							.writeObject(new Mensaje_Emitir_Fichero(m.get_origen(), m.get_destino(), fichero));
					break;
				case MENSAJE_PREPARADO_CLIENTESERVIDOR:
					System.out.println("Llegada CS, preparando SC...");
					String targetId = ((Mensaje_Preparado_ClienteServidor) m).get_destino();
					// Adquiero flujo por id de cliente, envio mensaje
					// Mensaje_Preparado_ServidorCliente
					_monitor.get_fout_by_client(targetId)
							.writeObject(new Mensaje_Preparado_ServidorCliente(m.get_origen(), m.get_destino(),
									((Mensaje_Preparado_ClienteServidor) m).get_addr(),
									((Mensaje_Preparado_ClienteServidor) m).get_port()));
					break;
				case MENSAJE_ANADIR_FICHERO:
					System.out.println("Añadiendo fichero...");
					_monitor.addInfo(m.get_origen(), ((Mensaje_Anadir_Fichero)m).get_file());
				default:
					System.out.println("Mensaje no reconocido");
					break;
				
				}
			} catch (IOException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
