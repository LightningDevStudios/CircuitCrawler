package com.lds.game.menu;

public class StorageHelper 
{
	private static final int MASK = 0xff;
	
	private StorageHelper()
	{
		
	}
	
	public static float byteArrayToFloat(byte test[])
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
	
	public static byte[] floatToByteArray(float f)
	{
		int i = Float.floatToRawIntBits(f);
		return intToByteArray(i);
	}
	
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
