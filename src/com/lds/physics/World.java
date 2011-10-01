package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;
import com.lds.math.*;

public class World 
{
	public Vector2 size;
	public ArrayList<Entity> entList = new ArrayList<Entity>();
	
	public World(Vector2 size, ArrayList<Entity> entList)
	{
		this.size = size;	
		this.entList = entList;
	}
}
