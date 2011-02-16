package com.lds.game.puzzle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class PuzzleSurface extends GLSurfaceView
{
	private IPuzzle puzzleRenderer;
	private Object syncObj;
		
	public PuzzleSurface(Context context, IPuzzle puzzle, Object syncObj) 
	{
		super(context);
		this.syncObj = syncObj;
		puzzleRenderer = puzzle;
		setRenderer(puzzleRenderer);
	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		queueEvent(new Runnable()
		{
			public void run()
			{
				puzzleRenderer.onTouchEvent(event);
			}
		});
		
		synchronized(syncObj)
		{
			try
			{
				syncObj.wait(1000L);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		return true;
	}
}
