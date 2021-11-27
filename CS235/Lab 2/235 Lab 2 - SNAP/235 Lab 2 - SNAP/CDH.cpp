#include <iostream>
#include <string>
#include <limits>
#include <fstream>
#include <sstream>

#include "CDH.h"
#include "Course.h"

using namespace std;

Cdh::Cdh(string className, string days, string time)
{
	courseName = className;
	weekDays = days;
	timeOfDay = time;
}

string Cdh::toString() const
{
	ostringstream out;
	out.clear();
	out << "cdh(" << getCourseName() << ", " << getDays() << ", " << getTimes() << ")." << endl;

	return out.str();
}

string Cdh::getDays() const
{
	return weekDays;
}

string Cdh::getTimes() const
{
	return timeOfDay;
}