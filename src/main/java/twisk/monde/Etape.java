package twisk.monde;

import twisk.outils.FabriqueNumero;
import java.util.Iterator;


public abstract class Etape implements Iterable<Etape> {

    private String nom;
    private int idEtape;
    private GestionnaireEtapes successeurs;


    public Etape(String nom) {
        assert(nom != null && !nom.isEmpty()): "Le nom de l'etape ne doit pas etre nul ou vide" ;

        this.nom         = nom;
        this.successeurs = new GestionnaireEtapes();
        this.idEtape     = FabriqueNumero.getInstance().getNumeroEtape();
    }

    /* —————————— SETTERS —————————— */

    public void ajouterSuccesseur(Etape... etapes) {
        assert (etapes != null)    : "Les etapes ne doivent pas etre nulles";
        assert (etapes.length > 0) : "Les etapes ne doivent pas etre vides";

        if(this.estUnGuichet()) {
            assert (etapes.length == 1)                    : "Le guichet ne peut pas avoir plusieurs successeurs";
            assert (etapes[0] != null)                     : "Les etapes ne doivent pas etre nulles";
            assert (etapes[0].estUneActiviteRestreinte())  : "Le guichet ne peut avoir qu'une activite restreinte comme successeur";
            assert (this.getSuccesseurs().nbEtapes() == 0) : "Le guichet ne peut pas avoir plusiers successeurs";

            this.successeurs.ajouter(etapes[0]);
        } else {
            /*
            * REGLE
            *      - Une activite restreinte ne peut etre que le successeur d'un guichet (donc pas d'une autre activite)
            */
            for (Etape etape : etapes) {
                assert (!etape.estUneActiviteRestreinte()) : "Une activite restreinte ne peut etre que le successeur d'un guichet";
            }

            this.successeurs.ajouter(etapes);
        }

    }


    /* —————————— GETTERS —————————— */

    public abstract boolean estUneActivite();

    public abstract boolean estUneActiviteRestreinte();

    public abstract boolean estUnGuichet();

    public String getNom() {
        return this.nom;
    }

    public int getIdEtape() {
        return this.idEtape;
    }

    public GestionnaireEtapes getSuccesseurs() {
        return this.successeurs;
    }

    public Iterator<Etape> iterator() {
        return this.successeurs.iterator();
    }

    public String toString() {
        StringBuilder str = new StringBuilder();

        // entrée : 1 successeur - balade au zoo
        str.append(String.format("%s : ", this.getNom()));
        str.append(String.format("%d successeur", this.successeurs.nbEtapes()));

        if(this.successeurs.nbEtapes() > 0) {
            str.append(" - ");

            for(Etape etape : this.successeurs) {
                str.append(etape.getNom());
                str.append(" - ");
            }
        }

        return str.toString();
    }
}
