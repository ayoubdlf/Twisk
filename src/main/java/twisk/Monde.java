package twisk;

import java.util.*;


public class Monde implements Iterable<Etape> {

    private GestionnaireEtapes etapes;
    private SasEntree entree;
    private SasSortie sortie;

    public Monde() {
        this.etapes = new GestionnaireEtapes();
        this.entree = new SasEntree();
        this.sortie = new SasSortie();
    }

    public void aCommeEntree(Etape... etapes) {}

    public void aCommeSortie(Etape... etapes) {}

    public void ajouter(Etape... etapes) {
        this.etapes.ajouter(etapes);
    }

    public int nbEtapes() {
        return 0;
    }

    public int nbGuichets() {
        return 0;
    }

    public Iterator<Etape> iterator() {
        return null;
    }

}
