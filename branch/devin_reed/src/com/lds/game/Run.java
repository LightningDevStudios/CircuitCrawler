package com.lds.game;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class Run extends Activity
{
	private GLSurfaceView glSurface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		glSurface = new GLSurfaceView(this);
		glSurface.setRenderer(new GameRenderer());
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
