package twisk.vues;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import twisk.mondeIG.*;
import twisk.simulation.Client;

import java.util.ArrayList;


public class VueActiviteIG extends VueEtapeIG {

    /**
     * Constructeur de la classe VueActiviteIG.
     * 
     * @param monde Le monde.
     * @param etape L'étape.
     */
    public VueActiviteIG(MondeIG monde, EtapeIG etape) {
        super(monde, etape);
    }


    // —————————— METHODES PUBLIQUES ——————————

    @Override
    public void draw() {
        VueActiviteIG vueActiviteIG = this;

        Runnable command = () -> {
            vueActiviteIG.getChildren().clear();

            vueActiviteIG.initStyle();
            vueActiviteIG.drawHeader();
            vueActiviteIG.drawBody();
        };

        if(Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);
        }
    }


    // —————————— METHODES PRIVES ——————————

    private void initStyle() {
        this.getStyleClass().add("vue-activite");

        if(this.etape.estSelectionne()) {
            this.getStyleClass().add("vue-activite-clicked");
        } else {
            this.getStyleClass().remove("vue-activite-clicked");
        }
    }

    private void drawHeader() {
        HBox header = new HBox();
        header.getStyleClass().add("vue-activite-header");

        ArrayList<SVGPath> icones = new ArrayList<>(3);
        if(this.etape.estUneEntree())   { icones.add(this.getIconeEntree()); }
        if(this.etape.estUneActivite()) { icones.add(this.getIconeActivite()); }
        if(this.etape.estUneSortie())   { icones.add(this.getIconeSortie()); }

        Label nom   = new Label(this.etape.getNom());
        HBox  temps = this.getTemps();

        // On met tout les elements dans un tableau (les icones, le nom, les temps et les espaces aussi)
        Region espace1 = new Region(); HBox.setHgrow(espace1, Priority.ALWAYS);
        Region espace2 = new Region(); HBox.setHgrow(espace2, Priority.ALWAYS);

        ArrayList<Node> nodes = new ArrayList<>(icones); // icones
        nodes.add(espace1);                              // espace
        nodes.add(nom);                                  // nom de l'etape
        nodes.add(espace2);                              // espace
        nodes.add(temps);                                // temps


        header.getChildren().addAll(nodes);
        this.getChildren().add(header);
    }

    /**
     * Dessine le corps de l'activité.
     */
    private void drawBody() {
        HBox body = new HBox();
        VBox.setVgrow(body, Priority.ALWAYS);
        body.getStyleClass().add("vue-activite-body");

        for (Client client : etape.getClients()) {
            // TODO: RANG
            VueClient vueClient = new VueClient();
            // vueClient.getStyleClass().add("client-circle"); // TODO:
            body.getChildren().add(vueClient);
        }

        this.getChildren().add(body);
    }


    private HBox getTemps() {
        HBox temps = new HBox();
        temps.getStyleClass().add("vue-activite-temps");

        ActiviteIG activite = (ActiviteIG) this.etape;
        temps.getChildren().add(new Label(String.format("%d ± %d", activite.getTemps(), activite.getEcartTemps())));

        return temps;
    }

}
