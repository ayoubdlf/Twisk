package twisk.vues;

import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;
import twisk.mondeIG.MondeIG;
import twisk.vues.ecouteurs.EcouteurBouton;
import java.util.function.Supplier;


public class VueOutils extends TilePane implements Observateur {

    private MondeIG monde;
    private Button guichet;


    /**
     * Constructeur de la classe VueOutils.
     * 
     * @param monde Le monde.
     */
    public VueOutils(MondeIG monde) {
        this.monde = monde;
        this.monde.ajouterObservateur(this);

        this.initialiser();
    }


    // —————————— METHODES PUBLIQUES ——————————

    @Override
    public void reagir() {}


    // —————————— METHODES PRIVES ——————————

    /**
     * Initialise les composants de l'interface utilisateur.
     * Crée les boutons et les associe à leurs actions.
     */
    private void initialiser() {
        this.getStyleClass().add("vue-outils");
        this.ajouterBoutonActivite();
        // this.ajouterBoutonGuichet();
    }

    private void ajouterBoutonActivite() {
        Button activite = new Button("Activite");
        activite.setGraphic(((Supplier<SVGPath>) () -> {
            SVGPath svg = new SVGPath();
            svg.setContent("M14.615 1.595a.75.75 0 0 1 .359.852L12.982 9.75h7.268a.75.75 0 0 1 .548 1.262l-10.5 11.25a.75.75 0 0 1-1.272-.71l1.992-7.302H3.75a.75.75 0 0 1-.548-1.262l10.5-11.25a.75.75 0 0 1 .913-.143Z");
            svg.setFillRule(FillRule.EVEN_ODD);
            svg.setStyle("-fx-fill: indigo-500");

            double scale = 14 / svg.getBoundsInLocal().getHeight();
            svg.setScaleX(scale);
            svg.setScaleY(scale);

            return svg;
        }).get());
        activite.getStyleClass().add("button-activite");
        activite.setOnAction(new EcouteurBouton(this.monde, activite));

        Tooltip tooltip = new Tooltip("Ajouter une activite");
        Tooltip.install(activite, tooltip);

        this.getChildren().add(activite);
    }

    private void ajouterBoutonGuichet() {
        this.guichet = new Button("Guichet");
        guichet.setGraphic(((Supplier<SVGPath>) () -> {
            SVGPath svg = new SVGPath();
            svg.setContent("M14.615 1.595a.75.75 0 0 1 .359.852L12.982 9.75h7.268a.75.75 0 0 1 .548 1.262l-10.5 11.25a.75.75 0 0 1-1.272-.71l1.992-7.302H3.75a.75.75 0 0 1-.548-1.262l10.5-11.25a.75.75 0 0 1 .913-.143Z");
            svg.setFillRule(FillRule.EVEN_ODD);
            svg.setStyle("-fx-fill: emerald-500");

            double scale = 14 / svg.getBoundsInLocal().getHeight();
            svg.setScaleX(scale);
            svg.setScaleY(scale);

            return svg;
        }).get());
        guichet.getStyleClass().add("button-guichet");
        guichet.setOnAction(new EcouteurBouton(this.monde, this.guichet));

        Tooltip tooltip = new Tooltip("Ajouter un guichet");
        Tooltip.install(guichet, tooltip);

        this.getChildren().add(guichet);
    }



}
