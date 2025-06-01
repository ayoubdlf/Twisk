package twisk.simulation;

import twisk.monde.Etape;
import java.util.*;


public class GestionnaireClients implements Iterable<Client> {

    private ArrayList<Client> clients;


    /**
     * Constructeur de la classe GestionnaireClients.
     * Initialise la liste des clients.
     */
    public GestionnaireClients() {
        this.clients = new ArrayList<>();
    }


    /* —————————— METHODES —————————— */

    /**
     * Instancie les clients identifiés par leur numéro de processus (numéro de client).
     * @param tabClients tableau des numéros de processus des clients
     */
    public void setClients(int... tabClients) {
        assert (tabClients != null)    : "Le tableau des numéros de processus des clients ne peut pas être nul";
        assert (tabClients.length > 0) : "Le tableau des numéros de processus des clients ne peut pas être vide";

        for(int pid : tabClients) {
            Client client = new Client(pid);
            this.clients.add(client);
        }
    }

    /**
     * Met à jour les attributs etape et rang d'un client.
     * @param numeroClient le numéro du client
     * @param etape l'étape du Monde dans laquelle se trouve le client
     * @param rang le rang du client, utile pour visualiser un ordre dans une file d'attente
     */
    public void allerA(int numeroClient, Etape etape, int rang) {
        assert (numeroClient >= 0) : "Le numéro du client doit être supérieur ou égal à 0";
        assert (etape != null)     : "L'étape ne peut pas être nulle";
        assert (rang >= 0)         : "Le rang doit être supérieur ou égal à 0";

        for(Client client : this.clients) {
            if(client.getNumeroClient() == numeroClient) {
                client.AllerA(etape, rang);
            }
        }
    }

    /**
     * Fait le ménage dans les clients, pour traiter une nouvelle simulation.
     * Supprime tous les clients de la liste.
     */
    public void nettoyer() {
        this.clients.clear();
    }


    /* —————————— GETTERS —————————— */

    /**
     * Retourne un itérateur sur la collection de clients.
     * @return un itérateur de clients
     */
    @Override
    public Iterator<Client> iterator() {
        return this.clients.iterator();
    }

    /**
     * Retourne le nombre de clients.
     *
     * @return le nombre de clients
     */
    public int getNbClients() {
        return this.clients.size();
    }
}
