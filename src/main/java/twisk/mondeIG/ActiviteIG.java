package twisk.mondeIG;

import twisk.outils.FabriqueIdentifiant;
import static twisk.outils.TailleComposants.*;


public class ActiviteIG extends EtapeIG {

    private int temps, ecartTemps;


    /**
     * Constructeur de la classe ActiviteIG.
     * 
     * @param nom Le nom de l'activité.
     * @param largeur La largeur de l'activité.
     * @param hauteur La hauteur de l'activité.
     */
    public ActiviteIG(String nom, int largeur, int hauteur) {
        super(nom, largeur, hauteur);
        this.setEstUneActivite(true);

        this.temps            = 2;
        this.ecartTemps       = 1;
        this.pointsDeControle = new PointDeControleIG[4];

        this.initPointsDeControle();
    }

    // —————————— GETTERS ——————————

    /**
     * Retourne le temps de l'activité.
     * 
     * @return le temps de l'activité
     */
    public int getTemps() {
        return this.temps;
    }

    /**
     * Retourne l'écart de temps de l'activité.
     * 
     * @return l'écart de temps de l'activité
     */
    public int getEcartTemps() {
        return this.ecartTemps;
    }


    // —————————— SETTERS ——————————

    /**
     * Définit le temps de l'activité.
     * 
     * @param temps le temps à définir, doit être supérieur à 0 et inférieur à 100
     */
    public void setTemps(int temps) {
        assert (temps > 0)   : "Le temps doit être supérieur à 0";
        assert (temps <= 100) : "Le temps doit être inferieur ou égale à 100";

        this.temps = temps;
    }

    /**
     * Définit l'écart de temps de l'activité.
     * 
     * @param ecartTemps l'écart de temps à définir, doit être supérieur à 0 et inférieur au temps
     */
    public void setEcartTemps(int ecartTemps) {
        assert (ecartTemps > 0)          : "L'écart temps doit être supérieur à 0";
        assert (ecartTemps < this.temps) : "L'écart temps doit être inférieur au temps";

        this.ecartTemps = ecartTemps;
    }


    // —————————— METHODES PRIVES ——————————

    /**
     * Initialise les points de contrôle de l'activité.
     */
    private void initPointsDeControle() {
        FabriqueIdentifiant.getInstance().resetNoPointDeControle();

        int x      = this.getLargeur() / 2; // centreX
        int y      = this.getHauteur() / 2; // centreY
        int radius = POINT_DE_CONTROLE_RADIUS;

        int[] pos1 = { (x - radius), (-radius) };
        int[] pos2 = { (this.getLargeur() - radius), (y - radius) };
        int[] pos3 = { (x - radius), (this.getHauteur() - radius) };
        int[] pos4 = { (-radius), (y - radius) };

        this.pointsDeControle[0] = new PointDeControleIG(this, pos1, "HAUT"); // HAUT
        this.pointsDeControle[1] = new PointDeControleIG(this, pos2, "DROITE"); // DROITE
        this.pointsDeControle[2] = new PointDeControleIG(this, pos3, "BAS"); // BAS
        this.pointsDeControle[3] = new PointDeControleIG(this, pos4, "GAUCHE"); // GAUCHE
    }
}
