package mensajes;

public class Mensaje_Anadir_Fichero extends Mensaje{

	private static final long serialVersionUID = 3221318426474306392L;
	private String _file;
	public Mensaje_Anadir_Fichero(String origen, String destino, String file) {
		super(MensajeType.MENSAJE_ANADIR_FICHERO, origen, destino);
		this.set_file(file);
	}
	public String get_file() {
		return _file;
	}
	public void set_file(String _file) {
		this._file = _file;
	}
}
