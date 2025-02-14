#include<stdlib.h>
#include<stdio.h>
#include<time.h>

#include "def.h"

#define sasEntree 0
#define sasSortie 1
#define guichet 2
#define num_sum_guichet 1
#define activite 3


void simulation (int ids){

    entrer(sasEntree);
    delai(6,3);
    transfert(sasEntree , guichet);

    p(ids , num_sem_guichet);
        transfert(guichet , activitee);
        delai(8,2);
    V(ids , num_sem_guichet);

    treansfer(activite , sasSortie);
    //fonction qui simule le parcours d'un client dans le graphe
    //faire appel aux :
    /*
     *void delai(int temps, int delta) // 0 < delta < temps < 100
     *void entrer(int etape)
     *void transfert(int source, int destination)
    */

}