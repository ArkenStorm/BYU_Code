import heapq


def twoSum(nums, target):
	values = {}  # value -> index
	for i in range(len(nums)):
		remainder = target - nums[i]
		if values.get(remainder) is not None:
			return [i, values.get(remainder)]
		values[nums[i]] = i


def maxArea(height):  # container with most water
	left = 0
	right = len(height) - 1
	best_area = (right - left) * min(height[left], height[right])
	while left < right - 1:
		if height[left] < height[right]:
			left += 1
		else:
			right -= 1
		best_area = max(best_area, (right - left) * min(height[left], height[right]))
	return best_area


def combination_sum(candidates, target):
	sol_set = set()
	# state = (length, curr_sol, sum)
	prio_queue = []
	for candidate in candidates:
		curr_sol = [candidate]
		heapq.heappush(prio_queue, (len(curr_sol), candidate, curr_sol))
		while len(prio_queue) > 0:
			curr_state = heapq.heappop(prio_queue)
			if curr_state[1] == target:
				sol_set.add(tuple(sorted(curr_state[2])))
			if (target - curr_state[1]) in candidates:
				sol = curr_state[2].copy()
				sol.append(target - curr_state[1])
				sol_set.add(tuple(sorted(sol)))
			for val in candidates:
				if curr_state[1] + val < target:
					sol = curr_state[2].copy()
					sol.append(val)
					heapq.heappush(prio_queue, (len(sol), curr_state[1] + val, sol))
	return sol_set


def minimumTotal(triangle):
	layer_sums = [triangle[0][0]]
	if len(triangle) == 1:
		return triangle[0][0]
	for i in range(1, len(triangle)):
		curr_sums = layer_sums
		layer_sums = []
		for j in range(len(triangle[i])):
			if j == 0:
				layer_sums.append(curr_sums[j] + triangle[i][j])
			elif j == len(triangle[i]) - 1:
				layer_sums.append(curr_sums[j - 1] + triangle[i][j])
			else:
				left = curr_sums[j - 1] + triangle[i][j]
				right = curr_sums[j] + triangle[i][j]
				layer_sums.append(min(left, right))
	return min(layer_sums)


def dfs(friend, visited, M):
	for i in range(len(M[friend])):
		if M[friend][i] and not i in visited:
			visited.add(i)
			dfs(i, visited, M)


def findCircleNum(M):
	visited = set()
	groups = 0
	while len(visited) < len(M):
		for i in range(len(M)):
			if not i in visited:
				visited.add(i)
				dfs(i, visited, M)
				groups += 1
	return groups


def tribonacci(n):
	solutions = [0, 1, 1]
	if n < len(solutions):
		return solutions[n]
	for i in range(2, n + 1):
		val = solutions[i] + solutions[i - 1] + solutions[i - 2]
		solutions.append(val)
	return solutions[n]


if __name__ == '__main__':
	M = [[1,1,0],[1,1,0],[0,0,1]]
	print(findCircleNum(M))
