package com.lds.game.entity;

import com.lds.Vector2f;

public class TeleporterLinker 
{
	protected static Vector2f link1Pos;
	protected static Vector2f link2Pos;
	protected static Object entTele1;
	protected static Object entTele2;
	
	public TeleporterLinker(Object ent, Object ent2)
	{
		link1Pos = new Vector2f(((Teleporter) ent).getPos());
		link2Pos = new Vector2f(((Teleporter) ent2).getPos());
		entTele1 = ent;
		entTele2 = ent2;
	}
	
	public static  Vector2f getLinkedPos(Object ent3)
	{
		if(ent3.equals(entTele1))
		{
			((Teleporter) entTele1).setActive(false);
			((Teleporter) entTele2).setActive(true);
			return link2Pos;
		}
		else if(ent3.equals(entTele2))
		{
			((Teleporter) entTele2).setActive(false);
			((Teleporter) entTele1).setActive(true);
			return link1Pos;
		}
		else {return null;}
	}
}
