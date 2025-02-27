#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "../ressources/codeC/def.h"

#define sasEntree 0
#define sasSortie 1
#define guichet 2
#define num_sem_guichet 1
#define activite 3


void simulation (int ids) {

    entrer(sasEntree);
    delai(2, 1);
    transfert(sasEntree, guichet);

    P(ids, num_sem_guichet);
        transfert(guichet, activite);
        delai(2, 1);
    V(ids, num_sem_guichet);

    transfert(activite, sasSortie);
}
