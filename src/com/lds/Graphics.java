package com.lds;

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
	public boolean onTouchEvent(MotionEvent e)
	{
		//grab touch input, pass it through to the generic renderer (in this case, com.lds.GameRenderer
		renderer.onTouchInput(e);
		
		//sync with OpenGL thread
		synchronized(syncObj)
		{
			try
			{
				syncObj.wait();
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
		}
		return true;
	}
	
	public interface Renderer extends GLSurfaceView.Renderer
	{
		public abstract void onTouchInput(MotionEvent e);
	}
}
