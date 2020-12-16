package mensajes;

import java.io.Serializable;

public class Mensaje_Confirmacion_Conexion extends Mensaje_Confirmacion implements Serializable{

	public Mensaje_Confirmacion_Conexion(String origen, String destino) {
		super(MensajeType.MENSAJE_CONFIRMACION_CONEXION, origen, destino);
	}
}
