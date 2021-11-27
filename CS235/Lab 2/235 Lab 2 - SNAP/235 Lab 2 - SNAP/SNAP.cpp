#include <iostream>
#include <string>
#include <limits>
#include <fstream>
#include <sstream>

#include "SNAP.h"
#include "Course.h"

using namespace std;

Snap::Snap(string inputID, string inputName, string inputAddress, string inputPhone)
{
	studentID = inputID;
	studentName = inputName;
	studentAddress = inputAddress;
	studentPhone = inputPhone;
}

string Snap::toString() const
{
	ostringstream out;
	out.clear();
	out << "snap(" << getID() << ", " << getName() << ", " << getAddress() << ", " << getPhone() << ")." << endl;

	return out.str();
}

string Snap::getName() const
{
	return studentName;
}

string Snap::getID() const
{
	return studentID;
}

string Snap::getAddress() const
{
	return studentAddress;
}

string Snap::getPhone() const
{
	return studentPhone;
}
