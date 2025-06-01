package twisk.vues;

import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import twisk.mondeIG.*;
import twisk.opanai.ChatGPT;
import twisk.outils.ThreadsManager;
import java.util.Objects;


public class VueMenu extends MenuBar implements Observateur {

    private MondeIG mondeIG;
    private Menu fichier, edition, monde, parametres, options, optionsChatGPT;


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
        this.menuChatGPT();

        this.getMenus().addAll(this.fichier, this.edition, this.monde, this.parametres, this.options, this.optionsChatGPT);

        this.reagir();
    }

    // —————————— METHODES PUBLIQUES ——————————

    @Override
    public void reagir() {
        VueMenu vueMenu = this;

        Runnable command = () -> {
            int nbEtapesSelectionnes = vueMenu.mondeIG.getNbEtapesSelectionnees();
            int nbArcsSelectionnes   = vueMenu.mondeIG.getNbArcsSelectionnes();

            vueMenu.fichier.getItems().forEach(item -> item.setDisable(vueMenu.mondeIG.estSimulationEnCours()));

            vueMenu.edition.getItems().forEach(item -> {
                item.setDisable(nbEtapesSelectionnes == 0 && nbArcsSelectionnes == 0);
                if((nbEtapesSelectionnes != 1 || nbArcsSelectionnes != 0) && item.getId() != null && item.getId().equals("renommer")) {
                    item.setDisable(true);
                }

                item.setDisable(vueMenu.mondeIG.estSimulationEnCours());
            });

            vueMenu.monde.getItems().forEach(item -> {
                if((nbEtapesSelectionnes != 1 || nbArcsSelectionnes != 0) && (item.getId() == null || item.getId() != null && !item.getId().equals("nbClients"))) {
                    item.setDisable(true);
                }

                item.setDisable(vueMenu.mondeIG.estSimulationEnCours());
            });

            vueMenu.parametres.getItems().forEach(item -> {
                item.setDisable(nbEtapesSelectionnes != 1 || vueMenu.mondeIG.estSimulationEnCours());
            });

            vueMenu.optionsChatGPT.getItems().forEach(item -> item.setDisable(vueMenu.mondeIG.estSimulationEnCours()));
        };

        if(Platform.isFxApplicationThread()) {
            command.run();
        } else {
            Platform.runLater(command);
        }
    }


    // —————————— METHODES PRIVES ——————————

    /**
     * Initialise le menu Fichier.
     */
    private void menuFichier() {
        this.fichier = new Menu("Fichier");

        MenuItem chargerJson = new MenuItem("Charger un monde");
        MenuItem sauverJson  = new MenuItem("Sauvegarder le monde");

        sauverJson.setOnAction(event  -> this.mondeIG.sauvegarderEnJson());
        chargerJson.setOnAction(event -> this.mondeIG.chargerDepuisJson());

        this.fichier.getItems().addAll(chargerJson, sauverJson);

        MenuItem quitter = new MenuItem("Quitter");
        quitter.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        quitter.setOnAction(event -> {
            ThreadsManager.getInstance().detruireTout();
            Platform.exit();
        });

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
        renommerSelection.setOnAction(event -> {
            EtapeIG etape = this.mondeIG.iteratorEtapesSelectionnees().next();
            afficherFenetreRenommer(this.mondeIG, etape);
        });

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
        delaiEtEcartTemps.setOnAction(event -> {
            if(this.mondeIG.getNbEtapesSelectionnees() != 1) { return; }
            EtapeIG etape = this.mondeIG.iteratorEtapesSelectionnees().next();
            afficherFenetreParametresTemps(this.mondeIG, etape);
        });

        // guichet - jetons
        MenuItem jetons = new MenuItem("Modifier le nombre de jetons");
        jetons.setOnAction(event -> {
            if(this.mondeIG.getNbEtapesSelectionnees() != 1) { return; }
            EtapeIG etape = this.mondeIG.iteratorEtapesSelectionnees().next();
            afficherFenetreParametresJetons(this.mondeIG, etape);
        });

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

    /**
     * Initialise le menu ChatGPT.
     */
    private void menuChatGPT() {
        this.optionsChatGPT = new Menu("ChatGPT");

        MenuItem chatgpt = new MenuItem("Generer un monde");

        chatgpt.setOnAction(event -> this.afficherFenetreChatGPT());

        this.optionsChatGPT.getItems().addAll(chatgpt);
    }

    /**
     * Affiche une boîte de dialogue permettant à l'utilisateur de renommer une étape spécifique.
     *
     * @param etape L'étape à renommer
     */
    public static void afficherFenetreRenommer(MondeIG mondeIG, EtapeIG etape) {
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
        dialogPane.getStylesheets().add(Objects.requireNonNull(VueMenu.class.getResource("/css/styles.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog");

        dialog.showAndWait().ifPresent(response -> {
            if (response == buttonValiderType) {
                String nom = textField.getText();

                if (!nom.isEmpty()) {
                    etape.setNom(nom);
                    mondeIG.deselectionnerSelection();
                    mondeIG.notifierObservateurs();
                }
            }
        });
    }

    /**
     * Affiche une boîte de dialogue permettant de modifier le temps et l'écart de temps d'une activité sélectionnée.
     */
    public static void afficherFenetreParametresTemps(MondeIG mondeIG, EtapeIG etape) {
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
        dialogPane.getStylesheets().add(Objects.requireNonNull(VueMenu.class.getResource("/css/styles.css")).toExternalForm());
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

            mondeIG.deselectionnerSelection();
            mondeIG.notifierObservateurs();
        });
    }

    /**
     * Affiche une boîte de dialogue permettant de modifier le nombre de jetons d'un guichet sélectionné.
     */
    public static void afficherFenetreParametresJetons(MondeIG mondeIG, EtapeIG etape) {
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
        dialogPane.getStylesheets().add(Objects.requireNonNull(VueMenu.class.getResource("/css/styles.css")).toExternalForm());
        dialogPane.getStyleClass().add("dialog");

        dialog.showAndWait().ifPresent(response -> {
            if (response == buttonValiderType) {
                String nbJetons = textField.getText();

                if (!nbJetons.isEmpty()) {
                    guichet.setJetons(Integer.parseInt(nbJetons));

                    mondeIG.deselectionnerSelection();
                    mondeIG.notifierObservateurs();
                }
            }
        });
    }

    /**
     * Affiche une boîte de dialogue pour définir le nombre de clients du monde.
     */
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

    /**
     * Affiche une boîte de dialogue permettant de décrire un monde à générer via ChatGPT.
     */
    private void afficherFenetreChatGPT() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Twisk");
        dialog.setHeaderText("Génération d'un monde via ChatGPT");

        // Boutons annuler et valider
        dialog.getDialogPane().getButtonTypes().clear();
        ButtonType buttonAnnulerType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonValiderType = new ButtonType("Générer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonAnnulerType, buttonValiderType);

        VBox content = new VBox(16);

        // description
        Label description = new Label("Veuillez décrire clairement le monde que vous souhaitez générer.");
        description.getStyleClass().add("dialog-description");
        content.getChildren().add(description);

        VBox group = new VBox();
        group.setSpacing(6);
        // nombre de clients
        Label nbClients = new Label("Prompt");
        nbClients.getStyleClass().add("dialog-clients-etape");

        // input
        TextArea textArea = new TextArea();
        textArea.setMinHeight(100);
        textArea.setPrefHeight(100);
        textArea.setWrapText(true);
        textArea.setStyle("-fx-alignment: top-left; -fx-padding: 2 1;");
        textArea.setPromptText("Décrivez ici votre monde (ex: un aéroport avec contrôle de sécurité, salle d'attente, portes d'embarquement...)");
        textArea.getStyleClass().add("dialog-input");
        group.getChildren().addAll(nbClients, textArea);

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
                if(textArea.getText().isEmpty() || textArea.getText().length() < 20) {
                    VueTwiskException.alert("Prompt invalide", "Veuillez écrire une description plus détaillée (au moins 20 caractères).");
                    return;
                }

                JsonObject mondeGenere = null;
                try {
                    mondeGenere = ChatGPT.demanderMonde(textArea.getText());
                } catch (Exception e) {
                    VueTwiskException.alert("Monde invalide", "Le monde généré par ChatGPT est incorrect. Veuillez réessayer avec une autre description.");
                }

                this.mondeIG.chargerDepuisJson(mondeGenere);
            }
        });
    }
}
