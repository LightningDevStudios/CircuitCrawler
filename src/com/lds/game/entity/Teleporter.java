package com.lds.game.entity;

import com.lds.game.SoundPlayer;
import com.lds.math.Vector2;
import com.lds.physics.Circle;

public class Teleporter extends Entity
{
	protected boolean active;
	private TeleporterLinker tpLink;
	
	public Teleporter(float size, Vector2 position) 
    {
        super(new Circle(size, position, false));
        active = true;
    }

	@Override
	public void interact(Entity ent)
	{		
		if (active && tpLink != null)
		{
			if (ent instanceof HoldObject && ((HoldObject)ent).isHeld())
				return;
			
			Vector2 newPos = tpLink.getLinkedPos(this);
			if (newPos != null)
			{
				ent.shape.setPos(tpLink.getLinkedPos(this));
				SoundPlayer.getInstance().playSound(SoundPlayer.TELEPORT);
			}
		}
	}
	
	@Override
	public void uninteract(Entity ent)		
	{
		active = true;
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
