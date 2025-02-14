CC     = gcc
CFLAGS = -Wall -Wextra -Werror
PATH_C = ../../../c

all: client.o libTwisk.dylib

client.o: ../../../c/client.c
	$(CC) $(CFLAGS) -c $< -o $@

libTwisk.dylib: client.o programmeC.o
	$(CC) -dynamiclib -o $@ $^

main: all
	export DYLD_LIBRARY_PATH=$$DYLD_LIBRARY_PATH:.; \
	# $(CC) -Wall -L. ../../../c/main.c -o main -lTwisk
	$(CC) -L. ../../../c/main.c -o main -lTwisk