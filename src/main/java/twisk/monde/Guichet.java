package twisk.monde;

import twisk.outils.FabriqueNumero;


public class Guichet extends Etape {

    private int nbJetons;
    private int idSemaphore;


    /**
     * Crée un guichet avec un nom et un nombre de jetons par défaut de 1.
     *
     * @param nom Le nom du guichet.
     */
    public Guichet(String nom) {
        this(nom, 1);
    }

    /**
     * Crée un guichet avec un nom et un nombre de jetons spécifié.
     *
     * @param nom Le nom du guichet.
     * @param nbJetons Le nombre de jetons du guichet, doit être supérieur à 0.
     */
    public Guichet(String nom, int nbJetons) {
        super(nom);

        assert (nbJetons > 0) : "Le nombre de jetons doit etre superieur ou egale à 1";

        this.nbJetons    = nbJetons;
        this.idSemaphore = FabriqueNumero.getInstance().getNumeroSemaphore();
    }

    /* —————————— SETTERS —————————— */

    /* —————————— GETTERS —————————— */

    @Override
    public boolean estUneEntree() {
        return false;
    }

    @Override
    public boolean estUneSortie() {
        return false;
    }
    
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

    /**
     * Retourne le numéro de sémaphore associé à ce guichet.
     *
     * @return Le numéro de sémaphore.
     */
    public int getNumeroSemaphore() {
        return this.idSemaphore;
    }

    /**
     * Retourne le nombre de jetons disponibles dans ce guichet.
     *
     * @return Le nombre de jetons.
     */
    public int getNbJetons() {
        return this.nbJetons;
    }

    @Override
    public String toC() {
        return this.toC(1);
    }

    public String toC(int tab) {
        StringBuilder sb = new StringBuilder();

        Etape successeur = this.getSuccesseur(0);

        sb.append("\t".repeat(tab)).append(String.format("P(ids, SEM_%s);\n", this.getNomC())); // On simule un passage par le guichet
        sb.append("\t".repeat(tab+1)).append(String.format("transfert(%s, %s);\n", this.getNomC(), successeur.getNomC()));

        if(successeur.estUneActiviteRestreinte()) {
            sb.append(((ActiviteRestreinte)successeur).toC(tab, this.getNomC()));
        } else {
            sb.append(successeur.toC(tab));
        }


        return sb.toString();
    }

}
