# Import a library of functions called 'pygame'
import pygame
import numpy as np
from math import radians as r
from math import pi


class Point:
	def __init__(self, x, y):
		self.x = x
		self.y = y


class Point3D:
	def __init__(self, x, y, z):
		self.x = x
		self.y = y
		self.z = z


class Line3D():
	def __init__(self, start, end):
		self.start = start
		self.end = end


size = [512, 512]
car_x_position = 5
car_z_position = 15
camera_angle = 0
camera_coords = Point3D(0, 3, -15)
fov = r(60)
zoom_x = 1/np.tan(fov/2)
zoom_y = zoom_x * (size[0] / size[1])
near = 0.1
far = 1000
# Set the height and width of the screen
screen = pygame.display.set_mode(size)


def load_obj(filename):
	vertices = []
	indices = []
	lines = []

	f = open(filename, "r")
	for line in f:
		t = str.split(line)
		if not t:
			continue
		if t[0] == "v":
			vertices.append(Point3D(float(t[1]), float(t[2]), float(t[3])))

		if t[0] == "f":
			for i in range(1, len(t) - 1):
				index1 = int(str.split(t[i], "/")[0])
				index2 = int(str.split(t[i + 1], "/")[0])
				indices.append((index1, index2))

	f.close()

	# Add faces as lines
	for index_pair in indices:
		index1 = index_pair[0]
		index2 = index_pair[1]
		lines.append(Line3D(vertices[index1 - 1], vertices[index2 - 1]))

	# Find duplicates
	duplicates = []
	for i in range(len(lines)):
		for j in range(i + 1, len(lines)):
			line1 = lines[i]
			line2 = lines[j]

			# Case 1 -> Starts match
			if line1.start.x == line2.start.x and line1.start.y == line2.start.y and line1.start.z == line2.start.z:
				if line1.end.x == line2.end.x and line1.end.y == line2.end.y and line1.end.z == line2.end.z:
					duplicates.append(j)
			# Case 2 -> Start matches end
			if line1.start.x == line2.end.x and line1.start.y == line2.end.y and line1.start.z == line2.end.z:
				if line1.end.x == line2.start.x and line1.end.y == line2.start.y and line1.end.z == line2.start.z:
					duplicates.append(j)

	duplicates = list(set(duplicates))
	duplicates.sort()
	duplicates = duplicates[::-1]

	# Remove duplicates
	for j in range(len(duplicates)):
		del lines[duplicates[j]]

	return lines


def load_house():
	house = []
	# Floor
	house.append(Line3D(Point3D(-5, 0, -5), Point3D(5, 0, -5)))
	house.append(Line3D(Point3D(5, 0, -5), Point3D(5, 0, 5)))
	house.append(Line3D(Point3D(5, 0, 5), Point3D(-5, 0, 5)))
	house.append(Line3D(Point3D(-5, 0, 5), Point3D(-5, 0, -5)))
	# Ceiling
	house.append(Line3D(Point3D(-5, 5, -5), Point3D(5, 5, -5)))
	house.append(Line3D(Point3D(5, 5, -5), Point3D(5, 5, 5)))
	house.append(Line3D(Point3D(5, 5, 5), Point3D(-5, 5, 5)))
	house.append(Line3D(Point3D(-5, 5, 5), Point3D(-5, 5, -5)))
	# Walls
	house.append(Line3D(Point3D(-5, 0, -5), Point3D(-5, 5, -5)))
	house.append(Line3D(Point3D(5, 0, -5), Point3D(5, 5, -5)))
	house.append(Line3D(Point3D(5, 0, 5), Point3D(5, 5, 5)))
	house.append(Line3D(Point3D(-5, 0, 5), Point3D(-5, 5, 5)))
	# Door
	house.append(Line3D(Point3D(-1, 0, 5), Point3D(-1, 3, 5)))
	house.append(Line3D(Point3D(-1, 3, 5), Point3D(1, 3, 5)))
	house.append(Line3D(Point3D(1, 3, 5), Point3D(1, 0, 5)))
	# Roof
	house.append(Line3D(Point3D(-5, 5, -5), Point3D(0, 8, -5)))
	house.append(Line3D(Point3D(0, 8, -5), Point3D(5, 5, -5)))
	house.append(Line3D(Point3D(-5, 5, 5), Point3D(0, 8, 5)))
	house.append(Line3D(Point3D(0, 8, 5), Point3D(5, 5, 5)))
	house.append(Line3D(Point3D(0, 8, 5), Point3D(0, 8, -5)))

	return house


def load_car():
	car = []
	# Front Side
	car.append(Line3D(Point3D(-3, 2, 2), Point3D(-2, 3, 2)))
	car.append(Line3D(Point3D(-2, 3, 2), Point3D(2, 3, 2)))
	car.append(Line3D(Point3D(2, 3, 2), Point3D(3, 2, 2)))
	car.append(Line3D(Point3D(3, 2, 2), Point3D(3, 1, 2)))
	car.append(Line3D(Point3D(3, 1, 2), Point3D(-3, 1, 2)))
	car.append(Line3D(Point3D(-3, 1, 2), Point3D(-3, 2, 2)))

	# Back Side
	car.append(Line3D(Point3D(-3, 2, -2), Point3D(-2, 3, -2)))
	car.append(Line3D(Point3D(-2, 3, -2), Point3D(2, 3, -2)))
	car.append(Line3D(Point3D(2, 3, -2), Point3D(3, 2, -2)))
	car.append(Line3D(Point3D(3, 2, -2), Point3D(3, 1, -2)))
	car.append(Line3D(Point3D(3, 1, -2), Point3D(-3, 1, -2)))
	car.append(Line3D(Point3D(-3, 1, -2), Point3D(-3, 2, -2)))

	# Connectors
	car.append(Line3D(Point3D(-3, 2, 2), Point3D(-3, 2, -2)))
	car.append(Line3D(Point3D(-2, 3, 2), Point3D(-2, 3, -2)))
	car.append(Line3D(Point3D(2, 3, 2), Point3D(2, 3, -2)))
	car.append(Line3D(Point3D(3, 2, 2), Point3D(3, 2, -2)))
	car.append(Line3D(Point3D(3, 1, 2), Point3D(3, 1, -2)))
	car.append(Line3D(Point3D(-3, 1, 2), Point3D(-3, 1, -2)))

	return car


def load_tire():
	tire = []
	# Front Side
	tire.append(Line3D(Point3D(-1, .5, .5), Point3D(-.5, 1, .5)))
	tire.append(Line3D(Point3D(-.5, 1, .5), Point3D(.5, 1, .5)))
	tire.append(Line3D(Point3D(.5, 1, .5), Point3D(1, .5, .5)))
	tire.append(Line3D(Point3D(1, .5, .5), Point3D(1, -.5, .5)))
	tire.append(Line3D(Point3D(1, -.5, .5), Point3D(.5, -1, .5)))
	tire.append(Line3D(Point3D(.5, -1, .5), Point3D(-.5, -1, .5)))
	tire.append(Line3D(Point3D(-.5, -1, .5), Point3D(-1, -.5, .5)))
	tire.append(Line3D(Point3D(-1, -.5, .5), Point3D(-1, .5, .5)))

	# Back Side
	tire.append(Line3D(Point3D(-1, .5, -.5), Point3D(-.5, 1, -.5)))
	tire.append(Line3D(Point3D(-.5, 1, -.5), Point3D(.5, 1, -.5)))
	tire.append(Line3D(Point3D(.5, 1, -.5), Point3D(1, .5, -.5)))
	tire.append(Line3D(Point3D(1, .5, -.5), Point3D(1, -.5, -.5)))
	tire.append(Line3D(Point3D(1, -.5, -.5), Point3D(.5, -1, -.5)))
	tire.append(Line3D(Point3D(.5, -1, -.5), Point3D(-.5, -1, -.5)))
	tire.append(Line3D(Point3D(-.5, -1, -.5), Point3D(-1, -.5, -.5)))
	tire.append(Line3D(Point3D(-1, -.5, -.5), Point3D(-1, .5, -.5)))

	# Connectors
	tire.append(Line3D(Point3D(-1, .5, .5), Point3D(-1, .5, -.5)))
	tire.append(Line3D(Point3D(-.5, 1, .5), Point3D(-.5, 1, -.5)))
	tire.append(Line3D(Point3D(.5, 1, .5), Point3D(.5, 1, -.5)))
	tire.append(Line3D(Point3D(1, .5, .5), Point3D(1, .5, -.5)))
	tire.append(Line3D(Point3D(1, -.5, .5), Point3D(1, -.5, -.5)))
	tire.append(Line3D(Point3D(.5, -1, .5), Point3D(.5, -1, -.5)))
	tire.append(Line3D(Point3D(-.5, -1, .5), Point3D(-.5, -1, -.5)))
	tire.append(Line3D(Point3D(-1, -.5, .5), Point3D(-1, -.5, -.5)))

	return tire


def point3d_to_homogenous(point):
	return np.array([[point.x], [point.y], [point.z], [1]])


def world_to_camera(world_coords, cam_coords, cam_angle):
	return rotate(-cam_angle)*translate(-cam_coords.x, -cam_coords.y, -cam_coords.z)*world_coords


def camera_clip(cam_coords):
	return clip()@cam_coords


def translate(cx, cy, cz):
	return np.matrix([[1, 0, 0, cx],
					  [0, 1, 0, cy],
					  [0, 0, 1, cz],
					  [0, 0, 0, 1]])


def rotate(angle):
	angle = r(angle)
	return np.matrix([[np.cos(angle), 0, -np.sin(angle), 0],
					  [0, 1, 0, 0],
					  [np.sin(angle), 0, np.cos(angle), 0],
					  [0, 0, 0, 1]])


def clip():
	return np.matrix([[zoom_x, 0, 0, 0],
					  [0, zoom_y, 0, 0],
					  [0, 0, (far + near) / (far - near), (-2*near*far) / (far - near)],
					  [0, 0, 1, 0]])


def to_screen_space(point, w):
	screen_transform = np.array([[size[0] / 2, 0, size[0] / 2],
								  [0, -size[1] / 2, size[1] / 2],
								  [0, 0, 1]])
	point_transform = np.array([[point.x / w], [point.y / w], [1]])
	screen_point = screen_transform@point_transform
	return Point(screen_point[0,0], screen_point[1,0])


def transform_line(line):  # line points already transformed in world coordinates
	point_start = point3d_to_homogenous(line.start)
	point_end = point3d_to_homogenous(line.end)
	start_camera = world_to_camera(point_start, camera_coords, camera_angle)
	end_camera = world_to_camera(point_end, camera_coords, camera_angle)
	clip_start = camera_clip(start_camera)
	clip_end = camera_clip(end_camera)
	start_w = clip_start[3,0]
	end_w = clip_end[3,0]

	if ((clip_start[0,0] > start_w and clip_end[0,0] > end_w) or
			(clip_start[0,0] < -start_w and clip_end[0,0] < -end_w) or
			(clip_start[1,0] > start_w and clip_end[1,0] > end_w) or
			(clip_start[1,0] < -start_w and clip_end[1,0] < -end_w) or
			(clip_start[2,0] > start_w and clip_end[2,0] > end_w) or
			(clip_start[2,0] < -start_w or clip_end[2,0] < -end_w)):
		return None
	start_screen = to_screen_space(Point(clip_start[0,0], clip_start[1,0]), start_w)
	end_screen = to_screen_space(Point(clip_end[0,0], clip_end[1,0]), end_w)
	return Line3D(start_screen, end_screen)


def draw_house_row(mirror=False):
	z_placement = 30 if mirror else 0
	rotation = 180 if mirror else 0
	for i in range(3):
		house_lines = load_house()
		for line in house_lines:
			start_transformed_coords = translate(i*15, 0, z_placement) @ rotate(rotation) @ point3d_to_homogenous(line.start)
			line.start = Point3D(start_transformed_coords[0,0], start_transformed_coords[1,0], start_transformed_coords[2,0])

			end_transformed_coords = translate(i * 15, 0, z_placement) @ rotate(rotation) @ point3d_to_homogenous(line.end)
			line.end = Point3D(end_transformed_coords[0,0], end_transformed_coords[1,0], end_transformed_coords[2,0])

			line = transform_line(line)
			if line is not None:
				pygame.draw.line(screen, RED, (int(line.start.x), int(line.start.y)), (int(line.end.x), int(line.end.y)))


def draw_edge_house():
	house_lines = load_house()
	for line in house_lines:
		start_transformed_coords = translate(-10, 0, 15)@rotate(90)@point3d_to_homogenous(line.start)
		line.start = Point3D(start_transformed_coords[0,0], start_transformed_coords[1,0], start_transformed_coords[2,0])

		end_transformed_coords = translate(-10, 0, 15) @ rotate(90) @ point3d_to_homogenous(line.end)
		line.end = Point3D(end_transformed_coords[0,0], end_transformed_coords[1,0], end_transformed_coords[2,0])

		line = transform_line(line)
		if line is not None:
			pygame.draw.line(screen, RED, (int(line.start.x), int(line.start.y)), (int(line.end.x), int(line.end.y)))


def draw_car():
	car_lines = load_car()
	car_transform = translate(car_x_position, 0, car_z_position) @ rotate(0)
	for i in range(2):
		for j in range(2):
			draw_tire(i, j, car_transform)
	for line in car_lines:
		start_transformed_coords = car_transform @ point3d_to_homogenous(line.start)
		line.start = Point3D(start_transformed_coords[0,0], start_transformed_coords[1,0], start_transformed_coords[2,0])

		end_transformed_coords = car_transform @ point3d_to_homogenous(line.end)
		line.end = Point3D(end_transformed_coords[0,0], end_transformed_coords[1,0], end_transformed_coords[2,0])

		line = transform_line(line)
		if line is not None:
			pygame.draw.line(screen, GREEN, (int(line.start.x), int(line.start.y)), (int(line.end.x), int(line.end.y)))


def draw_tire(i, j, car_transform):
	x_placement = 2.0 if i == 1 else -2.0
	z_placement = 1.5 if j == 1 else -1.5
	tire_lines = load_tire()
	for line in tire_lines:
		start_transformed_coords = car_transform @ translate(x_placement, 0, z_placement) @ rotate(0) @ point3d_to_homogenous(line.start)
		line.start = Point3D(start_transformed_coords[0,0], start_transformed_coords[1,0], start_transformed_coords[2,0])

		end_transformed_coords = car_transform @ translate(x_placement, 0, z_placement) @ rotate(0) @ point3d_to_homogenous(line.end)
		line.end = Point3D(end_transformed_coords[0,0], end_transformed_coords[1,0], end_transformed_coords[2,0])

		line = transform_line(line)
		if line is not None:
			pygame.draw.line(screen, BLUE, (int(line.start.x), int(line.start.y)), (int(line.end.x), int(line.end.y)))


# Initialize the game engine
pygame.init()

# Define the colors we will use in RGB format
BLACK = (0, 0, 0)
WHITE = (255, 255, 255)
BLUE = (0, 0, 255)
GREEN = (0, 255, 0)
RED = (255, 0, 0)

pygame.display.set_caption("Shape Drawing")

# Set needed variables
done = False
clock = pygame.time.Clock()
start = Point(0.0, 0.0)
end = Point(0.0, 0.0)


# Loop until the user clicks the close button.
while not done:

	# This limits the while loop to a max of 100 times per second.
	# Leave this out and we will use all CPU we can.
	clock.tick(100)

	# Clear the screen and set the screen background
	screen.fill(BLACK)

	draw_house_row()
	draw_edge_house()
	draw_house_row(True)
	draw_car()

	# Controller Code#
	#####################################################################

	for event in pygame.event.get():
		if event.type == pygame.QUIT:  # If user clicked close
			done = True

	pressed = pygame.key.get_pressed()

	# flip signs?
	if pressed[pygame.K_a]:
		camera_coords.x -= np.cos(r(camera_angle))
		camera_coords.z -= np.sin(r(camera_angle))
	elif pressed[pygame.K_d]:
		camera_coords.x += np.cos(r(camera_angle))
		camera_coords.z += np.sin(r(camera_angle))
	elif pressed[pygame.K_r]:
		camera_coords.y += 1
	elif pressed[pygame.K_f]:
		camera_coords.y -= 1
	elif pressed[pygame.K_w]:
		camera_coords.z += np.cos(r(camera_angle))
		camera_coords.x -= np.sin(r(camera_angle))
	elif pressed[pygame.K_s]:
		camera_coords.z -= np.cos(r(camera_angle))
		camera_coords.x += np.sin(r(camera_angle))
	elif pressed[pygame.K_q]:
		camera_angle += 1
	elif pressed[pygame.K_e]:
		camera_angle -= 1
	elif pressed[pygame.K_h]:
		camera_coords.x = 0
		camera_coords.y = 3
		camera_coords.z = -15
		camera_angle = 0
		car_x_position = 5
		car_z_position = 15

	# Viewer Code#
	#####################################################################

	# for s in line_list:
	#
	#
	#
	#
	# 	# BOGUS DRAWING PARAMETERS SO YOU CAN SEE THE HOUSE WHEN YOU START UP
	# 	pygame.draw.line(screen, RED, (20 * s.start.x + 200, -20 * s.start.y + 200),
	# 					 (20 * s.end.x + 200, -20 * s.end.y + 200))

	# Go ahead and update the screen with what we've drawn.
	# This MUST happen after all the other drawing commands.
	pygame.display.flip()

# Be IDLE friendly
pygame.quit()
