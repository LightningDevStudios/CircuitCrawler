package com.lds;

import com.lds.UI.*;
import com.lds.game.Game;

public class Finger 
{
	private Vector2f position;
	private UIEntity ent;
	private int ptrId;
	
	public Finger(final Vector2f position, final UIEntity ent, final int ptrId)
	{
		this.position = position;
		this.ent = ent;
		this.ptrId = ptrId;
	}
	
	public void onStackPush()
	{
		if (ent instanceof UIButton)
		{
			final UIButton button = (UIButton)ent;
			button.press();
		}
		else if (ent instanceof UIJoypad)
		{
			final UIJoypad joypad = (UIJoypad)ent;
			joypad.setFingerState(true);
		}
	}
	
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
		else if (ent instanceof UIButton)
		{
			//ent.updateColor(0.3f, 0.5f, 0.6f, 1.0f);
		}
	}
	
	public void onStackPop()
	{
		if (ent instanceof UIButton)
		{
			//ent.updateColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
		else if (ent instanceof UIJoypad)
		{
			final UIJoypad joypad = (UIJoypad)ent;
			joypad.setFingerState(false);
		}
	}
	
	public int getPointerId()
	{
		return ptrId;
	}
	
	public void setPosition(final Vector2f input)
	{
		position = input;
	}
}
