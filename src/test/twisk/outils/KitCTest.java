package twisk.outils;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;


class KitCTest {

    @BeforeEach
    void nettoyer() {
        KitC kitC = new KitC();

        kitC.creerEnvironnement();
    }

    @Test
    void creerEnvironnement() {
        KitC kitC = new KitC();

        Path path = Paths.get("/tmp/twisk");
        assertTrue(Files.exists(path) && Files.isDirectory(path));

        String[] files =  { "codeNatif.o", "def.h", "programmeC.o"};

        for (String fileName : files) {
            Path file = Paths.get(String.format("/tmp/twisk/%s", fileName));
            assertTrue(Files.exists(file));
        }
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
        } catch (IOException ignored) {}
    }

    @Test
    void compiler() {
        KitC kitC = new KitC();

        kitC.creerFichier("int main() { return 0; }");
        kitC.compiler();

        Path path = Paths.get("/tmp/twisk/client.o");
        assertTrue(Files.exists(path));
    }
}