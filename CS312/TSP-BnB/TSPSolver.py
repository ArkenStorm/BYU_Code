#!/usr/bin/python3
from copy import deepcopy

from PyQt5.QtCore import QLineF, QPointF

import time
import numpy as np
from TSPClasses import *
import heapq
import itertools



class TSPSolver:
	def __init__( self, gui_view ):
		self._scenario = None

	def setupWithScenario( self, scenario ):
		self._scenario = scenario


	''' <summary>
		This is the entry point for the default solver
		which just finds a valid random tour.  Note this could be used to find your
		initial BSSF.
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of solution, 
		time spent to find solution, number of permutations tried during search, the 
		solution found, and three null values for fields not used for this 
		algorithm</returns> 
	'''

	def defaultRandomTour( self, time_allowance=60.0 ):
		results = {}
		cities = self._scenario.getCities()
		ncities = len(cities)
		foundTour = False
		count = 0
		bssf = None
		start_time = time.time()
		while not foundTour and time.time()-start_time < time_allowance:
			# create a random permutation
			perm = np.random.permutation( ncities )
			route = []
			# Now build the route using the random permutation
			for i in range( ncities ):
				route.append( cities[ perm[i] ] )
			bssf = TSPSolution(route)
			count += 1
			if bssf.cost < np.inf:
				# Found a valid route
				foundTour = True
		end_time = time.time()
		results['cost'] = bssf.cost if foundTour else math.inf
		results['time'] = end_time - start_time
		results['count'] = count
		results['soln'] = bssf
		results['max'] = None
		results['total'] = None
		results['pruned'] = None
		return results


	''' <summary>
		This is the entry point for the greedy solver, which you must implement for 
		the group project (but it is probably a good idea to just do it for the branch-and
		bound project as a way to get your feet wet).  Note this could be used to find your
		initial BSSF.
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of best solution, 
		time spent to find best solution, total number of solutions found, the best
		solution found, and three null values for fields not used for this 
		algorithm</returns> 
	'''

	def greedy(self,time_allowance=60.0 ):
		results = {}
		cities = self._scenario.getCities()
		city_set = set(cities)
		best_city = None
		bssf = None
		count = 0

		start_time = time.time()
		while len(city_set) > 0 and time.time()-start_time < time_allowance:
			visited = {}
			initial_city = city_set.pop()
			visited[initial_city] = True
			curr_city = initial_city
			route = [initial_city]
			tour_complete = False
			while not tour_complete:
				closest_city = None
				cost = None
				lowest_cost = math.inf
				for city in cities:
					if not visited.get(city):
						cost = curr_city.costTo(city)
						if cost < lowest_cost:
							closest_city = city
							lowest_cost = cost
				if lowest_cost != math.inf and cost is not None:
					curr_city = closest_city
					visited[curr_city] = True
					route.append(curr_city)
				else:
					tour_complete = (len(route) == len(cities))
					new_bssf = TSPSolution(route)
					if tour_complete:
						count += 1
						if bssf is None or new_bssf.cost < bssf.cost:
							bssf = new_bssf
							best_city = initial_city
					break
		end_time = time.time()
		results['cost'] = bssf.cost if best_city is not None else math.inf
		results['time'] = end_time - start_time
		results['count'] = count
		results['soln'] = bssf
		results['max'] = None
		results['total'] = None
		results['pruned'] = None
		return results

	def reduce_matrix(self, matrix, lower_bound):  # returns matrix, LB
		for c_index in range(len(matrix)):  # city index and neighbor index
			low_cost = math.inf
			for n_index in range(len(matrix[c_index])):
				if matrix[c_index][n_index] < low_cost:
					low_cost = matrix[c_index][n_index]
					if low_cost == 0:
						break
			if low_cost != 0 and low_cost != math.inf:
				lower_bound += low_cost
				for n_index in range(len(matrix[c_index])):
					matrix[c_index][n_index] -= low_cost  # should make the one with the low_cost zero

		for col in range(len(matrix[0])):  # could be any index, they should all be the same
			low_cost = math.inf
			for row in range(len(matrix)):
				if matrix[row][col] < low_cost:
					low_cost = matrix[row][col]
					if low_cost == 0:
						break
			if low_cost != 0 and low_cost != math.inf:
				lower_bound += low_cost
				for row in range(len(matrix)):
					matrix[row][col] -= low_cost
		return matrix, lower_bound

	def initial_cost_matrix(self, cities):
		matrix = []
		lower_bound = 0
		for city in cities:
			matrix_row = []
			for neighbor in cities:  # this SHOULD get all the correct indices
				if city._name == neighbor._name:
					matrix_row.append(math.inf)
				else:
					matrix_row.append(city.costTo(neighbor))
			matrix.append(matrix_row)
		return self.reduce_matrix(matrix, lower_bound)

	#  This function goes through and infinities out the lowest paths, then goes through and checks to make
	# sure it's still a valid cost matrix
	def update_cost_matrix(self, matrix, source, destination, lower_bound):
		for col in range(len(matrix[0])):  # zeroes out the rows and columns
			matrix[source._index][col] = math.inf
		for row in range(len(matrix)):
			matrix[row][destination._index] = math.inf
		matrix[destination._index][source._index] = math.inf
		return self.reduce_matrix(matrix, lower_bound)

	''' <summary>
		This is the entry point for the branch-and-bound algorithm that you will implement
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of best solution, 
		time spent to find best solution, total number solutions found during search (does
		not include the initial BSSF), the best solution found, and three more ints: 
		max queue size, total number of states created, and number of pruned states.</returns> 
	'''

	def branchAndBound( self, time_allowance=60.0 ):
		results = {}
		cities = self._scenario.getCities()
		matrix, lower_bound = self.initial_cost_matrix(cities)
		tiebreaker = itertools.count()
		sols_found = 0
		bssf = self.greedy(time_allowance)['soln']
		max_stored = 0
		states_created = 0
		pruned = 0
		prio_queue = []
		initial_city = cities[0]
		# (cost, depth, tiebreaker, lower_bound, city, matrix, path)
		heapq.heappush(prio_queue, (0, 0, next(tiebreaker), lower_bound, cities[0], matrix, [initial_city]))
		current_state = None

		start_time = time.time()
		while len(prio_queue) > 0 and time.time()-start_time < time_allowance:
			if current_state is None:
				current_state = heapq.heappop(prio_queue)
			for i in range(len(matrix[current_state[4]._index])):
				if i != current_state[4]._index and cities[i]._name != initial_city._name:
					states_created += 1
					cost = current_state[5][current_state[4]._index][i]
					if cost + current_state[3] < bssf.cost:
						new_path = current_state[-1].copy()
						new_path.append(cities[i])
						new_matrix = deepcopy(current_state[5])
						new_matrix, new_lower_bound = self.update_cost_matrix(new_matrix, current_state[4], cities[i], cost + current_state[3])
						if new_lower_bound < bssf.cost:
							heapq.heappush(prio_queue, (cost, len(new_path) - 1, next(tiebreaker),
														new_lower_bound, cities[i], new_matrix, new_path))
						if len(prio_queue) > max_stored:
							max_stored = len(prio_queue)
					else:
						pruned += 1
			current_state = heapq.heappop(prio_queue)
			if len(current_state[-1]) == len(cities):
				new_bssf = TSPSolution(current_state[-1])
				sols_found += 1
				if new_bssf.cost <= bssf.cost:
					bssf = new_bssf

		end_time = time.time()
		results['cost'] = bssf.cost
		results['time'] = end_time - start_time
		results['count'] = sols_found
		results['soln'] = bssf
		results['max'] = max_stored
		results['total'] = states_created
		results['pruned'] = pruned
		return results



	''' <summary>
		This is the entry point for the algorithm you'll write for your group project.
		</summary>
		<returns>results dictionary for GUI that contains three ints: cost of best solution, 
		time spent to find best solution, total number of solutions found during search, the 
		best solution found.  You may use the other three field however you like.
		algorithm</returns> 
	'''

	def fancy( self,time_allowance=60.0 ):
		pass




