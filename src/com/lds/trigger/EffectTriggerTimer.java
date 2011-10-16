package com.lds.trigger;

/**
 * An Effect that starts a CauseTimePassed Cause. 
 * @author Lightning Development Studios
 * @see CauseTimePassed
 */
public class EffectTriggerTimer extends Effect 
{
	private CauseTimePassed countdown;
	private boolean pauseTimer;
	
	/**
	 * Initializes a new instance of the EffectTriggerTimer class.
	 * @param countdown The CauseTimePassed Cause to use.
	 * @param pauseTimer Whether or not to pause the timer when unfired. False means the timer gets stopped and reset when the effect is unfired.
	 */
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
