package com.lds.physics;

import java.util.ArrayList;

import com.lds.game.entity.Entity;

public class QuadTreeList 
{
	private ArrayList<ArrayList<Entity>> normalEntity;
	private ArrayList<ArrayList<Entity>> onLineEntity;
	
	public QuadTreeList(ArrayList<ArrayList<Entity>> normalEntity, ArrayList<ArrayList<Entity>> onLineEntity)
	{
		this.normalEntity = normalEntity;
		this.onLineEntity = onLineEntity;
	}
}
