package twisk.outils;

import javafx.concurrent.Task;
import java.util.ArrayList;


public class ThreadsManager {

    private static ThreadsManager    instance;
    private static ArrayList<Thread> threads;

    private ThreadsManager() {
        instance = null;
        threads  = new ArrayList<>();
    }

    // —————————— FUNCTIONS ——————————

    public static ThreadsManager getInstance() {
        if(instance == null) {
            instance = new ThreadsManager();
        }

        return instance;
    }

    public void lancer(Task<Void> task) {
        threads.add(new Thread(task));
        threads.getLast().start();
    }

    public void detruireTout() {
        for(Thread thread : threads) {
            thread.interrupt();
        }

        instance = null;
    }

}
