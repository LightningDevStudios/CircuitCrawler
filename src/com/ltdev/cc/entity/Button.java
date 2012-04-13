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

package com.ltdev.cc.entity;

import com.ltdev.cc.SoundPlayer;
import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

/**
 * A Button is an object that reacts to other objects being placed on top of it.
 * @author Lightning Development Studios
 */
public class Button extends Entity
{
	private boolean active, prevActive;
	
	/**
	 * Initializes a new instance of the Button class.
	 * @param position The Button's position.
	 */
	public Button(Vector2 position)
	{
		super(new Circle(69, position, false));
		
		this.tilesetX = 0;
		this.tilesetY = 0;
	}
	
	@Override
	public void update(GL11 gl)
	{
	    super.update(gl);
	    
	    //update the texture when activated/deactivated.
	    if (prevActive != active)
	    {	        
	        prevActive = active;
	    }
	}
	
	/**
	 * Gets a value indicating whether the Button is activated or not.
	 * @return A value indicating whether the Button is activated or not.
	 */
	public boolean isActive()
	{
		return active;
	}
	
	/**
	 * Activate the Button.
	 */
	public void activate()
	{
	    prevActive = active;
		active = true;
		SoundPlayer.playSound(SoundPlayer.SOUND_TEST);
	}
	
	/**
	 * Deactivate the button.
	 */
	public void deactivate()
	{
	    prevActive = active;
		active = false;
		SoundPlayer.playSound(SoundPlayer.SOUND_TEST);
	}
	
	@Override
	public void interact(Entity ent)
	{
	    super.interact(ent);
	    
		if (ent instanceof Player || ent instanceof HoldObject)
			activate();
	}
	
	@Override
	public void uninteract(Entity ent)
	{
		if (ent instanceof Player || ent instanceof HoldObject)
			deactivate();	
	}
}
