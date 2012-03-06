package com.ltdev.cc.trigger;

import com.ltdev.cc.entity.Door;
import com.ltdev.trigger.Effect;

/**
 * An Effect that opens a door when fired and closes it when unfired.
 * @author Lightning Development Studios
 */
public class EffectDoor extends Effect
{
	private Door door;
	
	/**
	 * Initializes a new instance of the EffectDoor class.
	 * @param door The door to open and close.
	 */
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
		door.close();
	}
}
