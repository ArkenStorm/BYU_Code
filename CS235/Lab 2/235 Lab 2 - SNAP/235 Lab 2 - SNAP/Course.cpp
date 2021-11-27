#include <iostream>
#include <string>
#include <limits>
#include <fstream>
#include <sstream>

#include "Course.h"

using namespace std;

Course::Course(string inputName)
{
	courseName = inputName;
}

string Course::toString() const
{
	return "reer";
}

string Course::getCourseName() const
{
	return courseName;
}