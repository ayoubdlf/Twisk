CC     = gcc
CFLAGS = -Wall -Wextra -Werror

all: run

ID=${id}

output/client${ID}.o: ../client${ID}.c
	$(CC) $(CFLAGS) -c $< -o $@

output/libTwisk.dylib: output/client${ID}.o ../../ressources/codeC/mac/programmeC.o
	$(CC) -dynamiclib -o $@ $^

output/main${ID}: output/client${ID}.o output/libTwisk.dylib
	$(CC) -Loutput ../main${ID}.c -o output/main${ID} -lTwisk

creer_output:
	mkdir output

clean:
	rm -rf output


run: clean creer_output output/main${ID}
	export DYLD_LIBRARY_PATH=$$DYLD_LIBRARY_PATH:output;\
	clear && ./output/main${ID}
