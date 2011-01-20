package com.lds.trigger;

import com.lds.Stopwatch;

public class CauseTimePassed extends Cause
{
	private int triggerTime, startTime;
	
	public CauseTimePassed (int triggerTime)
	{
		this.triggerTime = triggerTime;
		startTime = Stopwatch.elapsedTimeMs();
	}
	
	@Override
	public void update ()
	{
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
