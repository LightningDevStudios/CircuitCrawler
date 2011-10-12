package com.lds.trigger;

public class CauseNOT extends Cause
{
	private Cause cause;
	
	public CauseNOT(Cause cause)
	{
		this.cause = cause;
	}
	
	@Override
	public void update()
	{
		cause.update();
		
		if (!cause.isTriggered())
			trigger();
		
		else
			untrigger();
	}
}
