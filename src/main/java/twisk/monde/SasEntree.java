package twisk.monde;

public class SasEntree extends Activite {

    /**
     * Constructeur de la classe SasEntree.
     */
    public SasEntree() {
        super("entree");
    }

    /* —————————— GETTERS —————————— */

    @Override
    public boolean estUneEntree() {
        return true;
    }

}