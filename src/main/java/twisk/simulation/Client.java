package twisk.simulation;

import twisk.monde.Etape;


public class Client {

    private int pid;
    private int rang;
    private Etape etape;


    public Client(int pid) {
        this.pid   = pid;
        this.rang  = -1;
        this.etape = null;
    }


    /* —————————— METHODES —————————— */

    /**
     * Déplace le client vers une étape spécifique avec un rang donné.
     * @param etape l'étape vers laquelle le client se déplace
     * @param rang la position du client dans l'étape
     */
    public void AllerA(Etape etape, int rang) {
        assert (etape != null) : "L'étape ne peut pas être nulle";
        assert (rang >= 0)     : "Le rang doit être positif";

        this.etape = etape;
        this.rang  = rang;
    }


    /* —————————— GETTERS —————————— */

    /**
     * Retourne le numéro du client.
     * @return le numéro identifiant le client
     */
    public int getNumeroClient() {
        return this.pid;
    }

    /**
     * Retourne le rang du client dans l'étape actuelle.
     * @return le rang du client
     */
    public int getRang() {
        return this.rang;
    }

    /**
     * Retourne l'étape actuelle du client.
     * @return l'étape où se trouve le client
     */
    public Etape getEtape() {
        return this.etape;
    }

    /**
     * Retourne une représentation textuelle du client.
     * @return une chaîne de caractères décrivant le client
     */
    public String toString() {
        return String.format("Client: %d, Pid: %d, Rang: %d", this.pid, this.pid, this.rang);
    }

}
