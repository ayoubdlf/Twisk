package twisk.vues;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import twisk.mondeIG.MondeIG;
import twisk.outils.TailleComposants;
import twisk.simulation.Client;


public class VueClient extends Circle {

    private MondeIG monde;
    private Client client;

    public VueClient(MondeIG monde, Client client) {
        super(TailleComposants.CLIENT_RADIUS);

        this.monde  = monde;
        this.client = client;

        this.initialiser();
    }


    // —————————— METHODES PUBLIQUES ——————————

    // —————————— METHODES PRIVES ——————————

    private void initialiser() {
        this.getStyleClass().add("client-circle");

        Color color = this.getRandomColor(this.client.getNumeroClient());

        this.setFill(color);
    }

    /**
     * Génère une couleur RGB aléatoire en utilisant le pid.
     *
     * @param pid le pid du client
     * @return une couleur
     */
    private Color getRandomColor(int pid) {
        // Random rand = new Random(pid);
        // return Color.rgb(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)); // Les couleurs sont moches, on vas utiliser hsb du coup
        return Color.hsb((pid * 137.508) % 360, 0.5, 1.0);
    }
}
