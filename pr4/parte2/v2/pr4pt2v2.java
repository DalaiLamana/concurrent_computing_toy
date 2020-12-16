
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

interface Almacen {
	public void almacenar(int producto);

	public ArrayList<Integer> extraer();

	public boolean es_vacio();
}

/*
 * class AlmacenPequeño implements Almacen { //Monitor private static volatile
 * Integer producto = null;
 * 
 * public AlmacenPequeño() { }
 * 
 * public synchronized void almacenar(int p) { //True si ha almacenado, false si
 * no if(es_vacio()){ producto = p; return true; } else return false; }
 * 
 * public synchronized int extraer() { int p = -1; if(!es_vacio()){ p =
 * producto.intValue(); producto = null; } return p; } public boolean
 * es_vacio(){ return producto == null; } }
 */

class AlmacenGrande implements Almacen {
	private static volatile Queue<Integer> store;
	private final ReentrantLock mutex;
	private final Condition notFull;
	private final Condition notEmpty;
	private volatile static int _max_size;

	public AlmacenGrande(int n) {
		store = new LinkedList<Integer>();
		mutex = new ReentrantLock();
		notFull = mutex.newCondition();
		notEmpty = mutex.newCondition();
		_max_size = n;
	}

	public void almacenar(int producto) {
		mutex.lock();

		// int numProds = ThreadLocalRandom.current().nextInt(0, (_max_size -
		// store.size() + 1));
		int numProds = 5;
		try {
			while (_max_size < numProds + store.size())
				notFull.await(); // Esperando a que haya hueco

			int i = 0;
			while (store.size() < _max_size && i < numProds) {
				store.add(producto);
				i++;
			}
			notEmpty.signal();
		} catch (Exception e) {
			System.err.println(e.getMessage() + store.size());
			e.printStackTrace();
		} finally {
			mutex.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Integer> extraer() {
		mutex.lock();
		ArrayList<Integer> prods = new ArrayList<Integer>();
		prods.add(-1);
		// int numProds = ThreadLocalRandom.current().nextInt(1, store.size() + 1);
		int numProds = 5;
		try {

			while (store.size() < numProds)
				notEmpty.await(); // Esperando a que no este vacio
			System.out.println("Extrayendo ");
			prods = new ArrayList<Integer>();
			int i = 0;
			while (i < numProds) {
				prods.add(store.poll().intValue());
				i++;
			}
			notFull.signal();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			mutex.unlock();
			return (ArrayList<Integer>) prods.clone();
		}

	}

	public boolean es_vacio() {
		return store.isEmpty();
	}

	public boolean hay_hueco() {
		return _max_size > store.size();
	}
}

class Consm extends Thread {
	Almacen alm;

	Consm(Almacen dato) {
		alm = dato;
	}

	public void run() {
		int k = 0;
		while (k < 10) {
			ArrayList<Integer> prod = new ArrayList<Integer>(alm.extraer());
			System.out.println("Se han consumido los objetos:");
			int j = 0;
			boolean valid = true;
			while (j < prod.size() && valid) {
				if (prod.get(j) == -1) {
					System.out.println(j);
					valid = false;
				}
				j++;
			}
			if (valid)
				k++;
		}
	}
}

class Prod extends Thread {
	Almacen alm;
	int prod;

	Prod(Almacen dato, int producto) {
		alm = dato;
		prod = producto;
	}

	public void run() {
		synchronized (alm) {
			int k = 0;
			while (k < 10) {
				alm.almacenar(prod);
				k++;
				System.out.println("Almacenado " + prod);
			}
		}
	}
}

class Main {
	private static final int P = 50;
	private static final int C = 50;

	public static void main(String[] args) {
		Almacen alm = new AlmacenGrande(200);
		Thread ths[] = new Thread[P + C];
		for (int i = 0; i < P; i++) {
			ths[i] = new Prod(alm, i);
			ths[i].start();
		}
		for (int i = 0; i < C; i++) {
			ths[P + i] = new Consm(alm);
			ths[P + i].start();
		}
		for (Thread th : ths) {
			try {
				th.join();
			} catch (Exception e) {
				System.err.println(e);
			}
		}
		if (alm.es_vacio())
			System.out.println("El almacen esta vacio");
	}
}

