package twisk.vues;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import twisk.outils.TailleComposants;
import twisk.mondeIG.*;
import twisk.vues.ecouteurs.EcouteurPointDeControle;


public class VuePointDeControleIG extends Circle {

    private MondeIG monde;
    private PointDeControleIG pdc;

    /**
     * Constructeur de la classe VuePointDeControleIG.
     * 
     * @param monde Le monde.
     * @param pdc   Le point de contrôle.
     */
    public VuePointDeControleIG(MondeIG monde, PointDeControleIG pdc) {
        super(TailleComposants.POINT_DE_CONTROLE_RADIUS);

        this.monde = monde;
        this.pdc   = pdc;

        this.initialiser();

    }


    // —————————— GETTERS ——————————

    /**
     * Retourne le point de contrôle.
     * 
     * @return Le point de contrôle.
     */
    public PointDeControleIG getPointDeControle() {
        return this.pdc;
    }


    // —————————— METHODES PRIVEES ——————————

    /**
     * Initialise la vue du point de contrôle.
     */
    private void initialiser() {
        this.setId("PDC_" + this.pdc.getIdentifiant());
        this.pdc.updatePosition();
        this.relocate(this.pdc.getX(), this.pdc.getY());

        // ecouteur
        this.addEventHandler(MouseEvent.ANY, new EcouteurPointDeControle(this.monde, this.pdc, this));


        this.getStyleClass().add("vue-point-de-controle-activite");
    }

    public void draw() {
        if(this.pdc.estUtilise() || this.pdc.estUtiliseParArcTemporaire()) {
            if(this.pdc.getEtape().estUneActivite()) { this.getStyleClass().add("vue-point-de-controle-activite-clicked"); }
        } else {
            if(this.pdc.getEtape().estUneActivite()) { this.getStyleClass().add("vue-point-de-controle-activite"); }
        }
    }
}
