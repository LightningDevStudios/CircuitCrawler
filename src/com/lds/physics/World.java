package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.*;

public class World 
{
	public float Area;
	public ArrayList<Entity> entList = new ArrayList<Entity>();
	
	public World(float Area, ArrayList<Entity> entList)
	{
		this.Area = Area;	
		this.entList = entList;
	}
}
