package twisk.monde;

public class SasEntree extends Activite {

    public SasEntree() {
        super("entree");
    }

    /* —————————— GETTERS —————————— */

    public String toC() {
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("\tentrer(%s);\n", this.getNomC()));
        sb.append(String.format("\tdelai(%s, %s);\n", this.getTemps(), this.getEcartTemps()));

        for (Etape successeur : this) {
            sb.append(String.format("\ttransfert(%s, %s);\n\n", this.getNomC(), successeur.getNomC()));
            sb.append(successeur.toC());
        }

        return sb.toString();
    }

    /* —————————— SETTERS —————————— */

}