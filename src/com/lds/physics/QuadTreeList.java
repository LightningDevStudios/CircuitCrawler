package com.lds.physics;

import com.lds.game.entity.Entity;

import java.util.ArrayList;

public class QuadTreeList 
{
	private ArrayList<ArrayList<Entity>> normalEntity;
	private ArrayList<ArrayList<Entity>> onLineEntity;
	
	public QuadTreeList(ArrayList<ArrayList<Entity>> normalEntity, ArrayList<ArrayList<Entity>> onLineEntity)
	{
		this.setNormalEntity(normalEntity);
		this.setOnLineEntity(onLineEntity);
	}

	public ArrayList<ArrayList<Entity>> getOnLineEntity()
	{
	    return onLineEntity;
	}
	
	public void setOnLineEntity(ArrayList<ArrayList<Entity>> onLineEntity)
	{
	    this.onLineEntity = onLineEntity;
	}
	
	public ArrayList<ArrayList<Entity>> getNormalEntity()
	{
	    return normalEntity;
	}
	
	public void setNormalEntity(ArrayList<ArrayList<Entity>> normalEntity)
	{
	    this.normalEntity = normalEntity;
	}
}
