package mensajes;

import java.io.Serializable;

public abstract class Mensaje implements Serializable{
	
	private String _origen;
	private String _destino;
	private MensajeType _type;
	public Mensaje(MensajeType type, String origen, String destino){
		this._type = type;
		this._origen = origen;
		this._destino = destino;
	}
	public MensajeType getType() {
		return this._type;
	}
	public String get_origen() {
		return _origen;
	}
	public void set_origen(String _origen) {
		this._origen = _origen;
	}
	public String get_destino() {
		return _destino;
	}
	public void set_destino(String _destino) {
		this._destino = _destino;
	}
}
