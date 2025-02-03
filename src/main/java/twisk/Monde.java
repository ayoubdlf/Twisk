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

    public void aCommeEntree(Etape... etapes) {
        this.entree.ajouterSuccesseur(etapes);
    }

    public void aCommeSortie(Etape... etapes) {
        assert (etapes != null) : "Les etapes ne doivent pas etre nulles";

        for(Etape etape : etapes) {
            assert (etape != null) : "L'etape ne doit pas etre nulle";
            etape.ajouterSuccesseur(this.sortie);
        }
    }

    public void ajouter(Etape... etapes) {
        this.etapes.ajouter(etapes);
    }

    public int nbEtapes() {
        return this.etapes.nbEtapes();
    }

    public int nbActivites() {
        return this.etapes.nbActivites();
    }

    public int nbGuichets() {
        return this.etapes.nbGuichets();
    }

    public Iterator<Etape> iterator() {
        return this.etapes.iterator();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();


        // ENTREE
        str.append(this.entree.toString()).append("\n");

        // SORTIE
        str.append(this.sortie.toString()).append("\n");

        // ETAPES
        for (Etape etape : this.etapes) {
            str.append(etape.toString()).append("\n");;
        }

        return str.toString();
    }

}
