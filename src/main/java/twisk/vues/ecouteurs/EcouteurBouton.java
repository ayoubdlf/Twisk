package twisk.vues.ecouteurs;

import javafx.event.*;
import javafx.scene.control.Button;
import twisk.mondeIG.MondeIG;


public class EcouteurBouton implements EventHandler<ActionEvent> {

    private MondeIG monde;
    private Button  button;


    /**
     * Constructeur de la classe EcouteurBouton.
     * 
     * @param monde   Le monde.
     * @param button  Le bouton.
     */
    public EcouteurBouton(MondeIG monde, Button button) {
        this.monde  = monde;
        this.button = button;
    }


    // —————————— METHODES PUBLIQUES ——————————

    /**
     * Gère l'événement de clic sur le bouton.
     * 
     * @param actionEvent l'événement d'action déclenché par le clic sur le bouton
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        if(this.button.getId().equals("Activite") || this.button.getId().equals("Guichet")) {
            this.monde.ajouter(this.button.getId());
        }
    }

}