package twisk.outils;

public class FabriqueNumero {

    private static FabriqueNumero instance;
    private static int cptEtape;
    private static int cptSemaphore;


    /**
     * Constructeur de la fabrique de numéros.
     * Initialise les compteurs d'étapes et de sémaphores.
     */
    private FabriqueNumero() {
        cptEtape = -1;
        cptSemaphore = 0;
    }

    /* —————————— SETTERS —————————— */

    /**
     * Incrémente le compteur des sémaphores.
     */
    private void incrementNbSemaphore() {
        cptSemaphore++;
    }

    /**
     * Incrémente le compteur des étapes.
     */
    private void incrementNbEtape() {
        cptEtape++;
    }

    /**
     * Réinitialise les compteurs d'étapes et de sémaphores à leurs valeurs initiales.
     */
    public void reset() {
        cptEtape = -1;
        cptSemaphore = 0;
    }

    /* —————————— GETTERS —————————— */

    /**
     * Retourne l'instance de la fabrique de numéros (singleton).
     *
     * @return l'instance de FabriqueNumero
     */
    public static FabriqueNumero getInstance() {
        if(instance == null) {
            instance = new FabriqueNumero();
        }

        return instance;
    }

    /**
     * Incrémente et retourne le numéro de l'étape suivante.
     *
     * @return le numéro d'étape
     */
    public int getNumeroEtape() {
        this.incrementNbEtape();

        return cptEtape;
    }

    /**
     * Incrémente et retourne le numéro du sémaphore suivant.
     *
     * @return le numéro de sémaphore
     */
    public int getNumeroSemaphore() {
        this.incrementNbSemaphore();

        return cptSemaphore;
    }

}
