package com.lds.puzzles.logic;

public class EndGate extends Gate
{	
	public EndGate(GateType type, float posX, float posY)
	{
		super(type, posX, posY);
		input = new Gate[1];
	}
	
	public void update()
	{
	    
	}
}
