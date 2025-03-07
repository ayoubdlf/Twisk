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

    /* —————————— GETTERS —————————— */

    @Override
    public boolean estUneActivite() {
        return false;
    }

    @Override
    public boolean estUneActiviteRestreinte() {
        return false;
    }

    @Override
    public boolean estUnGuichet() {
        return true;
    }

    public int getNumeroSemaphore() {
        return this.idSemaphore;
    }

    public int getNbJetons() {
        return this.nbJetons;
    }

    @Override
    public String toC() {
        StringBuilder sb = new StringBuilder();

        for (Etape successeur : this) {
            sb.append("\tP(ids, ").append(this.getNom()).append(");\n"); // On simule un passage par le guichet
            sb.append("\ttransfert(").append(this.getNom()).append(", ").append(successeur.getNom()).append(");\n");

            sb.append(((Activite)successeur).toCSem(this.getNumeroSemaphore()));
        }


        return sb.toString();
    }

}
