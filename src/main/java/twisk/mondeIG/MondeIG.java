package twisk.mondeIG;

import com.google.gson.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import twisk.exceptions.MondeException;
import twisk.vues.VueMondeException;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import static twisk.outils.TailleComposants.*;


public class MondeIG extends SujetObserve implements Iterable<EtapeIG> {

    private HashMap<String, EtapeIG> etapes;
    private ArrayList<ArcIG> arcs;
    private PointDeControleIG[] arcTemporaire; // arc entre un point de controle et la souris
    private boolean animationArcs;
    private int nbClients;
    private boolean estSimulationEnCours;

    /**
     * Constructeur de la classe MondeIG.
     */
    public MondeIG() {
        this.etapes               = new HashMap<>();
        this.arcs                 = new ArrayList<>(5);
        this.arcTemporaire        = new PointDeControleIG[2];
        this.animationArcs        = false;
        this.nbClients            = 5; // Par default il y'aura 5 clients
        this.estSimulationEnCours = false;
    }


    // —————————— GETTERS ——————————

    /**
     * Retourne un itérateur sur les étapes du monde.
     *
     * @return Un itérateur sur les étapes.
     */
    @Override
    public Iterator<EtapeIG> iterator() {
        return this.etapes.values().iterator();
    }

    /**
     * Retourne un itérateur sur les arcs du monde.
     *
     * @return Un itérateur sur les arcs.
     */
    public Iterator<ArcIG> iteratorArcs() {
        return this.arcs.iterator();
    }

    /**
     * Retourne un itérateur sur les étapes sélectionnées.
     *
     * @return Un itérateur sur les étapes sélectionnées.
     */
    public Iterator<EtapeIG> iteratorEtapesSelectionnees() {
        return this.etapes.values().stream()
                .filter(EtapeIG::estSelectionne)
                .iterator();
    }

    /**
     * Retourne le nombre d'étapes sélectionnées.
     *
     * @return Le nombre d'étapes sélectionnées.
     */
    public int getNbEtapesSelectionnees() {
        return (int) this.etapes.values().stream()
                .filter(EtapeIG::estSelectionne)
                .count();
    }

    /**
     * Retourne le nombre d'arcs sélectionnés.
     *
     * @return Le nombre d'arcs sélectionnés.
     */
    public int getNbArcsSelectionnes() {
        return (int) this.arcs.stream()
                .filter(ArcIG::estSelectionne)
                .count();
    }

    /**
     * Retourne le point de controle temporaire.
     *
     * @return Le point de controle temporaire.
     */
    public PointDeControleIG[] getArcTemporaire() {
        return this.arcTemporaire[0] == null ? null : this.arcTemporaire;
    }

    /**
     * Retourne si les arcs sont en train d'être animés.
     *
     * @return Vrai si les arcs sont en train d'être animés, faux sinon.
     */
    public boolean estAnimationArcs() {
        return this.animationArcs;
    }

    public EtapeIG getEtape(String identifiant) {
        return this.etapes.get(identifiant);
    }

    /**
     * Retourne l'étape sélectionnée.
     *
     * @return L'étape sélectionnée ou null si aucune étape n'est sélectionnée.
     */
    public EtapeIG getEtapeSelectionnee() {
        return this.etapes.values().stream()
                .filter(EtapeIG::estSelectionne)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retourne le nombre de clients dans le monde.
     *
     * @return le nombre de clients
     */
    public int getNbClients() {
        return this.nbClients;
    }

    /**
     * Indique si une simulation est actuellement en cours dans le monde.
     *
     * @return true si une simulation est en cours, false sinon.
     */
    public boolean estSimulationEnCours() {
        return this.estSimulationEnCours;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du monde.
     *
     * @return Une chaîne de caractères représentant le monde.
     */
    @Override
    public String toString() {
        return String.format("[%s]", this.etapes.keySet());
    }


    // —————————— METHODES PUBLIQUES ——————————

    /**
     * Ajoute une étape de type spécifié au monde.
     *
     * @param type Le type de l'étape à ajouter (Activite ou Guichet).
     */
    public void ajouter(String type) {
        assert(type != null)                                      : "Le type de l'etape ne doit pas etre nul";
        assert(type.equals("Activite") || type.equals("Guichet")) : "Le type de l'etape doit etre 'Activite' ou 'Guichet'";

        EtapeIG etape = this.creationEtape(type);
        assert etape != null : "Le type de l'etape doit etre 'Activite' ou 'Guichet'";

        this.etapes.put(etape.getIdentifiant(), etape);

        this.notifierObservateurs();
    }

    /**
     * Ajoute un arc entre deux points de contrôle.
     *
     * @param p1 Le premier point de contrôle.
     * @param p2 Le deuxième point de contrôle.
     */
    public void ajouter(PointDeControleIG p1, PointDeControleIG p2) throws MondeException {
        assert(p1 != null && p2 != null) : "Les deux points de controles ne doivent pas etre nuls";
        this.supprimerArcTemporaire();

        this.checkConditionsAjouter(p1, p2);

        boolean estDansAxisX = (p1.estADroite() && p2.estAGauche()) || (p1.estAGauche() && p2.estADroite()); // DROITE-GAUCHE ou GAUCHE-DROITE
        boolean estDansAxisY = (p1.estEnHaut() && p2.estEnBas()) || (p1.estEnBas() && p2.estEnHaut());       // HAUT-BAS ou BAS-HAUT

        if(estDansAxisX || estDansAxisY) {
            this.arcs.add(new LigneDroiteIG(p1, p2));
        } else {
            this.arcs.add(new CourbeIG(p1, p2));
        }

        // Les points de controle sont utilise par un arc
        p1.setEstUtilise(true);
        p2.setEstUtilise(true);

        p1.setEstSortant(true);
        p2.setEstEntrant(true);

        // ajouter les successeurs / predecesseurs
        p1.getEtape().ajouterSuccesseur(p2.getEtape());
        p2.getEtape().ajouterPredecesseur(p1.getEtape());

        this.notifierObservateurs();
    }

    /**
     * Ajoute un point de controle temporaire.
     *
     * @param point Le point de controle temporaire à ajouter.
     */
    public void ajouter(PointDeControleIG point) throws MondeException {
        assert(point != null) : "Le point de controle ne doit pas etre null";

        if(this.arcTemporaire[0] == null) {
            this.arcTemporaire[0] = point;
            this.arcTemporaire[0].setEstUtiliseParArcTemporaire(true);
            this.notifierObservateurs();
        } else {
            this.arcTemporaire[1] = point;
            this.ajouter(this.arcTemporaire[0], this.arcTemporaire[1]);
            // le this.notifierObservateurs(), va etre appelle dans la fonction this.ajouter, donc pas besoin de le mettre ici
        }
    }

    /**
     * Désélectionne tous les éléments sélectionnés.
     */
    public void deselectionnerSelection() {
        this.arcs.forEach(arc -> arc.setEstSelectionne(false));
        this.etapes.values().forEach(etape -> etape.setEstSelectionne(false));
    }

    /**
     * Renomme les étapes sélectionnées avec le nom spécifié.
     *
     * @param nom Le nouveau nom à attribuer aux étapes sélectionnées.
     */
    public void renommerSelection(String nom) {
        this.etapes.values().stream()
                .filter(EtapeIG::estSelectionne)
                .forEach(etape -> etape.setNom(nom));

        this.deselectionnerSelection();
        this.notifierObservateurs();
    }

    /**
     * Supprime les éléments sélectionnés.
     */
    public void supprimerSelection() {
        this.supprimerArcsSelectionnes();
        this.supprimerArcsLieesAuxEtapesSelectionnes();
        this.supprimerEtapesSelectionnes();

        this.deselectionnerSelection();
        this.notifierObservateurs();
    }

    /**
     * Supprime les étapes sélectionnées.
     */
    public void supprimerEtapesSelectionnes() {
        List<EtapeIG> etapesASupprimer = this.etapes.values().stream()
                .filter(EtapeIG::estSelectionne)
                .toList();

        for (EtapeIG etape : etapesASupprimer) {
            this.supprimerEtape(etape);
        }
    }

    /**
     * Supprime les arcs sélectionnés.
     */
    public void supprimerArcsSelectionnes() {
        // On libere les points de controle
        this.arcs.stream()
                .filter(ArcIG::estSelectionne)
                .forEach(arc -> {
                    arc.getP1().setEstUtilise(false);
                    arc.getP2().setEstUtilise(false);

                    // on suprime les successeurs/predecesseurs lorsqu'on supprime un arc
                    arc.getP1().getEtape().supprimerSuccesseur(arc.getP2().getEtape());
                    arc.getP2().getEtape().supprimerPredecesseur(arc.getP1().getEtape());
                });

        this.arcs.removeIf(ArcIG::estSelectionne);
    }

    /**
     * Supprime les arcs liés aux étapes sélectionnées.
     */
    public void supprimerArcsLieesAuxEtapesSelectionnes() {
        // On libere les points de controle
        this.arcs.stream()
                .filter(arc -> arc.getP1().getEtape().estSelectionne() || arc.getP2().getEtape().estSelectionne())
                .forEach(arc -> {
                    arc.getP1().setEstUtilise(false);
                    arc.getP2().setEstUtilise(false);

                    // on suprime les successeurs/predecesseurs lorsqu'on supprime un arc
                    arc.getP1().getEtape().supprimerSuccesseur(arc.getP2().getEtape());
                    arc.getP2().getEtape().supprimerPredecesseur(arc.getP1().getEtape());
                });

        this.arcs.removeIf(arc -> arc.getP1().getEtape().estSelectionne() || arc.getP2().getEtape().estSelectionne());
    }

    /**
     * Supprime une étape du monde.
     *
     * @param etape L'étape à supprimer.
     */
    public void supprimerEtape(EtapeIG etape) {
        assert(etape != null) : "Le etape ne doit pas etre nul";

        // On supprime les arcs lie à cette etape
        this.arcs.forEach(arc -> {
            if(arc.getP1().getEtape().equals(etape) || arc.getP2().getEtape().equals(etape)) {
                arc.getP1().setEstUtilise(false);
                arc.getP2().setEstUtilise(false);

                // on suprime les successeurs/predecesseurs lorsqu'on supprime un arc
                arc.getP1().getEtape().supprimerSuccesseur(arc.getP2().getEtape());
                arc.getP2().getEtape().supprimerPredecesseur(arc.getP1().getEtape());
            }
        });

        // On supprime les arcs lie à cette etape
        this.arcs.removeIf(arc -> arc.getP1().getEtape().equals(etape) || arc.getP2().getEtape().equals(etape));

        // On supprime l'etape
        this.etapes.remove(etape.getIdentifiant());

        this.notifierObservateurs();
    }

    /**
     * Efface les éléments sélectionnés.
     */
    public void effacerSelection() {
        this.deselectionnerSelection();
        this.notifierObservateurs();
    }

    /**
     * Supprime l'arc temporaire.
     */
    public void supprimerArcTemporaire() {
        if(this.arcTemporaire[0] != null) { this.arcTemporaire[0].setEstUtiliseParArcTemporaire(false); }
        if(this.arcTemporaire[1] != null) { this.arcTemporaire[1].setEstUtiliseParArcTemporaire(false); }

        Arrays.fill(this.arcTemporaire, null);
    }

    /**
     * Ouvre un fichier JSON et charge les données dans le monde.
     * Affiche une alerte en cas d'erreur de lecture.
     */
    public void chargerDepuisJson() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Charger un fichier JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json"));

        File fichierJson = fileChooser.showOpenDialog(new Stage());
        if (fichierJson != null) {
            try (Scanner scanner = new Scanner(fichierJson)) {
                StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    stringBuilder.append(scanner.nextLine());
                }

                String jsonString = stringBuilder.toString();
                JsonObject JSON   = JsonParser.parseString(jsonString).getAsJsonObject();

                this.chargerDepuisJson(JSON);
            } catch (Exception e) {
                VueMondeException.alert("Erreur lors du chargement", e.getMessage());
            }
        }
    }

    /**
     * Charge les étapes et les arcs depuis un objet JSON.
     * Vide le contenu actuel avant de charger les nouvelles données.
     *
     * @param JSON l'objet JSON contenant les données à charger
     */
    public void chargerDepuisJson(JsonObject JSON) {
        try {
            this.etapes.clear();
            this.arcs.clear();

            // On set le nombre de clients
            this.nbClients = JSON.get("nbClients").getAsInt();

            // On ajoute els etapes
            JsonArray etapesJson = JSON.getAsJsonArray("etapes");
            for (JsonElement elem : etapesJson) {
                JsonObject etapeJson = elem.getAsJsonObject();
                this.ajouterDepuisJson(etapeJson);
            }

            // On ajoute les arcs
            JsonArray arcsJson = JSON.getAsJsonArray("arcs");
            for (JsonElement elem : arcsJson) {
                JsonObject arcJson = elem.getAsJsonObject();
                this.ajouterArcDepuisJson(arcJson);
            }

            this.notifierObservateurs();

        } catch (Exception e) {
            VueMondeException.alert("Erreur lors du chargement", e.getMessage());
        }
    }

    /**
     * Sauvegarde l'état du monde en JSON.
     */
    public void sauvegarderEnJson() {
        JsonObject JSON = new JsonObject();

        JSON.addProperty("nbClients", this.nbClients);

        JsonArray etapesJson = new JsonArray();
        JsonArray arcsJson   = new JsonArray();

        for (EtapeIG etape : this)  { etapesJson.add(etape.toJson()); }
        for (ArcIG arc : this.arcs) { arcsJson.add(arc.toJson()); }

        JSON.add("etapes", etapesJson);
        JSON.add("arcs",   arcsJson);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder sous..");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json"));

        File fichierJson = fileChooser.showSaveDialog(new Stage());
        if (fichierJson != null) {
            try (FileWriter writer = new FileWriter(fichierJson)) {
                writer.write(gson.toJson(JSON)); // le json
            } catch (Exception e) {
                VueMondeException.alert("Erreur lors de la sauvegarde", e.getMessage());
            }
        }
    }

    // —————————— SETTERS ——————————

    /**
     * Définit les étapes sélectionnées comme étant des entrées.
     */
    public void setSelectionCommeEntree() {
        this.etapes.values().stream()
                .filter(EtapeIG::estSelectionne)
                .forEach(etape -> etape.setEstUneEntree(!etape.estUneEntree()));

        this.deselectionnerSelection();
        this.notifierObservateurs();
    }

    /**
     * Définit les étapes sélectionnées comme étant des sorties.
     */
    public void setSelectionCommeSortie() {
        this.etapes.values().stream()
                .filter(EtapeIG::estSelectionne)
                .forEach(etape -> etape.setEstUneSortie(!etape.estUneSortie()));

        this.deselectionnerSelection();
        this.notifierObservateurs();
    }

    /**
     * Définit si les arcs sont en train d'être animés.
     *
     * @param animationArcs Vrai si les arcs sont en train d'être animés, faux sinon.
     */
    public void setAnimationArcs(boolean animationArcs) {
        this.animationArcs = animationArcs;
    }

    /**
     * Définit le nombre de clients dans le monde.
     *
     * @param nbClients le nombre de clients à définir
     */
    public void setNbClients(int nbClients) {
        this.nbClients = nbClients;
    }

    /**
     * Définit si une simulation est actuellement en cours dans le monde.
     *
     * @param estSimulationEnCours true pour indiquer qu'une simulation est en cours, false sinon.
     */
    public void setEstSimulationEnCours(boolean estSimulationEnCours) {
        this.estSimulationEnCours = estSimulationEnCours;
    }

    // —————————— METHODES DEDIE AU CHARGEMENT JSON ——————————
    /**
     * Ajoute une étape à partir d'un objet JSON.
     *
     * @param etapeJson L'objet JSON représentant l'étape.
     */
    public void ajouterDepuisJson(JsonObject etapeJson) {
        assert(etapeJson != null)  : "L'etape ne doit pas etre null";

        String type, nom, identifiant;
        int x, y;
        int temps = 0, ecartTemps = 0, jetons = 0;

        try {
            type           = etapeJson.get("type").getAsString();
            nom            = etapeJson.get("nom").getAsString();
            identifiant    = etapeJson.get("identifiant").getAsString();
            x              = etapeJson.get("x").getAsInt();
            y              = etapeJson.get("y").getAsInt();
        } catch (Exception e) {
            VueMondeException.alert("Erreur de chargement JSON", "Impossible de lire les propriétés de l'étape : " + e.getMessage());
            return;
        }

        boolean estUnGuichet   = type.equals("guichet");
        boolean estUneActivite = !estUnGuichet;

        if(estUneActivite) {
            try {
                temps      = etapeJson.get("temps").getAsInt();
                ecartTemps = etapeJson.get("ecartTemps").getAsInt();
            } catch (Exception e) {
                VueMondeException.alert("Erreur de chargement JSON", "Impossible de lire les propriétés 'temps' et 'ecart temps' de l'activite : " + e.getMessage());
                return;
            }
        }

        if(estUnGuichet) {
            try {
                jetons = etapeJson.get("jetons").getAsInt();
            } catch (Exception e) {
                VueMondeException.alert("Erreur de chargement JSON", "Impossible de lire la propriétés 'jetons' du guichet : " + e.getMessage());
                return;
            }
        }

        EtapeIG etape;

        if(estUnGuichet) {
            etape = this.creationGuichetDepuisJson(type, nom, identifiant, x, y, jetons);
        } else {
            etape = this.creationActiviteDepuisJson(type, nom, identifiant, x, y, temps, ecartTemps);
        }

        this.etapes.put(etape.getIdentifiant(), etape);

        this.notifierObservateurs();
    }

    /**
     * Ajoute un arc à partir d'un objet JSON.
     *
     * @param arcsJson L'objet JSON représentant l'arc.
     */
    private void ajouterArcDepuisJson(JsonObject arcsJson) {
        assert(arcsJson != null)  : "Les arcs ne doivent pas etre null";

        EtapeIG etapeSource, etapeDestination;
        int indexPdcEtapeSource, indexPdcEtapeDestination;

        try {
            JsonObject source        = arcsJson.get("source").getAsJsonObject();
            JsonObject destination   = arcsJson.get("destination").getAsJsonObject();

            etapeSource              = this.getEtapeByIdentifiant(source.get("identifiant").getAsString());
            etapeDestination         = this.getEtapeByIdentifiant(destination.get("identifiant").getAsString());

            indexPdcEtapeSource      = source.get("index").getAsInt();
            indexPdcEtapeDestination = destination.get("index").getAsInt();
        } catch (Exception e) {
            VueMondeException.alert("Erreur de chargement JSON", "Impossible de lire les propriétés de l'arc : " + e.getMessage());
            return;
        }

        if(etapeSource == null || etapeDestination == null) {
            VueMondeException.alert("Erreur de chargement JSON", "L'étape source ou destination spécifiée pour l'arc est introuvable.");
            return;
        }

        try {
            this.ajouter(etapeSource.getPointDeControle(indexPdcEtapeSource), etapeDestination.getPointDeControle(indexPdcEtapeDestination));
        } catch (MondeException e) {
            VueMondeException.alert("Erreur lors de l'ajout d'un arc", "Impossible d'ajouter l'arc : " + e.getMessage());
        }
    }

    /**
     * Crée une activité à partir des données JSON.
     *
     * @param type Le type de l'étape, "entree", "sortie", "activite", "guichet".
     * @param nom Le nom de l'étape.
     * @param identifiant L'identifiant de l'étape.
     * @param x La position horizontale.
     * @param y La position verticale.
     * @param temps Le temps d'exécution de l'activité.
     * @param ecartTemps L'écart de temps de l'activité.
     * @return L'objet ActiviteIG créé.
     */
    private EtapeIG creationActiviteDepuisJson(String type, String nom, String identifiant, int x, int y, int temps, int ecartTemps) {
        ActiviteIG activite = new ActiviteIG(nom, ETAPE_LARGEUR, ETAPE_HAUTEUR);
        activite.setIdentifiant(identifiant);
        activite.setTemps(temps);
        activite.setEcartTemps(ecartTemps);
        activite.setPosition(x, y);
        activite.setEstUneEntree(type.equals("entree"));
        activite.setEstUneSortie(type.equals("sortie"));

        return activite;
    }


    /**
     * Crée un guichet à partir des données JSON.
     *
     * @param type Le type de l'étape, "entree", "sortie", "activite", "guichet".
     * @param nom Le nom de l'étape.
     * @param identifiant L'identifiant de l'étape.
     * @param x La position horizontale.
     * @param y La position verticale.
     * @param jetons Le nombre de jetons du guichet.
     * @return L'objet GuichetIG créé.
     */
    private EtapeIG creationGuichetDepuisJson(String type, String nom, String identifiant, int x, int y, int jetons) {
        GuichetIG guichet = new GuichetIG(nom, ETAPE_LARGEUR, ETAPE_HAUTEUR);
        guichet.setIdentifiant(identifiant);
        guichet.setPosition(x, y);
        guichet.setEstUneEntree(type.equals("entree"));
        guichet.setJetons(jetons);

        return guichet;
    }

    // —————————— METHODES PRIVES ——————————

    /**
     * Crée une étape en fonction du type spécifié.
     *
     * @param type Le type de l'étape à créer.
     * @return L'étape créée ou null si le type est invalide.
     */
    private EtapeIG creationEtape(String type) {
        EtapeIG etape = null;

        switch (type) {
            case "Activite":
                etape = new ActiviteIG("Activite", ETAPE_LARGEUR, ETAPE_HAUTEUR);
                etape.setNom(String.format("Activite %s", etape.getIdentifiant().split("-")[1]));
                return etape;
            case "Guichet":
                etape = new GuichetIG("Guichet", ETAPE_LARGEUR, ETAPE_HAUTEUR);
                etape.setNom(String.format("Guichet %s", etape.getIdentifiant().split("-")[1]));
                return etape;
        }

        return null;
    }

    /**
     * Vérifie les conditions de validité avant d'ajouter un arc entre deux points de contrôle.
     *
     * @param p1 Le premier point de contrôle.
     * @param p2 Le second point de contrôle.
     * @throws MondeException si les conditions d'ajout ne sont pas respectées.
     */
    private void checkConditionsAjouter(PointDeControleIG p1, PointDeControleIG p2) throws MondeException {
        // p1 et p2 font partie de la meme etape
        if(p1.getEtape().equals(p2.getEtape())) {
            throw new MondeException("L'arc ne peut pas commencer et terminer dans la meme etape");
        }

        // p2 est deja lie à p1 avec un autre arc
        for (Iterator<ArcIG> it = this.iteratorArcs(); it.hasNext(); ) {
            ArcIG arc      = it.next();
            EtapeIG etape1 = arc.getP1().getEtape();
            EtapeIG etape2 = arc.getP2().getEtape();

            if(
                    etape1.equals(p1.getEtape()) && etape2.equals(p2.getEtape()) ||
                            etape1.equals(p2.getEtape()) && etape2.equals(p1.getEtape())
            ) {
                throw new MondeException("Le successeur est deja lie avec un arc à cette etape");
            }
        }

        // p1 et p2 font partie de la meme etape
        if(p1.getEtape().equals(p2.getEtape())) {
            throw new MondeException("L'arc ne peut pas commencer et terminer dans la meme etape");
        }

        // Le successeur ne peut pas etre une entree
        if(p2.getEtape().estUneEntree()) {

            // Une sortie ne peut pas avoir de successeurs
            if(p1.getEtape().estUneSortie()) {
                throw new MondeException("Une sortie ne peut pas avoir de successeurs");
            }
        }

        // Direction specifique si c'est un guichet
        if(p1.getEtape().estUnGuichet()) {
            PointDeControleIG pdcDroit  = p1.getEtape().getPointDeControle(0);
            PointDeControleIG pdcGauche = p1.getEtape().getPointDeControle(1);

            if(!pdcDroit.estUtilise() && !pdcGauche.estUtilise()) { return; }

            if(p1.equals(pdcDroit)  && !pdcDroit.estEntrant()  && !pdcGauche.estSortant()) { return; }
            if(p1.equals(pdcGauche) && !pdcGauche.estEntrant() && !pdcDroit.estSortant())  { return; }

            throw new MondeException("Cette direction pour le guichet est incorrecte.");
        }

        if(p2.getEtape().estUnGuichet()) {
            PointDeControleIG pdcDroit  = p2.getEtape().getPointDeControle(0);
            PointDeControleIG pdcGauche = p2.getEtape().getPointDeControle(1);

            if(!pdcDroit.estUtilise() && !pdcGauche.estUtilise()) { return; }

            if(p2.equals(pdcDroit)  && !pdcDroit.estSortant()  && !pdcGauche.estEntrant()) { return; }
            if(p2.equals(pdcGauche) && !pdcGauche.estSortant() && !pdcDroit.estEntrant())  { return; }

            throw new MondeException("Cette direction pour le guichet est incorrecte.");
        }
    }

    /**
     * Recherche une étape dans le monde à partir de son identifiant.
     *
     * @param identifiant L'identifiant de l'étape à rechercher.
     * @return L'étape correspondante si elle est trouvée, sinon null.
     */
    private EtapeIG getEtapeByIdentifiant(String identifiant) {
        for (EtapeIG etape : this.etapes.values()) {
            if (etape.getIdentifiant().equals(identifiant)) {
                return etape;
            }
        }

        return null;
    }
}

