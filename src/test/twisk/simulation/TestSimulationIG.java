package twisk.simulation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import twisk.exceptions.*;
import twisk.mondeIG.*;
import twisk.outils.FabriqueIdentifiant;

import static org.junit.jupiter.api.Assertions.*;


class TestSimulationIG {

    @BeforeEach
    void nettoyer() {
        FabriqueIdentifiant.getInstance().resetNbEtape();
        FabriqueIdentifiant.getInstance().resetNoPointDeControle();
    }

    @Test
    void testSansEntree() throws TwiskException {
        MondeIG mondeIG = new MondeIG();

        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");

        mondeIG.getEtape("ETAPE-0").setEstUneEntree(false);
        mondeIG.getEtape("ETAPE-2").setEstUneSortie(true);

        PointDeControleIG etape1 = mondeIG.getEtape("ETAPE-0").iterator().next();
        PointDeControleIG etape2 = mondeIG.getEtape("ETAPE-1").iterator().next();
        PointDeControleIG etape3 = mondeIG.getEtape("ETAPE-2").iterator().next();

        mondeIG.ajouter(etape1, etape2);
        mondeIG.ajouter(etape2, etape3);

        assertThrows(MondeException.class, () -> {
            SimulationIG simulation = new SimulationIG(mondeIG);
            simulation.verifierMondeIG();
        });
    }

    @Test
    void testSansSortie() throws TwiskException {
        MondeIG mondeIG = new MondeIG();

        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");

        mondeIG.getEtape("ETAPE-0").setEstUneEntree(true);
        mondeIG.getEtape("ETAPE-2").setEstUneSortie(false);

        PointDeControleIG etape1 = mondeIG.getEtape("ETAPE-0").iterator().next();
        PointDeControleIG etape2 = mondeIG.getEtape("ETAPE-1").iterator().next();
        PointDeControleIG etape3 = mondeIG.getEtape("ETAPE-2").iterator().next();

        mondeIG.ajouter(etape1, etape2);
        mondeIG.ajouter(etape2, etape3);

        assertThrows(MondeException.class, () -> {
            SimulationIG simulation = new SimulationIG(mondeIG);
            simulation.verifierMondeIG();
        });
    }

    @Test
    void testToutAccessibleDepuisEntree() throws TwiskException {
        MondeIG mondeIG = new MondeIG();

        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");

        mondeIG.getEtape("ETAPE-0").setEstUneEntree(true);
        mondeIG.getEtape("ETAPE-2").setEstUneSortie(true);

        PointDeControleIG etape1 = mondeIG.getEtape("ETAPE-0").iterator().next();
        PointDeControleIG etape2 = mondeIG.getEtape("ETAPE-1").iterator().next();
        PointDeControleIG etape3 = mondeIG.getEtape("ETAPE-2").iterator().next();

        // mondeIG.ajouter(etape1, etape2);
        mondeIG.ajouter(etape2, etape3);

        assertThrows(MondeException.class, () -> {
            SimulationIG simulation = new SimulationIG(mondeIG);
            simulation.verifierMondeIG();
        });
    }

    @Test
    void testToutEtapeMeneUneSortie() throws TwiskException {
        MondeIG mondeIG = new MondeIG();

        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");

        mondeIG.getEtape("ETAPE-0").setEstUneEntree(true);
        mondeIG.getEtape("ETAPE-2").setEstUneSortie(true);

        PointDeControleIG etape1 = mondeIG.getEtape("ETAPE-0").iterator().next();
        PointDeControleIG etape2 = mondeIG.getEtape("ETAPE-1").iterator().next();
        PointDeControleIG etape3 = mondeIG.getEtape("ETAPE-2").iterator().next();

        mondeIG.ajouter(etape1, etape2);
        // mondeIG.ajouter(etape2, etape3);

        assertThrows(MondeException.class, () -> {
            SimulationIG simulation = new SimulationIG(mondeIG);
            simulation.verifierMondeIG();
        });
    }

    @Test
    void testGuichetPeutPasEtreSortie() throws TwiskException {
        MondeIG mondeIG = new MondeIG();

        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Guichet");

        mondeIG.getEtape("ETAPE-0").setEstUneEntree(true);
        mondeIG.getEtape("ETAPE-2").setEstUneSortie(true);

        PointDeControleIG etape1 = mondeIG.getEtape("ETAPE-0").iterator().next();
        PointDeControleIG etape2 = mondeIG.getEtape("ETAPE-1").iterator().next();
        PointDeControleIG etape3 = mondeIG.getEtape("ETAPE-2").iterator().next();

        mondeIG.ajouter(etape1, etape2);
        mondeIG.ajouter(etape2, etape3);

        assertThrows(MondeException.class, () -> {
            SimulationIG simulation = new SimulationIG(mondeIG);
            simulation.verifierMondeIG();
        });
    }

    @Test
    void testSortieAvecSuccesseurs() throws TwiskException {
        MondeIG mondeIG = new MondeIG();

        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");

        mondeIG.getEtape("ETAPE-0").setEstUneEntree(true);
        mondeIG.getEtape("ETAPE-0").setEstUneSortie(true);

        PointDeControleIG etape1 = mondeIG.getEtape("ETAPE-0").iterator().next();
        PointDeControleIG etape2 = mondeIG.getEtape("ETAPE-1").iterator().next();

        mondeIG.ajouter(etape1, etape2);

        assertThrows(MondeException.class, () -> {
            SimulationIG simulation = new SimulationIG(mondeIG);
            simulation.verifierMondeIG();
        });
    }

    @Test
    void testGuichetPossedeUnSeulSuccesseur() throws TwiskException {
        MondeIG mondeIG = new MondeIG();

        mondeIG.ajouter("Guichet");
        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");

        mondeIG.getEtape("ETAPE-0").setEstUneEntree(true);
        mondeIG.getEtape("ETAPE-2").setEstUneSortie(true);


        PointDeControleIG etape1 = mondeIG.getEtape("ETAPE-0").iterator().next();
        PointDeControleIG etape2 = mondeIG.getEtape("ETAPE-1").iterator().next();
        PointDeControleIG etape3 = mondeIG.getEtape("ETAPE-2").iterator().next();

        mondeIG.ajouter(etape1, etape2);
        mondeIG.ajouter(etape1, etape3);
        mondeIG.ajouter(etape2, etape3);

        assertThrows(MondeException.class, () -> {
            SimulationIG simulation = new SimulationIG(mondeIG);
            simulation.verifierMondeIG();
        });
    }

    @Test
    void testGuichetPossedeUneSeulActiviteRestreinte() throws TwiskException {
        MondeIG mondeIG = new MondeIG();

        mondeIG.ajouter("Guichet");
        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");

        mondeIG.getEtape("ETAPE-0").setEstUneEntree(true);
        mondeIG.getEtape("ETAPE-2").setEstUneSortie(true);


        PointDeControleIG etape1 = mondeIG.getEtape("ETAPE-0").iterator().next();
        PointDeControleIG etape2 = mondeIG.getEtape("ETAPE-1").iterator().next();
        PointDeControleIG etape3 = mondeIG.getEtape("ETAPE-2").iterator().next();

        mondeIG.ajouter(etape1, etape2);
        mondeIG.ajouter(etape2, etape3);

        assertDoesNotThrow(() -> {
            SimulationIG simulation = new SimulationIG(mondeIG);
            simulation.verifierMondeIG();
        });
    }

    @Test
    void testActiviteRestreintePossedeUnGuichetCommePredecesseur() throws TwiskException {
        MondeIG mondeIG = new MondeIG();

        mondeIG.ajouter("Guichet");
        mondeIG.ajouter("Guichet");
        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");

        mondeIG.getEtape("ETAPE-0").setEstUneEntree(true);
        mondeIG.getEtape("ETAPE-2").setEstUneSortie(true);


        PointDeControleIG etape1 = mondeIG.getEtape("ETAPE-0").iterator().next();
        PointDeControleIG etape2 = mondeIG.getEtape("ETAPE-1").iterator().next();
        PointDeControleIG etape3 = mondeIG.getEtape("ETAPE-2").iterator().next();
        PointDeControleIG etape4 = mondeIG.getEtape("ETAPE-3").iterator().next();

        mondeIG.ajouter(etape1, etape3);
        mondeIG.ajouter(etape2, etape3);
        mondeIG.ajouter(etape3, etape4);

        assertThrows(MondeException.class, () -> {
            SimulationIG simulation = new SimulationIG(mondeIG);
            simulation.verifierMondeIG();
        });
    }

    @Test
    void testActiviteRestreinteNonEntree() throws TwiskException {
        MondeIG mondeIG = new MondeIG();

        mondeIG.ajouter("Guichet");
        mondeIG.ajouter("Activite");
        mondeIG.ajouter("Activite");

        mondeIG.getEtape("ETAPE-0").setEstUneEntree(true);
        mondeIG.getEtape("ETAPE-1").setEstUneEntree(true);
        mondeIG.getEtape("ETAPE-2").setEstUneSortie(true);


        PointDeControleIG etape1 = mondeIG.getEtape("ETAPE-0").iterator().next();
        PointDeControleIG etape2 = mondeIG.getEtape("ETAPE-1").iterator().next();
        PointDeControleIG etape3 = mondeIG.getEtape("ETAPE-2").iterator().next();

        mondeIG.ajouter(etape1, etape2);
        mondeIG.ajouter(etape2, etape3);

        assertThrows(MondeException.class, () -> {
            SimulationIG simulation = new SimulationIG(mondeIG);
            simulation.verifierMondeIG();
        });
    }
}