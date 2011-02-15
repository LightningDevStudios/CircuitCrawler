package com.lds.parser;

import java.util.HashMap;

public class CharacterData extends PhysEntData
{
	private int health, msPassed;
	private boolean isFlashing;
	public CharacterData(HashMap<String, String> characterHM)
	{
		super(characterHM);
		
		health = Integer.parseInt(characterHM.get("health"));
		msPassed = Integer.parseInt(characterHM.get("msPassed"));
		isFlashing = Boolean.parseBoolean(characterHM.get("isFlashing"));
	}
		
	public int getHealth()			{return health;}
	public int getMsPassed()		{return msPassed;}
	
	public void setHealth(int newHealth)		{health = newHealth;}
	public void setMsPassed(int newMsPassed)	{msPassed = newMsPassed;}
	
	public boolean getIsFlashing()	{return isFlashing;}
	
	public void setIsFlashing(boolean newIsFlashing) 	{isFlashing = newIsFlashing;}	
}
