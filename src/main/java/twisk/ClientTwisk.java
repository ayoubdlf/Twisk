package twisk;

import twisk.monde.*;
import twisk.simulation.Simulation;


public class ClientTwisk {

    public static void main(String[] args) {
        Monde monde           = mondeZoo();
        Simulation simulation = new Simulation();

        simulation.simuler(monde);
    }

    private static Monde mondeZoo() {
        Monde monde    = new Monde();

        Etape zoo      = new Activite("balade au zoo", 6, 3);
        Etape guichet  = new Guichet("acces au toboggan", 1);
        Etape toboggan = new Activite("toboggan", 4, 2);

        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);

        monde.ajouter(zoo, guichet, toboggan);
        monde.aCommeEntree(zoo);
        monde.aCommeSortie(toboggan);

        return monde;
    }

}
