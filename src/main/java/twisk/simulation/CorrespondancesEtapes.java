package twisk.simulation;

import twisk.monde.Etape;
import twisk.mondeIG.EtapeIG;
import java.util.HashMap;


public class CorrespondancesEtapes {

    HashMap<EtapeIG, Etape> etapes;

    public CorrespondancesEtapes() {
        this.etapes = new HashMap<>();
    }

    public void ajouter(EtapeIG etapeIG, Etape etape) {
        this.etapes.put(etapeIG, etape);
    }

    public Etape get(EtapeIG etapeIG) {
        return this.etapes.get(etapeIG);
    }
}