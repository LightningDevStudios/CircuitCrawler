package com.lds.trigger;

public class Trigger
{
	protected Cause cause;
	protected Effect effect;
	protected boolean firing;
	
	public Trigger(Cause cause, Effect effect)
	{
		this.cause = cause;
		this.effect = effect;
		firing = false;
	}
	
	public void update()
	{
		cause.update();
		
		if (!firing)
		{
			if(cause.isTriggered())
			{
				effect.fireOutput();
				firing = true;
			}
		}
				
		else
		{
			if(!cause.isTriggered())
			{
				effect.unfireOutput();
				firing = false;
			}
		}
	}
	
	public boolean isFiring()
	{
		return firing;
	}
	
	public Cause getCause()
	{
		return cause;
	}
	
	public Effect getEffect()
	{
		return effect;
	}
}
