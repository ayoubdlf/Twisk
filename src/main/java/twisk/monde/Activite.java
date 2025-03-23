package twisk.monde;

public class Activite extends Etape {

    private int temps;
    private int ecartTemps;


    /**
     * Constructeur de la classe Activite.
     * 
     * @param nom Le nom de l'activité.
     */
    public Activite(String nom) {
        this(nom, 2, 1);
    }

    /**
     * Constructeur de la classe Activite avec des paramètres spécifiques.
     * 
     * @param nom Le nom de l'activité.
     * @param temps Le temps de l'activité.
     * @param ecartTemps L'écart de temps de l'activité.
     */
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


    /**
     * Retourne le temps de l'activité.
     * 
     * @return Le temps de l'activité.
     */
    public int getTemps() {
        return this.temps;
    }

    /**
     * Retourne l'écart de temps de l'activité.
     * 
     * @return L'écart de temps de l'activité.
     */
    public int getEcartTemps() {
        return this.ecartTemps;
    }

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
        return this.toC(1);
    }

    public String toC(int tab) {
        StringBuilder sb = new StringBuilder();

        if(this.estUneEntree()) {
            sb.append("\t".repeat(tab)).append(String.format("entrer(%s);\n", this.getNomC()));
        }

        if(this.getNbSuccesseurs() == 1) {
            Etape successeur = this.getSuccesseur(0); // si l'etape possede un seul successeur, alors on pourra accede ce dernier à l'index 0

            sb.append("\t".repeat(tab)).append(String.format("delai(%d, %d);\n", this.getTemps(), this.getEcartTemps())); // sb.append("\tdelai(").append(this.getTemps()).append(", ").append(this.getEcartTemps()).append(");\n");
            sb.append("\t".repeat(tab)).append(String.format("transfert(%s, %s);\n\n", this.getNomC(), successeur.getNomC())); // sb.append("\ttransfert(").append(this.getNomC()).append(", ").append(successeur.getNomC()).append(");\n\n");
            sb.append(successeur.toC(tab));
        } else {
            // Cas plusiers successeurs
            sb.append("\t".repeat(tab)).append(String.format("switch (rand() %% %s) {\n", this.getNbSuccesseurs()));

            for(int i = 0; i < this.getNbSuccesseurs(); i++) {
                Etape successeur = this.getSuccesseur(i);

                sb.append("\t".repeat(tab+1)).append(String.format("case %d: {\n", i));
                    sb.append("\t".repeat(tab+2)).append(String.format("delai(%d, %d);\n", this.getTemps(), this.getEcartTemps())); // sb.append("\tdelai(").append(this.getTemps()).append(", ").append(this.getEcartTemps()).append(");\n");
                    sb.append("\t".repeat(tab+2)).append(String.format("transfert(%s, %s);\n\n", this.getNomC(), successeur.getNomC()));
                    sb.append(successeur.toC(tab+2));
                    sb.append("\t".repeat(tab+2)).append("break;\n");
                sb.append("\t".repeat(tab+1)).append("}\n");
            }
            sb.append("\t".repeat(tab)).append("}\n\n");

        }

        return sb.toString();

    }

}
