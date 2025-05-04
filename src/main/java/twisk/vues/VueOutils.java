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
        this.ajouterBoutonStartSimulation();
        this.ajouterBoutonActivite();
        this.ajouterBoutonGuichet();
    }

    private void ajouterBoutonStartSimulation() {
        Button simulation = new Button("Simulation");
        simulation.setId("Simulation");
        simulation.setGraphic(((Supplier<SVGPath>) () -> {
            SVGPath svg = new SVGPath();
            svg.setContent("M10.5 3.798v5.02a3 3 0 0 1-.879 2.121l-2.377 2.377a9.845 9.845 0 0 1 5.091 1.013 8.315 8.315 0 0 0 5.713.636l.285-.071-3.954-3.955a3 3 0 0 1-.879-2.121v-5.02a23.614 23.614 0 0 0-3 0Zm4.5.138a.75.75 0 0 0 .093-1.495A24.837 24.837 0 0 0 12 2.25a25.048 25.048 0 0 0-3.093.191A.75.75 0 0 0 9 3.936v4.882a1.5 1.5 0 0 1-.44 1.06l-6.293 6.294c-1.62 1.621-.903 4.475 1.471 4.88 2.686.46 5.447.698 8.262.698 2.816 0 5.576-.239 8.262-.697 2.373-.406 3.092-3.26 1.47-4.881L15.44 9.879A1.5 1.5 0 0 1 15 8.818V3.936Z");
            svg.setFillRule(FillRule.EVEN_ODD);
            svg.setStyle("-fx-fill: green-500");

            double scale = 14 / svg.getBoundsInLocal().getHeight();
            svg.setScaleX(scale);
            svg.setScaleY(scale);

            return svg;
        }).get());
        simulation.getStyleClass().add("button-simulation");
        simulation.setOnAction(new EcouteurBouton(this.monde, simulation));

        Tooltip tooltip = new Tooltip("Simuler le monde");
        Tooltip.install(simulation, tooltip);

        this.getChildren().add(simulation);
    }

    private void ajouterBoutonActivite() {
        Button activite = new Button("Activite");
        activite.setId("Activite");
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
        Button guichet = new Button("Guichet");
        guichet.setId("Guichet");
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
        guichet.setOnAction(new EcouteurBouton(this.monde, guichet));

        Tooltip tooltip = new Tooltip("Ajouter un guichet");
        Tooltip.install(guichet, tooltip);

        this.getChildren().add(guichet);
    }



}
