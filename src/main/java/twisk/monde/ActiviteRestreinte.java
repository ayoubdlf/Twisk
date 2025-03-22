package twisk.monde;

public class ActiviteRestreinte extends Activite {

    public ActiviteRestreinte(String nom) {
        super(nom);
    }

    public ActiviteRestreinte(String nom, int temps, int ecartTemps) {
        super(nom, temps, ecartTemps);
    }

    /* —————————— SETTERS —————————— */

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
