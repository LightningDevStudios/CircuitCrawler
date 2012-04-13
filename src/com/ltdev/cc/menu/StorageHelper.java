/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc.menu;

/**
 * A static class used to convert variables to a byte array and back.
 * @author Lightning Development Studios
 */
public final class StorageHelper 
{
	private static final int MASK = 0xff;
	
	/**
	 * Prevents initialization of this static class.
	 */
	private StorageHelper()
	{	
	}
	
	/**
	 * Converts a byte array to a float.
	 * @param test The byte array to convert.
	 * @return A float value.
	 */
	public static float byteArrayToFloat(byte[] test)
	{
		int bits = 0;
		int i = 0;
		for (int shifter = 3; shifter >= 0; shifter--)
		{
			bits |= ((int)test[i] & MASK) << (shifter * 8);
			i++;
		}
		return Float.intBitsToFloat(bits);
	}
	
	/**
	 * Converts a float to a byte array.
	 * @param f The float to convert to a byte array.
	 * @return A byte array that represents the same data as the float f.
	 */
	public static byte[] floatToByteArray(float f)
	{
		int i = Float.floatToRawIntBits(f);
		return intToByteArray(i);
	}
	
	/**
	 * Converts an int to a byte array.
	 * @param param The int to convert to a byte array.
	 * @return A byte array that represents the same data as int param.
	 */
	public static byte[] intToByteArray(int param)
	{
		byte[] result = new byte[4];
		for (int i = 0; i < 4; i++)
		{
			int offset = (result.length - 1 - i) * 8;
			result[i] = (byte) ((param >>> offset) & MASK);
		}
		return result;
	}
}
