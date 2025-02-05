package twisk.monde;

import twisk.outils.FabriqueNumero;


public class Guichet extends Etape {

    private int nbJetons;
    private int idSemaphore;


    public Guichet(String nom) {
        this(nom, 1);
    }

    public Guichet(String nom, int nbJetons) {
        super(nom);

        assert (nbJetons > 0) : "Le nombre de jetons doit etre superieur ou egale à 1";

        this.nbJetons    = nbJetons;
        this.idSemaphore = FabriqueNumero.getInstance().getNumeroSemaphore();
    }

    /* —————————— SETTERS —————————— */

    @Override
    public void ajouterSuccesseur(Etape... etapes) {
        /*
        * REGLES:
        *       - Apres un guichet il y'a qu'UNE SEULE ACTIVITE
        */

        assert (etapes != null)                        : "Les etapes ne doivent pas etre nulles";
        assert (etapes.length > 0)                     : "Il doit y avoir au moins une etape";
        assert (etapes[0] != null)                     : "Les etapes ne doivent pas etre nulles";
        assert (etapes[0].estUneActivite())            : "Le guichet ne peut avoir qu'une activite comme successeur";
        assert (this.getSuccesseurs().nbEtapes() == 0) : "Le guichet ne peut pas avoir plusiers successeurs";


        this.getSuccesseurs().ajouter(etapes[0]);
    }

    /* —————————— GETTERS —————————— */

    @Override
    public boolean estUneActivite() {
        return false;
    }

    @Override
    public boolean estUnGuichet() {
        return true;
    }

    public int getNumeroSemaphore() {
        return this.idSemaphore;
    }

}
