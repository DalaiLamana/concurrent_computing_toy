package mensajes;

import java.io.Serializable;

public class Mensaje_Preparado_ClienteServidor extends Mensaje implements Serializable{
	private static final long serialVersionUID = 7624935349125859850L;
	private int _port;
	private String _addr;
	public Mensaje_Preparado_ClienteServidor(String origen, String destino, String addr, int port) {
		super(MensajeType.MENSAJE_PREPARADO_CLIENTESERVIDOR, origen, destino);
		this._addr = addr;
		this._port = port;
		
	}
	public int get_port() {
		return _port;
	}
	public void set_port(int _port) {
		this._port = _port;
	}
	public String get_addr() {
		return _addr;
	}
	public void set_addr(String _addr) {
		this._addr = _addr;
	}
}
