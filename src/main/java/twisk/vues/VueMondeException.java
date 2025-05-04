package twisk.vues;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

import java.util.Objects;


public class VueMondeException {

    /**
     * Affiche une alerte d'erreur avec le titre et le message spécifiés.
     * 
     * @param titre Le titre de l'alerte d'erreur.
     * @param message Le message d'erreur à afficher.
     */
    public static void alert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Twisk");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(VueMondeException.class.getResource("/css/styles.css")).toExternalForm());
        dialogPane.getStyleClass().add("alert-error");

        HBox content = new HBox();
        VBox texte   = new VBox();

        Label labelTitre   = new Label(titre);
        Label labelMessage = new Label(message);

        content.getStyleClass().add("alert-error");
        labelTitre.getStyleClass().add("alert-error-titre");
        labelMessage.getStyleClass().add("alert-error-message");

        texte.getChildren().addAll(labelTitre, labelMessage);
        content.getChildren().addAll(getIcone(), texte);

        dialogPane.setHeader(new HBox()); // il y'aura rien fans le header. tout sera dans le content
        dialogPane.setContent(content);

        alert.showAndWait();
    }

    private static SVGPath getIcone() {
        SVGPath svg = new SVGPath();
        svg.setContent("M12 9v3.75m9-.75a9 9 0 1 1-18 0 9 9 0 0 1 18 0Zm-9 3.75h.008v.008H12v-.008Z");
        svg.setStyle("-fx-stroke: red-500; -fx-stroke-width: 1.5; -fx-fill: transparent; -fx-stroke-line-cap: round; -fx-stroke-line-join: round ");

        double scale = 14 / svg.getBoundsInLocal().getHeight();
        svg.setScaleX(scale);
        svg.setScaleY(scale);

        return svg;
    }

}
