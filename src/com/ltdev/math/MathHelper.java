package com.ltdev.math;

/**
 * A class containing various math helper methods.
 * @author Lightning Development Studios
 */
public final class MathHelper
{
    /**
     * Prevents initialization of MathHelper.
     */
    private MathHelper()
    {
        
    }

    /**
     * Returns the number of bits set in a byte bitfield.
     * @param b A byte bitfield.
     * @return The number of bits set in b.
     */
    public static int numberOfBitsSet(byte b)
    {
        int count = 0;
        for (int i = 0; i < 8; i++)
        {
            if ((b & 1) == 1)
                count++;
            b >>= 1;
        }
        
        return count;
    }
}
