package twisk;

import twisk.monde.*;
import twisk.simulation.Simulation;


public class ClientTwisk {

    public static void main(String[] args) {
        // Monde monde           = mondeCustom1();
        // Monde monde           = mondeCustom2();
        Monde monde           = mondeZoo();
        Simulation simulation = new Simulation();

        simulation.setNbClients(5);
        simulation.simuler(monde);
    }

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
        Monde monde  = new Monde();

        Etape etape1 = new Activite("etape1", 2, 1);
        Etape etape2 = new Activite("etape2", 2, 1);
        Etape etape3 = new Activite("etape3", 2, 1);
        Etape etape4 = new Activite("etape4", 2, 1);
        Etape etape5 = new Activite("etape5", 2, 1);

        etape1.ajouterSuccesseur(etape2, etape3);
        etape2.ajouterSuccesseur(etape4);
        etape3.ajouterSuccesseur(etape5);

        monde.ajouter(etape1, etape2, etape3, etape4, etape5);
        monde.aCommeEntree(etape1);
        monde.aCommeSortie(etape4, etape5);

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
