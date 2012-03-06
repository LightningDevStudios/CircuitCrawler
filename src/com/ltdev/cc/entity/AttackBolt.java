package com.ltdev.cc.entity;

import com.ltdev.EntityManager;
import com.ltdev.cc.Tile;
import com.ltdev.cc.Tile.TileType;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

public class AttackBolt extends Entity
{
	private Entity parent;
	
	/**
	 * \todo add real physics.
	 * @param position Position.
	 * @param direction Direction.
	 * @param parent Parent.
	 */
	public AttackBolt(Vector2 position, Vector2 direction, Entity parent)
	{
		super(new Rectangle(new Vector2(20, 40), position, direction.angleRad(), true));
		
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
		if (tile.getTileType() == TileType.WALL)
			EntityManager.removeEntity(this);
	}
	
	@Override
	public void update(GL11 gl)
	{
		super.update(gl);
		
		if (colorVec.w() == 0.0f) //if transparent
			EntityManager.removeEntity(this);
	}
	
	/**
	 * Gets the parent Entity.
	 * @return The parent Entity.
	 */
	public Entity getParent()
	{
	    return parent;
	}
}
