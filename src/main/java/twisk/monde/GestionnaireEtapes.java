package twisk.monde;

import java.util.*;


public class GestionnaireEtapes implements Iterable<Etape> {

    private ArrayList<Etape> etapes;


    public GestionnaireEtapes() {
        this.etapes = new ArrayList<>();
    }


    /* —————————— SETTERS —————————— */

    public void ajouter(Etape... etapes) {
        assert (etapes != null) : "Les etapes ne doivent pas etre nulles";

        for(Etape etape : etapes) {
            assert (etape != null) : "L'etape ne doit pas etre nulle";
            this.etapes.add(etape);
        }
    }

    /* —————————— GETTERS —————————— */

    public int nbEtapes() {
        return this.etapes.size();
    }

    public int nbActivites() {
        return (int) this.etapes.stream().filter(Etape::estUneActivite).count();
    }

    public int nbGuichets() {
        return (int) this.etapes.stream().filter(Etape::estUnGuichet).count();
    }

    public Iterator<Etape> iterator() {
        return this.etapes.iterator();
    }

}
