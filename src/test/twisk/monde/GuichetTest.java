package twisk.monde;

import org.junit.jupiter.api.Test;
import twisk.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;


class GuichetTest {
    @Test
    void testConstructeur1Parametre() {
        FabriqueNumero.getInstance().reset();

        Guichet[] guichet = {
                new Guichet("Guichet 1"),
                new Guichet("Guichet 2"),
                new Guichet("Guichet 3"),
                new Guichet("Guichet 4"),
                new Guichet("Guichet 5"),
        };

        for (int i = 0; i < guichet.length; i++) {
            Guichet activite = guichet[i];

            assertEquals(String.format("Guichet %d", i+1), activite.getNom());
            assertEquals(1, activite.getNbJetons());
            assertEquals(i, activite.getIdEtape());
        }
    }

    @Test
    void testConstructeur2ParametresCorrect() {
        FabriqueNumero.getInstance().reset();

        int[] parametres = { 2 , 7, 5 , 4 , 1 };

        Guichet[] guichet = {
                new Guichet("Guichet 1", parametres[0]),
                new Guichet("Guichet 2", parametres[1]),
                new Guichet("Guichet 3", parametres[2]),
                new Guichet("Guichet 4", parametres[3]),
                new Guichet("Guichet 5", parametres[4])
        };

        for (int i = 0; i < guichet.length; i++) {
            Guichet activite = guichet[i];
            int jetons = parametres[i];

            assertEquals(String.format("Guichet %d", i+1), activite.getNom());
            assertEquals(jetons, activite.getNbJetons());
            assertEquals(i, activite.getIdEtape());
        }
    }

    @Test
    void testConstructeur2ParametresIncorrect() {
        FabriqueNumero.getInstance().reset();

        int[] parametres = { -2 , -7, -5 , -4 , -1 };

        for (int jetons : parametres) {

            assertThrows(AssertionError.class, () -> {
                new Guichet("Guichet", jetons);
            });
        }
    }

    @Test
    void testGetNom() {

        int[] parametres = { 2 , 7, 5 , 4 , 1 };

        Guichet[] guichet = {
                new Guichet("Guichet 1", parametres[0]),
                new Guichet("Guichet 2", parametres[1]),
                new Guichet("Guichet 3", parametres[2]),
                new Guichet("Guichet 4", parametres[3]),
                new Guichet("Guichet 5", parametres[4])
        };


        for (int i = 0; i < guichet.length; i++) {
            Guichet activite = guichet[i];

            assertEquals(String.format("Guichet %d", i+1), activite.getNom());
        }
    }

    @Test
    void testGetNbJetons() {
        int[] parametres = { 2 , 7, 5 , 4 , 1 };

        Guichet[] guichet = {
                new Guichet("Guichet 1", parametres[0]),
                new Guichet("Guichet 2", parametres[1]),
                new Guichet("Guichet 3", parametres[2]),
                new Guichet("Guichet 4", parametres[3]),
                new Guichet("Guichet 5", parametres[4])
        };


        for (int i = 0; i < guichet.length; i++) {
            Guichet activite = guichet[i];
            int jetons      = parametres[i];

            assertEquals(jetons, activite.getNbJetons());
        }
    }

    @Test
    void testGetIdEtape() {
        FabriqueNumero.getInstance().reset();

        int[] parametres = { 2 , 7, 5 , 4 , 1 };

        Guichet[] guichet = {
                new Guichet("Guichet 1", parametres[0]),
                new Guichet("Guichet 2", parametres[1]),
                new Guichet("Guichet 3", parametres[2]),
                new Guichet("Guichet 4", parametres[3]),
                new Guichet("Guichet 5", parametres[4])
        };

        for (int i = 0; i < guichet.length; i++) {
            Guichet activite = guichet[i];

            assertEquals(i, activite.getIdEtape());
        }
    }

    @Test
    void testGetNumeroSemaphore() {
        FabriqueNumero.getInstance().reset();

        int[] parametres = { 2 , 7, 5 , 4 , 1 };

        Guichet[] guichet = {
                new Guichet("Guichet 1", parametres[0]),
                new Guichet("Guichet 2", parametres[1]),
                new Guichet("Guichet 3", parametres[2]),
                new Guichet("Guichet 4", parametres[3]),
                new Guichet("Guichet 5", parametres[4])
        };

        for (int i = 0; i < guichet.length; i++) {
            Guichet activite = guichet[i];

            assertEquals(i+1, activite.getNumeroSemaphore());
        }
    }

    @Test
    void testEstUnGuichet() {
        Guichet etape = new Guichet("Etape Test");

        assertTrue(etape.estUnGuichet());
    }

    @Test
    void testEstUneActivite() {
        Guichet etape = new Guichet("Etape Test");

        assertFalse(etape.estUneActivite());
    }

    @Test
    void testAjouterSuccesseur() {
        assertThrows(AssertionError.class, () -> {
            Guichet activite = new Guichet("Guichet");

            activite.ajouterSuccesseur(null);
        });

        assertThrows(AssertionError.class, () -> {
            Guichet guichet = new Guichet("Guichet");

            guichet.ajouterSuccesseur(new Guichet("Guichet Test"));
        });


        Guichet activite = new Guichet("Guichet 1");

        activite.ajouterSuccesseur(new ActiviteRestreinte("ActiviteRestreinte"));

        // TODO: meilleurs tests
    }
}