#include <stdlib.h>
#include <stdio.h>
#include "../ressources/codeC/def.h"

#define NB_ETAPES   4
#define NB_GUICHETS 1
#define NB_CLIENTS  6


int main(int argc, char** argv) {
    int tabJetonsGuichet[] = { 1 };
    int* simulation        = start_simulation(NB_ETAPES, NB_GUICHETS, NB_CLIENTS, tabJetonsGuichet);

    int* position = ou_sont_les_clients(NB_ETAPES , NB_CLIENTS);

    for(int i = 0 ; i < NB_ETAPES ; i++) {
        printf("Etape: %d", position[i]);
        for(int j = 0 ; j < (int)position[0] ; j++) {
            printf("%d, ", position[i+j+1]);
        }
        printf("\n");
    }

    nettoyage();

    return EXIT_SUCCESS;
}