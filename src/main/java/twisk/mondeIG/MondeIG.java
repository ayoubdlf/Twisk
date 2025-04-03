package twisk.mondeIG;

import twisk.exceptions.TwiskException;
import java.util.*;
import static twisk.outils.TailleComposants.*;


public class MondeIG extends SujetObserve implements Iterable<EtapeIG> {

    private HashMap<String, EtapeIG> etapes;
    private ArrayList<ArcIG> arcs;
    private PointDeControleIG[] arcTemporaire; // arc entre un point de controle et la souris
    private boolean animationArcs;


    /**
     * Constructeur de la classe MondeIG.
     */
    public MondeIG() {
        this.etapes        = new HashMap<>();
        this.arcs          = new ArrayList<>(5);
        this.arcTemporaire = new PointDeControleIG[2];
        this.animationArcs = false;
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
    public void ajouter(PointDeControleIG p1, PointDeControleIG p2) throws TwiskException {
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

        this.notifierObservateurs();
    }

    /**
     * Ajoute un point de controle temporaire.
     * 
     * @param point Le point de controle temporaire à ajouter.
     */
    public void ajouter(PointDeControleIG point) throws TwiskException {
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
                // etape = new GuichetIG("", ETAPE_LARGEUR, ETAPE_HAUTEUR);
                // etape.setNom(String.format("Guichet %s", etape.getIdentifiant().split("-")[1]));
                return etape;
        }

        return null;
    }

    private void checkConditionsAjouter(PointDeControleIG p1, PointDeControleIG p2) throws TwiskException {

        // Point de controle deja utilise
        if(p1.estUtilise() || p2.estUtilise()) {
            throw new TwiskException("Le point de controle est deja utilise");
        }

        // p1 et p2 font partie de la meme etape
        if(p1.getEtape().equals(p2.getEtape())) {
            throw new TwiskException("L'arc ne peut pas commencer et terminer dans la meme etape");
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
                throw new TwiskException("Le successeur est deja lie avec un arc à cette etape");
            }
        }

        // Le successeur ne peut pas etre une entree
        if(p2.getEtape().estUneEntree()) {
            throw new TwiskException("Le successeur ne peut pas etre une entree");
        }

        // Une sortie ne peut pas avoir de successeurs
        if(p1.getEtape().estUneSortie()) {
            throw new TwiskException("Une sortie ne peut pas avoir de successeurs");
        }

    }

}
