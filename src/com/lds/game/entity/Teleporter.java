package com.lds.game.entity;

public class Teleporter extends StaticEnt
{
	protected float teleLinkXPos, teleLinkYPos;
	public Teleporter(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide, float newLinkXPos, float newLinkYPos) 
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
		teleLinkXPos = newLinkXPos;
		teleLinkYPos = newLinkYPos;
	}
	public float teleportX()
	{
		return teleLinkXPos;
	}
	public float teleportY()
	{
		return teleLinkYPos;
	}
}
