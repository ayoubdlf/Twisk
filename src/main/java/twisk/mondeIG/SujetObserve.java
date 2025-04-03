package twisk.mondeIG;

import twisk.vues.Observateur;
import java.util.*;


public class SujetObserve {

    private ArrayList<Observateur> observateurs;


    /**
     * Constructeur de la classe SujetObserve.
     */
    public SujetObserve() {
        this.observateurs = new ArrayList<>(2);
    }


    // —————————— SETTERS ——————————

    /**
     * Ajoute un observateur à la liste des observateurs.
     *
     * @param observateur L'observateur à ajouter. Ne doit pas être nul.
     */
    public void ajouterObservateur(Observateur observateur) {
        assert (observateur != null) : "L'observateur ne doit pas etre nul";

        this.observateurs.add(observateur);
    }


    // —————————— METHODES PUBLIQUES ——————————

    /**
     * Notifie tous les observateurs en appelant leur méthode 'reagir'.
     */
    public void notifierObservateurs() {
        List<Observateur> observateurs = new ArrayList<>(this.observateurs);

        for (Observateur observateur : observateurs) {
            observateur.reagir();
        }
    }

}
