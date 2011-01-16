package com.lds.trigger;

import com.lds.game.Door;

public class DoorEffect extends Effect
{
	private Door door;
	
	public DoorEffect (Door door)
	{
		this.door = door;
	}
	
	@Override
	public void doEffect ()
	{
		door.open();
	}
	
	@Override
	public void undoEffect ()
	{
		door.close();
	}
}
