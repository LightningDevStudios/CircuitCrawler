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

package com.ltdev.cc.trigger;

import com.ltdev.Stopwatch;
import com.ltdev.trigger.Cause;

/**
 * A Cause that waits for a specified amount of time before triggering.
 * @author Lightning Development Studios
 */
public class CauseTimePassed extends Cause
{
	private int triggerTime, time;
	private boolean countdownHasStarted, paused;
	
	/**
	 * Initializes a new instance of the CauseTimePassed class.
	 * @param triggerTime The amount of time, in milliseconds, to wait before triggering.
	 */
	public CauseTimePassed(int triggerTime)
	{
		this.triggerTime = triggerTime;
		countdownHasStarted = true;
		time = 0;
	}
	
	/**
	 * Initializes a new instance of the CauseTimePassed class.
	 * @param triggerTime The amount of time, in milliseconds, to wait before triggering.
	 * @param countdownHasStarted A value of true will start the countdown as soon as the level has loaded.
	 * \todo Add a CauseLevelStarted & pair with an EffectTriggerTimer?
	 */
	public CauseTimePassed(int triggerTime, boolean countdownHasStarted)
	{
		this.countdownHasStarted = countdownHasStarted;
		this.triggerTime = triggerTime;
	}
	
	@Override
	public void update()
	{
		if (countdownHasStarted && !paused)
		{
			time += Stopwatch.getFrameTime();
			
			if (time > triggerTime)
			{
				trigger();
			}
		}
	}

	/**
	 * Starts the timer.
	 */
	public void startCountdown()
	{
		countdownHasStarted = true;
		paused = false;
	}
	
	/**
	 * Pauses the timer.
	 */
	public void pauseCountdown()
	{
		paused = true;
	}
	
	/**
	 * Stops the timer and resets the time passed to 0.
	 */
	public void stopCountdown()
	{
		countdownHasStarted = false;
		paused = false;
	}
}
