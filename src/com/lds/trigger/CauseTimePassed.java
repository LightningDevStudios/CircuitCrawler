package com.lds.trigger;

import com.lds.Stopwatch;

public class CauseTimePassed extends Cause
{
	private int triggerTime, startTime, timeRemaining;
	private boolean countdownHasStarted, isSet, isPaused;
	
	public CauseTimePassed (int triggerTime)
	{
		this.triggerTime = triggerTime;
		startTime = Stopwatch.elapsedTimeMs();
		countdownHasStarted = true;
		isSet = true;
		isPaused = false;
	}
	
	public CauseTimePassed (int triggerTime, boolean countdownHasStarted)
	{
		this.triggerTime = triggerTime;
		this.countdownHasStarted = countdownHasStarted;
		isSet = false;
		isPaused = false;
	}
	
	@Override
	public void update ()
	{
		if (countdownHasStarted)
		{
			if (!isSet)
			{
				startTime = Stopwatch.elapsedTimeMs();
				isSet = true;
			}
			if (isPaused)
			{
				startTime = Stopwatch.elapsedTimeMs() - timeRemaining;
				isPaused = false;
			}
			if (Stopwatch.elapsedTimeMs() - startTime > triggerTime)
			{
				trigger();
			}
			else
			{
				untrigger();
			}
		}
	}

	public void startCountdown()
	{
		this.countdownHasStarted = true;
	}
	
	public void pauseCountdown()
	{
		timeRemaining = triggerTime - (Stopwatch.elapsedTimeMs() - startTime);
		isPaused = true;
	}
		
	public void stopCountdown()
	{
		this.countdownHasStarted = false;
		this.isSet = false;
	}
}
