package com.ltdev.trigger;

/**
 * Abstract class used with an Effect to create a Trigger.<br/><br/>
 * 
 * Extending Cause is relatively simple. A subclass only needs to implement
 * update() and at some point call trigger() and optionally untrigger(). A triggered 
 * Cause in a Trigger will immediately fire the Effect's output.
 * @author Lightning Development Studios
 * @see Effect
 * @see Trigger
 */
public abstract class Cause 
{
	private boolean triggered;
	
	/**
	 * Creates a new instance of the Cause class.
	 */
	public Cause()
	{
		triggered = false;
	}
	
	/**
	 * An abstract method that gets called once per frame per trigger.
	 */
	public abstract void update();
	
	/**
	 * Gets a value indicating whether the Cause is currently triggered or not.
	 * @return A value indicating whether the Cause is currently triggered or not.
	 */
	public final boolean isTriggered()
	{
		return triggered;
	}
	
	/**
	 * Triggers the {@code Cause}.
	 */
	public final void trigger()
	{
		this.triggered = true;
	}
	
	/**
	 * Untriggers the Cause.
	 */
	public final void untrigger()
	{
		this.triggered = false;
	}
	
}
