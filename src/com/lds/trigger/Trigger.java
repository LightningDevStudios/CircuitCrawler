package com.lds.trigger;

public class Trigger
{
	private Cause cause;
	private Effect effect;
	
	public Trigger (Cause cause, Effect effect)
	{
		this.cause = cause;
		this.effect = effect;
	}
	
	public void update ()
	{
		if (cause.isTriggered())
		{
			effect.doEffect();
		}
		else
		{
			effect.undoEffect();
		}
	}
}
