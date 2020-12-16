package mensajes;

import java.io.Serializable;

public class Mensaje_Confirmacion_Cerrar_Conexion extends Mensaje_Confirmacion implements Serializable {

	public Mensaje_Confirmacion_Cerrar_Conexion(String origen, String destino) {
		super(MensajeType.MENSAJE_CONFIRMACION_CERRAR_CONEXION, origen, destino);
	}

}
