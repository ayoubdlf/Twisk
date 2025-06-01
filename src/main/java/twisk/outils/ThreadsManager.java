package twisk.outils;

import javafx.concurrent.Task;
import java.util.ArrayList;


public class ThreadsManager {

    private static ThreadsManager    instance;
    private static ArrayList<Thread> threads;

    /**
     * Constructeur du gestionnaire de threads.
     * Initialise la liste des threads.
     */
    private ThreadsManager() {
        threads  = new ArrayList<>();
    }

    // —————————— FUNCTIONS ——————————

    /**
     * Retourne l'instance du gestionnaire de threads.
     *
     * @return l'instance de ThreadsManager
     */
    public static ThreadsManager getInstance() {
        if(instance == null) {
            instance = new ThreadsManager();
        }

        return instance;
    }

    /**
     * Lance une tâche JavaFX dans un nouveau thread et l'ajoute à la liste des threads.
     *
     * @param task la tâche JavaFX à exécuter
     */
    public void lancer(Task<Void> task) {
        threads.add(new Thread(task));
        threads.getLast().start();
    }

    /**
     * Interrompt tous les threads lancés et vide la liste des threads.
     */
    public void detruireTout() {
        for(Thread thread : threads) {
            thread.interrupt();
        }

        threads.clear();
    }

}
