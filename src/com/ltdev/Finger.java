package com.ltdev;

import com.ltdev.cc.Game;
import com.ltdev.cc.ui.*;
import com.ltdev.math.Vector2;

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
			button.enableColorMode(1.0f, 0.0f, 0.0f, 1.0f);
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
			ent.disableColorMode();
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
