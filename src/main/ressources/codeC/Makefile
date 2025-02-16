CC     = gcc
CFLAGS = -Wall -Wextra -Werror

all: run


client.o: ../../c/client.c
	$(CC) $(CFLAGS) -c $< -o $@

libTwisk.so: client.o programmeC.o
	$(CC) -shared -o $@ $^

main: client.o libTwisk.so
	$(CC) -L. ../../c/main.c -o main -lTwisk

clean:
	rm -f libTwisk.so client.o main


run: clean main
	export LD_LIBRARY_PATH=$$LD_LIBRARY_PATH:.;\
	clear && ./main
