package twisk.simulation;

import twisk.exceptions.MondeException;
import twisk.monde.Monde;
import twisk.mondeIG.*;
import twisk.vues.Observateur;


public class SimulationIG implements Observateur {

    private MondeIG mondeIG;
    private Monde   monde;
    private Simulation simulation;


    public SimulationIG(MondeIG mondeIG, Simulation simulation) {
        assert (mondeIG != null)    : "Le monde ne doit pas etre null";
        assert (simulation != null) : "La simulation ne doit pas etre nulle";

        this.mondeIG = mondeIG;
        this.monde   = null;
        this.simulation = simulation;
        this.simulation.ajouterObservateur(this);
    }

    public void simuler() throws MondeException {
        // Verification du monde
        this.verifierMondeIG();

        // Creation du monde
        this.monde = this.creerMonde();
    }

    public void verifierMondeIG() throws MondeException {
        // 1. Il y'a au moins une entree
        if(!this.auMoinsUneEntree()) {
            throw new MondeException("Le monde doit avoir au moins une entree");
        }

        // 2. Il y'a au moins une sortie
        if(!this.auMoinsUneSortie()) {
            throw new MondeException("Le monde doit avoir au moins une sortie");
        }

        for (EtapeIG etape : this.mondeIG) {
            // 3. Toute activite est accessible depuis une entree
            if(!etape.estUneSortie() && !this.possedeEntree(etape)) {
                throw new MondeException("Une etape du monde ne possede pas d'entree");
            }

            // 4. Toute activite mene à une sortie
            if(!etape.estUneSortie() && !this.possedeSortie(etape)) {
                throw new MondeException("Une etape du monde ne possede pas de sortie");
            }

            // 5. Une sortie ne peut pas avoir de successeurs
            if(etape.estUneSortie() && etape.getSuccesseurs().size() > 1) {
                throw new MondeException("Une sortie ne peut pas avoir de successeurs");
            }

            if(etape.estUnGuichet()) {
                // 6. Un guichet ne peut pas etre une sortie
                if(etape.estUneSortie()) {
                    throw new MondeException("Un guichet ne peut pas etre une sortie");
                }

                // 7. Apres un guichet une seule etape
                if(etape.getSuccesseurs().size() > 1) {
                    throw new MondeException("Un guichet ne peut posseder qu'une seule activite restreinte comme successeur");
                }

                // 8. Apres un guichet une unique activite restreinte
                if(!etape.getSuccesseurs().getFirst().estUneActivite()) {
                    throw new MondeException("Un guichet ne peut posseder qu'une seule activite restreinte comme successeur");
                }

                // 9. Une activite restreinte connait qu'une seule etape, un guichet
                if(etape.getSuccesseurs().getFirst().estUneActivite() && etape.getPredecesseurs().size() > 1) {
                    throw new MondeException("Un guichet ne peut posseder qu'une seule activite restreinte comme successeur");
                }

                // 10. Une activite restreinte ne peut pas etre une entree
                if(etape.getSuccesseurs().getFirst().estUneEntree()) {
                    throw new MondeException("Une activite restreinte ne peut pas etre une entree");
                }
            }
        }

        // 11. cycles
    }

    private Monde creerMonde() {
        return null;
    }

    @Override
    public void reagir() {}

    private boolean auMoinsUneEntree() {
        for (EtapeIG etape : this.mondeIG) {
            if(etape.estUneEntree()) {
                return true;
            }
        }

        return false;
    }

    private boolean auMoinsUneSortie() {
        for (EtapeIG etape : this.mondeIG) {
            if(etape.estUneSortie()) {
                return true;
            }
        }

        return false;
    }

    private boolean possedeEntree(EtapeIG etape) {
        if(etape.estUneEntree()) { return true; }

        for (EtapeIG predecesseur : etape.getPredecesseurs()) {
            if(possedeEntree(predecesseur)) {
                return true;
            }
        }

        return false;
    }

    private boolean possedeSortie(EtapeIG etape) {
        if(etape.estUneSortie()) { return true; }

        for (EtapeIG successeur : etape.getSuccesseurs()) {
            if(possedeSortie(successeur)) {
                return true;
            }
        }

        return false;
    }
}
