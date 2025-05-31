package twisk.mondeIG;

import com.google.gson.JsonObject;
import twisk.outils.FabriqueIdentifiant;
import static twisk.outils.TailleComposants.POINT_DE_CONTROLE_RADIUS;


public class PointDeControleIG {

    private int x;
    private int y;
    private EtapeIG etape;
    private String  identifiant;
    private String  posStr;
    private boolean estUtilise; // si le point de controle est utilise dans un arc
    private boolean estUtiliseParArcTemporaire; // si le point de controle est utilise dans un arc temporaire


    /**
     * Constructeur pour créer un point de contrôle avec une étape et des coordonnées.
     * 
     * @param etape l'étape associée au point de contrôle
     * @param x la coordonnée x du point de contrôle
     * @param y la coordonnée y du point de contrôle
     */
    public PointDeControleIG(EtapeIG etape, int x, int y) {
        this.etape      = etape;
        this.x          = x;
        this.y          = y;
        this.estUtilise = false;
        this.estUtiliseParArcTemporaire = false;

        // Ici on regarde si etape != null, parce que lorsqu'on selectionne un arc (quand on a p1 mais pas p2), on n'a pas d'etape
        if(etape != null) {
            this.identifiant = String.format("%s_%s", this.etape.getIdentifiant(), FabriqueIdentifiant.getInstance().getIdentifiantPointDeControle()); // "(NOM ETAPE)_(IDENTIFIANT PDC)"
        }
    }

    /**
     * Constructeur pour créer un point de contrôle avec une étape et un tableau de positions.
     * 
     * @param etape l'étape associée au point de contrôle
     * @param pos un tableau contenant les coordonnées [x, y] du point de contrôle
     * @param posStr la position du point de contrôle
     */
    public PointDeControleIG(EtapeIG etape, int[] pos, String posStr) {
        this(etape, pos[0], pos[1]);

        this.posStr = posStr;
    }


    // —————————— GETTERS ——————————

    /**
     * Retourne l'étape associée au point de contrôle.
     * 
     * @return l'étape associée
     */
    public EtapeIG getEtape() {
        return this.etape;
    }

    /**
     * Retourne la coordonnée x du point de contrôle.
     * 
     * @return la coordonnée x
     */
    public int getX() {
        return (this.etape != null) ? (this.x + this.etape.getPosition()[0]) : this.x;
    }

    /**
     * Retourne la coordonnée y du point de contrôle.
     * 
     * @return la coordonnée y
     */
    public int getY() {
        return (this.etape != null) ? (this.y + this.etape.getPosition()[1]) : this.y;
    }

    /**
     * Retourne l'identifiant du point de contrôle.
     * 
     * @return l'identifiant du point de contrôle
     */
    public String getIdentifiant() {
        return this.identifiant;
    }

    /**
     * Retourne si le point de contrôle est utilisé.
     * 
     * @return Vrai si le point de contrôle est utilisé, faux sinon.
     */
    public boolean estUtilise() {
        return this.estUtilise;
    }

    /**
     * Retourne si le point de contrôle est clique par l'arc temporaire.
     *
     * @return Vrai si le point de contrôle est utilise dans l'arc temporaire, faux sinon.
     */
    public boolean estUtiliseParArcTemporaire() {
        return this.estUtiliseParArcTemporaire;
    }

    /**
     * Retourne si le point de contrôle est à gauche.
     * 
     * @return Vrai si le point de contrôle est à gauche, faux sinon.
     */
    public boolean estAGauche() {
        return this.posStr.equals("GAUCHE");
    }

    /**
     * Retourne si le point de contrôle est à droite.
     * 
     * @return Vrai si le point de contrôle est à droite, faux sinon.
     */
    public boolean estADroite() {
        return this.posStr.equals("DROITE");
    }

    /**
     * Retourne si le point de contrôle est en haut.
     * 
     * @return Vrai si le point de contrôle est en haut, faux sinon.
     */
    public boolean estEnHaut() {
        return this.posStr.equals("HAUT");
    }

    /**
     * Retourne si le point de contrôle est en bas.
     * 
     * @return Vrai si le point de contrôle est en bas, faux sinon.
     */
    public boolean estEnBas() {
        return this.posStr.equals("BAS");
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        json.addProperty("etape", this.getEtape().getIdentifiant());
        json.addProperty("index", Integer.parseInt(this.getIdentifiant().split("_")[1])-1);

        return json;
    }

    /**
     * Retourne une chaîne de caractères représentant le point de contrôle.
     * 
     * @return une chaîne de caractères représentant le point de contrôle
     */
    @Override
    public String toString() {
        return String.format("%s(%d, %d)", this.getIdentifiant(), this.getX(), this.getY());
    }


    // —————————— SETTERS ——————————

    /**
     * Définit si le point de contrôle est utilisé.
     * 
     * @param estUtilise Vrai si le point de contrôle est utilisé, faux sinon.
     */
    public void setEstUtilise(boolean estUtilise) {
        this.estUtilise = estUtilise;
    }

    public void setEstUtiliseParArcTemporaire(boolean estUtiliseParArcTemporaire) {
        this.estUtiliseParArcTemporaire = estUtiliseParArcTemporaire;
    }

    /**
     * Définit la position du point de contrôle avec des coordonnées x et y.
     *
     * @param x la coordonnée x à définir
     * @param y la coordonnée y à définir
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    // —————————— METHODES PUBLIQUES ——————————

    public void updatePosition() {
        int x      = this.etape.getLargeur() / 2; // centreX
        int y      = this.etape.getHauteur() / 2; // centreY
        int radius = POINT_DE_CONTROLE_RADIUS;

        int[] pos1 = { (x - radius), (-radius) };
        int[] pos2 = { (this.etape.getLargeur() - radius), (y - radius) };
        int[] pos3 = { (x - radius), (this.etape.getHauteur() - radius) };
        int[] pos4 = { (-radius), (y - radius) };

        if(this.posStr.equals("HAUT"))   { this.setPosition(pos1[0], pos1[1]); }
        if(this.posStr.equals("DROITE")) { this.setPosition(pos2[0], pos2[1]); }
        if(this.posStr.equals("BAS"))    { this.setPosition(pos3[0], pos3[1]); }
        if(this.posStr.equals("GAUCHE")) { this.setPosition(pos4[0], pos4[1]); }
    }

}
