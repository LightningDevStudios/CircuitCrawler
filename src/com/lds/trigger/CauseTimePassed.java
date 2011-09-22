package com.lds.trigger;

import com.lds.Stopwatch;

/**
 * A <code>Cause</code> that waits for a specified amount of time before triggering.
 * @author Lightning Development Studios
 *
 */
public class CauseTimePassed extends Cause
{
	private int triggerTime, time;
	private boolean countdownHasStarted, paused;
	
	public CauseTimePassed (int triggerTime)
	{
		this.triggerTime = triggerTime;
		countdownHasStarted = true;
		time = 0;
	}
	
	public CauseTimePassed (int triggerTime, boolean countdownHasStarted)
	{
		this.countdownHasStarted = countdownHasStarted;
		this.triggerTime = triggerTime;
	}
	
	@Override
	public void update ()
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

	public void startCountdown()
	{
		countdownHasStarted = true;
		paused = false;
	}
	
	public void pauseCountdown()
	{
		paused = true;
	}
		
	public void stopCountdown()
	{
		countdownHasStarted = false;
		paused = false;
	}
}
