package com.lds.trigger;

import com.lds.game.entity.Entity;

/**
 * A Cause that is triggered when an Entity is removed from the world.
 * @author Lightning Development Studios
 * \todo eixsts no longer works, find a new method to get this Cause working.
 */
public class CauseEntityDestruction extends Cause
{
	private Entity ent;
	
	/**
	 * Initializes a new instance of the CauseEntityDestruction class.
	 * @param ent The Entity to check for removal of.
	 */
	public CauseEntityDestruction(Entity ent)
	{
		super();
		this.ent = ent;
	}
	
	@Override
	public void update()
	{
		if (ent == null)
			trigger();
	}
}
