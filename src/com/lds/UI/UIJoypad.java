package com.lds.UI;

import com.lds.Enums.UIPosition;

public class UIJoypad extends UIEntity
{
	public UIJoypad(float xSize, float ySize, UIPosition position)
	{
		super(xSize, ySize, position);
	}
	
	public UIJoypad(float xSize, float ySize, float xRelative, float yRelative)
	{
		super (xSize, ySize, xRelative, yRelative);
	}
	
	public UIJoypad(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
	}
	
	public UIJoypad(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
	}
	
	public float getRelativeX (float xInput)
	{
		return xInput - xPos;
	}
	
	public float getRelativeY (float yInput)
	{
		return yInput - yPos;
	}
}
