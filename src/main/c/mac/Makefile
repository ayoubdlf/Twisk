CC     = gcc
CFLAGS = -Wall -Wextra -Werror

all: run


output/client.o: ../client.c
	$(CC) $(CFLAGS) -c $< -o $@

output/libTwisk.dylib: output/client.o ../../ressources/codeC/mac/programmeC.o
	$(CC) -dynamiclib -o $@ $^

output/main: output/client.o output/libTwisk.dylib
	$(CC) -Loutput main.c -o output/main -lTwisk

creer_output:
	mkdir output

clean:
	rm -rf output


run: clean creer_output output/main
	export DYLD_LIBRARY_PATH=$$DYLD_LIBRARY_PATH:.;\
	clear && ./main
