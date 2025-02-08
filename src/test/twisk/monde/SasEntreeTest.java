package twisk.monde;

import org.junit.jupiter.api.Test;
import twisk.outils.FabriqueNumero;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;


class SasEntreeTest {

    @Test
    void testConstructeur1Parametre() {
        FabriqueNumero.getInstance().reset();

        SasEntree[] sasEntree = {
                new SasEntree(),
                new SasEntree(),
                new SasEntree(),
                new SasEntree(),
                new SasEntree(),
        };

        for (int i = 0; i < sasEntree.length; i++) {
            SasEntree etape = sasEntree[i];

            assertEquals("entree", etape.getNom());
            assertEquals(2, etape.getTemps());
            assertEquals(1, etape.getEcartTemps());
            assertEquals(i, etape.getIdEtape());
        }
    }



    @Test
    void testGetNom() {
        SasEntree[] sasEntree = {
                new SasEntree(),
                new SasEntree(),
                new SasEntree(),
                new SasEntree(),
                new SasEntree(),
        };

        for (int i = 0; i < sasEntree.length; i++) {
            SasEntree etape = sasEntree[i];

            assertEquals("entree", etape.getNom());
        }
    }

    @Test
    void testGetIdEtape() {
        FabriqueNumero.getInstance().reset();

        SasEntree[] sasEntree = {
                new SasEntree(),
                new SasEntree(),
                new SasEntree(),
                new SasEntree(),
                new SasEntree(),
        };

        for (int i = 0; i < sasEntree.length; i++) {
            SasEntree etape = sasEntree[i];

            assertEquals(i, etape.getIdEtape());
        }
    }

    @Test
    void testEstUneActivite() {
        SasEntree etape = new SasEntree();

        assertTrue(etape.estUneActivite());
    }

    @Test
    void testEstUneActiviteRestreinte() {
        SasEntree etape = new SasEntree();

        assertFalse(etape.estUneActiviteRestreinte());
    }

    @Test
    void testEstUnGuichet() {
        SasEntree etape = new SasEntree();

        assertFalse(etape.estUnGuichet());
    }

    @Test
    void testAjouterSuccesseur() {
        assertThrows(AssertionError.class, () -> {

            SasEntree etape = new SasEntree();
            etape.ajouterSuccesseur(null);
        });

        assertThrows(AssertionError.class, () -> {

            SasEntree etape = new SasEntree();
            etape.ajouterSuccesseur();
        });


        SasEntree etape = new SasEntree();

        etape.ajouterSuccesseur(new Activite("activite"));

        // TODO: meilleurs tests
    }

    @Test
    void testIterator() {
        SasEntree entree = new SasEntree();

        Etape[] etapes = {
                new Activite("Activite"),
                new Activite("Activite"),
                new Guichet("Guichet"),
                new Activite("Activite"),
                new Guichet("Guichet")
        };

        entree.ajouterSuccesseur(etapes);

        Iterator<Etape> iterator = entree.iterator();

        for (Etape etape : entree.getSuccesseurs()) {
            assertTrue(iterator.hasNext());
            assertEquals(etape, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

}