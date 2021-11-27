import random


def prime_test(N, k):
	# This is the main function connected to the Test button. You don't need to touch it.
	return run_fermat(N,k), run_miller_rabin(N,k)


def mod_exp(x, y, N):
	if y == 0:
		return 1
	z = mod_exp(x, y // 2, N)
	return (z ** 2) % N if y % 2 == 0 else (((z ** 2) % N) * x) % N


def fprobability(k):
	return 1 - (1/(2**k))


def mprobability(k):
	return 1 - (1/(4**k))


def run_fermat(N,k):
	x_vals = []
	for i in range(k):
		x = random.randint(2, N-1)      # O(log(n)) time according to the internet
		while x in x_vals:				# check for avoiding duplicate x values
			x = random.randint(2, N-1)
		x_vals.append(x)
		result = mod_exp(x, N-1, N)
		if result != 1:
			return "composite"
	return 'prime'


def run_miller_rabin(N,k):
	x_vals = []
	for i in range(k):
		y = N - 1
		x = random.randint(2, N-1)
		while x in x_vals:
			x = random.randint(2, N-1)
		x_vals.append(x)
		result = mod_exp(x, N-1, N)
		if result != 1:
			return 'composite'
		while y % 2 == 0:
			result = mod_exp(x, y, N)
			if result == 1:
				y //= 2
			elif result == N - 1:
				break
			else:
				return 'composite'
	return 'prime'
