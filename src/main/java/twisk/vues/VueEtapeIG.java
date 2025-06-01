package twisk.vues;

import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import twisk.mondeIG.*;
import twisk.simulation.Client;
import twisk.vues.ecouteurs.EcouteurEtape;

import java.util.ArrayList;


public abstract class VueEtapeIG extends VBox {

    protected MondeIG  monde;
    protected EtapeIG  etape;


    /**
     * Constructeur de la classe VueEtapeIG.
     * 
     * @param monde Le monde.
     * @param etape L'étape.
     */
    public VueEtapeIG(MondeIG monde, EtapeIG etape) {
        this.monde  = monde;
        this.etape  = etape;

        this.initialiser();
    }


    // —————————— METHODES PUBLIQUES ——————————

    /**
     * Dessine la vue de l'etape.
     */
    public abstract void draw();


    // —————————— METHODES PRIVES ——————————

    /**
     * Initialise les propriétés de la vue.
     */
    private void initialiser() {
        this.getStyleClass().add("vue-etape");
        this.setMinWidth(this.etape.getLargeur());
        this.setPrefWidth(Region.USE_COMPUTED_SIZE);
        this.setMaxWidth(Double.MAX_VALUE);
        this.widthProperty().addListener((obs, old, val) -> this.etape.setLargeur(val.intValue()));
        this.setPrefHeight(this.etape.getHauteur());

        this.relocate(this.etape.getPosition()[0], this.etape.getPosition()[1]);
        this.draw();
        this.drawMenu();

        // ecouteur
        EcouteurEtape ecouteur = new EcouteurEtape(this.monde, this.etape, this);
        this.addEventHandler(MouseEvent.ANY, ecouteur);

        // dnd
        this.dragAndDrop();
    }

    private void dragAndDrop() {
        this.setOnDragDetected((MouseEvent event) -> {
            Dragboard dragBoard      = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            dragBoard.setDragView(new WritableImage(1, 1)); // on met une image vide, trasparente

            content.putString(this.etape.getIdentifiant());
            dragBoard.setContent(content);
        });
    }
    /**
     * Dessine le menu contextuel.
     */
    private void drawMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem entree     = new MenuItem("Entree");
        MenuItem sortie     = new MenuItem("Sortie");
        MenuItem renommer   = new MenuItem("Renommer");
        MenuItem parametres = new MenuItem("Parametres");
        MenuItem supprimer  = new MenuItem("Supprimer");
        contextMenu.getItems().addAll(entree, sortie, renommer, parametres, supprimer);

        entree.setOnAction(e -> {
            this.etape.setEstUneEntree(!this.etape.estUneEntree());
            this.monde.notifierObservateurs();
        });

        sortie.setOnAction(e -> {
            this.etape.setEstUneSortie(!this.etape.estUneSortie());
            this.monde.notifierObservateurs();
        });

        supprimer.setOnAction(e -> this.monde.supprimerEtape(this.etape));

        parametres.setOnAction(e -> {
            if(this.etape.estUneActivite()) {
                VueMenu.afficherFenetreParametresTemps(this.monde, this.etape);
            } else {
                VueMenu.afficherFenetreParametresJetons(this.monde, this.etape);
            }
        });

        renommer.setOnAction(e -> VueMenu.afficherFenetreRenommer(this.monde, this.etape));

        this.setOnContextMenuRequested(event -> {
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
            event.consume();
        });

    }

    /**
     * Dessine les clients de l'etape.
     */
    protected void drawBody() {
        HBox body = new HBox();
        VBox.setVgrow(body, Priority.ALWAYS);
        body.getStyleClass().add("vue-etape-body");

        ArrayList<Client> clients = new ArrayList<>(this.etape.getClients());
        clients.sort((c1, c2) -> c2.getRang() - c1.getRang());  // du plus grand rang, au plus petit rang

        body.getChildren().clear();
        for (Client client : clients) {
            VueClient vueClient = new VueClient(this.monde, client);
            body.getChildren().add(vueClient);

            // Afficher le rang au centre
            // Label rangTexte = new Label(Integer.toString(client.getRang()));
            // StackPane stack = new StackPane(vueClient, rangTexte);
            // body.getChildren().add(stack);
        }

        this.getChildren().add(body);
    }

    /**
     * Retourne l'icône SVG représentant une étape de type entrée.
     *
     * @return une forme SVG stylisée pour l'entrée
     */
    protected SVGPath getIconeEntree() {
        SVGPath svg = new SVGPath();

        svg.setContent("M16.5 3.75a1.5 1.5 0 0 1 1.5 1.5v13.5a1.5 1.5 0 0 1-1.5 1.5h-6a1.5 1.5 0 0 1-1.5-1.5V15a.75.75 0 0 0-1.5 0v3.75a3 3 0 0 0 3 3h6a3 3 0 0 0 3-3V5.25a3 3 0 0 0-3-3h-6a3 3 0 0 0-3 3V9A.75.75 0 1 0 9 9V5.25a1.5 1.5 0 0 1 1.5-1.5h6Zm-5.03 4.72a.75.75 0 0 0 0 1.06l1.72 1.72H2.25a.75.75 0 0 0 0 1.5h10.94l-1.72 1.72a.75.75 0 1 0 1.06 1.06l3-3a.75.75 0 0 0 0-1.06l-3-3a.75.75 0 0 0-1.06 0Z");
        // svg.setContent("M12 1.5a.75.75 0 0 1 .75.75V7.5h-1.5V2.25A.75.75 0 0 1 12 1.5ZM11.25 7.5v5.69l-1.72-1.72a.75.75 0 0 0-1.06 1.06l3 3a.75.75 0 0 0 1.06 0l3-3a.75.75 0 1 0-1.06-1.06l-1.72 1.72V7.5h3.75a3 3 0 0 1 3 3v9a3 3 0 0 1-3 3h-9a3 3 0 0 1-3-3v-9a3 3 0 0 1 3-3h3.75Z");
        svg.getStyleClass().add("svg-entree");
        svg.setFillRule(FillRule.EVEN_ODD);
        svg.setScaleX(14 / svg.getBoundsInLocal().getHeight());
        svg.setScaleY(14 / svg.getBoundsInLocal().getHeight());

        return svg;
    }

    /**
     * Retourne l'icône SVG représentant une étape de type activité.
     *
     * @return une forme SVG stylisée pour l'activité
     */
    protected SVGPath getIconeActivite() {
        SVGPath svg = new SVGPath();

        svg.setContent("M14.615 1.595a.75.75 0 0 1 .359.852L12.982 9.75h7.268a.75.75 0 0 1 .548 1.262l-10.5 11.25a.75.75 0 0 1-1.272-.71l1.992-7.302H3.75a.75.75 0 0 1-.548-1.262l10.5-11.25a.75.75 0 0 1 .913-.143Z");
        svg.getStyleClass().add("svg-activite");
        svg.setFillRule(FillRule.EVEN_ODD);
        svg.setScaleX(14 / svg.getBoundsInLocal().getHeight());
        svg.setScaleY(14 / svg.getBoundsInLocal().getHeight());

        return svg;
    }

    /**
     * Retourne l'icône SVG représentant une étape de type guichet.
     *
     * @return une forme SVG stylisée pour guichet
     */
    protected SVGPath getIconeGuichet() {
        SVGPath svg = this.getIconeActivite();
        svg.getStyleClass().add("svg-guichet");

        return svg;
    }

    /**
     * Retourne l'icône SVG représentant une étape de type sortie.
     *
     * @return une forme SVG stylisée pour l'sortie
     */
    protected SVGPath getIconeSortie() {
        SVGPath svg = new SVGPath();

        svg.setContent("M7.5 3.75A1.5 1.5 0 0 0 6 5.25v13.5a1.5 1.5 0 0 0 1.5 1.5h6a1.5 1.5 0 0 0 1.5-1.5V15a.75.75 0 0 1 1.5 0v3.75a3 3 0 0 1-3 3h-6a3 3 0 0 1-3-3V5.25a3 3 0 0 1 3-3h6a3 3 0 0 1 3 3V9A.75.75 0 0 1 15 9V5.25a1.5 1.5 0 0 0-1.5-1.5h-6Zm10.72 4.72a.75.75 0 0 1 1.06 0l3 3a.75.75 0 0 1 0 1.06l-3 3a.75.75 0 1 1-1.06-1.06l1.72-1.72H9a.75.75 0 0 1 0-1.5h10.94l-1.72-1.72a.75.75 0 0 1 0-1.06Z");
        // svg.setContent("M11.47 1.72a.75.75 0 0 1 1.06 0l3 3a.75.75 0 0 1-1.06 1.06l-1.72-1.72V7.5h-1.5V4.06L9.53 5.78a.75.75 0 0 1-1.06-1.06l3-3ZM11.25 7.5V15a.75.75 0 0 0 1.5 0V7.5h3.75a3 3 0 0 1 3 3v9a3 3 0 0 1-3 3h-9a3 3 0 0 1-3-3v-9a3 3 0 0 1 3-3h3.75Z");
        svg.getStyleClass().add("svg-sortie");
        svg.setFillRule(FillRule.EVEN_ODD);
        svg.setScaleX(14 / svg.getBoundsInLocal().getHeight());
        svg.setScaleY(14 / svg.getBoundsInLocal().getHeight());

        return svg;
    }

}
