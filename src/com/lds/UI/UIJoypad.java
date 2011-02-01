package com.lds.UI;

import com.lds.Enums.UIPosition;
import com.lds.Vector2f;

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

	public Vector2f getMovementVec(int x, int y)
	{
		return new Vector2f(x - xPos, y - yPos);
	}
	
	public Vector2f getMovementVec(Vector2f touchVec)
	{
		return new Vector2f(touchVec.getX() - xPos, touchVec.getY() - yPos);
	}
}
