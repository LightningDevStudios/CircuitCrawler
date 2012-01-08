package com.lds.game.menu;

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
