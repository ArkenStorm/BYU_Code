//Course, Room number

#ifndef CR_H
#define CR_H

#include "Course.h"

class Cr : public Course
{
public:
	Cr(string className = "none", string classRoom = "none");
	string toString() const;
	string getRoom() const;
	friend std::ostream& operator<< (std::ostream& os, const Cr& cr)
	{
		os << cr.toString();
		return os;
	}
private:
	string courseRoom;
};

#endif