CC     = gcc
CFLAGS = -Wall -Wextra -Werror

all: run


client.o: ../../../c/client.c
	$(CC) $(CFLAGS) -c $< -o $@

libTwisk.dylib: client.o programmeC.o
	$(CC) -dynamiclib -o $@ $^

main: client.o libTwisk.dylib
	$(CC) -L. ../../../c/main.c -o main -lTwisk

clean:
	rm -f libTwisk.dylib client.o main


run: clean main
	clear \
	export DYLD_LIBRARY_PATH=$$DYLD_LIBRARY_PATH:.;\
	clear && ./main
