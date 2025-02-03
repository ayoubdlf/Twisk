package twisk;

import java.util.Iterator;


public class Monde implements Iterable<Etape> {

    public Monde() {
    }

    public void aCommeEntree(Etape... etapes) {}

    public void aCommeSortie(Etape... etapes) {}

    public void ajouter(Etape... etapes) {}

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
