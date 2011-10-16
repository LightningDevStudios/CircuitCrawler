package com.lds.game.entity;

import com.lds.game.event.OnPuzzleActivatedListener;
import com.lds.math.Vector2;
import com.lds.physics.Rectangle;

public class PuzzleBox extends Entity
{
	private OnPuzzleActivatedListener listener;
	
	public PuzzleBox(float size, Vector2 position)
	{
		super(new Rectangle(size, position, true));
	}
	
	public PuzzleBox(float size, Vector2 position, float angle, Vector2 scale)
	{
		super(new Rectangle(size, position, angle, scale, true));
	}
	
	public void setPuzzleInitListener(OnPuzzleActivatedListener listener)
	{
		this.listener = listener;
	}
	
	public void run()
	{
		if (listener != null)
			listener.onPuzzleActivated();
	}
}
