package twisk.simulation;

import twisk.monde.Monde;

public class Simulation {

    public Simulation() {}

    public void simuler(Monde monde) {
        System.out.println(monde);
        String codeC = monde.toC();
        System.out.println("Code C généré :\n" + codeC);
    }


}
