""" Modified code from Peter Colling Ridge
	Original found at http://www.petercollingridge.co.uk/pygame-3d-graphics-tutorial
"""

import pygame, math
import numpy as np
import wireframe as wf
import basicShapes as shape


class WireframeViewer(wf.WireframeGroup):
	""" A group of wireframes which can be displayed on a Pygame screen """

	def __init__(self, width, height, name="Wireframe Viewer"):
		self.width = width
		self.height = height

		self.screen = pygame.display.set_mode((width, height))
		pygame.display.set_caption(name)

		self.wireframes = {}
		self.wireframe_colors = {}
		self.object_to_update = []

		self.displayNodes = False
		self.displayEdges = True
		self.displayFaces = True

		self.perspective = False
		self.eyeX = self.width / 2
		self.eyeY = 100
		self.light_color = np.array([1, 1, 1])
		self.view_vector = np.array([0, 0, -1])
		self.light_vector = np.array([0, 0, -1])

		self.background = (10, 10, 50)
		self.nodeColor = (250, 250, 250)
		self.nodeRadius = 4

		self.control = 0

	def addWireframe(self, name, wireframe):
		self.wireframes[name] = wireframe
		#   If color is set to None, then wireframe is not displayed
		self.wireframe_colors[name] = (250, 250, 250)

	def addWireframeGroup(self, wireframe_group):
		# Potential danger of overwriting names
		for name, wireframe in wireframe_group.wireframes.items():
			self.addWireframe(name, wireframe)

	def display(self):
		self.screen.fill(self.background)

		for name, wireframe in self.wireframes.items():
			nodes = wireframe.nodes

			if self.displayFaces:
				for (face, color) in wireframe.sortedFaces():
					v1 = (nodes[face[1]] - nodes[face[0]])[:3]
					v2 = (nodes[face[2]] - nodes[face[0]])[:3]

					normal = fast_cross_norm(v1, v2)
					towards_us = np.dot(normal, self.view_vector)

					# Only draw faces that face us
					if towards_us > 0:
						# Your lighting code here
						# Make note of the self.view_vector and self.light_vector
						# Use the Phong model

						l_dot_n = np.dot(self.light_vector, normal)
						reflect = 2 * l_dot_n * normal - self.light_vector

						m_gloss = 15
						m_ambient = 0.18
						m_diffuse = 0.37
						m_specular = 0.45

						ambient = self.light_color * m_ambient
						diffuse = (self.light_color * m_diffuse) * (np.dot(normal, self.light_vector)) if l_dot_n > 0 else 0
						specular = (self.light_color * m_specular) * clamp(np.dot(self.view_vector, reflect), 0.0,
																		   1.0) ** m_gloss if l_dot_n > 0 else 0

						# Once you have implemented diffuse and specular lighting, you will want to include them here
						light_total = ambient + specular + diffuse

						pygame.draw.polygon(self.screen, np.clip(light_total * color, 0, 255),
											[(nodes[node][0], nodes[node][1]) for node in face], 0)

				if self.displayEdges:
					for (n1, n2) in wireframe.edges:
						if self.perspective:
							if wireframe.nodes[n1][2] > -self.perspective and nodes[n2][2] > -self.perspective:
								z1 = self.perspective / (self.perspective + nodes[n1][2])
								x1 = self.width / 2 + z1 * (nodes[n1][0] - self.width / 2)
								y1 = self.height / 2 + z1 * (nodes[n1][1] - self.height / 2)

								z2 = self.perspective / (self.perspective + nodes[n2][2])
								x2 = self.width / 2 + z2 * (nodes[n2][0] - self.width / 2)
								y2 = self.height / 2 + z2 * (nodes[n2][1] - self.height / 2)

								pygame.draw.aaline(self.screen, color, (x1, y1), (x2, y2), 1)
						else:
							pygame.draw.aaline(self.screen, color, (nodes[n1][0], nodes[n1][1]),
											   (nodes[n2][0], nodes[n2][1]), 1)

			if self.displayNodes:
				for node in nodes:
					pygame.draw.circle(self.screen, color, (int(node[0]), int(node[1])), self.nodeRadius, 0)

		pygame.display.flip()

	def keyEvent(self, key):

		# Your code here
		if key == pygame.K_w:
			self.rotate_x(.1 * math.pi)
		elif key == pygame.K_s:
			self.rotate_x(-.1 * math.pi)
		elif key == pygame.K_a:
			self.rotate_y(-.1 * math.pi)
		elif key == pygame.K_d:
			self.rotate_y(.1 * math.pi)
		elif key == pygame.K_q:
			self.rotate_z(.1 * math.pi)
		elif key == pygame.K_e:
			self.rotate_z(-.1 * math.pi)
		return

	def run(self):
		""" Display wireframe on screen and respond to keydown events """

		running = True
		key_down = False
		while running:
			for event in pygame.event.get():
				if event.type == pygame.QUIT:
					running = False
				elif event.type == pygame.KEYDOWN:
					key_down = event.key
				elif event.type == pygame.KEYUP:
					key_down = None

			if key_down:
				self.keyEvent(key_down)

			self.display()
			self.update()

		pygame.quit()

	def rotate_x(self, angle):
		x_rot = np.array([[1, 0, 0],
						  [0, np.cos(angle), np.sin(angle)],
						  [0, -np.sin(angle), np.cos(angle)]])
		self.light_vector = x_rot @ self.light_vector

	def rotate_y(self, angle):
		y_rot = np.array([[np.cos(angle), 0, -np.sin(angle)],
						  [0, 1, 0],
						  [np.sin(angle), 0, np.cos(angle)]])
		self.light_vector = y_rot @ self.light_vector

	def rotate_z(self, angle):
		z_rot = np.array([[np.cos(angle), np.sin(angle), 0],
						  [-np.sin(angle), np.cos(angle), 0],
						  [0, 0, 1]])
		self.light_vector = z_rot @ self.light_vector


def fast_cross_norm(a, b):
	ax, ay, az = a
	bx, by, bz = b

	cx = ay * bz - az * by
	cy = az * bx - ax * bz
	cz = ax * by - ay * bx
	norm = math.sqrt(cx * cx + cy * cy + cz * cz)
	return np.array([cx / norm, cy / norm, cz / norm])


def clamp(x, low, high):
	return min(max(x, low), high)


def fast_clip(a, low, high):
	x, y, z = a
	return ([
		min(max(x, low), high),
		min(max(y, low), high),
		min(max(z, low), high),
	])


resolution = 52
viewer = WireframeViewer(600, 400)
viewer.addWireframe('sphere', shape.Spheroid((300, 200, 20), (160, 160, 160), resolution=resolution))

# Colour ball
faces = viewer.wireframes['sphere'].faces
for i in range(int(resolution / 4)):
	for j in range(resolution * 2 - 4):
		f = i * (resolution * 4 - 8) + j
		faces[f][1][1] = 0
		faces[f][1][2] = 0

viewer.displayEdges = False
viewer.run()
