package com.lds;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class Graphics extends GLSurfaceView
{
	private Renderer renderer;
	
	public Graphics(Context context, Renderer r) 
	{
		super(context);
		renderer = r;
		setRenderer(r);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		//grab touch input, pass it through to the generic renderer (in this case, com.lds.GameRenderer
		renderer.onTouchInput(e);
		return true;
	}
	
	public interface Renderer extends GLSurfaceView.Renderer
	{
		//TODO Pass in Gl10, so we can use the renderer.
		public abstract void onTouchInput(MotionEvent e);
	}
}
