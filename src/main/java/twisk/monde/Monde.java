package twisk.monde;

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

    /* —————————— SETTERS —————————— */

    public void aCommeEntree(Etape... etapes) {
        assert (etapes != null)    : "Les etapes ne doivent pas etre nulles";
        assert (etapes.length > 0) : "Les etapes ne doivent pas etre vides";

        for(Etape etape : etapes) {
            assert (etape != null)                     : "Les etapes ne doivent pas etre nulles";
            assert (!etape.estUneActiviteRestreinte()) : "Une activite restreinte ne peut pas etre une entree";

            this.entree.ajouterSuccesseur(etape);
        }
    }

    public void aCommeSortie(Etape... etapes) {
        assert (etapes != null)    : "Les etapes ne doivent pas etre nulles";
        assert (etapes.length > 0) : "Les etapes ne doivent pas etre vides";

        for(Etape etape : etapes) {
            assert (etape != null)         : "L'etape ne doit pas etre nulle";
            assert (!etape.estUnGuichet()) : "Un guichet ne peut pas etre une sortie";

            etape.ajouterSuccesseur(this.sortie);
        }
    }

    public void ajouter(Etape... etapes) {
        this.etapes.ajouter(etapes);
    }


    /* —————————— GETTERS —————————— */

    public int nbEtapes() {
        return this.etapes.nbEtapes();
    }

    public int nbActivites() {
        return this.etapes.nbActivites();
    }

    public int nbGuichets() {
        return this.etapes.nbGuichets();
    }

    public SasEntree getEntree() {
        return this.entree;
    }

    public SasSortie getSortie() {
        return this.sortie;
    }

    public GestionnaireEtapes getEtapes() {
        return this.etapes;
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
            str.append(etape.toString()).append("\n");
        }

        return str.toString();
    }

}
