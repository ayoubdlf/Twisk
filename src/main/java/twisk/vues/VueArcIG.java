package twisk.vues;

import javafx.animation.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import twisk.mondeIG.*;


public abstract class VueArcIG extends Pane {

    protected MondeIG  monde;
    protected ArcIG    arc;
    protected Timeline animation;


    /**
     * Constructeur de la classe VueArcIG.
     * 
     * @param monde Le monde.
     * @param arc   L'arc.
     */
    public VueArcIG(MondeIG monde, ArcIG arc) {
        this.monde     = monde;
        this.arc       = arc;
        this.animation = new Timeline();
    }


    // —————————— GETTERS ——————————

    /**
     * Retourne le monde associé à cette vue.
     * 
     * @return Le monde associé à cette vue.
     */
    public MondeIG getMonde() {
        return this.monde;
    }

    /**
     * Retourne l'arc associé à cette vue.
     * 
     * @return L'arc associé à cette vue.
     */
    public ArcIG getArc() {
        return this.arc;
    }


    // —————————— METHODES PUBLIQUES ——————————

    /**
     * Dessine l'arc.
     */
    public abstract void draw();


    // —————————— METHODES PRIVEES ——————————

    /**
     * Anime l'arc.
     * 
     * @param shape La forme à animer.
     */
    protected void animation(Shape shape) {
        this.animation.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(0), new KeyValue(shape.strokeDashOffsetProperty(), 10)),
                new KeyFrame(Duration.seconds(1.0), new KeyValue(shape.strokeDashOffsetProperty(), -10))
        );
        this.animation.setCycleCount(Animation.INDEFINITE);
        this.animation.setAutoReverse(false);

        if(this.arc.estSelectionne()) {
            this.animation.stop();
        } else {
            this.animation.play();
        }
    }

}
