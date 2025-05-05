package twisk.vues;

import javafx.scene.input.MouseEvent;
import javafx.scene.shape.*;
import twisk.mondeIG.*;
import twisk.vues.ecouteurs.EcouteurArc;
import static twisk.outils.TailleComposants.POINT_DE_CONTROLE_RADIUS;


public class VueLigneDroiteIG extends VueArcIG {

    private Line line;
    private Polyline polyline;


    public VueLigneDroiteIG(MondeIG monde, ArcIG arc) {
        super(monde, arc);

        this.line     = new Line();
        this.polyline = new Polyline();

        this.initialiser();
    }


    // —————————— METHODES PUBLIQUES ——————————

    @Override
    public void draw() {
        this.drawLine();
        this.drawPolyline();
    }


    // —————————— METHODES PRIVEES ——————————

    /**
     * Initialise les propriétés de la vue de l'arc.
     */
    private void initialiser() {
        this.setId("ARC_" + this.arc.getIdentifiant());

        this.line.getStyleClass().add("vue-arc-line");
        this.polyline.getStyleClass().add("vue-arc-polyline");

        this.line.addEventHandler(MouseEvent.ANY, new EcouteurArc(this.monde, this));
        this.polyline.addEventHandler(MouseEvent.ANY, new EcouteurArc(this.monde, this));

        this.getChildren().addAll(this.line, this.polyline);
        this.draw();
        this.animation(this.line);
    }

    /**
     * Dessine la ligne représentant l'arc.
     */
    private void drawLine() {
        int[] debut = { this.arc.getP1().getX() + POINT_DE_CONTROLE_RADIUS, this.arc.getP1().getY() + POINT_DE_CONTROLE_RADIUS };
        int[] fin   = { this.arc.getP2().getX() + POINT_DE_CONTROLE_RADIUS, this.arc.getP2().getY() + POINT_DE_CONTROLE_RADIUS };

        double minX = Math.min(debut[0], fin[0]);
        double minY = Math.min(debut[1], fin[1]);

        // On fait ca pour que pane commence et finis par les memes points que la ligne (sans ca on aura des problemes lors de la selection lorsqu'on a plusiers arcs dans le monde)
        this.setLayoutX(minX);
        this.setLayoutY(minY);
        this.setPrefWidth(Math.abs(fin[0] - debut[0]));
        this.setPrefHeight(Math.abs(fin[1] - debut[1]));

        this.line.setStartX(debut[0] - minX);
        this.line.setStartY(debut[1] - minY);
        this.line.setEndX(fin[0] - minX);
        this.line.setEndY(fin[1] - minY);

        if(this.arc.estSelectionne()) {
            this.line.getStyleClass().add("vue-arc-line-clicked");
        } else if(this.monde.estAnimationArcs()) {
            this.line.getStyleClass().add("vue-arc-line-animation");
        } else {
            this.line.getStyleClass().add("vue-arc-line");
        }
    }

    /**
     * Dessine la polyligne représentant la flèche de l'arc.
     */
    private void drawPolyline() {
        int[] debut          = { this.arc.getP1().getX() + POINT_DE_CONTROLE_RADIUS, this.arc.getP1().getY() + POINT_DE_CONTROLE_RADIUS };
        int[] fin            = { this.arc.getP2().getX() + POINT_DE_CONTROLE_RADIUS, this.arc.getP2().getY() + POINT_DE_CONTROLE_RADIUS };

        double angle         = Math.atan2(fin[1] - debut[1], fin[0] - debut[0]);
        double largeurFleche = 12;
        double angleFleche   = Math.PI / 6;

        double[] xPoints     = { fin[0], fin[0] - largeurFleche * Math.cos(angle - angleFleche), fin[0] - largeurFleche * Math.cos(angle + angleFleche) };
        double[] yPoints     = { fin[1], fin[1] - largeurFleche * Math.sin(angle - angleFleche), fin[1] - largeurFleche * Math.sin(angle + angleFleche) };

        double minX = this.getLayoutX();
        double minY = this.getLayoutY();

        this.polyline.getPoints().clear();
        this.polyline.getPoints().addAll(
            xPoints[0] - minX, yPoints[0] - minY,
            xPoints[1] - minX, yPoints[1] - minY,
            xPoints[2] - minX, yPoints[2] - minY,
            xPoints[0] - minX, yPoints[0] - minY
        );

        if(this.arc.estSelectionne()) {
            this.polyline.getStyleClass().add("vue-arc-polyline-clicked");
        } else {
            this.polyline.getStyleClass().add("vue-arc-polyline");
        }
    }
}
