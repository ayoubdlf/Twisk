package twisk.vues.ecouteurs;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.*;
import twisk.mondeIG.*;
import twisk.vues.VueEtapeIG;


public class EcouteurEtape implements EventHandler<MouseEvent> {

    private MondeIG    monde;
    private EtapeIG    etape;
    private VueEtapeIG vue;


    /**
     * Constructeur de la classe EcouteurEtape.
     *
     * @param monde Le monde.
     * @param etape L'étape.
     * @param vue   La vue.
     */
    public EcouteurEtape(MondeIG monde, EtapeIG etape, VueEtapeIG vue) {
        assert(monde != null) : "Le monde ne doit pas être null";
        assert(etape != null) : "L'étape ne doit pas être null";
        assert(vue != null)   : "La vue ne doit pas être null";

        this.monde = monde;
        this.etape = etape;
        this.vue   = vue;
    }


    // —————————— GETTERS ——————————

    /**
     * Retourne l'étape associée à l'écouteur.
     *
     * @return l'étape associée
     */
    public EtapeIG getEtape() {
        return this.etape;
    }


    // —————————— METHODES PUBLIQUES ——————————

    /**
     * Gère les événements de la souris sur l'etape.
     * @param event L'événement de la souris.
     */
    @Override
    public void handle(MouseEvent event) {
        if(event.getButton() == MouseButton.SECONDARY) { return; }

        if(event.getEventType() == MouseEvent.MOUSE_ENTERED) { this.vue.setCursor(Cursor.HAND); }
        if(event.getEventType() == MouseEvent.MOUSE_EXITED)  { this.vue.setCursor(Cursor.DEFAULT); }
        
        if(event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            this.etape.setEstSelectionne(!this.etape.estSelectionne());
            this.monde.notifierObservateurs();
        }
    }

}