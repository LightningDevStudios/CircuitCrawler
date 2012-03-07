package com.ltdev.cc.entity;

import javax.microedition.khronos.opengles.GL11;

import com.ltdev.EntityManager;
import com.ltdev.Stopwatch;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

public class SpikeWall extends Entity
{	
    private boolean extended;
    private Vector2 targetPos, initialPos, endPos;
    
	public SpikeWall(float size, Vector2 position, Vector2 endlocation, boolean left)
    {
        super(new Rectangle(new Vector2(size, size), position, 0, true));
        
        extended = false;
        initialPos = position;
        if(left)
            endPos = new Vector2(position.x() - 72, position.y());
        else
            endPos = new Vector2(position.x() + 72, position.y());
        
        this.tilesetX = 3;
        this.tilesetY = 1;
    }
	
	@Override
	public void update(GL11 gl)
	{
	    float frameTime = (float)Stopwatch.getFrameTime() / 1000f;
        if(extended)
            targetPos = Vector2.add(shape.getPos(), Vector2.scale( Vector2.subtract(initialPos, shape.getPos()), frameTime / 3));
        else
            targetPos = Vector2.add(shape.getPos(), Vector2.scale( Vector2.subtract(endPos, shape.getPos()), frameTime));
        shape.setPos(targetPos);
        
        if(targetPos.approxEquals(initialPos, 0.1f))
            extended = false;
        if(targetPos.approxEquals(endPos, 0.1f))
            extended = true;
	}
	
	@Override
	public void interact(Entity ent)
	{
	    if (ent instanceof Player)
	        EntityManager.removeEntity(ent);
	}
}
