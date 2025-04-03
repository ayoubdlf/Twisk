package twisk.monde;

import twisk.outils.FabriqueNumero;

import java.util.*;


public class Monde implements Iterable<Etape> {

    private GestionnaireEtapes etapes;
    private SasEntree entree;
    private SasSortie sortie;


    public Monde() {
        FabriqueNumero.getInstance().reset(); // TODO: est ce c'est correct ? (le cas ou on a plusiers mondes, chaque etape initiale du monde commencera pas 0)
        this.etapes = new GestionnaireEtapes();
        this.entree = new SasEntree();
        this.sortie = new SasSortie();

        this.etapes.ajouter(this.entree, this.sortie);
    }

    /* —————————— SETTERS —————————— */

    /**
     * Définit les étapes d'entrée pour le monde.
     * 
     * @param etapes Les étapes à ajouter comme entrée.
     * @throws AssertionError si les étapes sont nulles ou vides, ou si une étape est une activité restreinte.
     */
    public void aCommeEntree(Etape... etapes) {
        assert (etapes != null)    : "Les etapes ne doivent pas etre nulles";
        assert (etapes.length > 0) : "Les etapes ne doivent pas etre vides";

        for(Etape etape : etapes) {
            assert (etape != null)                     : "Les etapes ne doivent pas etre nulles";
            assert (!etape.estUneActiviteRestreinte()) : "Une activite restreinte ne peut pas etre une entree";

            this.entree.ajouterSuccesseur(etape);
        }
    }

    /**
     * Définit les étapes de sortie pour le monde.
     * 
     * @param etapes Les étapes à ajouter comme sortie.
     * @throws AssertionError si les étapes sont nulles ou vides, ou si une étape est un guichet.
     */
    public void aCommeSortie(Etape... etapes) {
        assert (etapes != null)    : "Les etapes ne doivent pas etre nulles";
        assert (etapes.length > 0) : "Les etapes ne doivent pas etre vides";

        for(Etape etape : etapes) {
            assert (etape != null)         : "L'etape ne doit pas etre nulle";
            assert (!etape.estUnGuichet()) : "Un guichet ne peut pas etre une sortie";

            etape.ajouterSuccesseur(this.sortie);
        }
    }

    /**
     * Ajoute des étapes au gestionnaire d'étapes du monde.
     * 
     * @param etapes Les étapes à ajouter.
     */
    public void ajouter(Etape... etapes) {
        this.etapes.ajouter(etapes);
    }


    /* —————————— GETTERS —————————— */

    /**
     * Retourne le nombre total d'étapes dans le monde.
     * 
     * @return Le nombre d'étapes.
     */
    public int nbEtapes() {
        return this.etapes.nbEtapes();
    }

    /**
     * Retourne le nombre total d'activités dans le monde.
     * 
     * @return Le nombre d'activités.
     */
    public int nbActivites() {
        return this.etapes.nbActivites();
    }

    /**
     * Retourne le nombre total de guichets dans le monde.
     * 
     * @return Le nombre de guichets.
     */
    public int nbGuichets() {
        return this.etapes.nbGuichets();
    }

    /**
     * Retourne l'étape d'entrée du monde.
     * 
     * @return L'étape d'entrée.
     */
    public SasEntree getEntree() {
        return this.entree;
    }

    /**
     * Retourne l'étape de sortie du monde.
     * 
     * @return L'étape de sortie.
     */
    public SasSortie getSortie() {
        return this.sortie;
    }

    /**
     * Retourne le gestionnaire d'étapes du monde.
     * 
     * @return Le gestionnaire d'étapes.
     */
    public GestionnaireEtapes getEtapes() {
        return this.etapes;
    }

    /**
     * Retourne l'étape à l'index spécifié.
     * 
     * @param i L'index de l'étape à retourner.
     * @return L'étape à l'index spécifié.
     */
    public Etape getEtape(int i) {
        return this.etapes.getEtape(i);
    }

    /**
     * Retourne un itérateur sur les étapes du monde.
     * 
     * @return Un itérateur sur les étapes.
     */
    public Iterator<Etape> iterator() {
        return this.etapes.iterator();
    }

    /**
     * Retourne un itérateur sur les guichets du monde.
     * 
     * @return Un itérateur sur les guichets.
     */
    public Iterator<Etape> iteratorGuichets() {
        return this.etapes.iteratorGuichets();
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du monde.
     * 
     * @return La représentation sous forme de chaîne de caractères.
     */
    public String toString() {
        StringBuilder str = new StringBuilder();

        // ENTREE
        // str.append(this.entree.toString()).append("\n");

        // SORTIE
        // str.append(this.sortie.toString()).append("\n");

        // ETAPES
        for (Etape etape : this.etapes) {
            str.append(etape.toString()).append("\n");
        }

        return str.toString();
    }

    /**
     * Génère le code C correspondant au monde.
     * 
     * @return Le code C généré.
     */
    public String toC() {
        StringBuilder sb = new StringBuilder();

        // headers
        sb.append("#include <time.h>\n");
        sb.append("#include \"def.h\"\n\n");

        for(Etape etape : this) {
            sb.append(String.format("#define %s %s\n", etape.getNomC(), etape.getIdEtape())); // sb.append("#define ").append(etapes.getNomC()).append(" ").append(etapes.getIdEtape()).append("\n");

            if(etape.estUnGuichet()) {
                sb.append(String.format("#define SEM_%s %s\n", etape.getNomC(), ((Guichet)etape).getNumeroSemaphore()));
            }

        }
        sb.append("\n");

        // body
        sb.append("\nvoid simulation(int ids) {\n");
            sb.append("\tsrand(time(NULL) + ids);\n\n");
            sb.append(this.entree.toC(1)); // generer le parcours des client
        sb.append("}\n");

        return sb.toString();
    }
}
