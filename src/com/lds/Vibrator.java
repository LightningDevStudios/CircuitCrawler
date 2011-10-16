package com.lds;

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
