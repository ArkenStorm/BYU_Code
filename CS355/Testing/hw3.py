import numpy as np

# input_array = np.array([[0, 0, 0, 0, 0, 0, 0],
# 						[0, 10, 11, 9, 25, 22, 0],
# 						[0, 8, 10, 9, 26, 28, 0],
# 						[0, 9, 99, 9, 24, 25, 0],
# 						[0, 11, 11, 12, 23, 22, 0],
# 						[0, 10, 11, 9, 22, 25, 0],
# 						[0, 0, 0, 0, 0, 0, 0]])

# input_array = np.array([[0, 0, 0, 0, 0, 0, 0],
# 						[0, 0, 0, 0, 0, 0, 0],
# 						[0, 0, 0, 1, 0, 0, 0],
# 						[0, 0, 1, 2, 1, 0, 0],
# 						[0, 0, 0, 3, 0, 0, 0],
# 						[0, 0, 0, 0, 0, 0, 0],
# 						[0, 0, 0, 0, 0, 0, 0]])

# input_array = np.array([[0, 0, 0, 0, 0, 0, 0],
# 						[0, 10, 11, 9, 25, 22, 0],
# 						[0, 8, 10, 9, 26, 28, 0],
# 						[0, 9, 8, 9, 24, 25, 0],
# 						[0, 11, 11, 12, 23, 22, 0],
# 						[0, 10, 11, 9, 22, 25, 0],
# 						[0, 0, 0, 0, 0, 0, 0]])

input_array = [[10,11,9,25,22],
			   [8,10,9,26,28],
			   [9,8,9,24,25],
			   [11,11,12,23,22],
			   [10,11,9,22,25]]

mask = ([[1, 2, 1],
		 [2, 4, 2],
		 [1, 2, 1]])

unsharp_mask = [[0, -1, 0],
				[-1, 5, -1],
				[0, -1, 0]]

x_sobel = [[-1,0,1],
		   [-2,0,2],
		   [-1,0,1]]

y_sobel = [[-1,-2,-1],
		   [0,0,0],
		   [1,2,1]]
# problems 1-4
# output_array = np.zeros((5, 5))

# problem 5
output_array = np.zeros((3,3))
for i in range(1, len(input_array) - 1):
	for j in range(1, len(input_array[0]) - 1):
		new_i = i - 1
		new_j = j - 1
		# problem 1
		# output_array[new_i][new_j] = (input_array[i][j] + input_array[i+1][j] + input_array[i+1][j+1] + input_array[i+1][j-1] + input_array[i][j+1] + input_array[i][j-1] + input_array[i-1][j] + input_array[i-1][j+1] + input_array[i-1][j-1]) / 9

		# problem 2
		# temp = [input_array[i][j], input_array[i+1][j], input_array[i+1][j+1], input_array[i+1][j-1], input_array[i][j+1], input_array[i][j-1], input_array[i-1][j], input_array[i-1][j+1], input_array[i-1][j-1]]
		# temp.sort()
		# output_array[new_i][new_j] = temp[len(temp) // 2]

		# problem 3
		# current = np.array([[input_array[i - 1][j - 1], input_array[i][j - 1], input_array[i + 1, j - 1]],
		# 				 [input_array[i - 1][j], input_array[i][j], input_array[i + 1][j]],
		# 				 [input_array[i - 1][j + 1], input_array[i][j + 1], input_array[i + 1][j + 1]]])
		#
		# temp = np.multiply(current, mask)
		# output_array[new_i][new_j] = np.sum(temp)

		# problem 4
		# current = np.array([[input_array[i - 1][j - 1], input_array[i][j - 1], input_array[i + 1, j - 1]],
		# 				 [input_array[i - 1][j], input_array[i][j], input_array[i + 1][j]],
		# 				 [input_array[i - 1][j + 1], input_array[i][j + 1], input_array[i + 1][j + 1]]])
		# temp = np.multiply(current, unsharp_mask)
		# output_array[new_i][new_j] = np.sum(temp)

		# problem 5
		current = np.array([[input_array[i - 1][j - 1], input_array[i - 1][j], input_array[i - 1][j + 1]],
							[input_array[i][j - 1], input_array[i][j], input_array[i][j + 1]],
							[input_array[i + 1][j - 1], input_array[i + 1][j], input_array[i + 1][j + 1]]])
		# temp = np.multiply(current, x_sobel) # x_sobel
		# temp = np.multiply(current, y_sobel) # y_sobel

		temp_x = np.multiply(current, x_sobel)
		temp_y = np.multiply(current, y_sobel)
		sum_x = np.sum(temp_x) / 8
		sum_y = np.sum(temp_y) / 8

		output_array[new_i][new_j] = (sum_x**2 + sum_y**2)**0.5

print(output_array)
