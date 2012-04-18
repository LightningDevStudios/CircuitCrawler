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

package com.ltdev.cc.parser;

import com.ltdev.cc.entity.Entity;
import com.ltdev.graphics.Texture;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class EntityData
{
	protected boolean isSolid;
	protected boolean circular;
	protected boolean willCollide;
	protected boolean textureModeEnabled, tilesetModeEnabled;
	protected float size, xPos, yPos, xScl, yScl, angle;	
	protected float[] texture;
	protected int tileX, tileY;
	protected Texture tex;
	
	protected Entity ent;
	
	private String id;
	
	public EntityData(HashMap<String, String> entHM)
	{
		if (entHM.get("size") != null)
			size = Float.parseFloat(entHM.get("size"));
		if (entHM.get("xPos") != null)
			xPos = Float.parseFloat(entHM.get("xPos"));
		if (entHM.get("yPos") != null)
			yPos = Float.parseFloat(entHM.get("yPos"));
		if (entHM.get("xScl") != null)
			xScl = Float.parseFloat(entHM.get("xScl"));
		if (entHM.get("yScl") != null)
			yScl = Float.parseFloat(entHM.get("yScl"));
		if (entHM.get("angle") != null)
			angle = Float.parseFloat(entHM.get("angle"));
		if (entHM.get("id") != null)
			id = entHM.get("id");
		
		isSolid = Boolean.parseBoolean(entHM.get("isSolid"));
		circular = Boolean.parseBoolean(entHM.get("circular"));
		willCollide = Boolean.parseBoolean(entHM.get("willCollide"));
	}
	
	
	//float setters/getters
	public void setSize(float newSize)
	{
	    size = newSize;
	}
	
	public void setXPos(float newXPos)
	{
	    xPos = newXPos;
	}
	
	public void setYPos(float newYPos)
	{
	    yPos = newYPos;
	}
	
	public void setXScl(float newXScl)
	{
	    xScl = newXScl;
	}
	
	public void setYScl(float newYScl)
	{
	    yScl = newYScl;
	}
	
	public void setAngle(float newAngle)
	{
	    angle = newAngle;
	}
	
	public void setIsSolid(boolean newIsSolid)
	{
	    isSolid = newIsSolid;
	}
	
	public void setCircular(boolean newCircular)
	{
	    circular = newCircular;
	}
	
	public float getSize()
	{
	    return size;
	}
	
	public float getXPos()
	{
	    return xPos;
	}
	
	public float getYPos()
	{
	    return yPos;
	}
	
	public float getXScl()
	{
	    return xScl;
	}
	
	public float getYScl()
	{
	    return yScl;
	}
	
	public float getAngle()
	{
	    return angle;
	}
	
	public boolean getIsSolid()
	{
	    return isSolid;
	}
	
	public boolean getCircular()
	{
	    return circular;
	}
	
	public String getID()
	{
	    return id;
	}
	
	public Entity getEnt()
	{
	    return ent;
	}
		
	public void createInst(ArrayList<Entity> entData)
	{
	}
}
