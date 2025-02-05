package twisk.outils;

public class FabriqueNumero {

    private static FabriqueNumero instance;
    private static int cptEtape;
    private static int cptSemaphore;


    private FabriqueNumero() {
        cptEtape = -1;
        cptSemaphore = 0;
    }

    /* —————————— SETTERS —————————— */

    public void incrementNbSemaphore() {
        cptSemaphore++;
    }

    public void incrementNbEtape() {
        cptEtape++;
    }

    public void reset() {
        cptEtape = -1;
        cptSemaphore = 0;
    }

    /* —————————— GETTERS —————————— */

    public static FabriqueNumero getInstance() {
        if(instance == null) {
            instance = new FabriqueNumero();
        }

        return instance;
    }

    public int getNumeroEtape() {
        this.incrementNbEtape();

        return cptEtape;
    }

    public int getNumeroSemaphore() {
        this.incrementNbSemaphore();

        return cptSemaphore;
    }

}
