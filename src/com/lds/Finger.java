package com.lds;

import com.lds.UI.*;
import com.lds.game.Game;

public class Finger 
{
	private Vector2f position;
	private UIEntity ent;
	
	public Finger(Vector2f position, UIEntity ent)
	{
		this.position = position;
		this.ent = ent;
	}
	
	public void onStackPush()
	{
		if (ent instanceof UIButton)
		{
			UIButton button = (UIButton)ent;
			button.press();
		}
	}
	
	public void update(Vector2f touchInput)
	{
		if (ent instanceof UIJoypad)
		{
			UIJoypad joypad = (UIJoypad)ent;
			joypad.setActive(true);
			joypad.setInputVec(position);
			Game.windowOutdated = true;
			Game.worldOutdated = true;
		}
	}
	
	public void onStackPop()
	{
		
	}
}
