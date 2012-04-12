package com.ltdev.cc.entity;

import com.ltdev.Stopwatch;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

import javax.microedition.khronos.opengles.GL11;

public class Door extends Entity
{
    private boolean open;
    private Vector2 targetPos;
    private Vector2 openedPosition, closedPosition;
    
	public Door(Vector2 position)
	{
	    this(72, position);
	}
	
	public Door(float size, Vector2 position)
    {
        super(new Rectangle(new Vector2(size - 10, size), position, 0, true));
        shape.setStatic(true);
        colorInterpSpeed = 1.0f;
        
        open = false;
        targetPos = Vector2.ZERO;
        openedPosition = position;
        closedPosition = new Vector2(openedPosition.x() - 72, openedPosition.y());
        
        this.tilesetX = 2;
        this.tilesetY = 1;
    }

	@Override
	public void update(GL11 gl)
	{
	    super.update(gl);
	    
	    float frameTime = (float)Stopwatch.getFrameTime() / 1000f;
	    if (open)
	        targetPos = Vector2.add(shape.getPos(), Vector2.scale(Vector2.subtract(closedPosition, shape.getPos()), frameTime));
	    else
            targetPos = Vector2.add(shape.getPos(), Vector2.scale(Vector2.subtract(openedPosition, shape.getPos()), frameTime));
	    shape.setPos(targetPos);
	}
	
	public void open()
	{
	    if (!open)
	    {
	        open = true;
	    }
	}
	
	public void close()
	{	
	    if (open)
        {
	        open = false;
        }
	}
}
