package com.lds.game.entity;

import com.lds.Vector2f;

public class TeleporterLinker 
{
	protected Vector2f link1Pos, link2Pos;
	protected Teleporter entTele1, entTele2;
	protected boolean oneWay;
	
	public TeleporterLinker(Teleporter ent, Teleporter ent2, boolean isOneWay)
	{
		link1Pos = new Vector2f(ent.getPos());
		link2Pos = new Vector2f(ent2.getPos());
		entTele1 = ent;
		entTele2 = ent2;
		oneWay = isOneWay;
	}
	
	public Vector2f getLinkedPos(Teleporter t)
	{
		if (t == entTele1)
		{
			return entTele2.getPos();
		}
		else if (t == entTele2)
		{
			return entTele1.getPos();
		}
		else
		{
			return null;
		}
	}
}
