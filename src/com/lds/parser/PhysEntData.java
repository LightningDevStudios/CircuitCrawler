package com.lds.parser;

import java.util.HashMap;

public class PhysEntData extends EntityData
{
	protected float moveSpeed;
	protected float rotSpeed;
	protected float sclSpeed;
	
	public PhysEntData(HashMap<String, String> physEntHM)
	{
		super(physEntHM);
		
		if(physEntHM.get("moveSpeed") != null)
			moveSpeed = Float.parseFloat(physEntHM.get("moveSpeed")); 
		else
			moveSpeed = 0.0f;
		if(physEntHM.get("rotSpeed") != null)
			rotSpeed = Float.parseFloat(physEntHM.get("rotSpeed"));
		else
			rotSpeed = 0.0f;
		if(physEntHM.get("sclSpeed") != null)
			sclSpeed = Float.parseFloat(physEntHM.get("sclSpeed"));
		else
			sclSpeed = 0.0f;
	}
	
	    //int setters/getters
	public void setMoveSpeed(float newMoveSpeed)	{moveSpeed = newMoveSpeed;}
	public void setRotSpeed(float newRotSpeed)		{rotSpeed = newRotSpeed;}
	public void setSclSpeed(float newSclSpeed)		{sclSpeed = newSclSpeed;}
	
	public float getMoveSpeed()		{return moveSpeed;}
	public float getRotSpeed()		{return	rotSpeed;}
	public float getSclSpeed()		{return sclSpeed;}
}
