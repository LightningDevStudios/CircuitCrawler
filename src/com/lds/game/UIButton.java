package com.lds.game;

import com.lds.Enums.UIPosition;

public class UIButton extends UIEntity
{
	public UIButton(float xSize, float ySize, UIPosition position)
	{
		super(xSize, ySize, position);
	}
	
	public UIButton(float xSize, float ySize, float xRelative, float yRelative)
	{
		super (xSize, ySize, xRelative, yRelative);
	}
	
	public UIButton(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
	}
	
	public UIButton(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
	}
	
	public void onPressed()
	{
		this.colorR = 1.0f;
	}
}
