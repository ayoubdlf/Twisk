package twisk.monde;

import org.junit.jupiter.api.Test;
import twisk.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;

class ActiviteRestreinteTest {

    @Test
    void testConstructeur1Parametre() {
        FabriqueNumero.getInstance().reset();

        ActiviteRestreinte[][] activites = {
                { new ActiviteRestreinte("ActiviteRestreinte 1") },
                { new ActiviteRestreinte("ActiviteRestreinte 2") },
                { new ActiviteRestreinte("ActiviteRestreinte 3") },
                { new ActiviteRestreinte("ActiviteRestreinte 4") },
                { new ActiviteRestreinte("ActiviteRestreinte 5") },
        };

        for (int i = 0; i < activites.length; i++) {
            ActiviteRestreinte activite = activites[i][0];

            assertEquals(String.format("ActiviteRestreinte %d", i+1), activite.getNom());
            assertEquals(2, activite.getTemps());
            assertEquals(1, activite.getEcartTemps());
            assertEquals(i, activite.getIdEtape());
        }
    }

    @Test
    void testConstructeur3ParametresCorrect() {
        FabriqueNumero.getInstance().reset();

        int[][] parametres = {
                { 2 , 1 },
                { 10, 1 },
                { 4 , 3 },
                { 4 , 1 },
                { 7 , 6 }
        };

        ActiviteRestreinte[][] activites = {
                { new ActiviteRestreinte("ActiviteRestreinte 1", parametres[0][0], parametres[0][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 2", parametres[1][0], parametres[1][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 3", parametres[2][0], parametres[2][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 4", parametres[3][0], parametres[3][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 5", parametres[4][0], parametres[4][1]) },
        };

        for (int i = 0; i < activites.length; i++) {
            ActiviteRestreinte activite = activites[i][0];
            int temps      = parametres[i][0];
            int ecartTemps = parametres[i][1];

            assertEquals(String.format("ActiviteRestreinte %d", i+1), activite.getNom());
            assertEquals(temps, activite.getTemps());
            assertEquals(ecartTemps, activite.getEcartTemps());
            assertEquals(i, activite.getIdEtape());
        }
    }

    @Test
    void testConstructeur3ParametresIncorrect() {
        FabriqueNumero.getInstance().reset();

        int[][] parametres = {
                { 0 , 4 },
                { 5 , 0 },
                { 1 , 1 },
                { -5, 5 },
                { 5, -5 }
        };

        for (int[] param : parametres) {
            int temps      = param[0];
            int ecartTemps = param[1];

            assertThrows(AssertionError.class, () -> {
                new ActiviteRestreinte("ActiviteRestreinte", temps, ecartTemps);
            });
        }
    }

    @Test
    void testGetNom() {
        int[][] parametres = {
                { 2 , 1 },
                { 10, 1 },
                { 4 , 3 },
                { 4 , 1 },
                { 7 , 6 }
        };

        ActiviteRestreinte[][] activites = {
                { new ActiviteRestreinte("ActiviteRestreinte 1", parametres[0][0], parametres[0][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 2", parametres[1][0], parametres[1][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 3", parametres[2][0], parametres[2][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 4", parametres[3][0], parametres[3][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 5", parametres[4][0], parametres[4][1]) },
        };

        for (int i = 0; i < activites.length; i++) {
            ActiviteRestreinte activite = activites[i][0];

            assertEquals(String.format("ActiviteRestreinte %d", i+1), activite.getNom());
        }
    }

    @Test
    void testGetTemps() {
        int[][] parametres = {
                { 2 , 1 },
                { 10, 1 },
                { 4 , 3 },
                { 4 , 1 },
                { 7 , 6 }
        };

        ActiviteRestreinte[][] activites = {
                { new ActiviteRestreinte("ActiviteRestreinte 1", parametres[0][0], parametres[0][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 2", parametres[1][0], parametres[1][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 3", parametres[2][0], parametres[2][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 4", parametres[3][0], parametres[3][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 5", parametres[4][0], parametres[4][1]) },
        };

        for (int i = 0; i < activites.length; i++) {
            ActiviteRestreinte activite = activites[i][0];
            int temps      = parametres[i][0];

            assertEquals(temps, activite.getTemps());
        }
    }

    @Test
    void testGetEcartTemps() {
        FabriqueNumero.getInstance().reset();

        int[][] parametres = {
                { 2 , 1 },
                { 10, 1 },
                { 4 , 3 },
                { 4 , 1 },
                { 7 , 6 }
        };

        ActiviteRestreinte[][] activites = {
                { new ActiviteRestreinte("ActiviteRestreinte 1", parametres[0][0], parametres[0][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 2", parametres[1][0], parametres[1][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 3", parametres[2][0], parametres[2][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 4", parametres[3][0], parametres[3][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 5", parametres[4][0], parametres[4][1]) },
        };

        for (int i = 0; i < activites.length; i++) {
            ActiviteRestreinte activite = activites[i][0];
            int ecartTemps = parametres[i][1];

            assertEquals(ecartTemps, activite.getEcartTemps());
        }
    }

    @Test
    void testGetIdEtape() {
        FabriqueNumero.getInstance().reset();

        int[][] parametres = {
                { 2 , 1 },
                { 10, 1 },
                { 4 , 3 },
                { 4 , 1 },
                { 7 , 6 }
        };

        ActiviteRestreinte[][] activites = {
                { new ActiviteRestreinte("ActiviteRestreinte 1", parametres[0][0], parametres[0][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 2", parametres[1][0], parametres[1][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 3", parametres[2][0], parametres[2][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 4", parametres[3][0], parametres[3][1]) },
                { new ActiviteRestreinte("ActiviteRestreinte 5", parametres[4][0], parametres[4][1]) },
        };

        for (int i = 0; i < activites.length; i++) {
            ActiviteRestreinte activite = activites[i][0];

            assertEquals(i, activite.getIdEtape());
        }
    }

    @Test
    void testEstUneActiviteRestreinte() {
        ActiviteRestreinte etape = new ActiviteRestreinte("Etape Test");

        assertTrue(etape.estUneActiviteRestreinte());
    }


    @Test
    void testEstUnGuichet() {
        ActiviteRestreinte etape = new ActiviteRestreinte("Etape Test");

        assertFalse(etape.estUnGuichet());
    }

    @Test
    void testAjouterSuccesseur() {
        assertThrows(AssertionError.class, () -> {
            ActiviteRestreinte activite = new ActiviteRestreinte("ActiviteRestreinte");

            activite.ajouterSuccesseur(null);
        });


        ActiviteRestreinte activite = new ActiviteRestreinte("ActiviteRestreinte 1");

        activite.ajouterSuccesseur(new ActiviteRestreinte("ActiviteRestreinte 2"));

        // TODO: meilleurs tests
    }

}