package com.lds.game;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;



public class Run extends Activity
{
	public GLSurfaceView glSurface;
	private float screenX, screenY;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//Grab screen information to initialize local entity ArrayList
		DisplayMetrics screen = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(screen);
		screenX = (float)screen.widthPixels;
		screenY = (float)screen.heightPixels;
		
		//Enable fullscreen
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//set up OpenGL rendering
		glSurface = new GLSurfaceView(this);
		glSurface.setRenderer(new GameRenderer(screenX, screenY));
		setContentView(glSurface);
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume();
		glSurface.onResume();
	}
	
	@Override
	protected void onPause ()
	{
		super.onPause();
		glSurface.onPause();
	}
}
