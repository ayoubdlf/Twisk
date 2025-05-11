package twisk.vues;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import twisk.mondeIG.*;
import twisk.simulation.Client;

import java.util.ArrayList;
import java.util.Comparator;


public class VueGuichetIG extends VueEtapeIG {

    /**
     * Constructeur de la classe VueGuichetIG.
     *
     * @param monde Le monde.
     * @param etape L'étape.
     */
    public VueGuichetIG(MondeIG monde, EtapeIG etape) {
        super(monde, etape);
    }


    // —————————— METHODES PUBLIQUES ——————————

    @Override
    public void draw() {
        VueGuichetIG vueGuichetIG = this;

        Runnable command = () -> {
            vueGuichetIG.getChildren().clear();

            vueGuichetIG.initStyle();
            vueGuichetIG.drawHeader();
            vueGuichetIG.drawBody();
        };

        if(Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);

        }
    }


    // —————————— METHODES PRIVES ——————————

    private void initStyle() {
        this.getStyleClass().add("vue-guichet");

        if(this.etape.estSelectionne()) {
            this.getStyleClass().add("vue-guichet-clicked");
        } else {
            this.getStyleClass().remove("vue-guichet-clicked");
        }
    }

    private void drawHeader() {
        HBox header = new HBox();
        header.getStyleClass().add("vue-guichet-header");

        ArrayList<SVGPath> icones = new ArrayList<>(3);
        if(this.etape.estUneEntree()) { icones.add(this.getIconeEntree()); }
        if(this.etape.estUnGuichet()) { icones.add(this.getIconeGuichet()); }

        Label nom   = new Label(this.etape.getNom());
        HBox  jetons = this.getJetons();

        // On met tout les elements dans un tableau (les icones, le nom, les jetons et les espaces aussi)
        Region espace1 = new Region(); HBox.setHgrow(espace1, Priority.ALWAYS);
        Region espace2 = new Region(); HBox.setHgrow(espace2, Priority.ALWAYS);

        ArrayList<Node> nodes = new ArrayList<>(icones); // icones
        nodes.add(espace1);                              // espace
        nodes.add(nom);                                  // nom de l'etape
        nodes.add(espace2);                              // espace
        nodes.add(jetons);                                // jetons


        header.getChildren().addAll(nodes);
        this.getChildren().add(header);
    }

    private HBox getJetons() {
        HBox jetons = new HBox();
        jetons.getStyleClass().add("vue-guichet-jetons");

        GuichetIG guichet = (GuichetIG) this.etape;
        jetons.getChildren().add(new Label(String.format("%d", guichet.getJetons())));

        return jetons;
    }

}
