package mensajes;
import java.io.Serializable;
import java.util.ArrayList;
public class Mensaje_Conexion extends Mensaje implements Serializable{
	ArrayList<String> _ficheros;
	public ArrayList<String> get_ficheros() {
		return _ficheros;
	}
	public void set_ficheros(ArrayList<String> _ficheros) {
		this._ficheros = _ficheros;
	}
	public Mensaje_Conexion(String origen, String destino, ArrayList<String> ficheros) {
		super(MensajeType.MENSAJE_CONEXION, origen, destino);
		this._ficheros = ficheros;
	}
}
