
import java.util.concurrent.Semaphore;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;

interface Almacen {
    public boolean almacenar(int producto);
    public int extraer();
    public boolean es_vacio();
}

class AlmacenPequeño implements Almacen { //Monitor
    private static volatile Integer producto = null;
 
    public AlmacenPequeño() {
    }

    public synchronized boolean almacenar(int p) { //True si ha almacenado, false si no
        if(es_vacio()){
            producto = p;
            return true;
        }
        else return false;
    }

    public synchronized int extraer() {
        int p = -1;
        if(!es_vacio()){
            p = producto.intValue();
            producto = null;
        }
            return p;
    }
    public boolean es_vacio(){
        return producto == null;
    }
}

class AlmacenGrande implements Almacen {
    private final Queue<Integer> store;
    private final int max;
    public AlmacenGrande(int n) {
        store = new LinkedList<Integer>();
        max = n;
    }

    public synchronized boolean almacenar(int producto) {
        if(store.size() < max){
            store.add(producto);
            return true;
        }
        return false;
    }

    public synchronized int extraer() {
        int producto = -1;
        if(!store.isEmpty()){
            producto = store.poll().intValue();
        }
        return producto;
    }
    public boolean es_vacio(){
        return store.isEmpty();
    }
    public boolean hay_hueco(){
        return store.size() < max;
    }
}

class Main {
    private static final int P = 100;
    private static final int C = 100;
    private static Almacen alm;

    public static void main(String[] args) {
        alm = new AlmacenGrande(200);
        Thread ths[] = new Thread[P+C];
        for (int i = 0; i < P; i++) {
            final int ii = i;
            ths[i] = new Thread(
                () -> {
                    int k = 0;
                    while(k < 100){
                        System.out.println("Almacenando " + ii);
                        //while(!alm.is_empty())//Comprobacion candidata
                        while(!((AlmacenGrande) alm).hay_hueco());
                        if(alm.almacenar(ii)) k++;
                        else System.out.println("Pues estaba lleno...");                         
                    }
                });
            ths[i].start();
        }
        for (int i = 0; i < C; i++) {
            ths[P+i] = new Thread(
                () -> {
                    int k = 0; int prod;
                    System.out.println("Extrayendo "); 
                    while(k < 100){
                        while(alm.es_vacio());//Comprobacion candidata
                        prod = alm.extraer();
                        if(prod != -1) k++;
                        else System.out.println("Está vacio...");     
                    }
                });
            ths[P+i].start();
        }
        for (Thread th : ths) {
            try {
                th.join();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
            if(alm.es_vacio())
                System.out.println("El almacen esta vacio");
    }
}
