CC     = gcc
CFLAGS = -Wall -Wextra -Werror

all: client.o libTwisk.dylib

client.o: ../client.c
	$(CC) $(CFLAGS) -c $< -o $@

libTwisk.dylib: client.o programmeC.o
	$(CC) -dynamiclib -o $@ $^