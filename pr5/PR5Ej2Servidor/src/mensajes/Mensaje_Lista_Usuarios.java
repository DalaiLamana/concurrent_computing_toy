package mensajes;

import java.io.Serializable;

public class Mensaje_Lista_Usuarios extends Mensaje implements Serializable{
	public Mensaje_Lista_Usuarios(String origen, String destino) {
		super(MensajeType.MENSAJE_LISTA_USARIOS, origen, destino);
	}
}
