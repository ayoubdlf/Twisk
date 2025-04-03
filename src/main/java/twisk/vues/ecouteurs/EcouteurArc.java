package twisk.vues.ecouteurs;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import twisk.mondeIG.MondeIG;
import twisk.vues.*;


public class EcouteurArc implements EventHandler<MouseEvent> {

    private MondeIG  monde;
    private VueArcIG vue;


    /**
     * Constructeur de la classe EcouteurArc.
     * 
     * @param monde Le monde.
     * @param vue   La vue.
     */
    public EcouteurArc(MondeIG monde, VueArcIG vue) {
        assert(monde != null) : "Le monde ne doit pas être null";
        assert(vue   != null) : "La vue ne doit pas être null";

        this.vue   = vue;
        this.monde = monde;
    }


    // —————————— METHODES PUBLIQUES ——————————


    /**
     * Gère les événements de la souris sur l'arc.
     * @param event L'événement de la souris.
     */
    @Override
    public void handle(MouseEvent event) {
        if(this.vue == null) { return; }
        if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            this.vue.getArc().setEstSelectionne(!this.vue.getArc().estSelectionne());
            this.monde.notifierObservateurs();

        } else if (event.getEventType() == MouseEvent.MOUSE_ENTERED) {
            this.vue.setCursor(Cursor.HAND);

        } else if (event.getEventType() == MouseEvent.MOUSE_EXITED) {
            this.vue.setCursor(Cursor.DEFAULT);

        }
    }

}