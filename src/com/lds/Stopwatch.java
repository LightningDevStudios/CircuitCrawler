package com.lds;

import android.os.SystemClock;

/**
 * A static class that manages time between frames.
 * @author Lightning Development Studios
 *
 */
public class Stopwatch 
{
	private static long previousFrame;
	private static long currentFrame;
	private static long frameTime;
	
	private Stopwatch() {}
	
	/**
	 * Initializes the Stopwatch with the current system time.
	 */
	public static void start()
	{
		//first frame will have a frametime of 0
		previousFrame = SystemClock.elapsedRealtime();
		currentFrame = SystemClock.elapsedRealtime();
	}
	
	/**
	 * Declares the beginning of a new frame and updates the time.
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
