package twisk.monde;

public class SasEntree extends Activite {

    public SasEntree() {
        super("entree");
    }

    public String toC() {
        StringBuilder sb = new StringBuilder();

        sb.append("\tentrer(").append(this.getNomC()).append(");\n");
        sb.append("\tdelai(").append(this.getTemps()).append(", ").append(this.getEcartTemps()).append(");\n");

        for (Etape successeur : this) {
            sb.append("\ttransfert(").append(this.getNomC()).append(", ").append(successeur.getNomC()).append(");\n\n");
            sb.append(successeur.toC());
        }

        return sb.toString();
    }

    /* —————————— SETTERS —————————— */

    /* —————————— GETTERS —————————— */

}