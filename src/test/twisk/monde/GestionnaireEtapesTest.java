package twisk.monde;

import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;


class GestionnaireEtapesTest {

    @Test
    void testAjouter() {
        GestionnaireEtapes gestionnaireEtapes = new GestionnaireEtapes();

        Etape[] etapes = {
                new Activite("Activite"),
                new Activite("Activite"),
                new Guichet("Guichet"),
                new ActiviteRestreinte("ActiviteRestreinte"),
                new Guichet("Guichet")
        };

        gestionnaireEtapes.ajouter(etapes);

        Iterator<Etape> iterator = gestionnaireEtapes.iterator();

        for (Etape etape : etapes) {
            assertTrue(iterator.hasNext());
            assertEquals(etape, iterator.next());
        }
    }


    @Test
    void testNbEtapes() {
        GestionnaireEtapes gestionnaireEtapes = new GestionnaireEtapes();

        Etape[] etapes = {
                new Activite("Activite"),
                new Activite("Activite"),
                new Guichet("Guichet"),
                new ActiviteRestreinte("ActiviteRestreinte"),
                new Guichet("Guichet")
        };

        gestionnaireEtapes.ajouter(etapes);

        assertEquals(5, gestionnaireEtapes.nbEtapes());
    }

    @Test
    void testNbActivites() {
        GestionnaireEtapes gestionnaireEtapes = new GestionnaireEtapes();

        Etape[] etapes = {
                new Activite("Activite"),
                new Activite("Activite"),
                new Guichet("Guichet"),
                new ActiviteRestreinte("ActiviteRestreinte"),
                new Guichet("Guichet")
        };

        gestionnaireEtapes.ajouter(etapes);

        assertEquals(3, gestionnaireEtapes.nbActivites());
    }

    @Test
    void testNbGuichets() {
        GestionnaireEtapes gestionnaireEtapes = new GestionnaireEtapes();

        Etape[] etapes = {
                new Activite("Activite"),
                new Activite("Activite"),
                new Guichet("Guichet"),
                new ActiviteRestreinte("ActiviteRestreinte"),
                new Guichet("Guichet")
        };

        gestionnaireEtapes.ajouter(etapes);

        assertEquals(2, gestionnaireEtapes.nbGuichets());
    }

    @Test
    void testIterator() {
        GestionnaireEtapes gestionnaireEtapes = new GestionnaireEtapes();

        Etape[] etapes = {
                new Activite("Activite"),
                new Activite("Activite"),
                new Guichet("Guichet"),
                new ActiviteRestreinte("ActiviteRestreinte"),
                new Guichet("Guichet")
        };

        gestionnaireEtapes.ajouter(etapes);

        Iterator<Etape> iterator = gestionnaireEtapes.iterator();

        for (Etape etape : etapes) {
            assertTrue(iterator.hasNext());
            assertEquals(etape, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

}