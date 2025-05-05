package twisk.simulation;

import twisk.monde.Etape;
import twisk.mondeIG.EtapeIG;
import java.util.HashMap;


public class CorrespondancesEtapes {

    private HashMap<EtapeIG, Etape> etapes;
    private HashMap<Etape, EtapeIG> etapesIG;

    public CorrespondancesEtapes() {
        this.etapes   = new HashMap<>();
        this.etapesIG = new HashMap<>();
    }

    public void ajouter(EtapeIG etapeIG, Etape etape) {
        this.etapes.put(etapeIG, etape);
        this.etapesIG.put(etape, etapeIG);
    }

    public Etape get(EtapeIG etapeIG) {
        return this.etapes.get(etapeIG);
    }

    public EtapeIG get(Etape etape) {
        return this.etapesIG.get(etape);
    }
}