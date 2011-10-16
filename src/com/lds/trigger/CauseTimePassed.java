package com.lds.trigger;

import com.lds.Stopwatch;

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
