OS := $(shell uname)
CC = gcc

ifeq ($(OS), Darwin)
    CFLAGS = -Wall -Wextra -Werror
    LDLAGS = -dynamiclib
    PATH   = ./mac
else
    CFLAGS = -Wall -Wextra -Werror -fPIC
    LDLAGS = -shared
    PATH   = .
endif

all: client.o libTwisk.dylib

client.o: client.c
	$(CC) $(CFLAGS) -c $< -o $@

libTwisk.dylib: client.o $(PATH)/programmeC.o
	$(CC) $(LDLAGS) -o $@ $^