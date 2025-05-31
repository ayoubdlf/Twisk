package twisk.mondeIG;

import java.util.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import twisk.outils.FabriqueIdentifiant;
import twisk.simulation.Client;
import static twisk.outils.TailleComposants.*;


public abstract class EtapeIG implements Iterable<PointDeControleIG> {

    private String nom;
    private int largeur;
    private int hauteur;
    private String identifiant;
    private int[]  position;
    protected PointDeControleIG[] pointsDeControle;
    private boolean estSelectionne;
    private boolean estUneEntree, estUneSortie, estUneActivite, estUnGuichet;
    ArrayList<EtapeIG> predecesseurs, successeurs;
    private ArrayList<Client> clients;


    /**
     * Constructeur de la classe EtapeIG.
     *
     * @param nom Le nom de l'étape.
     * @param largeur La largeur de l'étape.
     * @param hauteur La hauteur de l'étape.
     */
    public EtapeIG(String nom, int largeur, int hauteur) {
        assert (nom != null && !nom.isEmpty()) : "Le nom de l'etape ne doit pas etre nul ou vide";
        assert (largeur > 0)                   : "La largeur doit etre superieure à 0";
        assert (hauteur > 0)                   : "L'hauteur doit etre superieure à 0";

        this.nom              = nom;
        this.largeur          = largeur;
        this.hauteur          = hauteur;
        this.identifiant      = FabriqueIdentifiant.getInstance().getIdentifiantEtape();
        this.position         = new int[] { this.getPositionAleatoire(TWISK_LARGEUR - this.largeur), this.getPositionAleatoire(TWISK_HAUTEUR - this.hauteur) };
        this.pointsDeControle = null;
        this.estSelectionne   = false;

        this.estUneEntree     = false;
        this.estUneSortie     = false;
        this.estUneActivite   = false;
        this.estUnGuichet     = false;

        this.successeurs      = new ArrayList<>();
        this.predecesseurs    = new ArrayList<>();
        this.clients          = new ArrayList<>();
    }


    // —————————— GETTERS ——————————

    /**
     * Retourne le nom de l'étape.
     *
     * @return le nom de l'étape
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Retourne la largeur de l'étape.
     *
     * @return la largeur de l'étape
     */
    public int getLargeur() {
        return this.largeur;
    }

    /**
     * Retourne la hauteur de l'étape.
     *
     * @return la hauteur de l'étape
     */
    public int getHauteur() {
        return this.hauteur;
    }

    /**
     * Retourne l'identifiant de l'étape.
     *
     * @return l'identifiant de l'étape
     */
    public String getIdentifiant() {
        return this.identifiant;
    }

    /**
     * Retourne la position de l'étape.
     *
     * @return un tableau contenant les coordonnées de la position
     */
    public int[] getPosition() {
        return this.position;
    }

    /**
     * Indique si l'étape est sélectionnée.
     *
     * @return true si l'étape est sélectionnée, false sinon
     */
    public boolean estSelectionne() {
        return this.estSelectionne;
    }

    /**
     * Indique si l'étape est une entrée.
     *
     * @return true si l'étape est une entrée, false sinon
     */
    public boolean estUneEntree() {
        return this.estUneEntree;
    }

    /**
     * Indique si l'étape est une sortie.
     *
     * @return true si l'étape est une sortie, false sinon
     */
    public boolean estUneSortie() {
        return this.estUneSortie;
    }

    /**
     * Indique si l'étape est une activité.
     *
     * @return true si l'étape est une activité, false sinon
     */
    public boolean estUneActivite() {
        return this.estUneActivite;
    }

    /**
     * Indique si l'étape est un guichet.
     *
     * @return true si l'étape est un guichet, false sinon
     */
    public boolean estUnGuichet() {
        return this.estUnGuichet;
    }

    /**
     * Retourne un itérateur sur les points de contrôle de l'étape.
     *
     * @return un itérateur sur les points de contrôle
     */
    public Iterator<PointDeControleIG> iterator() {
        return Arrays.stream(this.pointsDeControle).iterator();
    }

    public ArrayList<Client> getClients() {
        return this.clients;
    }

    public String getType() {
        if(this.estUneEntree())   { return "entree"; }
        if(this.estUneSortie())   { return "sortie"; }
        if(this.estUneActivite()) { return "activite"; }
        if(this.estUnGuichet())   { return "guichet"; }

        return "";
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        json.addProperty("type", this.getType());
        json.addProperty("nom", this.getNom());
        json.addProperty("identifiant", this.getIdentifiant());
        json.addProperty("x", this.getPosition()[0]);
        json.addProperty("y", this.getPosition()[1]);

        if(this.estUnGuichet()) {
            json.addProperty("nbJetons", ((GuichetIG) this).getJetons());
        }

        if(this.estUneActivite()) {
            json.addProperty("temps", ((ActiviteIG) this).getTemps());
            json.addProperty("ecartTemps", ((ActiviteIG) this).getEcartTemps());
        }

        return json;
    }

    /**
     * Retourne une chaîne de caractères représentant l'étape.
     *
     * @return une chaîne de caractères représentant l'étape
     */
    public String toString() {
        if(this.estUneActivite) {
            ActiviteIG activiteIG = (ActiviteIG) this;
            return String.format("%s(%d, %d)", activiteIG.getNom(), activiteIG.getTemps(), activiteIG.getEcartTemps());
        }

        if(this.estUnGuichet) {
            GuichetIG activiteIG = (GuichetIG) this;
            return String.format("%s(%d)", activiteIG.getNom(), activiteIG.getJetons());
        }

        return null;
    }

    // —————————— SETTERS ——————————

    /**
     * Définit le nom de l'étape.
     *
     * @param nom le nom à définir, ne doit pas être nul ou vide
     */
    public void setNom(String nom) {
        assert (nom != null && !nom.isEmpty()) : "Le nom de l'etape ne doit pas etre nul ou vide";

        this.nom = nom;
    }

    /**
     * Définit la largeur de l'étape.
     *
     * @param largeur la largeur à définir, doit être supérieure à 0
     */
    public void setLargeur(int largeur) {
        assert (largeur > 0) : "La largeur doit etre superieure à 0";

        this.largeur = largeur;
    }

    /**
     * Définit la hauteur de l'étape.
     *
     * @param hauteur la hauteur à définir, doit être supérieure à 0
     */
    public void setHauteur(int hauteur) {
        assert (hauteur > 0) : "L'hauteur doit etre superieure à 0";

        this.hauteur = hauteur;
    }

    /**
     * Définit la position de l'étape.
     *
     * @param x la coordonnée x à définir
     * @param y la coordonnée y à définir
     */
    public void setPosition(int x, int y) {
        this.position[0] = x;
        this.position[1] = y;
    }

    /**
     * Définit si l'étape est sélectionnée.
     *
     * @param estSelectionne true pour sélectionner l'étape, false sinon
     */
    public void setEstSelectionne(boolean estSelectionne) {
        this.estSelectionne = estSelectionne;
    }

    /**
     * Définit si l'étape est une entrée.
     *
     * @param estUneEntree true si l'étape est une entrée, false sinon
     */
    public void setEstUneEntree(boolean estUneEntree) {
        this.estUneEntree = estUneEntree;
    }

    /**
     * Définit si l'étape est une sortie.
     *
     * @param estUneSortie true si l'étape est une sortie, false sinon
     */
    public void setEstUneSortie(boolean estUneSortie) {
        this.estUneSortie = estUneSortie;
    }

    /**
     * Définit si l'étape est une activité.
     *
     * @param estUneActivite true si l'étape est une activité, false sinon
     */
    public void setEstUneActivite(boolean estUneActivite) {
        this.estUneActivite = estUneActivite;
    }

    /**
     * Définit si l'étape est un guichet.
     *
     * @param estUnGuichet true si l'étape est un guichet, false sinon
     */
    public void setEstUnGuichet(boolean estUnGuichet) {
        this.estUnGuichet = estUnGuichet;
    }

    public void ajouterClient(Client client) {
        this.clients.add(client);
    }

    // —————————— METHODES PUBLIQUES ——————————

    public void ajouterSuccesseur(EtapeIG etape) {
        assert (etape != null) : "Les etapes ne doivent pas etre nulles";

        this.successeurs.add(etape);
    }

    public void ajouterPredecesseur(EtapeIG etape) {
        assert (etape != null) : "Les etapes ne doivent pas etre nulles";

        this.predecesseurs.add(etape);
    }

    /**
     * Supprime un successeur de cette étape.
     *
     * @param etape le successeur à supprimer, ne doit pas être nul
     */
    public void supprimerSuccesseur(EtapeIG etape) {
        assert (etape != null) : "L'etape ne doit pas etre nulle";

        this.successeurs.remove(etape);
    }

    /**
     * Supprime un prédecesseur de cette étape.
     *
     * @param etape le prédecesseur à supprimer, ne doit pas être nul
     */
    public void supprimerPredecesseur(EtapeIG etape) {
        assert (etape != null) : "L'etape ne doit pas etre nulle";

        this.predecesseurs.remove(etape);
    }

    public ArrayList<EtapeIG> getSuccesseurs() {
        return this.successeurs;
    }

    public ArrayList<EtapeIG> getPredecesseurs() {
        return this.predecesseurs;
    }

    public void supprimerClients() {
        this.clients.clear();
    }

    // —————————— METHODES DEDIE AU CHARGEMENT JSON ——————————

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }


    // —————————— METHODES PRIVES ——————————

    /**
     * Retourne une position aléatoire.
     *
     * @param max la valeur maximale
     * @return une position aléatoire entre 0 et max
     */
    private int getPositionAleatoire(int max) {
        assert (max > 0) : "Le max doit etre superieure à 0";

        Random random = new Random();

        return random.nextInt(max);
    }

    public PointDeControleIG getPointDeControle(int i) {
        return this.pointsDeControle[i];
    }
}

