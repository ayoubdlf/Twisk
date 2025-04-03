package twisk.vues;

import javafx.scene.input.MouseEvent;
import twisk.mondeIG.*;
import javafx.scene.shape.*;
import twisk.vues.ecouteurs.EcouteurArc;
import static twisk.outils.TailleComposants.POINT_DE_CONTROLE_RADIUS;


public class VueCourbeIG extends VueArcIG {

    private CubicCurve courbe;
    private Polyline   polyline;


    /**
     * Constructeur de la classe VueCourbeIG.
     * 
     * @param monde Le monde.
     * @param arc   L'arc.
     */
    public VueCourbeIG(MondeIG monde, ArcIG arc) {
        super(monde, arc);
        this.courbe   = new CubicCurve();
        this.polyline = new Polyline();

        this.initialiser();
    }


    // —————————— METHODES PUBLIQUES ——————————

    @Override
    public void draw() {
        this.drawCourbe();
        this.drawPolyline();
    }


    // —————————— METHODES PRIVEES ——————————

    /**
     * Initialise les propriétés de la vue de l'arc.
     */
    private void initialiser() {
        this.setId("ARC_" + this.arc.getIdentifiant());

        this.courbe.addEventHandler(MouseEvent.ANY, new EcouteurArc(this.monde, this));
        this.polyline.addEventHandler(MouseEvent.ANY, new EcouteurArc(this.monde, this));

        this.getChildren().addAll(this.courbe, this.polyline);
        this.draw();
        this.animation(this.courbe);
    }


    /**
     * Dessine la courbe.
     */
    private void drawCourbe() {
        int x1 = this.arc.getP1().getX() + POINT_DE_CONTROLE_RADIUS;
        int y1 = this.arc.getP1().getY() + POINT_DE_CONTROLE_RADIUS;
        int x2 = this.arc.getP2().getX() + POINT_DE_CONTROLE_RADIUS;
        int y2 = this.arc.getP2().getY() + POINT_DE_CONTROLE_RADIUS;

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);

        this.courbe.setStartX(x1 - minX);
        this.courbe.setStartY(y1 - minY);
        this.courbe.setEndX(x2 - minX);
        this.courbe.setEndY(y2 - minY);

        // On fait ca pour que le pane commence et fini par les memes points que la courbe (sans ca on aura des problemes lors de la selection lorsqu'on a plusiers arcs dans le monde)
        this.setLayoutX(minX);
        this.setLayoutY(minY);
        this.setPrefWidth(Math.abs(x2 - x1));
        this.setPrefHeight(Math.abs(y2 - y1));

        if(this.arc.estSelectionne()) {
            this.courbe.getStyleClass().add("vue-arc-courbe-clicked");
        } else if(this.monde.estAnimationArcs()) {
            this.courbe.getStyleClass().add("vue-arc-courbe-animation");
        } else {
            this.courbe.getStyleClass().add("vue-arc-courbe");
        }

        this.handleCourbes(x1, y1, x2, y2, minX, minY);
    }

    /**
     * Dessine le triangle.
     */
    private void drawPolyline() {
        // On utlise la formule de bezier (le triangle doit pouvoir suivre l'angle de la courbe)
        double t  = 0.99;
        double t1 = 1 - t;

        double[] debut = {
            Math.pow(t1, 3) * this.courbe.getStartX() + 3 * Math.pow(t1, 2) * t * this.courbe.getControlX1() + 3 * t1 * Math.pow(t, 2) * this.courbe.getControlX2() + Math.pow(t, 3) * this.courbe.getEndX(),
            Math.pow(t1, 3) * this.courbe.getStartY() + 3 * Math.pow(t1, 2) * t * this.courbe.getControlY1() + 3 * t1 * Math.pow(t, 2) * this.courbe.getControlY2() + Math.pow(t, 3) * this.courbe.getEndY()
        };
        double[] fin = { this.courbe.getEndX(), this.courbe.getEndY() };

        double angle         = Math.atan2(fin[1] - debut[1], fin[0] - debut[0]);
        double largeurFleche = 12;
        double angleFleche   = Math.PI / 6;

        double[] xPoints     = { fin[0], fin[0] - largeurFleche * Math.cos(angle - angleFleche), fin[0] - largeurFleche * Math.cos(angle + angleFleche)};
        double[] yPoints     = { fin[1], fin[1] - largeurFleche * Math.sin(angle - angleFleche), fin[1] - largeurFleche * Math.sin(angle + angleFleche)};

        double minX = this.getLayoutX();
        double minY = this.getLayoutY();
        this.polyline.setLayoutX(minX);
        this.polyline.setLayoutY(minY);

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

    private void handleCourbes(int x1, int y1, int x2, int y2, int minX, int minY) {
        // On a besoin de la position de des deux points (haut, droite, bas, gauche) pour ameillorer les courbes (courbes vers le haut, courbes vers la bas ...)
        boolean p1EstAGauche = this.arc.getP1().estAGauche();
        boolean p1EstADroite = this.arc.getP1().estADroite();
        boolean p1EstEnBas   = this.arc.getP1().estEnBas();
        boolean p1EstEnHaut  = this.arc.getP1().estEnHaut();

        boolean p2EstAGauche = this.arc.getP2().estAGauche();
        boolean p2EstADroite = this.arc.getP2().estADroite();
        boolean p2EstEnBas   = this.arc.getP2().estEnBas();
        boolean p2EstEnHaut  = this.arc.getP2().estEnHaut();


        // COURBE HAUT-HAUT
        if (p1EstEnHaut && p2EstEnHaut) {
            int controlX1 = ((x1 + x2) / 2) + (x1 < x2 ? -100 : 100);
            int controlX2 = ((x1 + x2) / 2) + (x1 > x2 ? -100 : 100);
            int controlY  = Math.min(y1, y2) - (Math.abs(x1 - x2) / 2);

            this.courbe.setControlX1(controlX1 - minX);
            this.courbe.setControlX2(controlX2 - minX);
            this.courbe.setControlY1(controlY - minY);
            this.courbe.setControlY2(controlY - minY);
        }

        // COURBE BAS-BAS
        if (p1EstEnBas && p2EstEnBas) {
            int controlX1 = ((x1 + x2) / 2) + (x1 < x2 ? -100 : 100);
            int controlX2 = ((x1 + x2) / 2) + (x1 > x2 ? -100 : 100);
            int controlY  = Math.max(y1, y2) + (Math.abs(x1 - x2) / 2);

            this.courbe.setControlX1(controlX1 - minX);
            this.courbe.setControlX2(controlX2 - minX);
            this.courbe.setControlY1(controlY - minY);
            this.courbe.setControlY2(controlY - minY);
        }

        // COURBE DROITE-DROITE
        if (p1EstADroite && p2EstADroite) {
            int controlX  = Math.max(x1, x2) + (Math.abs(y1 - y2) / 2);
            int controlY1 = ((y1 + y2) / 2) + (y1 < y2 ? -100 : 100);
            int controlY2 = ((y1 + y2) / 2) + (y1 > y2 ? -100 : 100);

            this.courbe.setControlX1(controlX - minX);
            this.courbe.setControlX2(controlX - minX);
            this.courbe.setControlY1(controlY1 - minY);
            this.courbe.setControlY2(controlY2 - minY);
        }

        // COURBE GAUCHE-GAUCHE
        if (p1EstAGauche && p2EstAGauche) {
            int controlX  = Math.min(x1, x2) - (Math.abs(y1 - y2) / 2);
            int controlY1 = ((y1 + y2) / 2) + (y1 < y2 ? -100 : 100);
            int controlY2 = ((y1 + y2) / 2) + (y1 > y2 ? -100 : 100);

            this.courbe.setControlX1(controlX - minX);
            this.courbe.setControlX2(controlX - minX);
            this.courbe.setControlY1(controlY1 - minY);
            this.courbe.setControlY2(controlY2 - minY);
        }
        
        // COURBE HAUT-GAUCHE | GAUCHE-HAUT
        if ((p1EstEnHaut && p2EstAGauche) || (p1EstAGauche && p2EstEnHaut)) {
            int controlX1 = (p1EstEnHaut && p2EstAGauche) ? Math.min(x1, x2) : Math.max(x1, x2);
            int controlX2 = (p1EstEnHaut && p2EstAGauche) ? Math.max(x1, x2) : Math.min(x1, x2);
            int controlY1 = Math.min(y1, y2);
            int controlY2 = Math.min(y1, y2);

            this.courbe.setControlX1(controlX1 - minX);
            this.courbe.setControlX2(controlX2 - minX);
            this.courbe.setControlY1(controlY1 - minY);
            this.courbe.setControlY2(controlY2 - minY);
        }

        // COURBE HAUT-DROITE | DROITE-HAUT
        if ((p1EstEnHaut && p2EstADroite) || (p1EstADroite && p2EstEnHaut)) {
            int controlX1 = (p1EstEnHaut && p2EstADroite) ? Math.max(x1, x2) : Math.min(x1, x2);
            int controlX2 = (p1EstEnHaut && p2EstADroite) ? Math.min(x1, x2): Math.max(x1, x2);
            int controlY1 = Math.min(y1, y2);
            int controlY2 = Math.min(y1, y2);

            this.courbe.setControlX1(controlX1 - minX);
            this.courbe.setControlX2(controlX2 - minX);
            this.courbe.setControlY1(controlY1 - minY);
            this.courbe.setControlY2(controlY2 - minY);
        }

        // COURBE BAS-GAUCHE | GAUCHE-BAS
        if ((p1EstEnBas && p2EstAGauche) || (p1EstAGauche && p2EstEnBas)) {
            int controlX1 = (p1EstEnBas && p2EstAGauche) ? Math.min(x1, x2) : Math.max(x1, x2);
            int controlX2 = (p1EstEnBas && p2EstAGauche) ? Math.max(x1, x2) : Math.min(x1, x2);
            int controlY1 = Math.max(y1, y2);
            int controlY2 = Math.max(y1, y2);

            this.courbe.setControlX1(controlX1 - minX);
            this.courbe.setControlX2(controlX2 - minX);
            this.courbe.setControlY1(controlY1 - minY);
            this.courbe.setControlY2(controlY2 - minY);
        }

        // COURBE BAS-DROITE | DROITE-BAS
        if ((p1EstEnBas && p2EstADroite) || (p1EstADroite && p2EstEnBas)) {
            int controlX1 = (p1EstEnBas && p2EstADroite) ? Math.max(x1, x2) : Math.min(x1, x2);
            int controlX2 = (p1EstEnBas && p2EstADroite) ? Math.min(x1, x2) : Math.max(x1, x2);
            int controlY1 = Math.max(y1, y2);
            int controlY2 = Math.max(y1, y2);

            this.courbe.setControlX1(controlX1 - minX);
            this.courbe.setControlX2(controlX2 - minX);
            this.courbe.setControlY1(controlY1 - minY);
            this.courbe.setControlY2(controlY2 - minY);
        }

    }

}
