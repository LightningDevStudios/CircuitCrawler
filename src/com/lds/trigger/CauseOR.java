package com.lds.trigger;

public class CauseOR extends Cause
{
	private Cause cause1, cause2;
	
	public CauseOR(Cause cause1, Cause cause2)
	{
		this.cause1 = cause1;
		this.cause2 = cause2;
	}
	
	@Override
	public void update()
	{
		cause1.update();
		cause2.update();
		
		if (cause1.isTriggered() || cause2.isTriggered())
		{
			trigger();
		}
		else
		{
			untrigger();
		}
	}
}
