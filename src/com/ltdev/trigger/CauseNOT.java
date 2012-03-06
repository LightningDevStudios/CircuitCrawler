package com.ltdev.trigger;

/**
 * A Cause that acts as a NOT gate for another Cause.
 * @author Lightning Development Studios
 */
public class CauseNOT extends Cause
{
	private Cause cause;
	
	/**
	 * Initializes a new instance of the CauseNOT class.
	 * @param cause The Cause to use.
	 */
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
