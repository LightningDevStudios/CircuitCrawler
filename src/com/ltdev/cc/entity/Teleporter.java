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

public class Teleporter extends Entity
{
	protected boolean active;
	private TeleporterLinker tpLink;
	private float rotationPerUpdate;
	
	public Teleporter(float size, Vector2 position) 
    {
        super(new Circle(size, position, false));
        active = true;
        
        this.tilesetX = 2;
        this.tilesetY = 2;
        
        rotationPerUpdate = 0.01f;
    }

	@Override
	public void interact(Entity ent)
	{
	    super.interact(ent);
	    
		if (active && tpLink != null)
		{
			if (ent instanceof HoldObject && ((HoldObject)ent).isHeld())
				return;
			
			Vector2 newPos = tpLink.getLinkedPos(this);
			if (newPos != null)
			{
				ent.shape.setPos(tpLink.getLinkedPos(this));
                SoundPlayer.playSound(SoundPlayer.TELEPORT);
			}
		}
		rotationPerUpdate += 0.1f;
	}
	
	@Override
	public void update(GL11 gl)
    {
	    super.update(gl);
	    
        this.shape.setAngle(this.shape.getAngle() + rotationPerUpdate);
    }
	
	@Override
	public void uninteract(Entity ent)		
	{
	    super.uninteract(ent);
		active = true;
		rotationPerUpdate = 0.01f;
	}
	
	public void setActive(boolean bool)
	{
		active = bool;
	}
	
	public void setTeleporterLinker(TeleporterLinker tpLink)
	{
		this.tpLink = tpLink;
	}
}
