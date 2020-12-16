package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Flujos{
		ObjectInputStream _in;
		ObjectOutputStream _out;

		public Flujos( ObjectOutputStream r, ObjectInputStream l) {
			this._out = r;
			this._in = l;
			
		}

		public Flujos(Socket s) throws IOException {
			this._out  = new ObjectOutputStream(s.getOutputStream());
			this._in= new ObjectInputStream(s.getInputStream());
		}

		// No seguro
		public ObjectOutputStream get_fout() {
			return _out;
		}

		// No seguro
		public ObjectInputStream get_fin() {
			return _in;
		}

		public void set_fout(ObjectOutputStream r) {
			this._out = r;
		}

		public void set_fin(ObjectInputStream l) {
			this._in = l;
		}
	}