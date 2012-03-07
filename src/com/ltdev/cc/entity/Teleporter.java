package com.ltdev.cc.entity;


import javax.microedition.khronos.opengles.GL11;

import com.ltdev.Stopwatch;
import com.ltdev.cc.SoundPlayer;
import com.ltdev.cc.physics.primitives.Circle;
import com.ltdev.math.Vector2;

public class Teleporter extends Entity
{
	protected boolean active;
	private TeleporterLinker tpLink;
	private float rotationPerUpdate;
	
	public Teleporter(float size, Vector2 position) 
    {
        super(new Circle(size, position, false));
        active = true;
        
        this.tilesetX = 2;
        this.tilesetY = 2;
        
        rotationPerUpdate = 0.01f;
    }

	@Override
	public void interact(Entity ent)
	{
	    super.interact(ent);
	    
		if (active && tpLink != null)
		{
			if (ent instanceof HoldObject && ((HoldObject)ent).isHeld())
				return;
			
			Vector2 newPos = tpLink.getLinkedPos(this);
			if (newPos != null)
			{
				ent.shape.setPos(tpLink.getLinkedPos(this));
                SoundPlayer.playSound(SoundPlayer.TELEPORT);
			}
		}
		rotationPerUpdate += 0.1f;
	}
	
	@Override
	public void update(GL11 gl)
    {
	    super.update(gl);
	    
        this.shape.setAngle(this.shape.getAngle() + rotationPerUpdate);
    }
	
	@Override
	public void uninteract(Entity ent)		
	{
	    super.uninteract(ent);
		active = true;
		rotationPerUpdate = 0.01f;
	}
	
	public void setActive(boolean bool)
	{
		active = bool;
	}
	
	public void setTeleporterLinker(TeleporterLinker tpLink)
	{
		this.tpLink = tpLink;
	}
}
