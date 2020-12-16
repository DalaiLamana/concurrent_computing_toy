

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

interface Almacen {
	public boolean almacenar(int producto, int times);

	public ArrayList<Integer> extraer(int times);

	public boolean es_vacio();
}

/*
 * class AlmacenPequeño implements Almacen { //Monitor private static volatile
 * Integer producto = null;
 * 
 * public AlmacenPequeño() { }
 * 
 * public synchronized boolean almacenar(int p) { //True si ha almacenado, false
 * si no if(es_vacio()){ producto = p; return true; } else return false; }
 * 
 * public synchronized int extraer() { int p = -1; if(!es_vacio()){ p =
 * producto.intValue(); producto = null; } return p; } public boolean
 * es_vacio(){ return producto == null; } }
 */

class AlmacenGrande implements Almacen {
	private static volatile Queue<Integer> store;
	private final int max;

	public AlmacenGrande(int n) {
		store = new LinkedList<Integer>();
		max = n;
	}

	public synchronized boolean almacenar(int producto, int times) {
		// System.out.println(store.size() + " "+ times);
		boolean b = false;
			try {
				while(store.size() > max - times)
					wait();

				 System.out.println(store.size() + " "+ times);
				for (int i = 0; i < times; i++)
					store.add(producto);
				store = store;
				b = true;
				notifyAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				return b;
			}
		
	}

	public synchronized ArrayList<Integer> extraer(int times) {
		ArrayList<Integer> prod = new ArrayList<Integer>();
		prod.add(-1);
		

			try {
				if (store.size() - times < 0)
					store.wait();
				System.out.println(store.size() + " extract "+ times);
				prod.clear();
				for (int i = 0; i < times; i++)
					prod.add(store.poll().intValue());

				store = store;
				notifyAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				return new ArrayList<Integer>(prod);
			}

	}

	public boolean es_vacio() {
		return store.isEmpty();
	}

	public boolean hay_hueco() {
		return store.size() < max;
	}
}

class Consm extends Thread {
	Almacen alm;
	int id;

	Consm(Almacen dato, int i) {
		alm = dato;
		id = i;
	}

	public void run() {
		int k = 0;
		// System.out.println("Extrayendo ");
		while (k < 10) {
			int j = 0;
			// int numProds = ThreadLocalRandom.current().nextInt(1, 5);
			int numProds = 5;
			ArrayList<Integer> prod = new ArrayList<Integer>();
			while (alm.es_vacio())
				;// Comprobacion candidata
			prod = alm.extraer(numProds);
			while (j < numProds && prod.get(j) != -1)
				j++;
			if (j == numProds)
				k++;
			else
				System.out.println("Está vacio...");
		}
		System.out.println(id + " ha terminado");
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
		int k = 0;
		while (k < 10) {
			// System.out.println("Almacenando " + prod);
			// while(!alm.is_empty())//Comprobacion candidata
			// int numProds = ThreadLocalRandom.current().nextInt(1, 5);
			int numProds = 5;
			while (!((AlmacenGrande) alm).hay_hueco())
				;
			if (alm.almacenar(prod, numProds))
				k++;
			// else System.out.println("Pues estaba lleno...");
		}
	}
}

class Main {
	private static final int P = 20;
	private static final int C = 20;
	private static Almacen alm;

	public static void main(String[] args) {
		alm = new AlmacenGrande(50);
		Thread ths[] = new Thread[P + C];
		for (int i = 0; i < P; i++) {
			ths[i] = new Prod(alm, i);
			ths[i].start();
		}
		for (int i = 0; i < C; i++) {
			ths[P + i] = new Consm(alm, i);
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
