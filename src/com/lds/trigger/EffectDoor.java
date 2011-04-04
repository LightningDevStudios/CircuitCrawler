package com.lds.trigger;

import com.lds.game.entity.Door;

public class EffectDoor extends Effect
{
	private Door door;
	
	public EffectDoor(Door door)
	{
		this.door = door;
	}
	
	@Override
	public void fireOutput()
	{
		door.open();
	}
	
	@Override
	public void unfireOutput()
	{
		door.tryToClose();
	}
}
