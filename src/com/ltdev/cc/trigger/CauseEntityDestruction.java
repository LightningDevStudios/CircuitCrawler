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

package com.ltdev.cc.trigger;

import com.ltdev.cc.entity.Entity;
import com.ltdev.trigger.Cause;

/**
 * A Cause that is triggered when an Entity is removed from the world.
 * @author Lightning Development Studios
 * \todo eixsts no longer works, find a new method to get this Cause working.
 */
public class CauseEntityDestruction extends Cause
{
	private Entity ent;
	
	/**
	 * Initializes a new instance of the CauseEntityDestruction class.
	 * @param ent The Entity to check for removal of.
	 */
	public CauseEntityDestruction(Entity ent)
	{
		super();
		this.ent = ent;
	}
	
	@Override
	public void update()
	{
		if (ent == null)
			trigger();
	}
}
