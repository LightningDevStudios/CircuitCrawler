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

import java.util.HashMap;

public class PhysEntData extends EntityData
{
	protected float moveSpeed;
	protected float rotSpeed;
	protected float sclSpeed;
	protected float friction;
	
	public PhysEntData(HashMap<String, String> physEntHM)
	{
		super(physEntHM);
		
		if (physEntHM.get("moveSpeed") != null)
			moveSpeed = Float.parseFloat(physEntHM.get("moveSpeed")); 
		else
			moveSpeed = 0.0f;
		if (physEntHM.get("rotSpeed") != null)
			rotSpeed = Float.parseFloat(physEntHM.get("rotSpeed"));
		else
			rotSpeed = 0.0f;
		if (physEntHM.get("sclSpeed") != null)
			sclSpeed = Float.parseFloat(physEntHM.get("sclSpeed"));
		else
			sclSpeed = 0.0f;
		if (physEntHM.get("friction") != null)
			friction = Float.parseFloat(physEntHM.get("friction"));
		else
			friction = 0.02f;
	}

	public void setMoveSpeed(float newMoveSpeed)
	{
	    moveSpeed = newMoveSpeed;
	}
	
	public void setRotSpeed(float newRotSpeed)
	{
	    rotSpeed = newRotSpeed;
	}
	
	public void setSclSpeed(float newSclSpeed)
	{
	    sclSpeed = newSclSpeed;
	}
	
	public float getMoveSpeed()
	{
	    return moveSpeed;
	}
	
	public float getRotSpeed()
	{
	    return	rotSpeed;
	}
	
	public float getSclSpeed()
	{
	    return sclSpeed;
	}
}
