package twisk.monde;

import org.junit.jupiter.api.Test;
import twisk.outils.FabriqueNumero;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;


class ActiviteTest {

    @Test
    void testConstructeur1Parametre() {
        FabriqueNumero.getInstance().reset();

        Activite[] activites = {
                 new Activite("Activite 1"),
                 new Activite("Activite 2"),
                 new Activite("Activite 3"),
                 new Activite("Activite 4"),
                 new Activite("Activite 5"),
        };

        for (int i = 0; i < activites.length; i++) {
            Activite activite = activites[i];

            assertEquals(String.format("Activite %d", i+1), activite.getNom());
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

        Activite[] activites = {
                new Activite("Activite 1", parametres[0][0], parametres[0][1]),
                new Activite("Activite 2", parametres[1][0], parametres[1][1]),
                new Activite("Activite 3", parametres[2][0], parametres[2][1]),
                new Activite("Activite 4", parametres[3][0], parametres[3][1]),
                new Activite("Activite 5", parametres[4][0], parametres[4][1]),
        };

        for (int i = 0; i < activites.length; i++) {
            Activite activite = activites[i];
            int temps      = parametres[i][0];
            int ecartTemps = parametres[i][1];

            assertEquals(String.format("Activite %d", i+1), activite.getNom());
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
                new Activite("Activite", temps, ecartTemps);
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

        Activite[] activites = {
                new Activite("Activite 1", parametres[0][0], parametres[0][1]),
                new Activite("Activite 2", parametres[1][0], parametres[1][1]),
                new Activite("Activite 3", parametres[2][0], parametres[2][1]),
                new Activite("Activite 4", parametres[3][0], parametres[3][1]),
                new Activite("Activite 5", parametres[4][0], parametres[4][1]),
        };

        for (int i = 0; i < activites.length; i++) {
            Activite activite = activites[i];

            assertEquals(String.format("Activite %d", i+1), activite.getNom());
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

        Activite[] activites = {
                new Activite("Activite 1", parametres[0][0], parametres[0][1]),
                new Activite("Activite 2", parametres[1][0], parametres[1][1]),
                new Activite("Activite 3", parametres[2][0], parametres[2][1]),
                new Activite("Activite 4", parametres[3][0], parametres[3][1]),
                new Activite("Activite 5", parametres[4][0], parametres[4][1]),
        };

        for (int i = 0; i < activites.length; i++) {
            Activite activite = activites[i];
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

        Activite[] activites = {
                new Activite("Activite 1", parametres[0][0], parametres[0][1]),
                new Activite("Activite 2", parametres[1][0], parametres[1][1]),
                new Activite("Activite 3", parametres[2][0], parametres[2][1]),
                new Activite("Activite 4", parametres[3][0], parametres[3][1]),
                new Activite("Activite 5", parametres[4][0], parametres[4][1]),
        };

        for (int i = 0; i < activites.length; i++) {
            Activite activite = activites[i];
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

        Activite[] activites = {
                new Activite("Activite 1", parametres[0][0], parametres[0][1]),
                new Activite("Activite 2", parametres[1][0], parametres[1][1]),
                new Activite("Activite 3", parametres[2][0], parametres[2][1]),
                new Activite("Activite 4", parametres[3][0], parametres[3][1]),
                new Activite("Activite 5", parametres[4][0], parametres[4][1]),
        };

        for (int i = 0; i < activites.length; i++) {
            Activite activite = activites[i];

            assertEquals(i, activite.getIdEtape());
        }
    }

    @Test
    void testEstUneActivite() {
        Activite etape = new Activite("Etape Test");

        assertTrue(etape.estUneActivite());
    }

    @Test
    void testEstUneActiviteRestreinte() {
        Activite etape = new Activite("Etape Test");

        assertFalse(etape.estUneActiviteRestreinte());
    }

    @Test
    void testEstUnGuichet() {
        Activite etape = new Activite("Etape Test");

        assertFalse(etape.estUnGuichet());
    }

    @Test
    void testAjouterSuccesseur() {
        assertThrows(AssertionError.class, () -> {
            Activite activite = new Activite("Activite");

            activite.ajouterSuccesseur(null);
        });

        assertThrows(AssertionError.class, () -> {
            Activite activite = new Activite("Activite");

            activite.ajouterSuccesseur();
        });

        assertThrows(AssertionError.class, () -> {
            Activite activite = new Activite("Activite");

            activite.ajouterSuccesseur(new ActiviteRestreinte("ActiviteRestreinte"));
        });

        Etape[] etapes = {
                new Activite("Activite"),
                new Guichet("Guichet"),
                new Activite("Activite"),
                new Guichet("Guichet"),
                new Activite("Activite")
        };

        Activite activite = new Activite("Activite");

        activite.ajouterSuccesseur(etapes);

        for(Etape etape : etapes) {
            assertTrue(activite.getSuccesseurs().contains(etape));
        }

        assertEquals(5, activite.getSuccesseurs().nbEtapes());
        assertEquals(3, activite.getSuccesseurs().nbActivites());
        assertEquals(2, activite.getSuccesseurs().nbGuichets());
    }

    @Test
    void testIterator() {
        Activite activite = new Activite("Activite");

        Etape[] etapes = {
                new Activite("Activite"),
                new Activite("Activite"),
                new Guichet("Guichet"),
                new Activite("Activite"),
                new Guichet("Guichet")
        };

        activite.ajouterSuccesseur(etapes);

        Iterator<Etape> iterator = activite.iterator();

        for (Etape etape : activite.getSuccesseurs()) {
            assertTrue(iterator.hasNext());
            assertEquals(etape, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

}