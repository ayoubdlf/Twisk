package twisk.monde;

import org.junit.jupiter.api.Test;
import twisk.outils.FabriqueNumero;

import static org.junit.jupiter.api.Assertions.*;

class SasSortieTest {

    @Test
    void testConstructeur1Parametre() {
        FabriqueNumero.getInstance().reset();

        SasSortie[] sasSortie = {
                new SasSortie(),
                new SasSortie(),
                new SasSortie(),
                new SasSortie(),
                new SasSortie(),
        };

        for (int i = 0; i < sasSortie.length; i++) {
            SasSortie etape = sasSortie[i];

            assertEquals("sortie", etape.getNom());
            assertEquals(2, etape.getTemps());
            assertEquals(1, etape.getEcartTemps());
            assertEquals(i, etape.getIdEtape());
        }
    }



    @Test
    void testGetNom() {
        SasSortie[] sasSortie = {
                new SasSortie(),
                new SasSortie(),
                new SasSortie(),
                new SasSortie(),
                new SasSortie(),
        };

        for (int i = 0; i < sasSortie.length; i++) {
            SasSortie etape = sasSortie[i];

            assertEquals("sortie", etape.getNom());
        }
    }

    @Test
    void testGetIdEtape() {
        FabriqueNumero.getInstance().reset();

        SasSortie[] sasSortie = {
                new SasSortie(),
                new SasSortie(),
                new SasSortie(),
                new SasSortie(),
                new SasSortie(),
        };

        for (int i = 0; i < sasSortie.length; i++) {
            SasSortie etape = sasSortie[i];

            assertEquals(i, etape.getIdEtape());
        }
    }

    @Test
    void testEstUneActivite() {
        SasSortie etape = new SasSortie();

        assertTrue(etape.estUneActivite());
    }

    @Test
    void testEstUneActiviteRestreinte() {
        SasSortie etape = new SasSortie();

        assertFalse(etape.estUneActiviteRestreinte());
    }

    @Test
    void testEstUnGuichet() {
        SasSortie etape = new SasSortie();

        assertFalse(etape.estUnGuichet());
    }

    @Test
    void testAjouterSuccesseur() {
        assertThrows(AssertionError.class, () -> {

            SasSortie etape = new SasSortie();
            etape.ajouterSuccesseur(null);
        });


        SasSortie etape = new SasSortie();

        etape.ajouterSuccesseur(new Activite("activite"));

        // TODO: meilleurs tests
    }

}