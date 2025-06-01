package twisk.vues;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.scene.shape.*;
import twisk.mondeIG.MondeIG;
import twisk.vues.ecouteurs.*;
import java.util.function.Supplier;


public class VueOutils extends TilePane implements Observateur {

    private MondeIG monde;
    private Button simulation, activite, guichet;

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
    public void reagir() {
        VueOutils vueOutils = this;
        Runnable command = () -> {
            vueOutils.updateBouttons();
        };

        if(Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);
        }
    }


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

    private void updateBouttons() {
        this.activite.setDisable(this.monde.estSimulationEnCours());
        this.guichet.setDisable(this.monde.estSimulationEnCours());
    }

    /**
     * Crée et ajoute le bouton de simulation à l'interface.
     */
    private void ajouterBoutonStartSimulation() {
        this.simulation = new Button("Simulation");
        simulation.setId("Simulation");
        simulation.setGraphic(((Supplier<SVGPath>) () -> {
            SVGPath svg = new SVGPath();
            svg.setContent("M4.5 5.653c0-1.427 1.529-2.33 2.779-1.643l11.54 6.347c1.295.712 1.295 2.573 0 3.286L7.28 19.99c-1.25.687-2.779-.217-2.779-1.643V5.653Z");
            svg.setFillRule(FillRule.EVEN_ODD);
            svg.setStyle("-fx-fill: green-500");

            double scale = 14 / svg.getBoundsInLocal().getHeight();
            svg.setScaleX(scale);
            svg.setScaleY(scale);

            return svg;
        }).get());
        simulation.getStyleClass().add("button-simulation");
        simulation.setOnAction(new EcouteurBoutonSimulation(this.monde, simulation));

        Tooltip tooltip = new Tooltip("Simuler le monde");
        Tooltip.install(simulation, tooltip);

        this.getChildren().add(simulation);
    }

    /**
     * Crée et ajoute le bouton de activite à l'interface.
     */
    private void ajouterBoutonActivite() {
        this.activite = new Button("Activite");
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

    /**
     * Crée et ajoute le bouton de guochet à l'interface.
     */
    private void ajouterBoutonGuichet() {
        this.guichet = new Button("Guichet");
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
