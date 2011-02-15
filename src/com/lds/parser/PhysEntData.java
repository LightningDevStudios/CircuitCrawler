package com.lds.parser;

import java.util.HashMap;

public class PhysEntData extends EntityData
{
	private float moveSpeed, rotSpeed, sclSpeed;
	
	public PhysEntData(HashMap<String, String> physEntHM)
	{
		super(physEntHM);
		
		moveSpeed = Float.parseFloat(physEntHM.get("moveSpeed")); 
		rotSpeed = Float.parseFloat(physEntHM.get("rotSpeed"));
		sclSpeed = Float.parseFloat(physEntHM.get("sclSpeed"));
	}
	
	    //int setters/getters
	public void setMoveSpeed(float newMoveSpeed)	{moveSpeed = newMoveSpeed;}
	public void setRotSpeed(float newRotSpeed)		{rotSpeed = newRotSpeed;}
	public void setSclSpeed(float newSclSpeed)		{sclSpeed = newSclSpeed;}
	
	public float getMoveSpeed()		{return moveSpeed;}
	public float getRotSpeed()		{return	rotSpeed;}
	public float getSclSpeed()		{return sclSpeed;}
}
