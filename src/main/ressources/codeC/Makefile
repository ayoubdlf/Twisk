CC     = gcc
CFLAGS = -Wall -Wextra -Werror -fPIC

all: client.o libTwisk.so

client.o: ../../c/client.c
	$(CC) $(CFLAGS) -c $< -o $@

libTwisk.so: client.o programmeC.o
	$(CC) -shared -o $@ $^