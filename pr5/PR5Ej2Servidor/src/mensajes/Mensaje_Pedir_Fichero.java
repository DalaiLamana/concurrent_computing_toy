package mensajes;

import java.io.Serializable;

public class Mensaje_Pedir_Fichero extends Mensaje implements Serializable{
	private String _file;
	public Mensaje_Pedir_Fichero(String origen, String destino, String file) {
		super(MensajeType.MENSAJE_PEDIR_FICHERO, origen, destino);
		this.set_file(file);
	}
	public String get_file() {
		return _file;
	}
	public void set_file(String _file) {
		this._file = _file;
	}
}
