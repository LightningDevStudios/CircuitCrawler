package com.ltdev.cc.entity;

import com.ltdev.cc.event.PuzzleActivatedListener;
import com.ltdev.cc.physics.primitives.Rectangle;
import com.ltdev.math.Vector2;

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
