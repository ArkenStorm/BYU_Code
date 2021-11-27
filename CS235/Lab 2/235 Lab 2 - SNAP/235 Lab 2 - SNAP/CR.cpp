#include <iostream>
#include <string>
#include <limits>
#include <fstream>
#include <sstream>

#include "CR.h"
#include "Course.h"

using namespace std;

Cr::Cr(string className, string classRoom)
{
	courseName = className;
	courseRoom = classRoom;
}

string Cr::toString() const
{
	ostringstream out;
	out.clear();
	out << "cr(" << getCourseName() << ", " << getRoom() << ")." << endl;

	return out.str();
}

string Cr::getRoom() const
{
	return courseRoom;
}
