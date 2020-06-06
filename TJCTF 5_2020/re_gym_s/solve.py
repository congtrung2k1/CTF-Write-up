from z3 import *

s = Solver()

x1, x2, x3, x4 = Ints('x1 x2 x3 x4')
solve(211 - (x1 * 4) - (x2 * 1) - (x3 * 5) - (x4 * 3) == 180, x1 + x2 + x3 + x4 == 7)
