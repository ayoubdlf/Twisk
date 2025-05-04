package twisk.vues.ecouteurs;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import twisk.exceptions.TwiskException;
import twisk.mondeIG.*;
import twisk.vues.*;


public class EcouteurPointDeControle implements EventHandler<MouseEvent> {

    private MondeIG monde;
    private PointDeControleIG pdc;
    private VuePointDeControleIG vue;


    /**
     * Constructeur de la classe EcouteurPointDeControle.
     *
     * @param monde Le monde.
     * @param vue   La vue.
     */
    public EcouteurPointDeControle(MondeIG monde, PointDeControleIG pdc, VuePointDeControleIG vue) {
        assert(monde != null) : "Le monde ne doit pas être null";
        assert(pdc   != null) : "Le point de controle ne doit pas être null";
        assert(vue   != null) : "La vue ne doit pas être null";

        this.monde = monde;
        this.pdc   = pdc;
        this.vue   = vue;
    }


    // —————————— METHODES PUBLIQUES ——————————

    /**
     * Gère les événements de la souris sur le point de contrôle.
     * @param event L'événement de la souris.
     */
    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {

            // if(this.pdc.estUtilise()) {
            //     VueTwiskException.alert("Ajout d'arc impossible", "Le point de controle est deja utilise par un arc");
            //
            //     this.monde.supprimerArcTemporaire();
            //     this.monde.notifierObservateurs();
            //     return;
            // }

            try {
                this.monde.ajouter(this.vue.getPointDeControle());
            } catch (TwiskException e) {
                VueTwiskException.alert("Ajout d'arc impossible", e.getMessage());

                this.monde.supprimerArcTemporaire();
                this.monde.notifierObservateurs();
            }
        }
    }

}

