package twisk.vues;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import twisk.mondeIG.*;
import twisk.vues.ecouteurs.EcouteurMonde;
import java.util.*;
import static twisk.outils.TailleComposants.POINT_DE_CONTROLE_RADIUS;


public class VueMondeIG extends Pane implements Observateur {

    private MondeIG monde;
    private EcouteurMonde ecouteur;

    /**
     * Constructeur de la classe VueMondeIG.
     * 
     * @param monde Le monde.
     */
    public VueMondeIG(MondeIG monde) {
        this.monde = monde;
        this.monde.ajouterObservateur(this);

        this.initialiser();
    }


    // —————————— METHODES PUBLIQUES ——————————

    @Override
    public void reagir() {
        this.draw();
    }


    // —————————— METHODES PRIVES ——————————

    /**
     * Initialise la vue.
     */
    private void initialiser() {

        this.ecouteur = new EcouteurMonde(this.monde);
        this.addEventHandler(MouseEvent.ANY, this.ecouteur);

        this.dragAndDrop();

        this.reagir();
    }

    /**
     * Dessine la vue.
     */
    private void draw() {
        VueMondeIG vueMondeIG = this;

        Runnable command = () -> {
            vueMondeIG.getChildren().clear();

            vueMondeIG.drawArcs();
            vueMondeIG.drawEtapes();
        };

        if(Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);
        }
    }

    /**
     * Dessine les arcs entre les étapes.
     */
    private void drawArcs() {
        // Affichage des arcs entre deux etapes
        for (Iterator<ArcIG> it = this.monde.iteratorArcs(); it.hasNext(); ) {
            ArcIG arc = it.next();
            VueArcIG vueArc;

            PointDeControleIG p1 = arc.getP1();
            PointDeControleIG p2 = arc.getP2();


            boolean estDansAxisX = (p1.estADroite() && p2.estAGauche()) || (p1.estAGauche() && p2.estADroite()); // DROITE-GAUCHE ou GAUCHE-DROITE
            boolean estDansAxisY = (p1.estEnHaut() && p2.estEnBas()) || (p1.estEnBas() && p2.estEnHaut());       // HAUT-BAS ou BAS-HAUT

            // LIGNE DROITE
            if(estDansAxisX || estDansAxisY) {
                vueArc = new VueLigneDroiteIG(this.monde, arc);
            } else {
                // COURBE
                vueArc = new VueCourbeIG(this.monde, arc);
            }

            this.getChildren().add(vueArc);
        }

        // LIGNE TEMPORARAIRE = LIGNE DROITE
        PointDeControleIG[] points = this.monde.getArcTemporaire();

        if(points != null) {
            PointDeControleIG p1 = points[0];
            PointDeControleIG p2 = new PointDeControleIG(null, (this.ecouteur.getX() - POINT_DE_CONTROLE_RADIUS), (this.ecouteur.getY() - POINT_DE_CONTROLE_RADIUS));
            VueArcIG vueArc      = new VueLigneDroiteIG(this.monde, new LigneDroiteIG(p1, p2));

            this.getChildren().add(vueArc);
        }
    }

    /**
     * Dessine les étapes dans la vue.
     */
    private void drawEtapes() {
        for(EtapeIG etape : this.monde) {
            VueEtapeIG vueEtape = null;
            
            if(etape.estUneActivite()) { vueEtape = new VueActiviteIG(this.monde, etape); }
            if(etape.estUnGuichet())   { vueEtape = new VueGuichetIG(this.monde, etape); }

            assert (vueEtape != null) : "L'etape ne doit pas etre nulle";
            this.getChildren().add(vueEtape);
            vueEtape.draw();

            // Afficher les points de controle
            this.drawPointsDeControle(etape);
        }
    }

    /**
     * Dessine les points de contrôle.
     * @param etape l'étape à laquelle les points de contrôle sont associés.
     */
    private void drawPointsDeControle(EtapeIG etape) {
        for (PointDeControleIG pdc : etape) {
            VuePointDeControleIG vuePdc = new VuePointDeControleIG(this.monde, pdc);
            this.getChildren().add(vuePdc);
            vuePdc.draw();
        }
    }

    private void dragAndDrop() {
        this.setOnDragOver(event -> {
            this.setCursor(Cursor.CLOSED_HAND);
            if (event.getGestureSource() != this && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }

            Dragboard dragBoard = event.getDragboard();

            if (dragBoard.hasString()) {
                EtapeIG etape = this.monde.getEtape(dragBoard.getString());
                etape.setPosition((int) event.getX() - (etape.getLargeur() / 2), (int) event.getY() - (etape.getHauteur() / 2));

                this.monde.notifierObservateurs();
            }

            event.consume();
        });

        this.setOnDragDropped(event -> {
            this.setCursor(Cursor.DEFAULT);
            Dragboard dragBoard = event.getDragboard();
            event.setDropCompleted(dragBoard.hasString());
            event.consume();
        });
    }
}
