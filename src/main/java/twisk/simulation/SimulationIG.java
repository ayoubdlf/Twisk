package twisk.simulation;

import twisk.exceptions.MondeException;
import twisk.monde.Monde;
import twisk.mondeIG.MondeIG;


public class SimulationIG {

    private MondeIG mondeIG;
    private Monde   monde;


    public SimulationIG(MondeIG mondeIG) {
        assert (mondeIG != null) : "Le monde ne doit pas etre null";

        this.mondeIG = mondeIG;
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
}
