package twisk.vues.ecouteurs;

import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.shape.*;
import twisk.mondeIG.MondeIG;
import twisk.simulation.SimulationIG;
import twisk.vues.VueMondeException;
import java.util.function.Supplier;


public class EcouteurBoutonSimulation implements EventHandler<ActionEvent> {

    private MondeIG      monde;
    private Button       button;
    private SimulationIG simulation;
    private boolean      estActivite;


    /**
     * Constructeur de la classe EcouteurBouton.
     *
     * @param monde   Le monde.
     * @param button  Le bouton.
     */
    public EcouteurBoutonSimulation(MondeIG monde, Button button) {
        this.monde       = monde;
        this.button      = button;
        this.simulation  = new SimulationIG(this.monde);
        this.estActivite = false;
        this.simulation = new SimulationIG(this.monde);
    }


    // —————————— METHODES PUBLIQUES ——————————

    /**
     * Gère l'événement de clic sur le bouton.
     * 
     * @param actionEvent l'événement d'action déclenché par le clic sur le bouton
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        if(!this.estActivite) {
            // On lance la simulation
            try {
                this.simulation.simuler();
            } catch (Exception e) {
                VueMondeException.alert("Simulation impossible", e.getMessage());
            }
        } else {
            // On stop la simulation
            try {
                this.simulation.stopSimulation();
                // this.simulation = null;
            } catch (Exception e) {
                VueMondeException.alert("Arret de la simulation impossible", e.getMessage());
            }
        }

        this.estActivite = !this.estActivite;
        this.button.setGraphic(((Supplier<SVGPath>) () -> {
            SVGPath svg = new SVGPath();
            svg.setContent(
                    this.estActivite ?
                            "M4.5 7.5a3 3 0 0 1 3-3h9a3 3 0 0 1 3 3v9a3 3 0 0 1-3 3h-9a3 3 0 0 1-3-3v-9Z":
                            "M4.5 5.653c0-1.427 1.529-2.33 2.779-1.643l11.54 6.347c1.295.712 1.295 2.573 0 3.286L7.28 19.99c-1.25.687-2.779-.217-2.779-1.643V5.653Z"
            );
            svg.setFillRule(FillRule.EVEN_ODD);
            svg.setStyle("-fx-fill: green-500");

            double scale = 14 / svg.getBoundsInLocal().getHeight();
            svg.setScaleX(scale);
            svg.setScaleY(scale);

            return svg;
        }).get());
    }

}