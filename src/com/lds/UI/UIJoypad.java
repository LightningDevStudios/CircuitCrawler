package com.lds.UI;

import javax.microedition.khronos.opengles.GL10;

import com.lds.Enums.UIPosition;
import com.lds.Vector2f;
import com.lds.game.Game;

public class UIJoypad extends UIEntity
{
	private Vector2f inputVec;
	private float inputAngle;
	private boolean active;
	
	private UIImage fingerCircle;
	private boolean usingFinger;
	
	public static final float MAX_SCALAR = 10;
	
	public UIJoypad(float xSize, float ySize, UIPosition position, float inputAngle)
	{
		super(xSize * Game.screenH, ySize * Game.screenH, position);
		inputVec = new Vector2f();
		this.inputAngle = inputAngle;
		active = false;
		fingerCircle = new UIImage(this.xSize / 2, this.ySize / 2, 0, 0);
		fingerCircle.enableTextureMode(Game.joystickin);
		usingFinger = false;
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
	
	@Override
	public void draw(GL10 gl)
	{
		super.draw(gl);
		
		if (usingFinger)
		{
			fingerCircle.updateVertexVBO(gl);
			fingerCircle.updateTextureVBO(gl);
			fingerCircle.updateGradientVBO(gl);
			fingerCircle.draw(gl);
		}
	}
	
	public void setInputVec(final float rawX, final float rawY)
	{
		inputVec.set(rawX - xPos, rawY - yPos);
		inputAngle = inputVec.angleDeg();
		
		//scale vector properly
		if (inputVec.mag() > xSize / 2)
			inputVec.scaleTo(xSize / 2);
		
		fingerCircle.setPos(Vector2f.scaleTo(inputVec, inputVec.mag() - fingerCircle.xSize / 2));
		/*final float newMag = (inputVec.mag() - fingerCircle.xSize / 2);
		fingerCircle.setXPos(inputVec.getX() * newMag);
		fingerCircle.setYPos(inputVec.getY() * newMag);*/
		
		inputVec.scaleTo(inputVec.mag() * MAX_SCALAR / xSize);
	}
	
	public void setInputVec(final Vector2f rawVec)
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
	
	public void setFingerState(boolean state)
	{
		usingFinger = state;
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
}
