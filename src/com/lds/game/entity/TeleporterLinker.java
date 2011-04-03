package com.lds.game.entity;

import com.lds.Vector2f;

public class TeleporterLinker 
{
	protected Teleporter entTele1, entTele2;
	protected boolean oneWay, active;
	
	public TeleporterLinker(Teleporter ent, Teleporter ent2, boolean isOneWay)
	{
		entTele1 = ent;
		entTele2 = ent2;
		oneWay = isOneWay;
		entTele1.setTeleporterLinker(this);
		entTele2.setTeleporterLinker(this);
	}
	
	public Vector2f getLinkedPos(Teleporter t)
	{
		if (t == entTele1)
		{
			entTele2.setActive(false);
			entTele1.setActive(true);
			return entTele2.getPos();
		}
		else if (t == entTele2 && !oneWay)
		{
			entTele1.setActive(false);
			entTele2.setActive(true);
			return entTele1.getPos();
		}
		else
		{
			return null;
		}
	}
}
