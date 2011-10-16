package com.lds.game.entity;

import com.lds.game.SoundPlayer;
import com.lds.math.Vector2;

public class Teleporter extends PhysEnt
{
	protected boolean active;
	private TeleporterLinker tpLink;
	
	public Teleporter(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide) 
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide, 0, 0, 0, 0);
		active = true;
	}
	public Teleporter(float size, float xPos, float yPos) 
	{
		super(size, xPos, yPos, 0.0f, 1.0f, 1.0f, true, false, false, 0, 0, 0, 0);
		active = true;
	}
	public  Vector2 getPos()
	{
		return new Vector2(getXPos(), getYPos());
	}
	
	@Override
	public void interact(Entity ent)
	{		
		if (active && ent instanceof PhysEnt && tpLink != null)
		{
			if (ent instanceof HoldObject && ((HoldObject)ent).isHeld())
				return;
			Vector2 newPos = tpLink.getLinkedPos(this);
			if (newPos != null)
			{
				((PhysEnt)ent).setPosNoInterp(newPos);
				SoundPlayer.getInstance().playSound(SoundPlayer.TELEPORT);
			}
		}
	}
	
	@Override
	public void uninteract(Entity ent)		
	{
		active = true;
	}
	
	@Override
	public boolean isColliding(Entity ent)
	{
		if (ent instanceof Tile || ent instanceof StaticEnt)
			return false;
		
		return  super.isColliding(ent);
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
