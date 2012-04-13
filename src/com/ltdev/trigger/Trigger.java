/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.trigger;

/**
 * Allows entities to interact with each other using a Cause and an Effect.
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
