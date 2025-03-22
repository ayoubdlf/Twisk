#include <time.h>
#include "../ressources/codeC/def.h"

#define ENTREE 0
#define SORTIE 1
#define ETAPE1 2
#define ETAPE2 3
#define SEM_ETAPE2 1
#define ETAPE3 4
#define SEM_ETAPE3 2
#define ETAPE4 5
#define ETAPE5 6

void simulation(int ids) {
	srand(time(NULL) + ids);

	entrer(ENTREE);
	delai(2, 1);
	transfert(ENTREE, ETAPE1);

	switch (rand() % 2) {
		case 0: {
			delai(2, 1);
			transfert(ETAPE1, ETAPE2);

			P(ids, SEM_ETAPE2);
				transfert(ETAPE2, ETAPE4);
				delai(2, 1);
			V(ids, SEM_ETAPE2);

			transfert(ETAPE4, SORTIE);

			break;
		}
		case 1: {
			delai(2, 1);
			transfert(ETAPE1, ETAPE3);

			P(ids, SEM_ETAPE3);
				transfert(ETAPE3, ETAPE5);
				delai(2, 1);
			V(ids, SEM_ETAPE3);

			transfert(ETAPE5, SORTIE);

			break;
		}
	}

}
