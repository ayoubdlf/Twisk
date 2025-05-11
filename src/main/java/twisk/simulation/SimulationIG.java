package twisk.simulation;

import javafx.concurrent.Task;
import twisk.exceptions.MondeException;
import twisk.monde.*;
import twisk.mondeIG.*;
import twisk.outils.*;
import twisk.vues.Observateur;
import java.lang.reflect.Method;


public class SimulationIG implements Observateur {

    private MondeIG mondeIG;
    private Monde monde;
    private CorrespondancesEtapes correspondancesEtapes;
    Object simulation;


    public SimulationIG(MondeIG mondeIG) {
        assert (mondeIG != null)    : "Le monde ne doit pas etre null";

        this.mondeIG               = mondeIG;
        this.monde                 = null;
        this.correspondancesEtapes = null;
        this.simulation            = null;
    }

    public void simuler() throws MondeException {
        // Verification du monde
        this.verifierMondeIG();

        // Creation du monde
        this.monde = this.creerMonde();

        // Simulation du monde

        SimulationIG classeActuelle = this; // on peut pas faire utiliser le `this` dans `ajouterObservateur.invoke(instance, this)`, du coup on le definis avant le Task

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws MondeException {
                try {
                    ClassLoaderPerso loader   = new ClassLoaderPerso(SimulationIG.class.getClassLoader());
                    Class<?> simulationClass  = loader.loadClass("twisk.simulation.Simulation");
                    simulation                = simulationClass.getDeclaredConstructor().newInstance();

                    Method ajouterObservateur = simulation.getClass().getMethod("ajouterObservateur", Observateur.class);
                    Method setNbClients       = simulation.getClass().getMethod("setNbClients", int.class);
                    Method simuler            = simulation.getClass().getMethod("simuler", Monde.class);

                    ajouterObservateur.invoke(simulation, classeActuelle);
                    setNbClients.invoke(simulation, mondeIG.getNbClients());
                    simuler.invoke(simulation, monde);

                    loader          = null;
                    simulationClass = null;
                    simulation      = null;
                    setNbClients    = null;
                    simuler         = null;

                    System.gc();
                } catch (Exception e) {
                    throw new MondeException("Erreur lors de la simulation du monde");
                }

                return null;
            }
        };

        ThreadsManager.getInstance().lancer(task);
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
                throw new MondeException("Chaque etape du monde doit etre accessible par une entree");
            }

            // 4. Toute activite mene à une sortie
            if(!etape.estUneSortie() && !this.possedeSortie(etape)) {
                throw new MondeException("Chaque etape du monde doit mener à une sortie");
            }

            // 5. Une sortie ne peut pas avoir de successeurs
            if(etape.estUneSortie() && etape.getSuccesseurs().size() > 1) {
                throw new MondeException("Une sortie ne peut pas avoir de successeurs");
            }

            // 6. Une activite restreinte ne peut posseder qu'un seul et unique guichet comme predecesseur
            if(etape.estUneActivite() && etape.getPredecesseurs().size() > 1 && etape.getPredecesseurs().getFirst().estUnGuichet()) {
                throw new MondeException("Une activite restreinte connait un seul et unique guichet");
            }

            if(etape.estUnGuichet()) {
                // 7. Un guichet ne peut pas etre une sortie
                if(etape.estUneSortie()) {
                    throw new MondeException("Un guichet ne peut pas etre une sortie");
                }

                // 8. Apres un guichet une seule etape
                if(etape.getSuccesseurs().size() > 1) {
                    throw new MondeException("Un guichet ne peut posseder qu'une seule activite restreinte comme successeur");
                }

                // 9. Apres un guichet une unique activite restreinte
                if(!etape.getSuccesseurs().getFirst().estUneActivite()) {
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
        correspondancesEtapes = new CorrespondancesEtapes();
        Monde monde = new Monde();

        // Creation des etapes
        for (EtapeIG etapeIG : this.mondeIG) {
            correspondancesEtapes.ajouter(etapeIG, this.transformerEtapeIGEnEtape(etapeIG));
        }

        // Definition des successeurs
        for (EtapeIG etapeIG : this.mondeIG) {
            for (EtapeIG successeurIG : etapeIG.getSuccesseurs()) {
                correspondancesEtapes.get(etapeIG).ajouterSuccesseur(correspondancesEtapes.get(successeurIG));
            }
        }

        // Ajout des etapes dans le monde
        for (EtapeIG etapeIG : this.mondeIG) {
            monde.ajouter(correspondancesEtapes.get(etapeIG));
        }

        // Ajout des entrees/sorties dans le monde
        for (EtapeIG etapeIG : this.mondeIG) {
            if(etapeIG.estUneEntree()) { monde.aCommeEntree(correspondancesEtapes.get(etapeIG)); }
            if(etapeIG.estUneSortie()) { monde.aCommeSortie(correspondancesEtapes.get(etapeIG)); }
        }

        return monde;
    }

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

    private Etape transformerEtapeIGEnEtape(EtapeIG etapeIG) {

        if (etapeIG.estUneEntree() || etapeIG.estUneSortie() || etapeIG.estUneActivite()) {
            ActiviteIG activiteIG = (ActiviteIG) etapeIG;

            // Activite Restreinte
            if(!etapeIG.getPredecesseurs().isEmpty() && etapeIG.getPredecesseurs().getFirst().estUnGuichet()) {
                return new ActiviteRestreinte(activiteIG.getNom(), activiteIG.getTemps(), activiteIG.getEcartTemps());
            }

            // Entree ou Sortie ou Activite
            return new Activite(activiteIG.getNom(), activiteIG.getTemps(), activiteIG.getEcartTemps());
        }

        if (etapeIG.estUnGuichet()) {
            GuichetIG guichetIG = (GuichetIG) etapeIG;
            return new Guichet(guichetIG.getNom(), guichetIG.getJetons());
        }

        return null;
    }

    @Override
    public void reagir() {
        try {
            Method classe = this.simulation.getClass().getMethod("getGestionnaireClients");
            GestionnaireClients getGestionnaireClients = (GestionnaireClients) classe.invoke(this.simulation);

            // On supprime les clients d'avant
            for (EtapeIG etapeIG : this.mondeIG) {
                etapeIG.supprimerClients();
            }

            for (Client client : getGestionnaireClients) {
                Etape etape = client.getEtape();
                if (etape != null) {
                    EtapeIG etapeIG = this.correspondancesEtapes.get(etape);
                    if (etapeIG != null) {
                        etapeIG.ajouterClient(client);
                    }
                }
            }

            this.mondeIG.notifierObservateurs(); // TODO: est-ce correct the ne pas avoir de this.mondeIG.ajouterObservateur(this) et de mettre ca ici? (OUI pour moi)
        } catch (Exception ignored) {}
    }
}
