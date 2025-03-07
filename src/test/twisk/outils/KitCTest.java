package twisk.outils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;


class KitCTest {

    @Test
    void creerEnvironnement() {
    }

    @Test
    void creerFichier() {
        KitC kitC = new KitC();
        kitC.creerFichier("AAA");

        Path path = Paths.get("/tmp/twisk/client.c");

        assertTrue(Files.exists(path));

        try {
            String str = Files.readString(path).trim();

            assertEquals("AAA", str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void compiler() {
    }

    @Test
    void construireLaBibliotheque() {
    }
}