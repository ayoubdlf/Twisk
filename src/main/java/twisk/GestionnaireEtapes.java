package twisk;

import java.util.*;


public class GestionnaireEtapes implements Iterable<Etape> {

    private ArrayList<Etape> etapes;


    public GestionnaireEtapes() {
        this.etapes = new ArrayList<>();
    }

    public void ajouter(Etape... etapes) {
        assert (etapes != null) : "Les etapes ne doivent pas etre nulles";

        for(Etape etape : etapes) {
            assert (etape != null) : "L'etape ne doit pas etre nulle";
            this.etapes.add(etape);
        }
    }

    public int nbEtapes() {
        return this.etapes.size();
    }

    public Iterator<Etape> iterator() {
        return this.etapes.iterator();
    }

}
