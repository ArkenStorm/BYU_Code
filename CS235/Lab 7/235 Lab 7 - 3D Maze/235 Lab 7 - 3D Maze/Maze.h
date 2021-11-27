#ifndef MAZE_H
#define MAZE_H
#include <string>
using std::string;

class Maze
{
public:
	enum CellValue { OPEN, BLOCKED, PATH, EXIT, TEMPORARY, LEFT, RIGHT, UP, DOWN, OUT, IN };

	Maze(int height, int width, int layers);
	~Maze();

	/** Set maze value
	@parm height
	@parm width
	@parm layer
	@parm value
	*/
	virtual void setValue(int height, int width, int layer, int value);

	/** Solve maze
	@return true if solveable, else false
	*/
	virtual bool find_maze_path();

	/** Output maze (same order as input maze)
	@return string of 2D layers
	*/
	virtual string toString() const;
	bool pathFinder(int height, int width, int layers);			//recursively finds the path


private:
	int mazeHeight;
	int mazeWidth;
	int mazeLayers;
	int ***maze3D;
};
#endif
