package com.lds.game;

import com.lds.Enums.UIPosition;
import com.lds.Stopwatch;

public class UIButton extends UIEntity
{
	private boolean pressed;
	private int intervalTime;
	
	public UIButton (float xSize, float ySize, UIPosition position)
	{
		super(xSize, ySize, position);
		pressed = false;
	}
	
	public UIButton (float xSize, float ySize, float xRelative, float yRelative)
	{
		super (xSize, ySize, xRelative, yRelative);
		pressed = false;
	}
	
	public UIButton (float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
		pressed = false;
	}
	
	public UIButton (float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
		pressed = false;
	}
	
	public void onPressed ()
	{
		//this.colorR = 1.0f;
	}
	
	public boolean isPressed ()
	{
		return pressed;
	}
	
	public boolean canPress(int interval)
	{
		if (Stopwatch.elapsedTimeInMilliseconds() - intervalTime >= interval)
			return true;
		
		else
			return false;
	}
	
	public void press ()
	{
		pressed = true;
	}
	
	public void unpress()
	{
		pressed = false;
	}
	
	public void setIntervalTime(int time)
	{
		this.intervalTime = time;
	}
}
