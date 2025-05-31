package twisk;

import twisk.monde.*;
import twisk.outils.ClassLoaderPerso;
import java.lang.reflect.Method;


/**
 * Classe principale pour exécuter l'application ClientTwisk.
 */
public class ClientTwisk {

    public static void main(String[] args) {
        Monde monde1 = mondeZoo();
        Monde monde2 = mondeCustom1();
        Monde monde3 = mondeCustom2();

        simuler(monde1, 2);
        // simuler(monde2, 2);
        // simuler(monde3, 2);

        // Simulation simulation = new Simulation();
        // simulation.setNbClients(5);
        // simulation.simuler(monde);
    }

    private static void simuler(Monde monde, int nbClients) {
        try {
            ClassLoaderPerso loader = new ClassLoaderPerso(ClientTwisk.class.getClassLoader());
            Class<?> simulation     = loader.loadClass("twisk.simulation.Simulation");
            Object instance         = simulation.getDeclaredConstructor().newInstance();

            Method setNbClients     = instance.getClass().getMethod("setNbClients", int.class);
            Method simuler          = instance.getClass().getMethod("simuler", Monde.class);

            setNbClients.invoke(instance, nbClients);
            simuler.invoke(instance, monde);

            loader       = null;
            simulation   = null;
            instance     = null;
            setNbClients = null;
            simuler      = null;

            System.gc();
        } catch (Exception ignored) {}
    }

    /**
     * Crée un monde de simulation avec un zoo et un toboggan.
     * @return Un Monde représentant le zoo.
     */
    private static Monde mondeZoo() {
        Monde monde    = new Monde();

        Etape zoo      = new Activite("balade au zoo", 6, 3);
        Etape guichet  = new Guichet("acces au toboggan", 1);
        Etape toboggan = new ActiviteRestreinte("toboggan", 4, 2);

        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);

        monde.ajouter(zoo, guichet, toboggan);
        monde.aCommeEntree(zoo);
        monde.aCommeSortie(toboggan);

        return monde;
    }

    private static Monde mondeCustom1() {
        // chaque etape i est successeurt de l'etape i-1
        Monde monde  = new Monde();

        Etape[] etapes = {
            new Activite("etape1", 4, 3),
            new Activite("etape2", 4, 3),
            new Activite("etape3", 4, 3),
            new Activite("etape4", 4, 3),
            new Activite("etape5", 4, 3),
            new Activite("etape6", 4, 3),
            new Activite("etape7", 4, 3),
            new Activite("etape8", 4, 3)
        };

        for (int i = 0; i < etapes.length-1; i++) {
            etapes[i].ajouterSuccesseur(etapes[i+1]);
        }

        monde.ajouter(etapes);
        monde.aCommeEntree(etapes[0]);
        monde.aCommeSortie(etapes[etapes.length - 1]);

        return monde;
    }

    private static Monde mondeCustom2() {
        Monde monde  = new Monde();

        Etape etape1 = new Activite("etape1", 2, 1);
        Etape etape2 = new Guichet("etape2", 2);
        Etape etape3 = new Guichet("etape3", 2);
        Etape etape4 = new ActiviteRestreinte("etape4", 2, 1);
        Etape etape5 = new ActiviteRestreinte("etape5", 2, 1);

        etape1.ajouterSuccesseur(etape2, etape3);
        etape2.ajouterSuccesseur(etape4);
        etape3.ajouterSuccesseur(etape5);

        monde.ajouter(etape1, etape2, etape3, etape4, etape5);
        monde.aCommeEntree(etape1);
        monde.aCommeSortie(etape4, etape5);

        return monde;
    }

}
