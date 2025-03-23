package twisk.monde;

import java.util.*;


public class GestionnaireEtapes implements Iterable<Etape> {

    private ArrayList<Etape> etapes;


    public GestionnaireEtapes() {
        this.etapes = new ArrayList<>();
    }


    /* —————————— SETTERS —————————— */

    /**
     * Ajoute une ou plusieurs étapes à la liste.
     *
     * @param etapes Les étapes à ajouter.
     */
    public void ajouter(Etape... etapes) {
        assert (etapes != null)    : "Les etapes ne doivent pas etre nulles";
        assert (etapes.length > 0) : "Les etapes ne doivent pas etre vides";

        for(Etape etape : etapes) {
            assert (etape != null) : "L'etape ne doit pas etre nulle";
            this.etapes.add(etape);
        }
    }

    /* —————————— GETTERS —————————— */

    /**
     * Retourne le nombre total d'étapes.
     *
     * @return Le nombre d'étapes.
     */
    public int nbEtapes() {
        return this.etapes.size();
    }

    /**
     * Retourne le nombre d'activités dans la liste d'étapes.
     *
     * @return Le nombre d'activités.
     */
    public int nbActivites() {
        return (int) this.etapes.stream().filter(Etape::estUneActivite).count();
    }

    /**
     * Retourne le nombre de guichets dans la liste d'étapes.
     *
     * @return Le nombre de guichets.
     */
    public int nbGuichets() {
        return (int) this.etapes.stream().filter(Etape::estUnGuichet).count();
    }

    /**
     * Vérifie si une étape donnée est présente dans la liste.
     *
     * @param etape L'étape à vérifier.
     * @return true si l'étape est présente, false sinon.
     */
    public boolean contains(Etape etape) {
        return this.etapes.contains(etape);
    }

    /**
     * Retourne un itérateur sur les étapes.
     *
     * @return Un itérateur sur les étapes.
     */
    public Iterator<Etape> iterator() {
        return this.etapes.iterator();
    }

    /**
     * Retourne un itérateur sur les guichets.
     *
     * @return Un itérateur sur les guichets.
     */
    public Iterator<Etape> iteratorGuichets() {
        return this.etapes.stream()
                .filter(Etape::estUnGuichet)
                .iterator();
    }

    /**
     * Retourne l'étape à l'index spécifié.
     *
     * @param i L'index de l'étape à retourner.
     * @return L'étape à l'index spécifié.
     */
    public Etape getEtape(int i) {
        return this.etapes.get(i);
    }
}
