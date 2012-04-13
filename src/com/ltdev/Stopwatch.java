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
