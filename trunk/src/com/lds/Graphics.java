package com.lds;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import javax.microedition.khronos.opengles.GL10;

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
		//test
		//TODO pass e instead of e.getX() and e.getY()
		renderer.onTouchInput( e.getX(), e.getY());
		return true;
	}
	
	public interface Renderer extends GLSurfaceView.Renderer
	{
		//TODO Pass in Gl10, so we can use the renderer.
		public abstract void onTouchInput(/*GL10 gl,*/ float xInput, float yInput);
	}
}
