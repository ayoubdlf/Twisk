#define ENTREE 0
#define SORTIE 1
#define BALADE_AU_ZOO 2
#define ACCES_AU_TOBOGGAN 3
#define SEM_ACCES_AU_TOBOGGAN 1
#define TOBOGGAN 4
#include "../ressources/codeC/def.h"

void simulation(int ids) {

	entrer(ENTREE);
	delai(2, 1);
	transfert(ENTREE, BALADE_AU_ZOO);

	delai(6, 3);
	transfert(BALADE_AU_ZOO, ACCES_AU_TOBOGGAN);

	P(ids, SEM_ACCES_AU_TOBOGGAN);
	transfert(ACCES_AU_TOBOGGAN, TOBOGGAN);
	delai(4, 2);
	V(ids, SEM_ACCES_AU_TOBOGGAN);

	transfert(TOBOGGAN, SORTIE);

}
