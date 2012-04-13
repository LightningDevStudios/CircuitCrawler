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

import com.ltdev.math.Vector2;

public class TeleporterLinker 
{
	protected Teleporter entTele1, entTele2;
	protected boolean oneWay, active;
	
	public TeleporterLinker(Teleporter ent, Teleporter ent2, boolean isOneWay)
	{
		entTele1 = ent;
		entTele2 = ent2;
		oneWay = isOneWay;
		entTele1.setTeleporterLinker(this);
		entTele2.setTeleporterLinker(this);
	}
	
	public Vector2 getLinkedPos(Teleporter t)
	{
		if (t == entTele1)
		{
			entTele2.setActive(false);
			entTele1.setActive(true);
			return entTele2.getPos();
		}
		else if (t == entTele2 && !oneWay)
		{
			entTele1.setActive(false);
			entTele2.setActive(true);
			return entTele1.getPos();
		}
		else
		{
			return null;
		}
	}
}
