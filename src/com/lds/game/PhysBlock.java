package com.lds.game;

public class PhysBlock extends PhysEnt //a default block
{
	//private boolean held;
	
	public PhysBlock (float _xPos, float _yPos)
	{
		super(Entity.DEFAULT_SIZE, _xPos, _yPos, 0.0f, 1.0f, 1.0f);
		//held = false;
	}
	
	/*public boolean isHeld ()
	{
		return held;
	}
	
	public void hold ()
	{
		held = true;
	}
	
	public void drop ()
	{
		held = false;
	}*/
}
