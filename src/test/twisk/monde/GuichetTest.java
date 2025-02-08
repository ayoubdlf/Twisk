package twisk.monde;

import org.junit.jupiter.api.Test;
import twisk.outils.FabriqueNumero;
import java.util.Iterator;
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
    void testAjouterSuccesseurIncorrect() {
        // test ajout null
        assertThrows(AssertionError.class, () -> {
            Guichet guichet = new Guichet("Guichet");

            guichet.ajouterSuccesseur(null);
        });

        assertThrows(AssertionError.class, () -> {
            Guichet guichet = new Guichet("Guichet");

            guichet.ajouterSuccesseur();
        });

        // test ajout d'un guichet comme successeur
        assertThrows(AssertionError.class, () -> {
            Guichet guichet = new Guichet("Guichet");

            guichet.ajouterSuccesseur(new Guichet("Guichet"));
        });



        Etape[] etapesIncorrectes1 = {
                new Activite("Activite"),
                new Guichet("Guichet"),
                new Activite("Activite"),
                new Guichet("Guichet"),
                new Activite("Activite")
        };

        Etape[] etapesIncorrectes2 = {
                new ActiviteRestreinte("ActiviteRestreinte 1"),
                new ActiviteRestreinte("ActiviteRestreinte 2"),
                new ActiviteRestreinte("ActiviteRestreinte 3"),
                new ActiviteRestreinte("ActiviteRestreinte 4"),
                new ActiviteRestreinte("ActiviteRestreinte 5")
        };


        Guichet guichet = new Guichet("Guichet");


        // test ajout de activites/guichets
        for(Etape etape : etapesIncorrectes1) {
            assertThrows(AssertionError.class, () -> { guichet.ajouterSuccesseur(etape); });
        }

        // test ajout de plusiers activites restreintes d'un seul coup
        assertThrows(AssertionError.class, () -> { guichet.ajouterSuccesseur(etapesIncorrectes2); });

        assertEquals(0, guichet.getSuccesseurs().nbEtapes());
        assertEquals(0, guichet.getSuccesseurs().nbActivites());
        assertEquals(0, guichet.getSuccesseurs().nbGuichets());


        ActiviteRestreinte activite = new ActiviteRestreinte("ActiviteRestreinte 1");
        guichet.ajouterSuccesseur(activite);

        assertEquals(1, guichet.getSuccesseurs().nbEtapes());
        assertEquals(1, guichet.getSuccesseurs().nbActivites());
        assertEquals(0, guichet.getSuccesseurs().nbGuichets());

        // test ajout d'une deuxieme activite restreinte
        assertThrows(AssertionError.class, () -> { guichet.ajouterSuccesseur(new ActiviteRestreinte("ActiviteRestreinte 2")); });
    }

    @Test
    void testAjouterSuccesseurCorrect() {
        Guichet guichet             = new Guichet("Guichet");
        ActiviteRestreinte activite = new ActiviteRestreinte("ActiviteRestreinte 1");

        guichet.ajouterSuccesseur(activite);

        assertTrue(guichet.getSuccesseurs().contains(activite));
        assertEquals(1, guichet.getSuccesseurs().nbEtapes());
        assertEquals(1, guichet.getSuccesseurs().nbActivites());
        assertEquals(0, guichet.getSuccesseurs().nbGuichets());
    }

    @Test
    void testIterator() {
        Guichet guichet = new Guichet("Guichet");

        guichet.ajouterSuccesseur(new ActiviteRestreinte("ActiviteRestreinte"));

        Iterator<Etape> iterator = guichet.iterator();

        for (Etape etape : guichet.getSuccesseurs()) {
            assertTrue(iterator.hasNext());
            assertEquals(etape, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }
}