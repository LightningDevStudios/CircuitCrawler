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

package com.ltdev;

import com.ltdev.cc.Game;
import com.ltdev.cc.ui.*;
import com.ltdev.math.Vector2;
import com.ltdev.math.Vector4;

/**
 * A class that makes handling multiple finger input simpler by wrapping data around the original action ID.
 * @author Lightning Development Studios
 */
public class Finger 
{
	private Vector2 position;
	private Control ent;
	private int ptrId;
	
	/**
	 * Initializes a new instance of the Finger class.
	 * @param position The position of the finger on the screen.
	 * @param ent The UI Control that the finger touched (if any).
	 * @param ptrId The action ID provided by Android.
	 */
	public Finger(final Vector2 position, final Control ent, final int ptrId)
	{
		this.position = position;
		this.ent = ent;
		this.ptrId = ptrId;
	}
	
	/**
	 * Called when the finger is first pusehd on to a stack of fingers.
	 */
	public void onStackPush()
	{
		if (ent instanceof UIButton)
		{
			final UIButton button = (UIButton)ent;
			button.setColor(new Vector4(1, 0, 0, 1));
			button.press();
		}
	}
	
	/**
	 * Called once a frame to keep each finger updated.
	 */
	public void update()
	{
		if (ent instanceof UIJoypad)
		{
			final UIJoypad joypad = (UIJoypad)ent;
			joypad.setActive(true);
			joypad.setInputVec(position);
			Game.windowOutdated = true;
			Game.worldOutdated = true;
		}
	}
	
	/**
	 * Called when this Finger is popped off the stack.
	 */
	public void onStackPop()
	{
		if (ent instanceof UIButton)
		{
		    ent.setColor(new Vector4(1, 1, 1, 1));
			((UIButton)ent).unpress();
		}
		else if (ent instanceof UIJoypad)
		{
			final UIJoypad joypad = (UIJoypad)ent;
			joypad.getFingerCircle().setPos(new Vector2(0, 0));
		}
	}
	
	/**
	 * Gets the raw Android ID.
	 * @return The raw Android ID.
	 */
	public int getPointerId()
	{
		return ptrId;
	}
	
	/**
	 * Sets the position of the finger on the screen.
	 * @param input The new finger position.
	 */
	public void setPosition(final Vector2 input)
	{
		position = input;
	}
}
