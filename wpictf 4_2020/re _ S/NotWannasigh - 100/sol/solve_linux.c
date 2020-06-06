#include <stdlib.h>
#include <stdio.h>
#include <time.h>

int main(){
	FILE * stream;
	stream = fopen("flag-gif.EnCiPhErEd","r+");
	FILE * out;
	out = fopen("a.gif","w+");

	fseek(stream, 0LL, 2);
	int len = ftell(stream);
	fseek(stream, 0LL, 0);

	srand(1585599106);
	for (int i = 0; i < len; i++){

		char c = fgetc(stream);
		int temp = rand();

		temp = (temp & 0xFF) ^ c;
		fwrite(&temp, 1, 1, out);
	}

	fclose(stream);
	fclose(out);
}

