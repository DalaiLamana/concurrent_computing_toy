//import java.util.concurrent.atomic.AtomicInteger;

/*abstract class Lock {
    public static final boolean SYNC = true;  // para probar la eficiencia
    protected int n;

    public Lock(int n) {
        this.n = n;
    }

    abstract public void take(int i);
    abstract public void release(int i);
}

class LockRompeEmpate extends Lock {
    private volatile int in[];
    private volatile int last[];

    public LockRompeEmpate(int n) {
        super(n);
        in = new int[n];
        last = new int[n];
        for (int i = 0; i < n; i++) {
            in[i] = -1;
            last[i] = -1;
        }
    }

    public void take(int i) {
        if (SYNC) {
            for (int j = 0; j < n; j++) {
                in[i] = j; in = in;
                last[j] = i; last = last;
                for (int k = 0; k < n; k++) {
                    if (k != i)
                        while (in[k] >= in[i] && last[j] == i) {
                            Thread.yield();
                        }
                }
            }
        }
    }

    public void release(int i) {
        if (SYNC) {
            in[i] = -1;
        }
    }
}

class LockTicket extends Lock {
    private AtomicInteger number;
    private volatile int next;
    private volatile int turn[];

    public LockTicket(int n) {
        super(n);
        number = new AtomicInteger(0);
        next = 0;
        turn = new int[n];
        for (int i = 0; i < n; i++) {
            turn[i] = -1;
        }
    }

    public void take(int i) {
        if (SYNC) {
            turn[i] = number.getAndIncrement(); turn = turn;
            while (turn[i] != next) {
                Thread.yield();
            }
        }
    }

    public void release(int i) {
        if (SYNC) {
            next++;
        }
    }
}

class LockBakery extends Lock {
    private volatile int turn[];

    public LockBakery(int n) {
        super(n);
        turn = new int[n];
        for (int i = 0; i < n; i++) {
            turn[i] = 0;
        }
    }

    public void take(int i) {
        if (SYNC) {
            turn[i] = 1;
            int max = 0;
            for (int x : turn) {
                if (x > max) {
                    max = x;
                }
            }
            turn[i] = max + 1;
            turn = turn;
            for (int j = 0; j < n; j++) {
                while (turn[j] != 0
                        && (turn[i] > turn[j] || (turn[i] == turn[j] && i > j))) {
                    Thread.yield();
                }
            }
        }
    }

    public void release(int i) {
        if (SYNC) {
            turn[i] = 0;
        }
    }
}*/

class syncData{
    private int _data = 0;
    
    public synchronized void inc() {
        _data++;
    }
    
    public synchronized void dec() {
        _data--;
    }
    public String toString(){
        return Integer.toString(this._data);
    }
}

class Main {
    //private static final int M = 550;  // LockRompeEmpate
    //private static final int M = 150000;  // LockTicket
    private static final int M = 40000;  // LockBakery
    private static syncData data = new syncData();

    public static void main(String[] args) {
        Thread ths[] = new Thread[2*M];
        for (int i = 0; i < M; i++) {
            //final int ii = i;
            ths[i] = new Thread(
                () -> {
                    /*
                    //System.out.println("" + ii + " empieza");
                    lock.take(ii);
                    //System.out.println("" + ii + " entra");
                    n++;
                    lock.release(ii);
                    //System.out.println("" + ii + " termina");
                    */
                    data.inc();
                });
            ths[i].start();
        }
        for (int i = M; i < 2*M; i++) {
            //final int ii = i;
            ths[i] = new Thread(
                () -> {
                    /*
                    //System.out.println("" + ii + " empieza");
                    lock.take(ii);
                    //System.out.println("" + ii + " entra");
                    n--;
                    lock.release(ii);
                    //System.out.println("" + ii + " termina");
                    */
                    data.dec();
                });
            ths[i].start();
        }
        for (int i = 0; i < M*2; i++) {
            try {
                ths[i].join();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
        System.out.println("0 = " + data);
    }
}
