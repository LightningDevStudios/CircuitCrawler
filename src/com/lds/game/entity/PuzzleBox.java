package com.lds.game.entity;

import com.lds.game.event.OnPuzzleActivatedListener;

public class PuzzleBox extends StaticEnt
{
	private OnPuzzleActivatedListener listener;
	
	public PuzzleBox(float size, float xPos, float yPos, boolean circular, boolean willCollide)
	{
		super(size, xPos, yPos, circular, willCollide);
	}
	
	public PuzzleBox(float size, float xPos, float yPos, float angle, float xScl, float yScl, boolean isSolid, boolean circular, boolean willCollide)
	{
		super(size, xPos, yPos, angle, xScl, yScl, isSolid, circular, willCollide);
	}
	
	public void setPuzzleInitListener(OnPuzzleActivatedListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	public void interact(Entity ent)
	{
		if (ent instanceof Player)
		{
			if (listener != null)
				listener.onPuzzleActivated();
		}
	}
}
