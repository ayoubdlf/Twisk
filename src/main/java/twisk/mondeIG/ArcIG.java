package twisk.mondeIG;

import com.google.gson.JsonObject;

public abstract class ArcIG {

    private PointDeControleIG p1, p2;
    private boolean estSelectionne;
    private String  identifiant;


    /**
     * Constructeur de la classe ArcIG.
     * 
     * @param p1 Le premier point de contrôle.
     * @param p2 Le deuxième point de contrôle.
     */
    public ArcIG(PointDeControleIG p1, PointDeControleIG p2) {
        assert(p1 != null && p2 != null) : "Les deux points de controle ne doit pas etre nuls";

        this.p1             = p1;
        this.p2             = p2;
        this.estSelectionne = false;
        this.identifiant    = String.format("%s_%s", p1.getIdentifiant(), p2.getIdentifiant());
    }


    // —————————— GETTERS ——————————

    /**
     * Retourne le premier point de contrôle de l'arc.
     * 
     * @return le premier point de contrôle
     */
    public PointDeControleIG getP1() {
        return this.p1;
    }

    /**
     * Retourne le deuxième point de contrôle de l'arc.
     * 
     * @return le deuxième point de contrôle
     */
    public PointDeControleIG getP2() {
        return this.p2;
    }

    /**
     * Retourne si l'arc est sélectionné.
     * 
     * @return true si l'arc est sélectionné, false sinon
     */
    public boolean estSelectionne() {
        return this.estSelectionne;
    }

    /**
     * Retourne l'identifiant de l'arc.
     * 
     * @return l'identifiant de l'arc
     */
    public String getIdentifiant() {
        return this.identifiant;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();

        json.add("source",      this.getP1().toJson());
        json.add("destination", this.getP2().toJson());

        return json;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de ArcIG.
     * 
     * @return une chaîne de caractères représentant ArcIG
     */
    @Override
    public String toString() {
        return String.format("[%s, %s, %s]", this.p1.getIdentifiant(), this.p2.getIdentifiant(), this.estSelectionne);
    }


    // —————————— SETTERS ——————————

    /**
     * Définit l'état de sélection de l'arc.
     * 
     * @param estSelectionne true pour sélectionner l'arc, false sinon
     */
    public void setEstSelectionne(boolean estSelectionne) {
        this.estSelectionne = estSelectionne;
    }

}
