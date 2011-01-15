package com.lds.game;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.lds.Graphics;

public class Run extends Activity
{
	public Graphics glSurface;
	public Object syncObj;
	private float screenX;
	private float screenY;
	
	
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
		syncObj = new Object();
		glSurface = new Graphics(this, new GameRenderer(screenX, screenY, this, syncObj));
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
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}
