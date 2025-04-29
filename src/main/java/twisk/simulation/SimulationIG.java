package twisk.simulation;

import twisk.exceptions.MondeException;
import twisk.monde.Monde;
import twisk.mondeIG.MondeIG;
import twisk.mondeIG.SujetObserve;
import twisk.vues.Observateur;


public class SimulationIG implements Observateur {

    private MondeIG mondeIG;
    private Monde   monde;
    private Simulation simulation;


    public SimulationIG(MondeIG mondeIG, Simulation simulation) {
        assert (mondeIG != null) : "Le monde ne doit pas etre null";

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

    private void verifierMondeIG() throws MondeException {
        // tester si le MondeIG est totalement correct
    }

    private Monde creerMonde() {
        return null;
    }

    @Override
    public void reagir() {

    }
}
