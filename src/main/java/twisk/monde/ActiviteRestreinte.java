package twisk.monde;

public class ActiviteRestreinte extends Activite {

    /**
     * Constructeur de l'activité restreinte avec un nom.
     * 
     * @param nom le nom de l'activité
     */
    public ActiviteRestreinte(String nom) {
        super(nom);
    }

    /**
     * Constructeur de l'activité restreinte avec un nom, un temps et un écart de temps.
     * 
     * @param nom le nom de l'activité
     * @param temps le temps associé à l'activité
     * @param ecartTemps l'écart de temps associé à l'activité
     */
    public ActiviteRestreinte(String nom, int temps, int ecartTemps) {
        super(nom, temps, ecartTemps);
    }

    /* —————————— GETTERS —————————— */

    @Override
    public boolean estUneActiviteRestreinte() {
        return true;
    }

    public String toC(String nomGuichet) {
        return this.toC(1, nomGuichet);
    }

    public String toC(int tab, String nomGuichet) {
        StringBuilder sb = new StringBuilder();

        sb.append("\t".repeat(tab+1)).append(String.format("delai(%d, %d);\n", this.getTemps(), this.getEcartTemps()));
        sb.append("\t".repeat(tab)).append(String.format("V(ids, SEM_%s);\n\n", nomGuichet)); // Libération du guichet

        for (Etape successeur : this) {
            sb.append("\t".repeat(tab)).append(String.format("transfert(%s, %s);\n\n", this.getNomC(), successeur.getNomC()));
            sb.append(successeur.toC(tab));
        }

        return sb.toString();
    }

}
