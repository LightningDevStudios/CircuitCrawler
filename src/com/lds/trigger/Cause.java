package com.lds.trigger;

public abstract class Cause 
{
	private boolean triggered;
	
	public Cause()
	{
		triggered = false;
	}
	
	public void update()
	{
		
	}
	
	public boolean isTriggered()
	{
		return triggered;
	}
	
	public void trigger()
	{
		this.triggered = true;
	}
	
	public void untrigger()
	{
		this.triggered = false;
	}
	
}
