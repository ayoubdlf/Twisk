#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include "../ressources/codeC/def.h"

#define sasEntree 0
#define sasSortie 1
#define guichet1 2
#define activite1 3
#define guichet2 4
#define activite2 5


#define num_sem_guichet1 1
#define num_sem_guichet2 2



void simulation (int ids) {

    entrer(sasEntree);
    delai(2, 1);
    transfert(sasEntree, guichet1);

    P(ids, num_sem_guichet1);
        transfert(guichet1, activite1);
        delai(2, 1);
    V(ids, num_sem_guichet1);

    delai(2, 1);
    transfert(activite1, guichet2);

    P(ids, num_sem_guichet2);
        transfert(guichet2, activite2);
        delai(2, 1);
    V(ids, num_sem_guichet2);

    transfert(activite2, sasSortie);
}