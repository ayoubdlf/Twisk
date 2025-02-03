package twisk;

public class Activite extends Etape {

    private int temps;
    private int ecartTemps;


    public Activite(String nom) {
        this(nom, 2, 1);
    }

    public Activite(String nom , int temps, int ecartTemps) {
        super(nom);

        assert(temps > 0)      : "Le temps ne dois pas etre inferieur a 0";
        assert(ecartTemps > 0) : "L'ecartTemps ne dois pas etre inferieur a 0";

        assert(temps > ecartTemps):"le temps doit etre superieur à l'ecart temps";

        this.temps      = temps;
        this.ecartTemps = ecartTemps;
    }
    @Override
    public boolean estUneActivite() {
        return true;
    }

    public boolean estUnGuichet() {
        return false;
    }

}
