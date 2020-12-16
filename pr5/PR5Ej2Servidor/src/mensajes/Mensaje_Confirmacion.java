package mensajes;

import java.io.Serializable;

public class Mensaje_Confirmacion extends Mensaje implements Serializable{
	public Mensaje_Confirmacion(MensajeType type, String origen, String destino) {
		super(type, origen, destino);
	}
}
