package twisk;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import twisk.mondeIG.MondeIG;
import twisk.vues.*;
import java.util.Objects;
import static twisk.outils.TailleComposants.*;


public class Main extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();

        MondeIG monde   = new MondeIG();

        VueOutils  vueOutils  = new VueOutils(monde);
        VueMenu    vueMenu    = new VueMenu(monde);
        VueMondeIG vueMondeIG = new VueMondeIG(monde);


        root.setCenter(vueMondeIG);
        root.setTop(vueMenu);
        root.setBottom(vueOutils);


        Scene scene = new Scene(root, TWISK_LARGEUR, TWISK_HAUTEUR);
        stage.setTitle("Twisk");
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
