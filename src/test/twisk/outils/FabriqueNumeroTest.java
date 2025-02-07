package twisk.outils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FabriqueNumeroTest {

    @Test
    void reset() {
        FabriqueNumero.getInstance().reset();
        assertEquals(-1+1,  FabriqueNumero.getInstance().getNumeroEtape());
        assertEquals(0+1,  FabriqueNumero.getInstance().getNumeroSemaphore());
    }

    @Test
    void getNumeroEtape() {
        FabriqueNumero.getInstance().reset();
        assertEquals(-1+1,  FabriqueNumero.getInstance().getNumeroEtape());
        assertEquals(0+1,  FabriqueNumero.getInstance().getNumeroEtape());
        assertEquals(1+1,  FabriqueNumero.getInstance().getNumeroEtape());
        assertEquals(2+1,  FabriqueNumero.getInstance().getNumeroEtape());
        assertEquals(3+1,  FabriqueNumero.getInstance().getNumeroEtape());
    }

    @Test
    void getNumeroSemaphore() {
        FabriqueNumero.getInstance().reset();

        assertEquals(0+1,  FabriqueNumero.getInstance().getNumeroSemaphore());
        assertEquals(1+1,  FabriqueNumero.getInstance().getNumeroSemaphore());
        assertEquals(2+1,  FabriqueNumero.getInstance().getNumeroSemaphore());
        assertEquals(3+1,  FabriqueNumero.getInstance().getNumeroSemaphore());
    }
}