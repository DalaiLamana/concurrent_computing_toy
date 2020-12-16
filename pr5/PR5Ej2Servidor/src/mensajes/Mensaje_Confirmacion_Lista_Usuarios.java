package mensajes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
public class Mensaje_Confirmacion_Lista_Usuarios extends Mensaje_Confirmacion implements Serializable{
	HashMap<String, ArrayList<String>> _usuarios;
	public Mensaje_Confirmacion_Lista_Usuarios(String origen, String destino, HashMap<String, ArrayList<String>> usuarios) {
		super(MensajeType.MENSAJE_CONFIRMACION_LISTA_USARIOS, origen, destino);
		this._usuarios = usuarios;
	}
	public HashMap<String, ArrayList<String>> get_usuarios() {
		return _usuarios;
	}
	public void set_usuarios(HashMap<String, ArrayList<String>> _usuarios) {
		this._usuarios = _usuarios;
	}
}
