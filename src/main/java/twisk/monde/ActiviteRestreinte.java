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
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("\t\tdelai(%d, %d);\n", this.getTemps(), this.getEcartTemps()));
        sb.append(String.format("\tV(ids, SEM_%s);\n\n", nomGuichet)); // Libération du guichet

        for (Etape successeur : this) {
            sb.append(String.format("\ttransfert(%s, %s);\n\n", this.getNomC(), successeur.getNomC()));
            sb.append(successeur.toC());
        }

        return sb.toString();
    }

}
