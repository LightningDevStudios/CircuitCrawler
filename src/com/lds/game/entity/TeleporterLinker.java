package com.lds.game.entity;

import com.lds.Vector2f;

public class TeleporterLinker 
{
	protected static Vector2f link1Pos, link2Pos, link3Pos;
	protected static Teleporter entTele1, entTele2, entTele3;
	protected static boolean oneWay, threeWay;
	
	public TeleporterLinker(Teleporter ent, Teleporter ent2, boolean isOneWay, boolean isThreeWay)
	{
		link1Pos = new Vector2f(ent.getPos());
		link2Pos = new Vector2f(ent2.getPos());
		entTele1 = ent;
		entTele2 = ent2;
		oneWay = isOneWay;
		threeWay = isThreeWay;
	}
	
	public TeleporterLinker(Teleporter ent, Teleporter ent2, Teleporter ent3, boolean isOneWay, boolean isThreeWay)
	{
		link1Pos = new Vector2f(ent.getPos());
		link2Pos = new Vector2f(ent2.getPos());
		link3Pos = new Vector2f(ent3.getPos());
		entTele1 = ent;
		entTele2 = ent2;
		entTele3 = ent3;
		oneWay = isOneWay;
		threeWay = isThreeWay;
	}
	
	public static  Vector2f getLinkedPos(Teleporter ent3)
	{
		if(ent3.equals(entTele1))
		{
			entTele1.setActive(false);
			entTele2.setActive(true);
			return link2Pos;
		}
		else if(ent3.equals(entTele2))
		{
			{
				if(oneWay)
				{
					entTele1.setActive(false);
					entTele2.setActive(true);
					return link2Pos;
				}
				else if(threeWay)
				{
					entTele1.setActive(false);
					entTele2.setActive(false);
					entTele3.setActive(true);
					return link3Pos;
				}
				else
				{
					entTele2.setActive(false);
					entTele1.setActive(true);
					return link1Pos;
				}
			}
		}
		else if(ent3.equals(entTele3))
		{
			return link3Pos;
		}
		else {return null;}
	}
	
	public void setOneWay(boolean bool)
	{
		oneWay = bool;
	}
}
