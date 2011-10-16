package com.lds.trigger;

/**
 * A Cause that acts as an AND gate for other Causes.
 * @author Lightning Development Studios
 */
public class CauseAND extends Cause
{
	private Cause cause1, cause2;
	
	/**
	 * Initializes a new instance of the CauseAND class.
	 * @param cause1 The first cause to use.
	 * @param cause2 The second cause to use.
	 */
	public CauseAND(Cause cause1, Cause cause2)
	{
		this.cause1 = cause1;
		this.cause2 = cause2;
	}
	
	@Override
	public void update()
	{
		cause1.update();
		cause2.update();
		
		if (cause1.isTriggered() && cause2.isTriggered())
		{
			trigger();
		}
		else
		{
			untrigger();
		}
	}
}
