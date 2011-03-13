package com.lds.game.entity;

public class SpikeBall extends PhysEnt
{
	protected float moveX, moveY;
	protected float oldXPos, oldYPos;
	protected float rotateDirection;
	
	public SpikeBall(float size, float xPos, float yPos, boolean circular, boolean willCollide, float moveSpeed, float rotSpeed, float sclSpeed, float friction, float newMoveX, float newMoveY, float rotateDir) 
	{
		super(size, xPos, yPos, circular, willCollide, moveSpeed, rotSpeed, sclSpeed, friction);
		moveX = newMoveX;
		moveY = newMoveY;
		oldXPos = xPos;
		oldYPos = yPos;
		rotateDirection = rotateDir;
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(this.getXPos() == moveX && this.getYPos() == moveY)
		{
			this.moveTo(oldXPos, oldYPos);
		}
		else if(this.getXPos() == oldXPos && this.getYPos() == oldYPos)
		{
			this.moveTo(moveX, moveY);
		}
		else {}
		this.rotate(10 * rotateDirection); // negative 1 or positive 1.
	}
	
}
