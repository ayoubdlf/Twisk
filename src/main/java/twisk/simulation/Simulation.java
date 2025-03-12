package twisk.simulation;

import twisk.monde.Etape;
import twisk.monde.Guichet;
import twisk.monde.Monde;
import twisk.outils.KitC;

import java.util.ArrayList;
import java.util.Iterator;

public class Simulation {

    private Monde monde;
    private KitC kitC;

    public Simulation() {
        this.kitC = new KitC();

        kitC.creerEnvironnement();
    }

    public void simuler(Monde monde) {
        this.monde = monde;
        System.out.println(monde);

        this.kitC.creerFichier(this.monde.toC());
        this.kitC.compiler();
        this.kitC.construireLaBibliotheque();

        System.load("/tmp/twisk/libTwisk.so");
        this.startSimulation();
    }


    private void startSimulation() {
        int[] tabJetonsGuichet = this.getTabJetonsGuichets();

        int nbEtapes = this.monde.nbEtapes();
        int nbGuichets = this.monde.nbGuichets();
        int nbClient = 5;

        int[] simulation = start_simulation(nbEtapes, nbGuichets, nbClient, tabJetonsGuichet);

        this.afficherListeClients(nbEtapes, nbClient);
        this.afficherPositionsClients(nbEtapes, nbGuichets, nbClient);

        nettoyage();
    }


    // Ajout des fonctions natives
     public native int[] start_simulation(int nbEtapes, int nbGuichet, int nbClients, int[] tabJetonsGuichets);
     public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);
     public native void nettoyage();

     private boolean estTouteEtapeDansSortie(int NB_ETAPES, int NB_CLIENTS) {
         int[] positions = ou_sont_les_clients(NB_ETAPES , NB_CLIENTS);

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

     private void afficherListeClients(int NB_ETAPES, int NB_CLIENTS) {
         int[] positions = ou_sont_les_clients(NB_ETAPES , NB_CLIENTS);

         System.out.print("\nles clients : ");

         for (int i = 0; i < NB_CLIENTS; i++) {
             System.out.printf("%d%s", positions[i+1], (i < NB_CLIENTS - 1) ? ", " : ""); // Si on est dans le dernier client, n'affiche pas la virgule
         }

         System.out.print("\n");
     }

    private void afficherPositionsClients(int NB_ETAPES, int NB_GUICHETS, int NB_CLIENTS) {
         int[] positions;

         while(!estTouteEtapeDansSortie(NB_ETAPES, NB_CLIENTS)) {
             positions = ou_sont_les_clients(NB_ETAPES, NB_GUICHETS);

             for (int nbEtape = 0; nbEtape < NB_ETAPES; nbEtape++) {
                 int nbClients = positions[nbEtape * (NB_CLIENTS + 1)];

                 System.out.printf("etape %d (%s) %d clients: ", nbEtape, this.monde.getNomEtape(nbEtape), nbClients);

                 for (int i = 0; i < nbClients; i++) {
                     int client = positions[nbEtape * (NB_CLIENTS + 1) + (i+1)];
                     System.out.printf("%d%s", client, ((i+1) < nbClients) ? ", " : ""); // Si on est dans le dernier client, n'affiche pas la virgule
                 }

                 System.out.print("\n");
             }

             System.out.print("———————————————————————————————————————————————————————————————————————————————\n");

             try {
                 Thread.sleep(1000);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

         }
    }

}
