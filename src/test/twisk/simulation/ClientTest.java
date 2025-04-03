package twisk.simulation;

import org.junit.jupiter.api.Test;
import twisk.monde.*;
import static org.junit.jupiter.api.Assertions.*;


class ClientTest {

    @Test
    void allerA() {
        Client client = new Client(5);
        Etape  etape  = new Activite("Activite");

        assertNull(client.getEtape());
        assertEquals(-1, client.getRang());

        client.AllerA(etape, 3);

        assertEquals(etape, client.getEtape());
        assertEquals(3, client.getRang());
    }

    @Test
    void getNumeroClient() {
        Client client1 = new Client(5);
        assertEquals(5, client1.getNumeroClient());
        
        Client client2 = new Client(10);
        assertEquals(10, client2.getNumeroClient());
    }

    @Test
    void getRang() {
        Client client = new Client(5);
        Etape  etape  = new Activite("Activite");

        assertEquals(-1, client.getRang());

        client.AllerA(etape, 3);

        assertEquals(3, client.getRang());
    }

    @Test
    void getEtape() {
        Client client = new Client(5);
        Etape  etape  = new Activite("Activite");

        assertNull(client.getEtape());

        client.AllerA(etape, 3);

        assertEquals(etape, client.getEtape());
    }
}