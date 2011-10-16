package com.lds.UI;

import com.lds.Enums.UIPosition;
import com.lds.Texture;
import com.lds.game.Game;
import com.lds.math.Vector2;

import javax.microedition.khronos.opengles.GL10;

public class UIJoypad extends UIEntity
{
    public static final float MAX_SCALAR = 10;
    
	private Vector2 inputVec;
	private float inputAngle;
	private boolean active;
	
	private UIImage fingerCircle;
	
	public UIJoypad(float xSize, float ySize, UIPosition position, float inputAngle, Texture joystickin)
	{
		super(xSize * Game.screenH, ySize * Game.screenH, position);
		inputVec = new Vector2();
		this.inputAngle = inputAngle;
		active = false;
		fingerCircle = new UIImage(halfSize.getX(), halfSize.getY(), 0, 0);
		fingerCircle.enableTextureMode(joystickin);
	}
	
	public UIJoypad(float xSize, float ySize, float xRelative, float yRelative, float inputAngle)
	{
		super(xSize, ySize, xRelative, yRelative);
		inputVec = new Vector2();
		this.inputAngle = inputAngle;
		active = false;
	}
	
	public UIJoypad(float xSize, float ySize, UIPosition position, float topPad, float leftPad, float bottomPad, float rightPad, float inputAngle) 
	{
		super(xSize, ySize, position, topPad, leftPad, bottomPad, rightPad);
		inputVec = new Vector2();
		this.inputAngle = inputAngle;
		active = false;
	}
	
	public UIJoypad(float xSize, float ySize, float xRelative, float yRelative, float topPad, float leftPad, float bottomPad, float rightPad, float inputAngle)
	{
		super(xSize, ySize, xRelative, yRelative, topPad, leftPad, bottomPad, rightPad);
		inputVec = new Vector2();
		this.inputAngle = inputAngle;
		active = false;
	}
	
	@Override
	public void draw(GL10 gl)
	{
		super.draw(gl);
		
		fingerCircle.updateVertexVBO(gl);
		fingerCircle.updateTextureVBO(gl);
		fingerCircle.updateGradientVBO(gl);
		fingerCircle.draw(gl);
	}
	
	public void setInputVec(final float rawX, final float rawY)
	{
		inputVec.set(rawX - pos.getX(), rawY - pos.getY());
		inputAngle = inputVec.angleDeg();
		
		//scale vector properly
		if (inputVec.length() > size.getX() / 2)
			inputVec.scaleTo(size.getX() / 2);
		
		//\TODO choose one method of moving inner circle
		//fingerCircle.setPos(Vector2f.scaleTo(inputVec, inputVec.mag() - fingerCircle.xSize / 2));
		fingerCircle.setPos(inputVec);
		
		inputVec.scaleTo(inputVec.length() * MAX_SCALAR / size.getX());
	}
	
	public void setInputVec(final Vector2 rawVec)
	{
		this.setInputVec(rawVec.getX(), rawVec.getY());
	}
	
	public void clearInputVec()
	{
		inputVec.set(0.0f, 0.0f);
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
		
	public void genHardwareBuffers(GL10 gl)
	{
		super.genHardwareBuffers(gl);
		fingerCircle.genHardwareBuffers(gl);
	}
	
	public void updateVertexVBO(GL10 gl)
	{
		super.updateVertexVBO(gl);
		fingerCircle.updateVertexVBO(gl);
	}
	
	public void updateGradientVBO(GL10 gl)
	{
		super.updateGradientVBO(gl);
		fingerCircle.updateGradientVBO(gl);
	}
	
	public void updateTextureVBO(GL10 gl)
	{
		super.updateTextureVBO(gl);
		fingerCircle.updateTextureVBO(gl);
	}
	
	public UIImage getFingerCircle()
	{
	    return fingerCircle;
	}
}
