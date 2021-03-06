package com.ltdev.cc.ui;

import com.ltdev.Texture;
import com.ltdev.cc.Game;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

public class UIJoypad extends Control
{
    public static final float MAX_SCALAR = 10;
    
	private Vector2 inputVec;
	private float inputAngle;
	private boolean active;
	
	private UIImage fingerCircle;
	
	public UIJoypad(float xSize, float ySize, UIPosition position, float inputAngle, Texture joystickin)
	{
		super(xSize * Game.screenH, ySize * Game.screenH, position);
		inputVec = new Vector2(0, 0);
		this.inputAngle = inputAngle;
		active = false;
		fingerCircle = new UIImage(halfSize.x(), halfSize.y(), 0, 0);
		fingerCircle.enableTextureMode(joystickin);
	}
	
	public UIJoypad(float xSize, float ySize, float xRelative, float yRelative, float inputAngle)
	{
		super(xSize, ySize, xRelative, yRelative);
		inputVec = new Vector2(0, 0);
		this.inputAngle = inputAngle;
		active = false;
	}
	
	public UIJoypad(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad, float inputAngle) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
		inputVec = new Vector2(0, 0);
		this.inputAngle = inputAngle;
		active = false;
	}
	
	public UIJoypad(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad, float inputAngle)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
		inputVec = new Vector2(0, 0);
		this.inputAngle = inputAngle;
		active = false;
	}
	
	@Override
	public void draw(GL11 gl)
	{
		super.draw(gl);
		
		fingerCircle.updateVertexVBO(gl);
		fingerCircle.updateTextureVBO(gl);
		fingerCircle.updateGradientVBO(gl);
		fingerCircle.draw(gl);
	}
	
	public void setInputVec(final float rawX, final float rawY)
	{
		inputVec = new Vector2(rawX - pos.x(), rawY - pos.y());
		inputAngle = inputVec.angleRad();
		
		//scale vector properly
		if (inputVec.length() > size.x() / 2)
			inputVec = Vector2.scaleTo(inputVec, size.x() / 2);
		
		//\TODO choose one method of moving inner circle
		//fingerCircle.setPos(Vector2f.scaleTo(inputVec, inputVec.mag() - fingerCircle.xSize / 2));
		fingerCircle.setPos(inputVec);
		
		inputVec = Vector2.scaleTo(inputVec, inputVec.length() * MAX_SCALAR / size.x());
	}
	
	public void setInputVec(final Vector2 rawVec)
	{
		this.setInputVec(rawVec.x(), rawVec.y());
	}
	
	public void clearInputVec()
	{
		inputVec = new Vector2(0, 0);
	}

	public Vector2 getInputVec()
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
		
	public void genHardwareBuffers(GL11 gl)
	{
		super.genHardwareBuffers(gl);
		fingerCircle.genHardwareBuffers(gl);
	}
	
	public void updateVertexVBO(GL11 gl)
	{
		super.updateVertexVBO(gl);
		fingerCircle.updateVertexVBO(gl);
	}
	
	public void updateGradientVBO(GL11 gl)
	{
		super.updateGradientVBO(gl);
		fingerCircle.updateGradientVBO(gl);
	}
	
	public void updateTextureVBO(GL11 gl)
	{
		super.updateTextureVBO(gl);
		fingerCircle.updateTextureVBO(gl);
	}
	
	public UIImage getFingerCircle()
	{
	    return fingerCircle;
	}
}
