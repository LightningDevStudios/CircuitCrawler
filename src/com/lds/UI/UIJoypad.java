package com.lds.UI;

import com.lds.Enums.UIPosition;
import com.lds.Vector2f;

public class UIJoypad extends UIEntity
{
	Vector2f inputVec;
	float inputAngle;
	boolean active;
	public static final float MAX_SCALAR = 10;
	
	public UIJoypad(float xSize, float ySize, UIPosition position, float inputAngle)
	{
		super(xSize, ySize, position);
		inputVec = new Vector2f();
		this.inputAngle = inputAngle;
		active = false;
	}
	
	public UIJoypad(float xSize, float ySize, float xRelative, float yRelative, float inputAngle)
	{
		super (xSize, ySize, xRelative, yRelative);
		inputVec = new Vector2f();
		this.inputAngle = inputAngle;
		active = false;
	}
	
	public UIJoypad(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad, float inputAngle) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
		inputVec = new Vector2f();
		this.inputAngle = inputAngle;
		active = false;
	}
	
	public UIJoypad(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad, float inputAngle)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
		inputVec = new Vector2f();
		this.inputAngle = inputAngle;
		active = false;
	}
	
	public void setInputVec(float rawX, float rawY)
	{
		inputVec.set(rawX - xPos, rawY - yPos);
		inputAngle = inputVec.angleDeg();
		
		//scale vector properly
		if (inputVec.mag() > xSize / 2)
			inputVec.scaleTo(xSize / 2);
		
		inputVec.scaleTo(inputVec.mag() * MAX_SCALAR / xSize);
	}
	
	public void setInputVec(Vector2f rawVec)
	{
		this.setInputVec(rawVec.getX(), rawVec.getY());
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
	
	public boolean isActive()
	{
		return active;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
	}
}
