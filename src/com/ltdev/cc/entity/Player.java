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

public class Player extends Entity
{
	private HoldObject hObj;
	private boolean controlled;
	
	public Player(Vector2 position, float angle)
	{
		super(new Circle(DEFAULT_SIZE, position, angle, true));
		controlled = true;
		shape.setStaticFriction(2);
		shape.setKineticFriction(200);
	}
	
	
	@Override
	public void interact(Entity ent)
	{
	    super.interact(ent);
	}
	
	public void holdObject(HoldObject hObj)
	{
		this.hObj = hObj;
		hObj.hold();
		updateHeldObjectPosition();
	}
	
	public void dropObject()
	{
		hObj.drop();
		hObj = new Block(0, new Vector2(0, 0));
		hObj = null;
	}
	
	/**
	 * \todo actually push the object with physics.
	 */
	public void throwObject()
	{
		dropObject();
	}

	/**
	 * \todo do this with physics.
	 */
	public void updateHeldObjectPosition()
	{
		/*float heldDistance = hObj.halfSize * hObj.getXScl() + this.halfSize + 10.0f;
		Vector2 directionVec = new Vector2(angle);
		directionVec.scale(heldDistance).add(posVec);
		hObj.setPos(directionVec);
		hObj.setAngle(angle);*/
	}
	
	/**
	 * \todo make player flash once it is hit.
	 * @param gl The OpenGL context.
	 */
	@Override
    public void update(GL11 gl)
    {
        super.update(gl); 
    }
	
	public static void kill()
	{
	    System.out.println("LOLZ PLAYERZ ARE DETH");
	}
	
	public void disableUserControl()
	{
		if (hObj != null)
			dropObject();
		controlled = false;
	}
	
	public boolean userHasControl()
	{
		return controlled;
	}
	
	public HoldObject getHeldObject()
	{
		return hObj;
	}
	
	public boolean isHoldingObject()
	{
	    return hObj != null;
	}

    public void addImpulse(Vector2 f)
    {
        shape.addImpulse(f);
    }
}
