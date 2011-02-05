package com.lds;

import com.lds.game.event.*;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class Graphics extends GLSurfaceView
{
	private Renderer renderer;
	private Object syncObj;
	
	public Graphics(Context context, Renderer r, Object syncObj) 
	{
		super(context);
		this.syncObj = syncObj;
		renderer = r;
		setRenderer(r);
	}
	
	@Override
	public boolean onTouchEvent(final MotionEvent event)
	{
		//grab touch input, pass it through to the generic renderer (in this case, com.lds.GameRenderer
		queueEvent(new Runnable() 
		{
			public void run()
			{
				renderer.onTouchInput(event);
			}
		});
		
		//sync with OpenGL thread
		synchronized(syncObj)
		{
			try
			{
				syncObj.wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public void setGameOverEvent(final OnGameOverListener listener)
	{
		queueEvent(new Runnable()
		{
			public void run()
			{
				renderer.setGameOverEvent(listener);
			}
		});
	}
	
	public void setGameInitializedEvent(final OnGameInitializedListener listener)
	{
		queueEvent(new Runnable()
		{
			public void run()
			{
				renderer.setGameInitializedEvent(listener);
			}
		});
	}
			
	public interface Renderer extends GLSurfaceView.Renderer
	{
		public abstract void onTouchInput(MotionEvent event);
		public abstract void setGameOverEvent(OnGameOverListener listener);
		public abstract void setGameInitializedEvent(OnGameInitializedListener listener);
	}
}
