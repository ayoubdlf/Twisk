package twisk.simulation;

import twisk.monde.*;
import twisk.mondeIG.SujetObserve;
import twisk.outils.KitC;
import twisk.outils.ThreadsManager;

import java.util.*;

/**
 * Classe Simulation pour la simulation de l'application Twisk.
 */
public class Simulation extends SujetObserve {

    private Monde               monde;
    private KitC                kitC;
    private int                 nbClients;
    private GestionnaireClients gestionnaireClients;
    private int[]               positionClients;
    private boolean             simulationActive;


    public Simulation() {
        this.kitC                = new KitC();
        this.nbClients           = 0;
        this.gestionnaireClients = new GestionnaireClients();
        this.positionClients     = null;
        this.simulationActive    = false;

        kitC.creerEnvironnement();
    }

    /* —————————— GETTERS —————————— */

    /**
     * Récupère le nombre de clients.
     * 
     * @return Le nombre de clients actuellement dans la simulation.
     */
    public int getNbClients() {
        return this.nbClients;
    }

    public GestionnaireClients getGestionnaireClients() {
        return this.gestionnaireClients;
    }


    /* —————————— SETTERS —————————— */

    /**
     * Définit le nombre de clients pour la simulation.
     * 
     * @param nbClients Le nombre de clients à définir. Doit être supérieur à 0.
     * @throws AssertionError Si nbClients est inférieur ou égal à 0.
     */
    public void setNbClients(int nbClients) {
        assert (nbClients > 0) : "Le nombre de clients doit etre superieur à 0";

        this.nbClients = nbClients;
    }


    /* —————————— METHODES PUBLIQUES —————————— */

    /**
     * Simule le processus de simulation en utilisant le monde spécifié.
     * 
     * @param monde Le monde à simuler.
     */
    public void simuler(Monde monde) {
        this.monde = monde;

        this.kitC.creerFichier(this.monde.toC());
        this.kitC.compiler();
        this.kitC.construireLaBibliotheque();

        try {
            System.load("/tmp/twisk/libTwisk.so");
        } catch (UnsatisfiedLinkError ignored) {}

        this.startSimulation();
    }

    public void stopSimulation() {
        this.simulationActive = false;
        ThreadsManager.getInstance().detruireTout();

    }


    /* —————————— METHODES PRIVES —————————— */

    private void startSimulation() {

        System.out.println(this.monde);

        int[] tabJetonsGuichet = this.getTabJetonsGuichets();
        int nbEtapes           = this.monde.nbEtapes();
        int nbGuichets         = this.monde.nbGuichets();
        this.simulationActive  = true;

        System.out.println(111);
        int[] simulation       = start_simulation(nbEtapes, nbGuichets, this.getNbClients(), tabJetonsGuichet);
        System.out.println(222);
        System.out.println(Arrays.toString(simulation));

        this.afficherListeClients(simulation, this.getNbClients());
        this.afficherPositionsClients(nbEtapes, this.getNbClients());

        this.gestionnaireClients.nettoyer();
        this.kitC.killProcessus(simulation);
        this.nettoyage();
    }

    private boolean estTouteEtapeDansSortie(int NB_ETAPES, int NB_CLIENTS) {
        int[] positions     = ou_sont_les_clients(NB_ETAPES , NB_CLIENTS);
        int nbClientsSortie = positions[(NB_CLIENTS + 1)]; // on sait que par default la sortie est l'etape numero 1 (entree: 0, sortie: 1, etc..)

        return nbClientsSortie == NB_CLIENTS;
    }

    private int[] getTabJetonsGuichets() {
        ArrayList<Integer> tabJetonsGuichets = new ArrayList<>();

        for (Iterator<Etape> it = this.monde.iteratorGuichets(); it.hasNext(); ) {
            Guichet guichet = (Guichet) it.next();
            tabJetonsGuichets.add(guichet.getNbJetons());
        }

        return tabJetonsGuichets.stream().mapToInt(Integer::intValue).toArray();
    }

    private void afficherListeClients(int[] pids, int NB_CLIENTS) {
        this.gestionnaireClients.setClients(pids);

        System.out.print("pids des clients : ");
        for (int i = 0; i < NB_CLIENTS; i++) {
            System.out.printf("%d%s", pids[i], (i < NB_CLIENTS - 1) ? ", " : ""); // Si on est dans le dernier client, n'affiche pas la virgule
        }

        System.out.print("\n\n");
    }

    private void afficherEtape(int nbEtape, int NB_CLIENTS) {
        System.out.format(
            "etape %-2d %-20s %d clients ",
            this.monde.getEtape(nbEtape).getIdEtape(),
            "(" + this.monde.getEtape(nbEtape).getNom() + ")",
            NB_CLIENTS
        );
    }

    private void afficherPositionsClients(int NB_ETAPES, int NB_CLIENTS) {
        while(this.simulationActive) {
            try {
                this.positionClients = ou_sont_les_clients(NB_ETAPES, NB_CLIENTS);
                // System.out.println(Arrays.toString(positions));

                for (int i = 0; i < NB_ETAPES; i++) {
                    int nbClients = this.positionClients[this.monde.getEtape(i).getIdEtape() * (NB_CLIENTS + 1)];

                    this.afficherEtape(i, nbClients);

                    for (int j = 0; j < nbClients; j++) {
                        int client = this.positionClients[this.monde.getEtape(i).getIdEtape() * (NB_CLIENTS + 1) + (j + 1)];
                        System.out.printf("%d%s", client, ((j+1) < nbClients) ? ", " : "");

                        this.gestionnaireClients.allerA(client, this.monde.getEtape(i), j+1);
                    }

                    System.out.print("\n");
                }

                if(estTouteEtapeDansSortie(NB_ETAPES, NB_CLIENTS)) { break; }

                System.out.println(); // nouvelle ligne entre chaque seconde

                this.notifierObservateurs();
                Thread.sleep(1000); // 1000ms

            } catch (InterruptedException ignored) {
            }
        }

        System.out.println("\nsimulation terminee.");
        this.notifierObservateurs();
    }


    /* —————————— FONCTIONS NATIVES —————————— */

    /**
     * Fonction native qui démarre la simulation.
     * 
     * @param nbEtapes Le nombre d'étapes dans la simulation.
     * @param nbGuichet Le nombre de guichets dans la simulation.
     * @param nbClients Le nombre de clients à simuler.
     * @param tabJetonsGuichets Le tableau des jetons des guichets.
     * @return Un tableau d'entiers représentant les résultats de la simulation.
     */
    public native int[] start_simulation(int nbEtapes, int nbGuichet, int nbClients, int[] tabJetonsGuichets);
   
    /**
     * Fonction native qui détermine où se trouvent les clients.
     * 
     * @param nbEtapes Le nombre d'étapes dans la simulation.
     * @param nbClients Le nombre de clients dans la simulation.
     * @return Un tableau d'entiers représentant les positions des clients.
     */
    public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);
    
    /**
     * Fonction native qui effectue le nettoyage, en libérant la mémoire, après la simulation.
     */
    public native void nettoyage();
}