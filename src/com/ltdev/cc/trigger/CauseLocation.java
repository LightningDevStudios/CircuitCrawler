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

import com.ltdev.cc.entity.Player;
import com.ltdev.trigger.Cause;

/**
 * A Cause that is triggered when the player is within a location.
 * @author Lighting Development Studios
 * \todo Allow for any Entity, not just the player.
 */
public class CauseLocation extends Cause
{
	private Player player;
	private float minX, maxX, minY, maxY;
	
	/**
	 * Initializes a new instance of the CauseLocation class.
	 * @param player The level's player instance.
	 * @param minX The location minimum on the X axis.
	 * @param maxX The location maximum on the X axis.
	 * @param minY The location minimum on the Y axis.
	 * @param maxY The location maximum on the Y axis.
	 */
	public CauseLocation(Player player, float minX, float maxX, float minY, float maxY)
	{
		this.player = player;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}
	
	@Override
	public void update()
	{
		if (player.getPos().x() >= minX
		 && player.getPos().x() <= maxX 
		 && player.getPos().y() >= minY 
		 && player.getPos().y() <= maxY)
		{
			trigger();
		}
		else
		{
			untrigger();
		}
	}
}
