package twisk.outils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.file.*;
import java.util.ArrayList;


public class KitC {

    /**
     * Constructeur de la classe KitC.
     */
    public KitC() {}

    /* —————————— METHODES PUBLIQUES —————————— */

    /**
     * Crée l'environnement nécessaire pour la simulation.
     */
    public void creerEnvironnement() {
        try {
            this.supprimerDossier(); // supprimer le dossier `/tmp/twisk` s'il existe

            Path directory = Paths.get("/tmp/twisk");

            Files.createDirectories(directory); // création du répertoire twisk sous /tmp.

            // copie des fichiers programmeC.o et def.h sous /tmp/twisk
            String[] liste = { "programmeC.o", "def.h", "codeNatif.o" };

            for (String nom : liste) {
                InputStream src = getClass().getResourceAsStream("/codeC/" + nom);
                Path dest       = directory.resolve(nom);

                if(this.isMac() && ((nom.equals("programmeC.o") || nom.equals("codeNatif.o")))) {
                    src = getClass().getResourceAsStream("/codeC/mac/" + nom);
                }

                if(src != null) {
                    Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Crée un fichier avec le code C fourni.
     * 
     * @param codeC Le code C à écrire dans le fichier.
     */
    public void creerFichier(String codeC) {
        try {
            Path fichier = Paths.get("/tmp/twisk/client.c");
            Files.write(fichier, codeC.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compile le fichier client.c en générant un fichier objet client.o.
     */
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Construit la bibliothèque partagée à partir des fichiers objets.
     */
    public void construireLaBibliotheque() {
        String commande;

        if(this.isMac()) {
            commande = "gcc -dynamiclib /tmp/twisk/programmeC.o /tmp/twisk/codeNatif.o /tmp/twisk/client.o -o /tmp/twisk/libTwisk.so";
        } else {
            commande = "gcc -shared /tmp/twisk/programmeC.o /tmp/twisk/codeNatif.o /tmp/twisk/client.o -o /tmp/twisk/libTwisk.so";
        }

        ProcessBuilder pb = new ProcessBuilder(commande.split(" "));

        try {
            pb.inheritIO().start().waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprime le dossier temporaire utilisé pour l'environnement.
     */
    public void supprimerDossier() {
        String commande   = "rm -rf /tmp/twisk";

        ProcessBuilder pb = new ProcessBuilder(commande.split(" "));

        try {
            pb.inheritIO().start().waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tue tout les processus pour les PIDs fournis.
     *
     * @param pids Les processus à tuer.
     */
    public void killProcessus(int[] pids) {
        if (pids == null) { return; }

        for (int pid : pids) {
            String commande = "kill -9 " + pid;
            ProcessBuilder pb = new ProcessBuilder(commande.split(" "));

            try {
                pb.inheritIO().start().waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* —————————— METHODES PRIVEES —————————— */

    private boolean isMac() {
        // Check si l'utilisateur est riche ou pas
        String os = System.getProperty("os.name").toLowerCase();

        return os.contains("mac") || os.contains("darwin");
    }

}
