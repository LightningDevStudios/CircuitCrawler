package com.ltdev.cc.entity;

import com.ltdev.Direction;
import com.ltdev.Stopwatch;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

public class SpikeWall extends Entity
{	
    private boolean extended;
    private Vector2 targetPos, initialPos, endPos;
    
	public SpikeWall(float size, Vector2 position, Direction dir)
    {
        super(new Rectangle(new Vector2(size, size), position, 270, true));
        shape.setStatic(true);
        
        extended = false;
        initialPos = position;
        if (dir == Direction.LEFT)
        {
            endPos = new Vector2(position.x() - 72, position.y());
            shape.setAngle(270);
        }
        else if (dir == Direction.RIGHT)
        {
            endPos = new Vector2(position.x() + 72, position.y());
            shape.setAngle(90);
        }
        else if (dir == Direction.DOWN)
        {
            endPos = new Vector2(position.x(), position.y() - 72);
            shape.setAngle(0);
        }
        else if (dir == Direction.UP)
        {
            endPos = new Vector2(position.x() + 72, position.y() + 72);
            shape.setAngle(180);
        }

        this.tilesetX = 3;
        this.tilesetY = 1;
    }
	
	@Override
	public void update(GL11 gl)
	{
	    super.update(gl);
	    
	    float frameTime = (float)Stopwatch.getFrameTime() / 1000f;

        if (extended)
            targetPos = Vector2.add(shape.getPos(), Vector2.scale(Vector2.subtract(initialPos, shape.getPos()), frameTime));
        else
            targetPos = Vector2.add(shape.getPos(), Vector2.scale(Vector2.subtract(endPos, shape.getPos()), frameTime * 10));
        shape.setPos(targetPos);
        
        if (targetPos.approxEquals(initialPos, 2))
            extended = false;
        if (targetPos.approxEquals(endPos, 1))
            extended = true;
	}
	
	@Override
	public void interact(Entity ent)
	{
	    super.interact(ent);
	    
	    if (ent instanceof Player)
	        Player.kill();
	}
}
