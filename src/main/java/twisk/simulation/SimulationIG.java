package twisk.simulation;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import twisk.exceptions.MondeException;
import twisk.monde.*;
import twisk.mondeIG.*;
import twisk.outils.*;
import twisk.vues.Observateur;
import twisk.vues.ecouteurs.EcouteurBoutonSimulation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;


public class SimulationIG implements Observateur {

    private MondeIG mondeIG;
    private Monde monde;
    private CorrespondancesEtapes correspondancesEtapes;
    Object simulation;
    Timeline notifierTimeline;
    private boolean estActive;
    private EcouteurBoutonSimulation boutonSimulation;


    public SimulationIG(MondeIG mondeIG) {
        assert (mondeIG != null)    : "Le monde ne doit pas etre null";

        this.mondeIG               = mondeIG;
        this.monde                 = null;
        this.correspondancesEtapes = null;
        this.simulation            = null;
        this.estActive             = false;
    }

    public SimulationIG(MondeIG mondeIG, EcouteurBoutonSimulation boutonSimulation) {
        this(mondeIG);
        assert (boutonSimulation != null) : "Le bouton de simulation ne doit pas etre null";

        this.boutonSimulation = boutonSimulation;
    }


    // —————————— METHODES PUBLIQUES ——————————

    public void simuler() throws MondeException {
        // Verification du monde
        this.verifierMondeIG();

        // Creation du monde
        this.monde = this.creerMonde();

        // Simulation du monde
        this.startSimulation();
    }

    private void startSimulation() {

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws MondeException {
                try {
                    ClassLoaderPerso loader   = new ClassLoaderPerso(SimulationIG.this.getClass().getClassLoader());
                    Class<?> simulationClass  = loader.loadClass("twisk.simulation.Simulation");
                    simulation                = simulationClass.getDeclaredConstructor().newInstance();

                    Method ajouterObservateur = simulation.getClass().getMethod("ajouterObservateur", Observateur.class);
                    Method setNbClients       = simulation.getClass().getMethod("setNbClients", int.class);
                    Method simuler            = simulation.getClass().getMethod("simuler", Monde.class);

                    ajouterObservateur.invoke(simulation, SimulationIG.this);
                    setNbClients.invoke(simulation, mondeIG.getNbClients());
                    simuler.invoke(simulation, monde);

                    loader          = null;
                    simulationClass = null;
                    simulation      = null;
                    setNbClients    = null;
                    simuler         = null;
                    System.gc();
                    SimulationIG.this.stopSimulation();
                } catch (Exception e) {
                    throw new MondeException("Erreur lors de la simulation du monde");
                }

                return null;
            }
        };

        ThreadsManager.getInstance().lancer(task);

        // Ici c'est la seule solution que j'ai trouve à mon bug, appeler `this.mondeIG.notifierObservateurs()` chaque seconde ici et non dans la classe Simulation
        notifierTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> Platform.runLater(() -> {
            this.mondeIG.notifierObservateurs();
            this.reagir();
        })));
        notifierTimeline.setCycleCount(Animation.INDEFINITE);
        notifierTimeline.play();
    }

    public void stopSimulation() throws MondeException {
        try {
            if (this.simulation != null) {
                Method stopSimulation = this.simulation.getClass().getMethod("stopSimulation");
                stopSimulation.invoke(this.simulation);
            }

            // On supprime les clients d'avant (ca evite de voir des clients lorsqu'on relance une simulation, psk il etait sauvegarde toujours en memoire)
            for (EtapeIG etapeIG : this.mondeIG) {
                etapeIG.supprimerClients();
            }

            this.estActive = false;
            this.setEstActive(false);
            this.notifierTimeline.stop(); // Au lieu de `this.mondeIG.notifierObservateurs();`
            Platform.runLater(() -> {
                this.boutonSimulation.updateBouton();
                this.mondeIG.notifierObservateurs();
                this.reagir();
            });
        } catch (Exception e) {
            throw new MondeException("Erreur lors de l'arret de la simulation");
        }
    }

    public boolean estActive() {
        return estActive;
    }

    public void setEstActive(boolean estActive) {
        this.estActive = estActive;
    }

    // —————————— METHODES PRIVES ——————————

    void verifierMondeIG() throws MondeException {
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
        for (EtapeIG depart : this.mondeIG) {
            for (EtapeIG successeur : depart.getSuccesseurs()) {
                // Si un successeur peut atteindre le départ, il y a un cycle
                if (this.estAccessibleDepuis(depart, successeur)) {
                    throw new MondeException("Cycle détecté : l'étape '" + depart.getNom() + "' est accessible depuis son successeur '" + successeur.getNom() + "'");
                }
            }
        }
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

    /**
     * Vérifie s'il existe un chemin depuis 'arrivee' vers 'depart'.
     */
    public boolean estAccessibleDepuis(EtapeIG depart, EtapeIG arrivee) {
        Set<String> visites = new HashSet<>();
        return this.existeChemin(arrivee, depart, visites);
    }

    /**
     * Méthode récursive qui cherche un chemin de 'depart' vers 'cible'.
     *
     * @param depart    L'étape de depart
     * @param cible     L'étape que l'on cherche à atteindre
     * @param visites   Ensemble des identifiants déjà parcourus
     * @return true si l'on trouve 'cible' dans le sous-graphe de 'depart'
     */
    private boolean existeChemin(EtapeIG depart, EtapeIG cible, Set<String> visites) {
        // 1. Si on est arrivé sur la cible, on a trouvé un chemin
        if (depart.equals(cible)) {
            return true;
        }

        // 2. Si on a déjà exploré ce noeud, on s'arrête
        String idCourant = depart.getIdentifiant();
        if (visites.contains(idCourant)) {
            return false;
        }

        // 3. On marque 'depart' comme visité
        visites.add(idCourant);

        // 4. On explore récursivement tous les successeurs
        for (EtapeIG successeur : depart.getSuccesseurs()) {
            if (existeChemin(successeur, cible, visites)) {
                return true; // Dès qu'on trouve un chemin, on remonte en retour
            }
        }

        // 5. Aucun successeur n'a mené à la cible
        return false;
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

        } catch (Exception ignored) {}
    }
}
