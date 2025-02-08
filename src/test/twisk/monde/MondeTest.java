package twisk.monde;

import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;


class MondeTest {

    @Test
    void testACommeEntree() {
        assertThrows(AssertionError.class, () -> {
            Monde monde = new Monde();

            monde.aCommeEntree(null);
        });

        assertThrows(AssertionError.class, () -> {
            Monde monde = new Monde();

            monde.aCommeEntree();
        });

        Monde monde    = new Monde();

        Etape zoo      = new Activite("balade au zoo", 6, 3);
        Etape guichet  = new Guichet("acces au toboggan", 1);
        Etape toboggan = new ActiviteRestreinte("toboggan", 4, 2);

        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);

        monde.ajouter(zoo, guichet, toboggan);
        monde.aCommeEntree(zoo);
        monde.aCommeSortie(toboggan);


        assertTrue(monde.getEntree().getSuccesseurs().contains(zoo));
        assertFalse(monde.getEntree().getSuccesseurs().contains(guichet));
        assertFalse(monde.getEntree().getSuccesseurs().contains(toboggan));

        assertTrue(monde.getEtapes().contains(zoo));
        assertTrue(monde.getEtapes().contains(guichet));
        assertTrue(monde.getEtapes().contains(toboggan));

        assertTrue(monde.getSortie().getPredecesseurs().contains(toboggan));
        assertFalse(monde.getSortie().getPredecesseurs().contains(guichet));
        assertFalse(monde.getSortie().getPredecesseurs().contains(zoo));
    }

    @Test
    void testACommeSortie() {
        assertThrows(AssertionError.class, () -> {
            Monde monde = new Monde();

            monde.aCommeEntree(null);
        });

        assertThrows(AssertionError.class, () -> {
            Monde monde = new Monde();

            monde.aCommeEntree();
        });

        Monde monde    = new Monde();

        Etape zoo      = new Activite("balade au zoo", 6, 3);
        Etape guichet  = new Guichet("acces au toboggan", 1);
        Etape toboggan = new ActiviteRestreinte("toboggan", 4, 2);

        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);

        monde.ajouter(zoo, guichet, toboggan);
        monde.aCommeEntree(zoo);
        monde.aCommeSortie(toboggan);


        assertTrue(monde.getEntree().getSuccesseurs().contains(zoo));
        assertFalse(monde.getEntree().getSuccesseurs().contains(guichet));
        assertFalse(monde.getEntree().getSuccesseurs().contains(toboggan));

        assertTrue(monde.getEtapes().contains(zoo));
        assertTrue(monde.getEtapes().contains(guichet));
        assertTrue(monde.getEtapes().contains(toboggan));

        assertTrue(monde.getSortie().getPredecesseurs().contains(toboggan));
        assertFalse(monde.getSortie().getPredecesseurs().contains(guichet));
        assertFalse(monde.getSortie().getPredecesseurs().contains(zoo));
    }

    @Test
    void testAjouter() {
        assertThrows(AssertionError.class, () -> {
            Monde monde = new Monde();

            monde.aCommeEntree(null);
        });

        assertThrows(AssertionError.class, () -> {
            Monde monde = new Monde();

            monde.aCommeEntree();
        });

        Monde monde    = new Monde();

        Etape zoo      = new Activite("balade au zoo", 6, 3);
        Etape guichet  = new Guichet("acces au toboggan", 1);
        Etape toboggan = new ActiviteRestreinte("toboggan", 4, 2);

        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);

        monde.ajouter(zoo, guichet, toboggan);
        monde.aCommeEntree(zoo);
        monde.aCommeSortie(toboggan);


        assertTrue(monde.getEntree().getSuccesseurs().contains(zoo));
        assertFalse(monde.getEntree().getSuccesseurs().contains(guichet));
        assertFalse(monde.getEntree().getSuccesseurs().contains(toboggan));

        assertTrue(monde.getEtapes().contains(zoo));
        assertTrue(monde.getEtapes().contains(guichet));
        assertTrue(monde.getEtapes().contains(toboggan));

        assertTrue(monde.getSortie().getPredecesseurs().contains(toboggan));
        assertFalse(monde.getSortie().getPredecesseurs().contains(guichet));
        assertFalse(monde.getSortie().getPredecesseurs().contains(zoo));
    }

    @Test
    void testNbEtapes() {
        Monde monde    = new Monde();

        Etape zoo      = new Activite("balade au zoo", 6, 3);
        Etape guichet  = new Guichet("acces au toboggan", 1);
        Etape toboggan = new ActiviteRestreinte("toboggan", 4, 2);

        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);

        monde.ajouter(zoo, guichet, toboggan);
        monde.aCommeEntree(zoo);
        monde.aCommeSortie(toboggan);


        assertEquals(3, monde.nbEtapes());
    }

    @Test
    void testNbActivites() {
        Monde monde    = new Monde();

        Etape zoo      = new Activite("balade au zoo", 6, 3);
        Etape guichet  = new Guichet("acces au toboggan", 1);
        Etape toboggan = new ActiviteRestreinte("toboggan", 4, 2);

        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);

        monde.ajouter(zoo, guichet, toboggan);
        monde.aCommeEntree(zoo);
        monde.aCommeSortie(toboggan);


        assertEquals(2, monde.nbActivites());
    }

    @Test
    void testNbGuichets() {
        Monde monde    = new Monde();

        Etape zoo      = new Activite("balade au zoo", 6, 3);
        Etape guichet  = new Guichet("acces au toboggan", 1);
        Etape toboggan = new ActiviteRestreinte("toboggan", 4, 2);

        zoo.ajouterSuccesseur(guichet);
        guichet.ajouterSuccesseur(toboggan);

        monde.ajouter(zoo, guichet, toboggan);
        monde.aCommeEntree(zoo);
        monde.aCommeSortie(toboggan);


        assertEquals(1, monde.nbGuichets());
    }

    @Test
    void testIterator() {
        Monde monde = new Monde();

        Etape[] etapes = {
                new Activite("Activite"),
                new Activite("Activite"),
                new Guichet("Guichet"),
                new Activite("Activite"),
                new Guichet("Guichet")
        };

        monde.ajouter(etapes);

        Iterator<Etape> iterator = monde.iterator();

        for (Etape etape : monde.getEtapes()) {
            assertTrue(iterator.hasNext());
            assertEquals(etape, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }
}