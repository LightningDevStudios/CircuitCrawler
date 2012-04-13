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
	 * Initializes a new instance of the Cause class.
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
