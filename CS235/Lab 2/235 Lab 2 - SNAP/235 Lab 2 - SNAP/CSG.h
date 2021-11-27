//Course, Student ID, Grade

#ifndef CSGH_H
#define CSG_H

#include "Course.h"
using namespace std;

class Csg : public Course
{
public:
	Csg(string className = "none", string inputID = "0000", string grade = "none");
	string toString() const;
	string getID() const;
	string getGrade() const;
	friend std::ostream& operator<< (ostream& os, const Csg& csg)
	{
		os << csg.toString();
		return os;
	}
private:
	string studentID;
	string studentGrade;
};

#endif