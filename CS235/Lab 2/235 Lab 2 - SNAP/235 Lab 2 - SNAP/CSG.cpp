#include <iostream>
#include <string>
#include <limits>
#include <fstream>
#include <sstream>

#include "CSG.h"
#include "Course.h"

using namespace std;

Csg::Csg(string className, string inputID, string grade)
{
	studentID = inputID;
	studentGrade = grade;
}

string Csg::toString() const
{
	ostringstream out;
	out.clear();
	out << "csg(" << getCourseName() << "," << getID() << "," << getGrade() << ")." << endl;
	return out.str();
}

string Csg::getID() const
{
	return studentID;
}

string Csg::getGrade() const
{
	return studentGrade;
}