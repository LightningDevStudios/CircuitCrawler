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

package com.ltdev;

import android.content.Context;

/**
 * A static class that provides phone vibration functionality from anywhere.
 * @author Lightning Development Studios
 */
public final class Vibrator 
{
    /**
     * Allows the Vibrator to be disabled per user settings.
     */
    private static boolean enabled;
    
    /**
     * Forces the Vibrator class to be static.
     */
    private Vibrator()
    {
        
    }
    
    /**
     * Vibrates the phone for a specified duration.
     * @param context The current Android Context.
     * @param time The amount of time, in milliseconds, to vibrate the phone.
     */
    public static void vibrate(Context context, long time)
    {
        if (enabled)
        {
            android.os.Vibrator vibrator = (android.os.Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE); 
            vibrator.vibrate((long)time); 
        }
    }
    
    /**
     * Set a value indicating whether vibration is allowed or not.
     * @param state Whether or not to vibrate the phone when requested.
     */
    public static void setEnableState(boolean state)
    {
        enabled = state;
    }
    
    /**
     * Gets a value indicating whether vibration is allowed or not.
     * @return Whether or not the phone is allowed to vibrate.
     */
    public static boolean getEnableState()
    {
        return enabled;
    }
}
