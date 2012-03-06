package com.ltdev.trigger;

/**
 * A Cause that acts as an XOR gate between two other causes.
 * @author Lightning Development Studios
 */
public class CauseXOR extends Cause
{
	private Cause cause1, cause2;
	
	/**
	 * Initializes a new instance of the CauseXOR class.
	 * @param cause1 The first Cause to use.
	 * @param cause2 The second Cause to use.
	 */
	public CauseXOR(Cause cause1, Cause cause2)
	{
		this.cause1 = cause1;
		this.cause2 = cause2;
	}
	
	@Override
	public void update()
	{
		cause1.update();
		cause2.update();
		
		if (cause1.isTriggered() != cause2.isTriggered())
		{
			trigger();
		}
		else
		{
			untrigger();
		}
	}
}
