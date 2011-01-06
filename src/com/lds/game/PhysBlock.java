package com.lds.game;

public class PhysBlock extends PhysEnt //a default block
{
	private static boolean held;
	
	public PhysBlock (float _xPos, float _yPos)
	{
		super(Entity.DEFAULT_SIZE, _xPos, _yPos, 0.0f, 1.0f, 1.0f);
		held = false;
		rotSpeed = 3.0f;
		moveSpeed = 1.0f;
	}
	
	public static boolean isHeld ()
	{
		return held;
	}
	
	public static void hold ()
	{
		held = true;
	}
	
	public static void drop ()
	{
		held = false;
		System.out.println("Shit just droppped");
	}
}
