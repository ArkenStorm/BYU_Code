//Student ID, Name, Address, & Phone

#ifndef SNAP_H
#define SNAP_H

using namespace std;

class Snap
{
public:
	Snap(string inputID = "0000", string inputName = "none", string inputAddress = "none", string inputPhone = "none");
	string getName() const;
	string getID() const;
	string getAddress() const;
	string getPhone() const;
	string toString() const;
	friend std::ostream& operator<< (ostream& os, const Snap& snap)
	{
		os << snap.toString();
		return os;
	}
private:
	string studentID;
	string studentName;
	string studentAddress;
	string studentPhone;
};


#endif
