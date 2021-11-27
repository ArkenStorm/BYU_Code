#ifndef COURSE_H
#define COURSE_H

using namespace std;

class Course
{
public:
	Course(string inputName = "none");
	string courseName;
	virtual string toString() const = 0;
	string getCourseName() const;
	friend ostream& operator<< (ostream& os, const Course& course)
	{
		os << course.toString();
		return os;
	}
};

#endif
