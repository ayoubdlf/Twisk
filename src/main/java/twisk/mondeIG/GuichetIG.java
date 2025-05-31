package twisk.mondeIG;

import twisk.outils.FabriqueIdentifiant;
import static twisk.outils.TailleComposants.POINT_DE_CONTROLE_RADIUS;


public class GuichetIG extends EtapeIG {

    private int jetons;


    /**
     * Constructeur de la classe GuichetIG.
     *
     * @param nom Le nom du guichet.
     * @param largeur La largeur du guichet.
     * @param hauteur La hauteur du guichet.
     */
    public GuichetIG(String nom, int largeur, int hauteur) {
        super(nom, largeur, hauteur);
        this.setEstUnGuichet(true);

        this.jetons           = 1;
        this.pointsDeControle = new PointDeControleIG[2];

        this.initPointsDeControle();
    }

    // —————————— GETTERS ——————————

    /**
     * Retourne le nombre de jetons de du guichet.
     * 
     * @return le nombre de jetons de du guichet
     */
    public int getJetons() {
        return this.jetons;
    }


    // —————————— SETTERS ——————————

    /**
     * Définit le nombre de jetons du guichet.
     * 
     * @param jetons le nombre de jetons à définir, doit être supérieur à 0
     */
    public void setJetons(int jetons) {
        assert (jetons > 0)   : "Le temps doit être supérieur à 0";

        this.jetons = jetons;
    }


    // —————————— METHODES PRIVES ——————————

    /**
     * Initialise les points de contrôle du guichet.
     */
    private void initPointsDeControle() {
        FabriqueIdentifiant.getInstance().resetNoPointDeControle();

        int x      = this.getLargeur() / 2; // centreX
        int y      = this.getHauteur() / 2; // centreY
        int radius = POINT_DE_CONTROLE_RADIUS;

        int[] pos2 = { (this.getLargeur() - radius), (y - radius) };
        int[] pos4 = { (-radius), (y - radius) };

        this.pointsDeControle[0] = new PointDeControleIG(this, pos2, "DROITE"); // DROITE
        this.pointsDeControle[1] = new PointDeControleIG(this, pos4, "GAUCHE"); // GAUCHE
    }
}
