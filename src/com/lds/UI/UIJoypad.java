package com.lds.UI;

import com.lds.Enums.UIPosition;
import com.lds.Vector2f;

public class UIJoypad extends UIEntity
{
	Vector2f inputVec;
	float inputAngle;
	
	public UIJoypad(float xSize, float ySize, UIPosition position, float inputAngle)
	{
		super(xSize, ySize, position);
		inputVec = new Vector2f();
		this.inputAngle = inputAngle;
	}
	
	public UIJoypad(float xSize, float ySize, float xRelative, float yRelative, float inputAngle)
	{
		super (xSize, ySize, xRelative, yRelative);
		inputVec = new Vector2f();
		this.inputAngle = inputAngle;
	}
	
	public UIJoypad(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad, float inputAngle) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
		inputVec = new Vector2f();
		this.inputAngle = inputAngle;
	}
	
	public UIJoypad(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad, float inputAngle)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
		inputVec = new Vector2f();
		this.inputAngle = inputAngle;
	}
	
	public void setInputVec(float rawX, float rawY)
	{
		inputVec.set(rawX - xPos, rawY - yPos);
		inputAngle = inputVec.angleDeg();
	}
	
	public void clearInputVec()
	{
		inputVec.set(0.0f, 0.0f);
	}

	public Vector2f getInputVec()
	{
		return inputVec;
	}
	
	public float getInputAngle()
	{
		return inputAngle;
	}
}
