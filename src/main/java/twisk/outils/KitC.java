package twisk.outils;

import java.io.*;
import java.nio.file.*;


public class KitC {

    public KitC() {}

    public void creerEnvironnement() {
        try {
            this.supprimerDossier(); // supprimer le dossier `/tmp/twisk` s'il existe

            Path directory = Paths.get("/tmp/twisk");

            Files.createDirectories(directory); // création du répertoire twisk sous /tmp.

            // copie des fichiers programmeC.o et def.h sous /tmp/twisk
            String[] liste = { "programmeC.o", "def.h" };

            for (String nom : liste) {
                InputStream src = getClass().getResourceAsStream("/codeC/" + nom);
                Path dest       = directory.resolve(nom);

                if(this.isMac() && nom.equals("programmeC.o")) {
                    src = getClass().getResourceAsStream("/codeC/mac/" + nom);
                }

                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void creerFichier(String codeC) {
        try {
            Path fichier = Paths.get("/tmp/twisk/client.c");
            Files.write(fichier, codeC.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compiler() {
        String commande;

        if(this.isMac()) {
            commande = "gcc -Wall -c /tmp/twisk/client.c -o /tmp/twisk/client.o";
        } else {
            commande = "gcc -Wall -ansi -pedantic -fPIC -c /tmp/twisk/client.c -o /tmp/twisk/client.o";
        }

        ProcessBuilder pb = new ProcessBuilder(commande.split(" "));

        try {
            pb.inheritIO().start().waitFor();
        } catch (Exception ignored) {}
    }

    public void construireLaBibliotheque() {
        String commande;

        if(this.isMac()) {
            commande = "gcc -dynamiclib /tmp/twisk/programmeC.o /tmp/twisk/client.o -o /tmp/twisk/libTwisk.so";
        } else {
            commande = "gcc -shared /tmp/twisk/programmeC.o /tmp/twisk/client.o -o /tmp/twisk/libTwisk.so";
        }

        ProcessBuilder pb = new ProcessBuilder(commande.split(" "));

        try {
            pb.inheritIO().start().waitFor();
        } catch (Exception ignored) {}
    }

    private void supprimerDossier() {
        String commande   = "rm -rf /tmp/twisk";

        ProcessBuilder pb = new ProcessBuilder(commande.split(" "));

        try {
            pb.inheritIO().start().waitFor();
        } catch (Exception ignored) {}
    }

    private boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();

        return os.contains("mac") || os.contains("darwin");
    }

}
