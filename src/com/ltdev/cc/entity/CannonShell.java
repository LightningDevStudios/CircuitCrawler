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

import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

public class CannonShell extends Entity
{	
	/**
	 * @param position Position.
	 * @param angle The shell's angle in radians.
	 */
	public CannonShell(Vector2 position, float angle)
	{
		super(new Circle(15, position, angle, true));
		
		shape.setKineticFriction(0);
        shape.setStaticFriction(0);
		
		this.tilesetX = 2;
		this.tilesetY = 0;
	}
	
	@Override
	public void interact(Entity ent)
    {
	    
    }	
	
	@Override
	public void update(GL11 gl)
	{
	    
	}
}
