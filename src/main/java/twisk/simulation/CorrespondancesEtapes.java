package twisk.simulation;

import twisk.monde.Etape;
import twisk.mondeIG.EtapeIG;
import java.util.HashMap;


public class CorrespondancesEtapes {

    private HashMap<EtapeIG, Etape> etapes;
    private HashMap<Etape, EtapeIG> etapesIG;

    /**
     * Correspondance entre les étapes graphiques (EtapeIG) et les étapes du monde (Etape).
     */
    public CorrespondancesEtapes() {
        this.etapes   = new HashMap<>();
        this.etapesIG = new HashMap<>();
    }

    /**
     * Ajoute une correspondance entre une étape graphique et une étape du monde.
     *
     * @param etapeIG l'étape graphique
     * @param etape l'étape du monde
     */
    public void ajouter(EtapeIG etapeIG, Etape etape) {
        this.etapes.put(etapeIG, etape);
        this.etapesIG.put(etape, etapeIG);
    }

    /**
     * Retourne l'étape du monde associée à une étape graphique.
     *
     * @param etapeIG l'étape graphique
     * @return l'étape du monde correspondante
     */
    public Etape get(EtapeIG etapeIG) {
        return this.etapes.get(etapeIG);
    }

    /**
     * Retourne l'étape graphique associée à une étape du monde.
     *
     * @param etape l'étape du monde
     * @return l'étape graphique correspondante
     */
    public EtapeIG get(Etape etape) {
        return this.etapesIG.get(etape);
    }
}