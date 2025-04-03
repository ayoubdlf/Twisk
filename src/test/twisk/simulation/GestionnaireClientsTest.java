package twisk.simulation;

import org.junit.jupiter.api.*;
import twisk.monde.*;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;


class GestionnaireClientsTest {

    private GestionnaireClients gestionnaire;
    private int[] tabClients;
    
    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireClients();
        tabClients   = new int[]{ 1, 2, 3, 4, 5 };
    }

    @Test
    void setClients() {
        assertEquals(0, gestionnaire.getNbClients());

        gestionnaire.setClients(tabClients);

        assertEquals(5, gestionnaire.getNbClients());

        Iterator<Client> it = gestionnaire.iterator();

        for (int i = 0; i < 5; i++) {
            assertTrue(it.hasNext());
            Client client = it.next();
            assertEquals(tabClients[i], client.getNumeroClient());
        }

        assertFalse(it.hasNext());
    }

    @Test
    void allerA() {
        gestionnaire.setClients(tabClients);

        Etape etape = new Activite("Activite");

        gestionnaire.allerA(1, etape, 0);
        gestionnaire.allerA(2, etape, 123);

        for (Client client : gestionnaire) {
            if(client.getNumeroClient() == 1) { assertEquals(0, client.getRang());}
            if(client.getNumeroClient() == 2) { assertEquals(123, client.getRang());}
        }
    }

    @Test
    void nettoyer() {
        gestionnaire.setClients(tabClients);
        assertEquals(5, gestionnaire.getNbClients());

        gestionnaire.nettoyer();

        assertEquals(0, gestionnaire.getNbClients());
        assertFalse(gestionnaire.iterator().hasNext());
    }

    @Test
    void iterator() {
        assertFalse(gestionnaire.iterator().hasNext());

        gestionnaire.setClients(tabClients);

        Iterator<Client> it = gestionnaire.iterator();
        int res = 0;

        while (it.hasNext()) {
            Client client = it.next();
            assertNotNull(client);
            res += 1;
        }

        assertEquals(5, res);
    }
    
    @Test
    void getNbClients() {
        assertEquals(0, gestionnaire.getNbClients());

        gestionnaire.setClients(tabClients);
        assertEquals(5, gestionnaire.getNbClients());

        gestionnaire.nettoyer();
        assertEquals(0, gestionnaire.getNbClients());
    }
}