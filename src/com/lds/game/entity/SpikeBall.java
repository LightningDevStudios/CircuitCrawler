package com.lds.game.entity;

public class SpikeBall extends PhysEnt
{
	protected float moveX, moveY;
	protected float oldXPos, oldYPos;
	protected float rotateDir;
	
	public SpikeBall(float size, float xPos, float yPos, boolean circular, boolean willCollide, float moveSpeed, float rotSpeed, float sclSpeed, float friction, float moveX, float moveY, float rotateDir) 
	{
		super(size, xPos, yPos, circular, willCollide, moveSpeed, rotSpeed, sclSpeed, friction);
		this.moveX = moveX;
		this.moveY = moveY;
		oldXPos = xPos;
		oldYPos = yPos;
		this.rotateDir = rotateDir;
	}
	
	@Override
	public void update()
	{
		super.update();
		
		//\TODO clean it up!
		if(this.getXPos() == moveX && this.getYPos() == moveY)
		{
			this.push(oldXPos, oldYPos);
		}
		else if(this.getXPos() == oldXPos && this.getYPos() == oldYPos)
		{
			this.push(moveX, moveY);
		}
		else {}
		this.rotate(10 * rotateDir); // negative 1 or positive 1.
	}
	
}
