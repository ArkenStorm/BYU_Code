#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
#include <utility>
#include "Set.h"
#include "UnorderedMap.h"

using namespace std;

#ifdef _MSC_VER
#define _CRTDBG_MAP_ALLOC  
#include <crtdbg.h>
#define VS_MEM_CHECK _CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
#else
#define VS_MEM_CHECK;
#endif

int main(int argc, char* argv[])
{
	VS_MEM_CHECK;

	ifstream in(argv[1]);
	ofstream out(argv[2]);





	return 0;
}