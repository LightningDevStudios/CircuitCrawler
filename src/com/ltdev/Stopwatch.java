package com.ltdev;

import android.os.SystemClock;

/**
 * A static class that manages time between frames.
 * @author Lightning Development Studios
 */
public final class Stopwatch 
{
	private static long previousFrame;
	private static long currentFrame;
	private static long frameTime;
	
	/**
	 * Prevents initialization of the static Stopwatch class.
	 */
	private Stopwatch() 
	{
	    
	}
	
	/**
	 * Initializes the Stopwatch with the current system time.
	 */
	public static void start()
	{
		previousFrame = SystemClock.elapsedRealtime();
		currentFrame = SystemClock.elapsedRealtime();
	}
	
	/**
	 * Declares the beginning of a new frame and updates the time.
	 * \bug First frame will have a frametime of 0.
	 */
	public static void tick()
	{
		previousFrame = currentFrame;
		currentFrame = SystemClock.elapsedRealtime();
		frameTime = currentFrame - previousFrame;
	}
	
	/**
	 * Retrieves the amount of time the previous frame took to render.
	 * @return The amount of time the previous frame took to render, in milliseconds.
	 */
	public static long getFrameTime()
	{
		return frameTime;
	}
}
