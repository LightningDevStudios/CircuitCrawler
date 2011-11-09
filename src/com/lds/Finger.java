package com.lds;

import com.lds.UI.*;
import com.lds.game.Game;
import com.lds.math.Vector2;

public class Finger 
{
	private Vector2 position;
	private Control ent;
	private int ptrId;
	
	public Finger(final Vector2 position, final Control ent, final int ptrId)
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
			button.enableColorMode(1.0f, 0.0f, 0.0f, 1.0f);
			button.press();
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
	}
	
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
	
	public int getPointerId()
	{
		return ptrId;
	}
	
	public void setPosition(final Vector2 input)
	{
		position = input;
	}
}
