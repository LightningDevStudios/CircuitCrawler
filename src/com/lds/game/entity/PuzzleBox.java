package com.lds.game.entity;

import com.lds.game.event.PuzzleActivatedListener;
import com.lds.math.Vector2;
import com.lds.physics.primatives.Rectangle;

public class PuzzleBox extends Entity
{
	private PuzzleActivatedListener listener;
	
	public PuzzleBox(float size, Vector2 position)
	{
		super(new Rectangle(new Vector2(size, size), position, true));
	}
	
	public void setPuzzleInitListener(PuzzleActivatedListener listener)
	{
		this.listener = listener;
	}
	
	public void run()
	{
		if (listener != null)
			listener.onPuzzleActivated();
	}
}
