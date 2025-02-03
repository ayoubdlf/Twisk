package twisk;

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
