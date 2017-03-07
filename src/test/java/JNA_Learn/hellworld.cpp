#include<stdio.h>
#include <stdlib.h>
#include "hellworld.h"

BufferTrance::BufferTrance()
{
    this->savebyte = NULL;
}

char* BufferTrance::getSavebyte()
{
	return this->savebyte; // null
}

int BufferTrance::setSavebyte(char* inbyte)
{
    this->savebyte = inbyte;
	return 1;
}

BufferTrance::~BufferTrance()
{
    free(this->savebyte);
}
