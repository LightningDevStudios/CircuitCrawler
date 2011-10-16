package com.lds.trigger;

/**
 * Used to run some code when a {@code Trigger} is triggered.
 * @author Lightning Development Studios
 */
public abstract class Effect 
{
    /**
     * Called when the {@code Trigger} is triggered.
     */
	public abstract void fireOutput();
	
	/**
	 * Called when the {@code Trigger} is untriggered. (set to false after being set to true)
	 */
	public abstract void unfireOutput();
}
