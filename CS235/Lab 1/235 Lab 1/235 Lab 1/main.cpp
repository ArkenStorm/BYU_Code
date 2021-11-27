#include <iostream>
#include <string>
#include <fstream>
#include <stdlib.h>
#include <iomanip>
#include <limits>
#include <sstream>

using namespace std;

#ifdef _MSC_VER
#define _CRTDBG_MAP_ALLOC  
#include <crtdbg.h>
#define VS_MEM_CHECK _CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF | _CRTDBG_LEAK_CHECK_DF);
#else
#define VS_MEM_CHECK
#endif

string GradeFinder(int grade, double classAverage);

int main(int argc, char* argv[])
{
	VS_MEM_CHECK;

	int numStudents = 0;
	int numExams = 0;
	int numAs = 0;
	int numBs = 0;
	int numCs = 0;
	int numDs = 0;
	int numEs = 0;
	int examNumber = 0;
	double gradeAverage = 0.0;
	double allExamAverages = 0.0;
	double tempSum = 0.0;
	double totalStudentAverage = 0.0;
	double* examAverages = nullptr;
	string* studentNames = nullptr;
	string studentAndScores;
	string studentName;
	string tempInfo;
	istringstream scoresISS;
	//ostream& outputOSS = cout;

	if (argc < 3)
	{
		cerr << "Please provide name of input and output files";
		return 1;
	}
	cout << "Input file: " << argv[1] << endl;
	ifstream in(argv[1]);
	if (!in)
	{
		cerr << "Unable to open " << argv[1] << " for input";
		return 2;
	}

	cout << "Output file: " << argv[2] << endl;
	ofstream out(argv[2]);
	if (!out)
	{
		in.close();
		cerr << "Unable to open " << argv[2] << " for output";
		return 3;
	}

	in >> numStudents >> numExams;
	in.ignore(std::numeric_limits<int>::max(), '\n');

	studentNames = new string[numStudents];
	examAverages = new double[numExams];

	int **studentArray = new int*[numStudents];
	for (int i = 0; i < numStudents; ++i)
	{
		studentArray[i] = new int[numExams];
	}

	for (int i = 0; i < numStudents; ++i)
	{
		getline(in, studentAndScores);
		size_t p = 0;
		while (!isdigit(studentAndScores[p]))
		{
			++p;
		}

		studentName = studentAndScores.substr(0, p);
		studentNames[i] = studentName;

		tempInfo = studentAndScores.substr(p);
		scoresISS.clear();
		scoresISS.str(tempInfo);

		for (int j = 0; j < numExams; ++j)
		{
			scoresISS >> studentArray[i][j];
		}
	}

	out << "Exam Averages: " << endl;

	for (int i = 0; i < numExams; ++i)
	{
		gradeAverage = 0.0;
		tempSum = 0.0;
		numAs = 0;
		numBs = 0;
		numCs = 0;
		numDs = 0;
		numEs = 0;

		out << "Exam " << i + 1 << " average = ";
		for (int k = 0; k < numStudents; ++k)
		{
			tempSum += studentArray[k][examNumber];
		}

		gradeAverage = tempSum / numStudents;
		examAverages[i] = gradeAverage;

		for (int k = 0; k < numStudents; ++k)
		{
			//increments number of each letter grade
			if (GradeFinder(studentArray[k][examNumber], gradeAverage) == "A")
			{
				++numAs;
			}

			else if (GradeFinder(studentArray[k][examNumber], gradeAverage) == "B")
			{
				++numBs;
			}

			else if (GradeFinder(studentArray[k][examNumber], gradeAverage) == "C")
			{
				++numCs;
			}

			else if (GradeFinder(studentArray[k][examNumber], gradeAverage) == "D")
			{
				++numDs;
			}

			else if (GradeFinder(studentArray[k][examNumber], gradeAverage) == "E")
			{
				++numEs;
			}
		}

		out << fixed << setprecision(1) << gradeAverage << setw(8) << numAs << "(A)" << setw(8) << numBs << "(B)" << setw(8) << numCs << "(C)";
		out << setw(8) << numDs << "(D)" << setw(8) << numEs << "(E)" << endl;

		++examNumber;
	}

	out << endl << "Student Exam Grades: " << endl;

	for (int i = 0; i < numStudents; ++i)
	{
		out << fixed << setprecision(0) << setw(20) << studentNames[i] << " ";

		for (int j = 0; j < numExams; ++j)
		{
			out << setw(8) << studentArray[i][j] << "(" << GradeFinder(studentArray[i][j], examAverages[j]) << ")";
		}
		out << endl;
	}

	tempSum = 0.0;
	for (int i = 0; i < numExams; ++i)
	{
		tempSum += examAverages[i];
	}
	allExamAverages = tempSum / numExams;

	out << endl << "**BONUS**" << endl;
	out << "Class Average = " << fixed << setprecision(1) << allExamAverages << endl;
	out << "Student Final Exam Grade: " << endl;

	for (int i = 0; i < numStudents; ++i)
	{
		out << fixed << setprecision(1) << setw(20) << studentNames[i] << "  ";

		totalStudentAverage = 0.0;
		for (int j = 0; j < numExams; ++j)
		{
			totalStudentAverage += studentArray[i][j];
		}
		totalStudentAverage /= numExams;

		out << totalStudentAverage << "(" << GradeFinder(totalStudentAverage, allExamAverages) << ")";
		out << endl;
	}


	for (int i = 0; i < numStudents; ++i)
	{
		delete[] studentArray[i];
	}
	delete[] studentArray;

	delete[] studentNames;
	delete[] examAverages;
	
	return 0;
}

string GradeFinder(int grade, double classAverage)
{
	string letterGrade;

	if (grade >= classAverage + 15)
	{
		letterGrade = "A";
	}

	else if (grade > classAverage + 5 && grade < classAverage + 15)
	{
		letterGrade = "B";
	}

	else if (grade <= classAverage + 5 && grade >= classAverage - 5)
	{
		letterGrade = "C";
	}

	else if (grade < classAverage - 5 && grade > classAverage - 15)
	{
		letterGrade = "D";
	}

	else if (grade <= classAverage - 15)
	{
		letterGrade = "E";
	}

	return letterGrade;
}