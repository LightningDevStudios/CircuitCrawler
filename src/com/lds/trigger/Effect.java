package com.lds.trigger;

/**
 * Used to run some code when a Trigger is triggered.
 * @author Lightning Development Studios
 */
public abstract class Effect 
{
    /**
     * Called when the Trigger is triggered.
     */
	public abstract void fireOutput();
	
	/**
	 * Called when the Trigger is untriggered. (set to false after being set to true)
	 */
	public abstract void unfireOutput();
}
