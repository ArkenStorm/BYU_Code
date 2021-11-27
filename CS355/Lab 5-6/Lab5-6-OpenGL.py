import sys
import math
import time

try:
    from OpenGL.GLUT import *
    from OpenGL.GL import *
    from OpenGL.GLU import *
    from OpenGL.GL import glOrtho
    from OpenGL.GLU import gluPerspective
    from OpenGL.GL import glRotated
    from OpenGL.GL import glTranslated
    from OpenGL.GL import glLoadIdentity
    from OpenGL.GL import glMatrixMode
    from OpenGL.GL import GL_MODELVIEW
    from OpenGL.GL import GL_PROJECTION
except:
    print("ERROR: PyOpenGL not installed properly. ")

DISPLAY_WIDTH = 500.0
DISPLAY_HEIGHT = 500.0
translate_x = 25
translate_y = -3
translate_z = -20
view_angle = 0
view_mode = "perspective"
car_x_position = -25
car_z_position = 10
start_time = time.time()

def init():
    glClearColor(0.0, 0.0, 0.0, 0.0)
    glShadeModel(GL_FLAT)

def drawHouse():
    glLineWidth(2.5)
    glColor3f(1.0, 0.0, 0.0)
    #Floor
    glBegin(GL_LINES)
    glVertex3f(-5.0, 0.0, -5.0)
    glVertex3f(5, 0, -5)
    glVertex3f(5, 0, -5)
    glVertex3f(5, 0, 5)
    glVertex3f(5, 0, 5)
    glVertex3f(-5, 0, 5)
    glVertex3f(-5, 0, 5)
    glVertex3f(-5, 0, -5)
    #Ceiling
    glVertex3f(-5, 5, -5)
    glVertex3f(5, 5, -5)
    glVertex3f(5, 5, -5)
    glVertex3f(5, 5, 5)
    glVertex3f(5, 5, 5)
    glVertex3f(-5, 5, 5)
    glVertex3f(-5, 5, 5)
    glVertex3f(-5, 5, -5)
    #Walls
    glVertex3f(-5, 0, -5)
    glVertex3f(-5, 5, -5)
    glVertex3f(5, 0, -5)
    glVertex3f(5, 5, -5)
    glVertex3f(5, 0, 5)
    glVertex3f(5, 5, 5)
    glVertex3f(-5, 0, 5)
    glVertex3f(-5, 5, 5)
    #Door
    glVertex3f(-1, 0, 5)
    glVertex3f(-1, 3, 5)
    glVertex3f(-1, 3, 5)
    glVertex3f(1, 3, 5)
    glVertex3f(1, 3, 5)
    glVertex3f(1, 0, 5)
    #Roof
    glVertex3f(-5, 5, -5)
    glVertex3f(0, 8, -5)
    glVertex3f(0, 8, -5)
    glVertex3f(5, 5, -5)
    glVertex3f(-5, 5, 5)
    glVertex3f(0, 8, 5)
    glVertex3f(0, 8, 5)
    glVertex3f(5, 5, 5)
    glVertex3f(0, 8, 5)
    glVertex3f(0, 8, -5)
    glEnd()

def drawCar():
    glLineWidth(2.5)
    glColor3f(0.0, 1.0, 0.0)
    glBegin(GL_LINES)
    #Front Side
    glVertex3f(-3, 2, 2)
    glVertex3f(-2, 3, 2)
    glVertex3f(-2, 3, 2)
    glVertex3f(2, 3, 2)
    glVertex3f(2, 3, 2)
    glVertex3f(3, 2, 2)
    glVertex3f(3, 2, 2)
    glVertex3f(3, 1, 2)
    glVertex3f(3, 1, 2)
    glVertex3f(-3, 1, 2)
    glVertex3f(-3, 1, 2)
    glVertex3f(-3, 2, 2)
    #Back Side
    glVertex3f(-3, 2, -2)
    glVertex3f(-2, 3, -2)
    glVertex3f(-2, 3, -2)
    glVertex3f(2, 3, -2)
    glVertex3f(2, 3, -2)
    glVertex3f(3, 2, -2)
    glVertex3f(3, 2, -2)
    glVertex3f(3, 1, -2)
    glVertex3f(3, 1, -2)
    glVertex3f(-3, 1, -2)
    glVertex3f(-3, 1, -2)
    glVertex3f(-3, 2, -2)
    #Connectors
    glVertex3f(-3, 2, 2)
    glVertex3f(-3, 2, -2)
    glVertex3f(-2, 3, 2)
    glVertex3f(-2, 3, -2)
    glVertex3f(2, 3, 2)
    glVertex3f(2, 3, -2)
    glVertex3f(3, 2, 2)
    glVertex3f(3, 2, -2)
    glVertex3f(3, 1, 2)
    glVertex3f(3, 1, -2)
    glVertex3f(-3, 1, 2)
    glVertex3f(-3, 1, -2)
    glEnd()

def drawTire():
    glLineWidth(2.5)
    glColor3f(0.0, 0.0, 1.0)
    glBegin(GL_LINES)
    #Front Side
    glVertex3f(-1, .5, .5)
    glVertex3f(-.5, 1, .5)
    glVertex3f(-.5, 1, .5)
    glVertex3f(.5, 1, .5)
    glVertex3f(.5, 1, .5)
    glVertex3f(1, .5, .5)
    glVertex3f(1, .5, .5)
    glVertex3f(1, -.5, .5)
    glVertex3f(1, -.5, .5)
    glVertex3f(.5, -1, .5)
    glVertex3f(.5, -1, .5)
    glVertex3f(-.5, -1, .5)
    glVertex3f(-.5, -1, .5)
    glVertex3f(-1, -.5, .5)
    glVertex3f(-1, -.5, .5)
    glVertex3f(-1, .5, .5)
    #Back Side
    glVertex3f(-1, .5, -.5)
    glVertex3f(-.5, 1, -.5)
    glVertex3f(-.5, 1, -.5)
    glVertex3f(.5, 1, -.5)
    glVertex3f(.5, 1, -.5)
    glVertex3f(1, .5, -.5)
    glVertex3f(1, .5, -.5)
    glVertex3f(1, -.5, -.5)
    glVertex3f(1, -.5, -.5)
    glVertex3f(.5, -1, -.5)
    glVertex3f(.5, -1, -.5)
    glVertex3f(-.5, -1, -.5)
    glVertex3f(-.5, -1, -.5)
    glVertex3f(-1, -.5, -.5)
    glVertex3f(-1, -.5, -.5)
    glVertex3f(-1, .5, -.5)
    #Connectors
    glVertex3f(-1, .5, .5)
    glVertex3f(-1, .5, -.5)
    glVertex3f(-.5, 1, .5)
    glVertex3f(-.5, 1, -.5)
    glVertex3f(.5, 1, .5)
    glVertex3f(.5, 1, -.5)
    glVertex3f(1, .5, .5)
    glVertex3f(1, .5, -.5)
    glVertex3f(1, -.5, .5)
    glVertex3f(1, -.5, -.5)
    glVertex3f(.5, -1, .5)
    glVertex3f(.5, -1, -.5)
    glVertex3f(-.5, -1, .5)
    glVertex3f(-.5, -1, -.5)
    glVertex3f(-1, -.5, .5)
    glVertex3f(-1, -.5, -.5)
    glEnd()

def display():
    glClear (GL_COLOR_BUFFER_BIT)
    glColor3f (1.0, 1.0, 1.0)
    # viewing transformation


    #Your Code Here
    glLoadIdentity()
    if view_mode == "perspective":
        gluPerspective(90, 1, .1, 50)
    else:
        glOrtho(-10, 10, -10, 10, 0.1, 50.0)
    glRotated(view_angle, 0.0, 1.0, 0.0)  # this rotates the y axis by the view_angle
    glTranslatef(translate_x, translate_y, translate_z)

    draw_neighborhood()
    draw_full_car()

    glFlush()


def keyboard(key, x, y):
    global translate_x
    global translate_y
    global translate_z
    global view_angle
    global view_mode
    global car_x_position
    global car_z_position
    global start_time

    if key == chr(27):
        import sys
        sys.exit(0)

    if key == b'a':
        translate_x += math.cos(math.radians(view_angle))
        translate_z += math.sin(math.radians(view_angle))
    elif key == b'd':
        translate_x -= math.cos(math.radians(view_angle))
        translate_z -= math.sin(math.radians(view_angle))
    elif key == b'w':
        translate_z += math.cos(math.radians(view_angle))
        translate_x -= math.sin(math.radians(view_angle))
    elif key == b's':
        translate_z -= math.cos(math.radians(view_angle))
        translate_x += math.sin(math.radians(view_angle))
    elif key == b'q':
        view_angle -= 1
    elif key == b'e':
        view_angle += 1
    elif key == b'r':
        translate_y -= 1
    elif key == b'f':
        translate_y += 1
    elif key == b'h':
        translate_x = 25
        translate_y = -3
        translate_z = -20
        view_angle = 0
        car_x_position = -25
        car_z_position = 10
        start_time = time.time()
    elif key == b'o':
        view_mode = "orthographic"
    elif key == b'p':
        view_mode = "perspective"

    glutPostRedisplay()


def display_proxy(a):
    display()
    glutTimerFunc(16, display_proxy, 0)


def draw_house_row(mirror=False):
    for i in range(3):
        glPushMatrix()
        z_placement = 30 if mirror else 0
        rotation = 180 if mirror else 0
        glTranslated(-i*15, 0, z_placement)
        glRotated(rotation, 0, 1, 0)
        drawHouse()
        glPopMatrix()


def draw_edge_house():
    glPushMatrix()
    glTranslated(-40, 0, 15)
    glRotated(90, 0, 1, 0)
    drawHouse()
    glPopMatrix()


def draw_neighborhood():
    draw_house_row()
    draw_house_row(True)
    draw_edge_house()


def draw_tires():
    anim_time = get_time()
    x_car_offset = 2.0
    z_car_offset = 1.5
    for i in range(2):
        for j in range(2):
            glPushMatrix()
            x_placement = x_car_offset if i == 1 else -x_car_offset
            z_placement = z_car_offset if j == 1 else -z_car_offset
            glTranslated(x_placement, 0, z_placement)
            glRotated(-50 * anim_time, 0, 0, 1)
            drawTire()
            glPopMatrix()


def get_time():
    return time.time() - start_time


def draw_full_car():
    anim_time = get_time()
    glPushMatrix()
    glTranslated(car_x_position + anim_time, 0, car_z_position)
    drawCar()
    draw_tires()
    glPopMatrix()



glutInit(sys.argv)
glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB)
glutInitWindowSize(int(DISPLAY_WIDTH), int(DISPLAY_HEIGHT))
glutInitWindowPosition(100, 100)
glutCreateWindow(b'OpenGL Lab')
init()
glutDisplayFunc(display)
glutKeyboardFunc(keyboard)
glutTimerFunc(16, display_proxy, 0)
glutMainLoop()
