//Course, Day, Time

#ifndef CDH_H
#define CDH_H

#include "Course.h"
using namespace std;

class Cdh: public Course
{
public:
	Cdh(string className = "none", string days = "none", string time = "none");
	string toString() const;
	string getDays() const;
	string getTimes() const;
	friend std::ostream& operator<< (ostream& os, const Cdh& cdh)
	{
		os << cdh.toString();
		return os;
	}
private:
	string weekDays;
	string timeOfDay;
};

#endif