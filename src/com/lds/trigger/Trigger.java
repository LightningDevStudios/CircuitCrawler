package com.lds.trigger;

/**
 * Allows entities to interact with each other using a {@code Cause} and an {@code Effect}.
 * @author Lightning Development Studios
 * @see Cause
 * @see Effect
 * \todo Add a new feature: fire once. The trigger will delete itself / not update after the first time it's triggered 
 */
public class Trigger
{
	protected Cause cause;
	protected Effect effect;
	protected boolean firing;
	
	/**
	 * Initializes a new instance of the Trigger class.
	 * @param cause The cause.
	 * @param effect The effect.
	 */
	public Trigger(Cause cause, Effect effect)
	{
		this.cause = cause;
		this.effect = effect;
		firing = false;
	}
	
	/**
	 * Updates the cause and fires/unfires the effect accordingly.
	 */
	public void update()
	{
	    //update the cause
		cause.update();
		
		if (!firing)
		{
		    //fire the output
			if (cause.isTriggered())
			{
				effect.fireOutput();
				firing = true;
			}
		}
		
		//unfire the output when cause is untriggered after being first triggered
		else if (!cause.isTriggered())
		{
			effect.unfireOutput();
			firing = false;
		}
	}
	
	/**
	 * Gets a value indicating whether the effect is being fired or not.
	 * @return Whether or not the effect is being fired.
	 */
	public boolean isFiring()
	{
		return firing;
	}
	
	/**
	 * Gets this trigger's cause.
	 * @return The cause for this trigger.
	 */
	public Cause getCause()
	{
		return cause;
	}
	
	/**
	 * Gets this trigger's effect.
	 * @return The effect for this trigger.
	 */
	public Effect getEffect()
	{
		return effect;
	}
}
