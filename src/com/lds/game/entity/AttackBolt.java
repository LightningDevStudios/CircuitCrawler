package com.lds.game.entity;

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
		//this.directionVec = directionVec;
		//this.angle = angle + 90.0f;
		//rotMat = Matrix4.rotateZ((float)Math.toRadians(this.angle));
		//this.move(directionVec.getX() * 5.0f, directionVec.getY() * 5.0f);
		//this.push(directionVec.scale(0.5f));
		this.setColorInterpSpeed(1.4f);
		this.initColorInterp(1.0f, 1.0f, 1.0f, 0.0f);
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
	public void update()
	{
		super.update();
		if (colorVec.getW() == 0.0f) //if transparent
			EntityManager.removeEntity(this);
	}
	
	public Entity getParent()
	{
	    return parent;
	}
}
