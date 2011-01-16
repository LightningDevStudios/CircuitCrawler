package com.lds.trigger;

public abstract class Cause 
{
	private boolean triggered;
	
	public Cause ()
	{
		triggered = false;
	}
	
	public boolean isTriggered ()
	{
		return triggered;
	}
}
