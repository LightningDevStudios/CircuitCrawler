package com.lds.game.entity;

import javax.microedition.khronos.opengles.GL11;

import com.lds.EntityManager;
import com.lds.math.Vector2;
import com.lds.physics.Rectangle;

public class AttackBolt extends Entity
{
	private Entity parent;
	
	/**
	 * \todo add real physics.
	 */
	public AttackBolt(Vector2 position, Vector2 direction, Entity parent)
	{
		super(new Rectangle(20.0f, position, direction.angleRad(), new Vector2(2, 1), true));
		
		this.setColorInterpSpeed(1.4f);
		this.initColorInterp(1.0f, 1.0f, 1.0f, 0.0f);
		this.tilesetX = 1;
		this.tilesetY = 3;
	}
	
	@Override
	public void interact(Entity ent)
	{
		if (parent != ent)
		{
			EntityManager.removeEntity(this);
		}
	}
	
	@Override
	public void tileInteract(Tile tile)
	{
		if (tile.isWall())
			EntityManager.removeEntity(this);
	}
	
	@Override
	public void update(GL11 gl)
	{
		super.update(gl);
		if (colorVec.getW() == 0.0f) //if transparent
			EntityManager.removeEntity(this);
	}
	
	public Entity getParent()
	{
	    return parent;
	}
}
