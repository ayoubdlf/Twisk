CC     = gcc
CFLAGS = -Wall -Wextra -Werror

all: run


output/client.o: client.c
	$(CC) $(CFLAGS) -fPIC -c $< -o $@

output/libTwisk.so: output/client.o ../ressources/codeC/programmeC.o
	$(CC) -shared -o $@ $^

output/main: output/client.o output/libTwisk.so
	$(CC) -Loutput main.c -o output/main -lTwisk

creer_output:
	mkdir output

clean:
	rm -rf output


run: clean creer_output output/main
	export LD_LIBRARY_PATH=$$LD_LIBRARY_PATH:output;\
	clear && output/main
