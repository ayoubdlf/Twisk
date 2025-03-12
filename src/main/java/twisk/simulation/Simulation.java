package twisk.simulation;

import twisk.monde.Monde;
import twisk.outils.KitC;

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
    }

    // Ajout des fonctions natives
     public native int[] start_simulation(int nbEtapes, int nbGuichet, int nbClients, int[] tabJetonsGuichets);
     public native int[] ou_sont_les_clients(int nbEtapes, int nbClients);
     public native void nettoyage();




}
