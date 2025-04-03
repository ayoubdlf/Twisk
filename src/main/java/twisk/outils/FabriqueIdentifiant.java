package twisk.outils;


public class FabriqueIdentifiant {

    private static FabriqueIdentifiant instance;
    private static int nbEtape;
    private static int noPointDeControle;

    
    /**
     * Constructeur de la classe FabriqueIdentifiant.
     */
    private FabriqueIdentifiant() {
        resetNbEtape();
        resetNoPointDeControle();
    }

    /**
     * Retourne l'instance de FabriqueIdentifiant.
     * 
     * @return l'instance de FabriqueIdentifiant
     */
    public static FabriqueIdentifiant getInstance() {
        if(instance == null) {
            instance = new FabriqueIdentifiant();
        }

        return instance;
    }

    /**
     * Génère un identifiant pour une étape.
     * 
     * @return une chaîne de caractères représentant l'identifiant de l'étape
     */
    public String getIdentifiantEtape() {
        nbEtape += 1;

        return String.format("ETAPE-%d", nbEtape);
    }

    /**
     * Réinitialise le compteur d'étapes à -1.
     */
    public void resetNbEtape() {
        nbEtape = -1;
    }


    /* POINT DE CONTROLE */

    /**
     * Génère un identifiant pour un point de contrôle.
     * 
     * @return une chaîne de caractères représentant l'identifiant du point de contrôle
     */
    public String getIdentifiantPointDeControle() {
        noPointDeControle += 1;
        return Integer.toString(noPointDeControle);
    }

    /**
     * Réinitialise le compteur de points de contrôle à 0.
     */
    public void resetNoPointDeControle() {
        noPointDeControle = 0;
    }

}
