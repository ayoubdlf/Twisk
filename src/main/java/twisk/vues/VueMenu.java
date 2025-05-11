package twisk.vues;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import twisk.mondeIG.*;
import java.util.Objects;


public class VueMenu extends MenuBar implements Observateur {

    private MondeIG mondeIG;
    private Menu fichier, edition, monde, parametres, options;


    /**
     * Constructeur de la classe VueMenu.
     * 
     * @param mondeIG Le monde.
     */
    public VueMenu(MondeIG mondeIG) {
        this.mondeIG = mondeIG;
        this.mondeIG.ajouterObservateur(this);

        this.menuFichier();
        this.menuEdition();
        this.menuMonde();
        this.menuParametres();
        this.menuOptions();

        this.getMenus().addAll(this.fichier, this.edition, this.monde, this.parametres, this.options);

        this.reagir();
    }

    // —————————— METHODES PUBLIQUES ——————————

    @Override
    public void reagir() {
        int nbEtapesSelectionnes = this.mondeIG.getNbEtapesSelectionnees();
        int nbArcsSelectionnes   = this.mondeIG.getNbArcsSelectionnes();

        this.edition.getItems().forEach(item -> {
            item.setDisable(nbEtapesSelectionnes == 0 && nbArcsSelectionnes == 0);
            if((nbEtapesSelectionnes != 1 || nbArcsSelectionnes != 0) && item.getId() != null && item.getId().equals("renommer")) {
                item.setDisable(true);
            }
        });

        this.monde.getItems().forEach(item -> {
            if((nbEtapesSelectionnes != 1 || nbArcsSelectionnes != 0) &&  (item.getId() == null || item.getId() != null && !item.getId().equals("nbClients"))) {
                item.setDisable(true);
            }
        });

        this.parametres.getItems().forEach(parametre -> parametre.setDisable(nbEtapesSelectionnes != 1));
    }


    // —————————— METHODES PRIVES ——————————

    /**
     * Initialise le menu Fichier.
     */
    private void menuFichier() {
        this.fichier = new Menu("Fichier");

        MenuItem quitter = new MenuItem("Quitter");
        quitter.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        quitter.setOnAction(event -> Platform.exit());

        this.fichier.getItems().add(quitter);
    }

    /**
     * Initialise le menu Édition.
     */
    private void menuEdition() {
        this.edition = new Menu("Edition");

        MenuItem supprimerSelection = new MenuItem("Supprimer la sélection");
        MenuItem renommerSelection  = new MenuItem("Renommer la sélection");
        MenuItem effacerSelection   = new MenuItem("Effacer la sélection");

        supprimerSelection.setOnAction(event -> this.mondeIG.supprimerSelection());

        renommerSelection.setId("renommer");
        renommerSelection.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        renommerSelection.setOnAction(event -> this.afficherFenetreRenommer());

        effacerSelection.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        effacerSelection.setOnAction(event -> this.mondeIG.effacerSelection());

        this.edition.getItems().addAll(supprimerSelection, renommerSelection, effacerSelection);
    }

    /**
     * Initialise le menu Monde.
     */
    private void menuMonde() {
        this.monde = new Menu("Monde");

        MenuItem entree    = new MenuItem("Entree");
        MenuItem sortie    = new MenuItem("Sortie");
        MenuItem nbClients = new MenuItem("Modifier le nombre de clients");

        entree.setOnAction(event -> this.mondeIG.supprimerSelection());

        entree.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        entree.setOnAction(event -> this.mondeIG.setSelectionCommeEntree());

        sortie.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        sortie.setOnAction(event -> this.mondeIG.setSelectionCommeSortie());

        nbClients.setOnAction(event -> this.afficherFenetreMondeNbClients());
        nbClients.setId("nbClients");

        this.monde.getItems().addAll(entree, sortie, nbClients);
    }

    /**
     * Initialise le menu Paramètres.
     */
    private void menuParametres() {
        this.parametres = new Menu("Parametres");
        parametres.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));

        // activite - temps et ecartTemps
        MenuItem delaiEtEcartTemps = new MenuItem("Modifier les temps");
        delaiEtEcartTemps.setOnAction(event -> this.afficherFenetreParametresTemps());

        // guichet - jetons
        MenuItem jetons = new MenuItem("Modifier le nombre de jetons");
        jetons.setOnAction(event -> this.afficherFenetreParametresJetons());

        this.parametres.getItems().addAll(delaiEtEcartTemps, jetons);
    }

    /**
     * Initialise le menu Option.
     */
    private void menuOptions() {
        this.options = new Menu("Style");

        MenuItem animationArcs = new MenuItem("Animation arcs");

        animationArcs.setOnAction(event -> {
            this.mondeIG.setAnimationArcs(!this.mondeIG.estAnimationArcs());
            this.mondeIG.notifierObservateurs();
        });

        this.options.getItems().addAll(animationArcs);
    }

    private void afficherFenetreRenommer() {
        EtapeIG etape = this.mondeIG.iteratorEtapesSelectionnees().next();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Twisk");
        dialog.setHeaderText("Renommer l'étape");

        // Boutons annuler et valider
        dialog.getDialogPane().getButtonTypes().clear();
        ButtonType buttonAnnulerType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonValiderType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonAnnulerType, buttonValiderType);

        VBox content = new VBox(16);

        // description
        Label description = new Label("Veuillez entrer le nouveau nom de l'étape");
        description.getStyleClass().add("dialog-description");
        content.getChildren().add(description);

        VBox group = new VBox();
        group.setSpacing(6);
        // nom etape
        Label nomEtape = new Label("Nom de l'étape");
        nomEtape.getStyleClass().add("dialog-nom-etape");

        // input
        TextField textField = new TextField();
        textField.setPromptText("Entrez le nouveau nom");
        textField.setText(etape.getNom());
        textField.getStyleClass().add("dialog-input");
        group.getChildren().addAll(nomEtape, textField);

        content.getChildren().add(group);

        // Style the buttons
        Button buttonAnnuler = (Button) dialog.getDialogPane().lookupButton(buttonAnnulerType);
        Button buttonValider = (Button) dialog.getDialogPane().lookupButton(buttonValiderType);

        buttonAnnuler.getStyleClass().add("dialog-button-annuler");
        buttonValider.getStyleClass().add("dialog-button-valider");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog");

        dialog.showAndWait().ifPresent(response -> {
            if (response == buttonValiderType) {
                String nom = textField.getText();

                if (!nom.isEmpty()) {
                    etape.setNom(nom);
                    this.mondeIG.deselectionnerSelection();
                    this.mondeIG.notifierObservateurs();
                }
            }
        });
    }

    private void afficherFenetreParametresTemps() {
        if(this.mondeIG.getNbEtapesSelectionnees() != 1) { return; }
        EtapeIG etape = this.mondeIG.iteratorEtapesSelectionnees().next();
        if(!etape.estUneActivite()) { return; }

        ActiviteIG activite = (ActiviteIG) etape;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Twisk");
        dialog.setHeaderText("Parametres temps");

        // Boutons annuler et valider
        dialog.getDialogPane().getButtonTypes().clear();
        ButtonType buttonAnnulerType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonValiderType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonAnnulerType, buttonValiderType);

        VBox content = new VBox(16);

        // description
        Label description = new Label("Veuillez saisier les temps de l'étape");
        description.getStyleClass().add("dialog-description");
        content.getChildren().add(description);

        VBox group = new VBox();
            group.setSpacing(14);
            // temps
            Slider tempsSlider = new Slider(0, 100, activite.getTemps());
            Label tempsLabel   = new Label("Temps: " + (int) tempsSlider.getValue());

            tempsSlider.setShowTickLabels(true);
            tempsSlider.setShowTickMarks(true);
            tempsLabel.getStyleClass().add("dialog-temps");

            // ecart temps
            Slider ecartTempsSlider = new Slider(0, 100, activite.getEcartTemps());
            Label ecartTempsLabel   = new Label("Ecart temps: " + (int) ecartTempsSlider.getValue());

            ecartTempsSlider.setShowTickLabels(true);
            ecartTempsSlider.setShowTickMarks(true);
            ecartTempsLabel.getStyleClass().add("dialog-ecart-temps");

            tempsSlider.valueProperty().addListener((obs, n, val) -> tempsLabel.setText("Temps: " + val.intValue()));
            ecartTempsSlider.valueProperty().addListener((obs, n, val) -> ecartTempsLabel.setText("Ecart temps: " + val.intValue()));

        group.getChildren().addAll(tempsLabel, tempsSlider, ecartTempsLabel, ecartTempsSlider);
        content.getChildren().add(group);

        // Style the buttons
        Button buttonAnnuler = (Button) dialog.getDialogPane().lookupButton(buttonAnnulerType);
        Button buttonValider = (Button) dialog.getDialogPane().lookupButton(buttonValiderType);

        buttonAnnuler.getStyleClass().add("dialog-button-annuler");
        buttonValider.getStyleClass().add("dialog-button-valider");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog");


        dialog.showAndWait().ifPresent(response -> {
            if (response == buttonValiderType) {
                int ecartTemps = (int) ecartTempsSlider.getValue();
                int temps      = (int) tempsSlider.getValue();

                if(temps <= 0)          { VueTwiskException.alert("Parametre invalide", "Le temps doit etre superieur à 0"); return;  }
                if(ecartTemps <= 0)     { VueTwiskException.alert("Parametre invalide", "L' ecart temps doit etre superieur à 0"); return;  }
                if(temps <= ecartTemps) { VueTwiskException.alert("Parametre invalide", "L' ecart temps doit etre inferieur au temps"); return;  }

                activite.setTemps(temps);
                activite.setEcartTemps(ecartTemps);
            }

            this.mondeIG.deselectionnerSelection();
            this.mondeIG.notifierObservateurs();
        });
    }

    private void afficherFenetreParametresJetons() {
        if(this.mondeIG.getNbEtapesSelectionnees() != 1) { return; }
        EtapeIG etape = this.mondeIG.iteratorEtapesSelectionnees().next();
        if(!etape.estUnGuichet()) { return; }

        GuichetIG guichet = (GuichetIG) etape;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Twisk");
        dialog.setHeaderText("Parametres jetons");


        // Boutons annuler et valider
        dialog.getDialogPane().getButtonTypes().clear();
        ButtonType buttonAnnulerType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonValiderType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonAnnulerType, buttonValiderType);

        VBox content = new VBox(16);

        // description
        Label description = new Label("Veuillez entrer le nouveau nombre de jetons");
        description.getStyleClass().add("dialog-description");
        content.getChildren().add(description);

        VBox group = new VBox();
        group.setSpacing(6);
        // nombre de jetons
        Label jetons = new Label("Nombre de jetons");
        jetons.getStyleClass().add("dialog-jetons-etape");

        // input
        TextField textField = new TextField();
        textField.setPromptText("Entrez le nouveau nombre de jetons");
        textField.setText(Integer.toString(guichet.getJetons()));
        textField.getStyleClass().add("dialog-input");
        group.getChildren().addAll(jetons, textField);

        content.getChildren().add(group);

        // Style the buttons
        Button buttonAnnuler = (Button) dialog.getDialogPane().lookupButton(buttonAnnulerType);
        Button buttonValider = (Button) dialog.getDialogPane().lookupButton(buttonValiderType);

        buttonAnnuler.getStyleClass().add("dialog-button-annuler");
        buttonValider.getStyleClass().add("dialog-button-valider");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog");

        dialog.showAndWait().ifPresent(response -> {
            if (response == buttonValiderType) {
                String nbJetons = textField.getText();

                if (!nbJetons.isEmpty()) {
                    guichet.setJetons(Integer.parseInt(nbJetons));

                    this.mondeIG.deselectionnerSelection();
                    this.mondeIG.notifierObservateurs();
                }
            }
        });
    }

    private void afficherFenetreMondeNbClients() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Twisk");
        dialog.setHeaderText("Parametres Monde");

        // Boutons annuler et valider
        dialog.getDialogPane().getButtonTypes().clear();
        ButtonType buttonAnnulerType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonValiderType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonAnnulerType, buttonValiderType);

        VBox content = new VBox(16);

        // description
        Label description = new Label("Veuillez entrer le nombre de clients");
        description.getStyleClass().add("dialog-description");
        content.getChildren().add(description);

        VBox group = new VBox();
        group.setSpacing(6);
        // nombre de clients
        Label nbClients = new Label("Nombre de clients");
        nbClients.getStyleClass().add("dialog-clients-etape");

        // input
        TextField textField = new TextField();
        textField.setPromptText("Entrez le nombre de clients");
        textField.setText(Integer.toString(this.mondeIG.getNbClients()));
        textField.getStyleClass().add("dialog-input");
        group.getChildren().addAll(nbClients, textField);

        content.getChildren().add(group);

        // Style the buttons
        Button buttonAnnuler = (Button) dialog.getDialogPane().lookupButton(buttonAnnulerType);
        Button buttonValider = (Button) dialog.getDialogPane().lookupButton(buttonValiderType);

        buttonAnnuler.getStyleClass().add("dialog-button-annuler");
        buttonValider.getStyleClass().add("dialog-button-valider");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog");

        dialog.showAndWait().ifPresent(response -> {
            if (response == buttonValiderType) {
                String nbClientsStr = textField.getText();

                if (!nbClientsStr.isEmpty()) {
                    try {
                        int nbClientsInt = Integer.parseInt(nbClientsStr);
                        if(nbClientsInt <= 0 || nbClientsInt >= 50) { VueTwiskException.alert("Parametre invalide", "Le nombre de clients doit etre superieur à 0 et inferieur à 50"); return;  }

                        this.mondeIG.setNbClients(nbClientsInt);
                        this.mondeIG.notifierObservateurs(); // TODO: pas vrainment besoin de notifier les observateurs
                    } catch (Exception e) {
                        VueTwiskException.alert("Parametre invalide", "Le nombre de clients doit etre un entier superieur à 0");
                    }
                }
            }
        });
    }
}
