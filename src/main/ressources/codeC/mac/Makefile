CC     = gcc
CFLAGS = -Wall -Wextra -Werror
PATH_C = ../../../c

all: main


client.o: ../../../c/client.c
	$(CC) $(CFLAGS) -c $< -o $@

libTwisk.dylib: client.o programmeC.o
	$(CC) -dynamiclib -o $@ $^

main: client.o libTwisk.dylib
	export DYLD_LIBRARY_PATH=$$DYLD_LIBRARY_PATH:.; \
	$(CC) -L. ../../../c/main.c -o main -lTwisk

clean:
	rm -f libTwisk.dylib client.o main


run: clean all
	clear && ./main
