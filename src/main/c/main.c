#include <stdlib.h>
#include <stdio.h>
#include "../ressources/codeC/def.h"

#define NB_ETAPES   4
#define NB_GUICHETS 1
#define NB_CLIENTS  6


int main(int argc, char** argv) {
    int tabJetonsGuichet[] = { 1 };
    int* simulation        = start_simulation(NB_ETAPES, NB_GUICHETS, NB_CLIENTS, tabJetonsGuichet);

    nettoyage();

    return EXIT_SUCCESS;
}