CC     = gcc
CFLAGS = -Wall -Wextra -Werror -fPIC

all: client.o libTwisk.so

client.o: ../../c/client.c
	$(CC) $(CFLAGS) -c $< -o $@

libTwisk.so: client.o programmeC.o
	$(CC) -shared -o $@ $^


main: all
	export LD_LIBRARY_PATH=$$LD_LIBRARY_PATH:.; \
	# $(CC) -Wall -L. ../../../c/main.c -o main -lTwisk
	$(CC) -L. ../../../c/main.c -o main -lTwisk