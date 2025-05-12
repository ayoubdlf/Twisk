package twisk.monde;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import twisk.mondeIG.ActiviteIG;
import twisk.mondeIG.EtapeIG;
import twisk.mondeIG.GuichetIG;
import twisk.outils.FabriqueNumero;

import java.text.Normalizer;
import java.util.Iterator;


public abstract class Etape implements Iterable<Etape> {

    private String nom;
    private int idEtape;
    private GestionnaireEtapes successeurs;
    private GestionnaireEtapes predecesseurs;


    public Etape(String nom) {
        assert(nom != null && !nom.isEmpty()): "Le nom de l'etape ne doit pas etre nul ou vide" ;

        this.nom           = nom;
        this.successeurs   = new GestionnaireEtapes();
        this.predecesseurs = new GestionnaireEtapes();
        this.idEtape       = FabriqueNumero.getInstance().getNumeroEtape();
    }

    /* —————————— SETTERS —————————— */

    /**
     * Ajoute un ou plusieurs successeurs à l'étape.
     * 
     * @param etapes un ou plusieurs objets de type Etape à ajouter comme successeurs.
     * @throws AssertionError si les étapes sont nulles ou vides, ou si les règles de successeurs ne sont pas respectées.
     */
    public void ajouterSuccesseur(Etape... etapes) {
        assert (etapes != null)    : "Les etapes ne doivent pas etre nulles";
        assert (etapes.length > 0) : "Les etapes ne doivent pas etre vides";

        if(this.estUnGuichet()) {
            assert (etapes.length == 1)                           : "Le guichet ne peut pas avoir plusieurs successeurs";
            assert (etapes[0] != null)                            : "Les etapes ne doivent pas etre nulles";
            assert (etapes[0].estUneActiviteRestreinte())         : "Le guichet ne peut avoir qu'une activite restreinte comme successeur";
            assert (etapes[0].getPredecesseurs().nbEtapes() == 0) : "Une activite restreinte ne peut avoir qu'un seul predecesseur";
            assert (this.getSuccesseurs().nbEtapes() == 0)        : "Le guichet ne peut pas avoir plusiers successeurs";

            this.successeurs.ajouter(etapes[0]);          // on ajoute le succeseur
            etapes[0].ajouterPredecesseur(this); // on notifie le succeseur sur son predecesseur

        } else {
            /*
            * REGLE
            *      - Une activite restreinte ne peut etre que le successeur d'un guichet (donc pas d'une autre activite)
            */
            for (Etape etape : etapes) {
                assert (etape != null)                     : "Les etapes ne doivent pas etre nulles";
                assert (!etape.estUneActiviteRestreinte()) : "Une activite restreinte ne peut etre que le successeur d'un guichet";
            }

            this.successeurs.ajouter(etapes); // on ajoute le succeseur

            for(Etape etape : etapes) {
                etape.ajouterPredecesseur(this);  // on notifie le succeseur sur son predecesseur
            }
        }

    }

    private void ajouterPredecesseur(Etape... etapes) {
        assert (etapes != null)    : "Les etapes ne doivent pas etre nulles";
        assert (etapes.length > 0) : "Les etapes ne doivent pas etre vides";

        this.predecesseurs.ajouter(etapes);
    }


    /* —————————— GETTERS —————————— */

    /**
     * Vérifie si l'étape est une entrée.
     * 
     * @return true si l'étape est une entrée, false sinon.
     */
    public abstract boolean estUneEntree();

    /**
     * Vérifie si l'étape est une sortie.
     * 
     * @return true si l'étape est une sortie, false sinon.
     */
    public abstract boolean estUneSortie();

    /**
     * Vérifie si l'étape est une activité.
     * 
     * @return true si l'étape est une activité, false sinon.
     */
    public abstract boolean estUneActivite();

    /**
     * Vérifie si l'étape est une activité restreinte.
     * 
     * @return true si l'étape est une activité restreinte, false sinon.
     */
    public abstract boolean estUneActiviteRestreinte();

    /**
     * Vérifie si l'étape est un guichet.
     * 
     * @return true si l'étape est un guichet, false sinon.
     */
    public abstract boolean estUnGuichet();

    /**
     * Retourne le nom de l'étape.
     * 
     * @return le nom de l'étape.
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Retourne l'identifiant de l'étape.
     * 
     * @return l'identifiant de l'étape.
     */
    public int getIdEtape() {
        return this.idEtape;
    }

    /**
     * Retourne le gestionnaire des successeurs de l'étape.
     * 
     * @return le gestionnaire des successeurs.
     */
    public GestionnaireEtapes getSuccesseurs() {
        return this.successeurs;
    }

    /**
     * Retourne le nombre de successeurs de l'étape.
     * 
     * @return le nombre de successeurs.
     */
    public int getNbSuccesseurs() {
        return this.successeurs.nbEtapes();
    }

    /**
     * Retourne le successeur à l'index spécifié.
     * 
     * @param i l'index du successeur.
     * @return le successeur à l'index spécifié.
     */
    public Etape getSuccesseur(int i) {
        return this.successeurs.getEtape(i);
    }

    /**
     * Retourne le gestionnaire des prédécesseurs de l'étape.
     * 
     * @return le gestionnaire des prédécesseurs.
     */
    public GestionnaireEtapes getPredecesseurs() {
        return this.predecesseurs;
    }

    /**
     * Retourne le nom de l'étape au format C.
     * 
     * @return le nom de l'étape formaté pour C.
     */
    public String getNomC() {
        String nom = this.getNom().toUpperCase();
        nom = nom.replace(" ", "_"); // Remplace les espaces par des underscores (utilile lors de la creation des define dans le toC())
        nom = nom.replace("-", "_"); // Remplace les tirets par des underscores

        nom = Normalizer.normalize(nom, Normalizer.Form.NFD).replaceAll("\\p{M}", ""); // Enleve les accents
        // nom = this.estUnGuichet() ? "GUICHET_" + nom : nom;

        StringBuilder nomCorrect = new StringBuilder();

        for (char c : nom.toCharArray()) {
            // ne rien faire si le character est 'anglais' ou si c'est un chiffre ou si c'est un underscore
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '_')) {
                nomCorrect.append(c);
            } else {
                /*
                * Convertir en unicode si letter n'est pas a-z ou A-Z
                * On convertir le caractere en unicode et on enlevant les backslash
                * Exemple:
                *   TEST               = u0054u0045u0053u0054
                *   🇱🇧                 = ud83cuddf1ud83cudde7
                *   お前はもう死んでいる。 = ud83cuddf1ud83cudde7
                * */

                nomCorrect.append("u").append(String.format("%04X", (int) c));
            }
        }


        return nomCorrect.toString();
    }

    public Iterator<Etape> iterator() {
        return this.successeurs.iterator();
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

        if(this.estUnGuichet()) {
            json.addProperty("nbJetons", ((Guichet) this).getNbJetons());
        }

        if(this.estUneActivite()) {
            json.addProperty("temps", ((Activite) this).getTemps());
            json.addProperty("ecartTemps", ((Activite) this).getEcartTemps());
        }

        JsonArray jsonSuccesseurs = new JsonArray();
        for (Etape successeur : this.getSuccesseurs()) {
            jsonSuccesseurs.add(successeur.getNom());
        }

        json.add("successeurs", jsonSuccesseurs);

        return json;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'étape.
     * 
     * @return une chaîne représentant l'étape.
     */
    public String toString() {
        StringBuilder str = new StringBuilder();

        // entrée : 1 successeur - balade au zoo
        str.append(String.format("%s : ", this.getNom()));
        str.append(String.format("%d successeur", this.successeurs.nbEtapes()));

        if(this.successeurs.nbEtapes() > 0) {
            str.append(" - ");

            for(Etape etape : this.successeurs) {
                str.append(etape.getNom());
                str.append(" - ");
            }
        }

        return str.toString();
    }

    /**
     * Convertit l'étape en code C.
     * 
     * @return le code C représentant l'étape.
     */
    public abstract String toC();

    /**
     * Convertit l'étape en code C avec un niveau d'indentation spécifié.
     * 
     * @param tab le niveau d'indentation.
     * @return le code C représentant l'étape avec indentation.
     */
    public abstract String toC(int tab);

}
