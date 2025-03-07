#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include "../ressources/codeC/def.h"

#define NB_ETAPES   4
#define NB_GUICHETS 1
#define NB_CLIENTS  6



char* getNomEtape(int etape) {
    switch(etape) {
        case 0 : return "entree";
        case 1 : return "sortie";
        case 2 : return "guichet";
        case 3 : return "activite";

        default: return "idk";
    }
}

int estTouteEtapeDansSortie(int* positions) {
    if (positions == NULL) { return EXIT_FAILURE; }

    int nbClientsSortie = positions[1 * (NB_CLIENTS + 1)]; // on sait que par default la sortie est l'etape numero 1 (entree: 0, sortie: 1, etc..)

    return (nbClientsSortie == NB_CLIENTS) ? EXIT_SUCCESS : EXIT_FAILURE;
}

void afficherListeClients() {
    int* positions = ou_sont_les_clients(NB_ETAPES , NB_CLIENTS);

    printf("\nles clients : ");

    for (int i = 0; i < NB_CLIENTS; i++) {
        printf("%d%s", positions[i+1], (i < NB_CLIENTS - 1) ? ", " : ""); // Si on est dans le dernier client, n'affiche pas la virgule
    }

    printf("\n");
}

void afficherPositionClients() {
    int* positions = NULL;

    while(estTouteEtapeDansSortie(positions) == EXIT_FAILURE) {
        positions = ou_sont_les_clients(NB_ETAPES , NB_CLIENTS);

        for (int nbEtape = 0; nbEtape < NB_ETAPES; nbEtape++) {
            int nbClients = positions[nbEtape * (NB_CLIENTS + 1)];

            printf("etape %d (%s) %d clients: ", nbEtape, getNomEtape(nbEtape), nbClients);

            for (int i = 0; i < nbClients; i++) {
                int client = positions[nbEtape * (NB_CLIENTS + 1) + (i+1)];
                printf("%d%s", client, ((i+1) < nbClients) ? ", " : ""); // Si on est dans le dernier client, n'affiche pas la virgule
            }

            printf("\n");
        }

        printf("———————————————————————————————————————————————————————————————————————————————\n");

        sleep(1);
    }
}


int main(int argc, char** argv) {
    int tabJetonsGuichet[] = { 1 };
    int* simulation        = start_simulation(NB_ETAPES, NB_GUICHETS, NB_CLIENTS, tabJetonsGuichet);

    afficherListeClients();
    afficherPositionClients();

    nettoyage();

    return EXIT_SUCCESS;
}