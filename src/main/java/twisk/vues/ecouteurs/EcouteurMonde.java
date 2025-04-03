package twisk.vues.ecouteurs;

import javafx.event.*;
import javafx.scene.input.MouseEvent;
import twisk.mondeIG.MondeIG;


public class EcouteurMonde implements EventHandler<MouseEvent> {

    private MondeIG monde;
    private int x;
    private int y;


    /**
     * Constructeur de la classe EcouteurMonde.
     * 
     * @param monde Le monde.
     */
    public EcouteurMonde(MondeIG monde) {
        this.monde = monde;
        this.x     = 0;
        this.y     = 0;
    }


    // —————————— GETTERS ——————————

    /**
     * Retourne la coordonnée X de la souris.
     * 
     * @return la coordonnée X
     */
    public int getX() {
        return this.x;
    }

    /**
     * Retourne la coordonnée Y de la souris.
     * 
     * @return la coordonnée Y
     */
    public int getY() {
        return this.y;
    }


    // —————————— SETTERS ——————————

    /**
     * Définit la coordonnée X de la souris.
     * 
     * @param x la nouvelle coordonnée X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Définit la coordonnée Y de la souris.
     * 
     * @param y la nouvelle coordonnée Y
     */
    public void setY(int y) {
        this.y = y;
    }


    // —————————— METHODES PUBLIQUES ——————————

    /**
     * Gère l'événement de mouvement de la souris.
     * Met à jour les coordonnées de la souris et notifie les observateurs si un point est sélectionné.
     * 
     * @param event l'événement de mouvement de la souris
     */
    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            this.setX((int) event.getX());
            this.setY((int) event.getY());

            // update la position de la souris uniquement si on a un arc temporaire, sinon y'a pas besoin
            if(this.monde.getArcTemporaire() != null) {
                this.monde.notifierObservateurs();
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if(event.getClickCount() == 2) {
                this.monde.supprimerArcTemporaire();
                this.monde.notifierObservateurs();
            }
        }
    }

}