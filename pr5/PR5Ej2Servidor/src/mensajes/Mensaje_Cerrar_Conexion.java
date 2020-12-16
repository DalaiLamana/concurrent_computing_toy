package mensajes;

import java.io.Serializable;

public class Mensaje_Cerrar_Conexion extends Mensaje implements Serializable {
	public Mensaje_Cerrar_Conexion(String origen, String destino) {
		super(MensajeType.MENSAJE_CERRAR_CONEXION, origen, destino);
	}
}
