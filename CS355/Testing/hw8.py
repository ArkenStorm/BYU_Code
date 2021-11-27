import numpy as np

n = np.array([1, 1, -1]) / (3**.5)
l = np.array([2, 3, -1]) / (14**.5)
r = 2 * (np.dot(l, n)) * n - l
light_coloring = np.array([1.0, 1.0, .80])
s = .9 * light_coloring
s_amb = .1 * light_coloring
v = np.array([[0, 0, -1]])
m_diff = np.array([.1, .2, .5])
m_spec = np.array([.5, .5, .5])
m_gloss = 4
m_amb = m_diff

c_diff = (s * m_diff) * (np.dot(n, l))
print(c_diff)
c_spec = (s * m_spec) * (np.dot(v, r))**m_gloss
print(c_spec)
c_amb = s_amb * m_amb
print(c_amb)
c_tot = c_amb + c_diff + c_spec
print(c_tot)