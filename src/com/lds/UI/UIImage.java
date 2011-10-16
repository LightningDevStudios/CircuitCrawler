package com.lds.UI;

import com.lds.Enums.UIPosition;

public class UIImage extends UIEntity
{
	public UIImage(float xSize, float ySize, UIPosition position)
	{
		super(xSize, ySize, position);
	}
	
	public UIImage(float xSize, float ySize, float xRelative, float yRelative)
	{
		super(xSize, ySize, xRelative, yRelative);
	}
	
	public UIImage(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
	}
	
	public UIImage(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
	}
}
