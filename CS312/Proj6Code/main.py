def combination_sum(candidates, target):
	sol_set = set()
	while len(candidates) > 0:
		candidate = candidates.pop()
		curr_total = 0
		curr_sol = []
		while curr_total < target:
			curr_total += candidate
			curr_sol.append(candidate)
			if curr_total == target:
				sol_set.add(tuple(sorted(curr_sol)))
			if (target - curr_total) in candidates:
				sol = curr_sol.copy()
				sol.append(target - curr_total)
				sol_set.add(tuple(sorted(sol)))
			for val in candidates:
				new_total = curr_total
				sol = curr_sol.copy()
				while new_total < target:
					new_total += val
					sol.append(val)
					if new_total == target:
						sol_set.add(tuple(sorted(sol)))
					if (target - new_total) in candidates:
						new_sol = sol.copy()
						new_sol.append(target - new_total)
						sol_set.add(tuple(sortead(new_sol)))
	return sol_set

if __name__ == '__main__':
	candidates = [3, 2, 8]
	target = 18
	print(combination_sum(candidates, target))
