/* 
 * CS:APP Data Lab 
 * 
 * Nathan Nelson 
 * nathantn 
 * 
 * bits.c - Source file with your solutions to the Lab.
 *          This is the file you will hand in to your instructor.
 *
 * WARNING: Do not include the <stdio.h> header; it confuses the dlc
 * compiler. You can still use printf for debugging without including
 * <stdio.h>, although you might get a compiler warning. In general,
 * it's not good practice to ignore compiler warnings, but in this
 * case it's OK.  
 */

#if 0
/*
 * Instructions to Students:
 *
 * STEP 1: Read the following instructions carefully.
 */

You will provide your solution to the Data Lab by
editing the collection of functions in this source file.

INTEGER CODING RULES:
 
  Replace the "return" statement in each function with one
  or more lines of C code that implements the function. Your code 
  must conform to the following style:
 
  int Funct(arg1, arg2, ...) {
      /* brief description of how your implementation works */
      int var1 = Expr1;
      ...
      int varM = ExprM;

      varJ = ExprJ;
      ...
      varN = ExprN;
      return ExprR;
  }

  Each "Expr" is an expression using ONLY the following:
  1. Integer constants 0 through 255 (0xFF), inclusive. You are
      not allowed to use big constants such as 0xffffffff.
  2. Function arguments and local variables (no global variables).
  3. Unary integer operations ! ~
  4. Binary integer operations & ^ | + << >>
    
  Some of the problems restrict the set of allowed operators even further.
  Each "Expr" may consist of multiple operators. You are not restricted to
  one operator per line.

  You are expressly forbidden to:
  1. Use any control constructs such as if, do, while, for, switch, etc.
  2. Define or use any macros.
  3. Define any additional functions in this file.
  4. Call any functions.
  5. Use any other operations, such as &&, ||, -, or ?:
  6. Use any form of casting.
  7. Use any data type other than int.  This implies that you
     cannot use arrays, structs, or unions.

 
  You may assume that your machine:
  1. Uses 2s complement, 32-bit representations of integers.
  2. Performs right shifts arithmetically.
  3. Has unpredictable behavior when shifting an integer by more
     than the word size.

EXAMPLES OF ACCEPTABLE CODING STYLE:
  /*
   * pow2plus1 - returns 2^x + 1, where 0 <= x <= 31
   */
  int pow2plus1(int x) {
     /* exploit ability of shifts to compute powers of 2 */
     return (1 << x) + 1;
  }

  /*
   * pow2plus4 - returns 2^x + 4, where 0 <= x <= 31
   */
  int pow2plus4(int x) {
     /* exploit ability of shifts to compute powers of 2 */
     int result = (1 << x);
     result += 4;
     return result;
  }

FLOATING POINT CODING RULES

For the problems that require you to implent floating-point operations,
the coding rules are less strict.  You are allowed to use looping and
conditional control.  You are allowed to use both ints and unsigneds.
You can use arbitrary integer and unsigned constants.

You are expressly forbidden to:
  1. Define or use any macros.
  2. Define any additional functions in this file.
  3. Call any functions.
  4. Use any form of casting.
  5. Use any data type other than int or unsigned.  This means that you
     cannot use arrays, structs, or unions.
  6. Use any floating point data types, operations, or constants.


NOTES:
  1. Use the dlc (data lab checker) compiler (described in the handout) to 
     check the legality of your solutions.
  2. Each function has a maximum number of operators (! ~ & ^ | + << >>)
     that you are allowed to use for your implementation of the function. 
     The max operator count is checked by dlc. Note that '=' is not 
     counted; you may use as many of these as you want without penalty.
  3. Use the btest test harness to check your functions for correctness.
  4. Use the BDD checker to formally verify your functions
  5. The maximum number of ops for each function is given in the
     header comment for each function. If there are any inconsistencies 
     between the maximum ops in the writeup and in this file, consider
     this file the authoritative source.

/*
 * STEP 2: Modify the following functions according the coding rules.
 * 
 *   IMPORTANT. TO AVOID GRADING SURPRISES:
 *   1. Use the dlc compiler to check that your solutions conform
 *      to the coding rules.
 *   2. Use the BDD checker to formally verify that your solutions produce 
 *      the correct answers.
 */


#endif
/* Copyright (C) 1991-2016 Free Software Foundation, Inc.
   This file is part of the GNU C Library.

   The GNU C Library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   The GNU C Library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with the GNU C Library; if not, see
   <http://www.gnu.org/licenses/>.  */
/* This header is separate from features.h so that the compiler can
   include it implicitly at the start of every compilation.  It must
   not itself include <features.h> or any other header that includes
   <features.h> because the implicit include comes before any feature
   test macros that may be defined in a source file before it first
   explicitly includes a system header.  GCC knows the name of this
   header in order to preinclude it.  */
/* glibc's intent is to support the IEC 559 math functionality, real
   and complex.  If the GCC (4.9 and later) predefined macros
   specifying compiler intent are available, use them to determine
   whether the overall intent is to support these features; otherwise,
   presume an older compiler has intent to support these features and
   define these macros by default.  */
/* wchar_t uses Unicode 8.0.0.  Version 8.0 of the Unicode Standard is
   synchronized with ISO/IEC 10646:2014, plus Amendment 1 (published
   2015-05-15).  */
/* We do not support C11 <threads.h>.  */
/* Rating 1 -- 2 points each */
/* 
 * evenBits - return word with all even-numbered bits set to 1
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 8
 *   Rating: 1
 */
int evenBits(void) {
	// A byte with 1's in all the even positions, i.e. 0b01010101
	int evenByte = 0x55;
	// Left shift by a byte and then add evenByte; this gives us two even bytes
	int twoEvenBytes = (evenByte << 8) + evenByte;
	// Same process to get four even bytes.
	int fourEvenBytes = (twoEvenBytes << 16) + twoEvenBytes;
	return fourEvenBytes;
}
/* 
 * minusOne - return a value of -1 
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 2
 *   Rating: 1
 */
int minusOne(void) {
	return ~0;
}
/* 
 * upperBits - pads n upper bits with 1's
 *  You may assume 0 <= n <= 32
 *  Example: upperBits(4) = 0xF0000000
 *  Legal ops: ! ~ & ^ | + << >>
 *  Max ops: 10
 *  Rating: 1
 */
int upperBits(int n) {
	// This gives us 0x80000000 if n != 0; 0x0000000 if n == 0.
	int value = (!!n) << 31;
	int nMinusOne = n + ~0;
	// This right shift fills in the left side with 1's if n != 0; fills in the
	// left side with zeroes if n == 0.
	int result = value >> nMinusOne;
	return result;
}
/* Rating 2 -- 3 points each */
/* 
 * getByte - Extract byte n from word x
 *   Bytes numbered from 0 (LSB) to 3 (MSB)
 *   Examples: getByte(0x12345678,1) = 0x56
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 6
 *   Rating: 2
 */
int getByte(int x, int n) {
	// Shift right by n * 8 bits to get the nth byte down to the LSB position,
	// then mask only the LSB:
	return (x >> (n << 3)) & 0xff;
}
/* 
 * isNotEqual - return 0 if x == y, and 1 otherwise 
 *   Examples: isNotEqual(5,5) = 0, isNotEqual(4,5) = 1
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 6
 *   Rating: 2
 */
int isNotEqual(int x, int y) {
	// Subtract y from x and then check if it's equal to zero:
	// return !!(x + ~y + 1);
	return !!(x ^ y);
}
/* Rating 3 -- 2 points each */
/* 
 * conditional - same as x ? y : z 
 *   Example: conditional(2,4,5) = 4
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 16
 *   Rating: 3
 */
int conditional(int x, int y, int z) {
	// zcoeff is all 0's if x != 0; all 1's otherwise.
	int zcoeff = ~(!x) + 1;
	// This expression removes y if zcoeff is all 1's and removes z if zcoeff
	// is all 0's.
	return (~zcoeff & y) + (zcoeff & z);
}
/* 
 * isGreater - if x > y  then return 1, else return 0 
 *   Example: isGreater(4,5) = 0, isGreater(5,4) = 1
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 24
 *   Rating: 3
 */
int isGreater(int x, int y) {
	//int notX = ~x;
	//int notSignX = notX >> 31;
	int signY = y >> 31;
	// This is all 1's if x and y have the same sign
	//int shouldCompute = notSignX ^ signY;

	int signYCoeff = (x ^ y) >> 31;

	int XminusYminus1 = x + ~y;
	int signXminusY = XminusYminus1 >> 31;


	int result = (~(signXminusY | signYCoeff) + (signY & signYCoeff)) & 1;

	// This expression equals y - x
	// int YminusX = y + notX + 1;
	// This is the sign of y minus x
	// int signYminusX = YminusX >> 31;

	// This expression is equivalent to 
	//     shouldCompute ? (signYminusX & 1) : (signY & 1)
	// int result = ((signYminusX & shouldCompute) + (signY & ~shouldCompute)) &
	//   	1;
	return result;
}
/* Rating 4 -- 1 point each */
/* 
 * absVal - absolute value of x
 *   Example: absVal(-1) = 1.
 *   You may assume -TMax <= x <= TMax
 *   Legal ops: ! ~ & ^ | + << >>
 *   Max ops: 10
 *   Rating: 4
 */
int absVal(int x) {
	int fullXIsNegative = x >> 31;
	// flips the bits of x and adds 1 if x is negative; does nothing to x if x
	// is nonnegative.
	return (x + fullXIsNegative) ^ fullXIsNegative;
}
/* Float Rating 2 -- 3 points each */
/* 
 * float_neg - Return bit-level equivalent of expression -f for
 *   floating point argument f.
 *   Both the argument and result are passed as unsigned int's, but
 *   they are to be interpreted as the bit-level representations of
 *   single-precision floating point values.
 *   When argument is NaN, return argument.
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 10
 *   Rating: 2
 */
unsigned float_neg(unsigned uf) {
	// If uf is NaN, return uf
	if((uf & 0x7fffffffu) > 0x7f800000u) {
		return uf;
	}

	// Else return -uf
	return uf ^ 0x80000000u;
}
/* Float Rating 4 -- 1 point each */
/* 
 * float_f2i - Return bit-level equivalent of expression (int) f
 *   for floating point argument f.
 *   Argument is passed as unsigned int, but
 *   it is to be interpreted as the bit-level representation of a
 *   single-precision floating point value.
 *   Anything out of range (including NaN and infinity) should return
 *   0x80000000u.
 *   Legal ops: Any integer/unsigned operations incl. ||, &&. also if, while
 *   Max ops: 30
 *   Rating: 4
 */
int float_f2i(unsigned uf) {
	// The true exponent is the bits represented minus 127;
	// The amount we need to shift is the exponent minus 23:
	int shiftAmount = ((uf >> 23) & 0xff) - 150;
	unsigned fraction = uf & 0x7fffffu;
	// The true mantissa is a one followed by the fraction:
	unsigned mantissa = fraction | 0x800000u;
	// This is 0 if uf is positive, nonzero otherwise:
	unsigned sign = uf >> 31;
	int result = 0;

	// This must be a variable so that it is automatically cast to an int
	// (-23) without using the '-' operator:
	int neg23 = 0xffffffe9u;

	// Numbers between 1 and -1 (exclusive) will all round to zero:
	if(shiftAmount < neg23) {
		return 0;
	}
	// If the exponent is greater than 31, we can't represent it in an int:
	if(shiftAmount > 8) {
		return 0x80000000u;
	}

	// If the shiftAmount is positive, left shift by that much:
	if(shiftAmount > 0) {
		result = mantissa << shiftAmount;
	}
	// Else if it's negative, right shift by that much:
	else {
		result = mantissa >> -shiftAmount;
	}

	// If the sign bit is 1, return a negative number:
	if(sign) {
		return -result;
	}
	// If the sign bit is 0, return a positive number:
	else {
		return result;
	}
}
