package twisk.monde;

public class Activite extends Etape {

    private int temps;
    private int ecartTemps;


    public Activite(String nom) {
        this(nom, 2, 1);
    }

    public Activite(String nom , int temps, int ecartTemps) {
        super(nom);

        assert(temps > 0)          : "Le temps doit etre superieur à 0";
        assert(ecartTemps > 0)     : "L'ecart temps doit etre superieur à 0";
        assert(temps > ecartTemps) : "Le temps doit etre superieur à l'ecart temps";

        this.temps      = temps;
        this.ecartTemps = ecartTemps;
    }

    /* —————————— SETTERS —————————— */


    /* —————————— GETTERS —————————— */


    public int getTemps() {
        return this.temps;
    }

    public int getEcartTemps() {
        return this.ecartTemps;
    }

    @Override
    public boolean estUneActivite() {
        return true;
    }

    @Override
    public boolean estUneActiviteRestreinte() {
        return false;
    }

    @Override
    public boolean estUnGuichet() {
        return false;

    }
    @Override
    public String toC() {
        StringBuilder sb = new StringBuilder();

        sb.append("\tdelai(").append(this.getTemps()).append(", ").append(this.getEcartTemps()).append(");\n");

        for (Etape successeur : this) {
            sb.append("\ttransfert(").append(this.getNom()).append(", ").append(successeur.getNom()).append(");\n\n");
            sb.append(successeur.toC());
        }

        return sb.toString();
    }

    public String toCSem(int nbSemaphores) {
        StringBuilder sb = new StringBuilder();

        sb.append("\tdelai(").append(this.getTemps()).append(", ").append(this.getEcartTemps()).append(");\n");
        sb.append("\tV(ids, ").append(nbSemaphores).append(");\n\n"); // Libération du guichet

        for (Etape successeur : this) {
            sb.append("\ttransfert(").append(this.getNom()).append(", ").append(successeur.getNom()).append(");\n\n");
            sb.append(successeur.toC());
        }

        return sb.toString();
    }


}
