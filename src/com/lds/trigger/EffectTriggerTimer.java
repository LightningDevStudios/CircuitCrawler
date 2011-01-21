package com.lds.trigger;

public class EffectTriggerTimer extends Effect 
{
	private CauseTimePassed countdown;
	private boolean pauseTimer;
	
	public EffectTriggerTimer(CauseTimePassed countdown, boolean pauseTimer)
	{
		this.countdown = countdown;
		this.pauseTimer = pauseTimer;
	}
	
	@Override
	public void fireOutput()
	{
		countdown.startCountdown();
	}
	
	@Override
	public void unfireOutput()
	{
		if (pauseTimer)
			countdown.pauseCountdown();
		else
			countdown.stopCountdown();
	}
}
