package com.lds.puzzles.logic;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.view.MotionEvent;

import com.lds.Texture;
import com.lds.TextureLoader;
import com.lds.game.R;
import com.lds.game.puzzle.IPuzzle;
import com.lds.game.puzzle.event.OnPuzzleFailListener;
import com.lds.game.puzzle.event.OnPuzzleInitializedListener;
import com.lds.game.puzzle.event.OnPuzzleSuccessListener;
import com.lds.Enums;

public class LogicPuzzle implements IPuzzle
{
	@Override
	public void onDrawFrame(GL10 arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig egl)
	{
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glClearColor(0.39f, 0.58f, 0.93f, 1.0f);
		
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDepthMask(false);
		gl.glEnable(GL10.GL_DITHER);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		createPuzzle();
	}

	@Override
	public void onTouchEvent(MotionEvent event)
	{
		float xPos = event.getX();
		float yPos = event.getY();
	}

	@Override
	public void setContext(Context context) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void setSyncObj(Object o) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void setPuzzleInitializedEvent(OnPuzzleInitializedListener listener)
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void setPuzzleSuccessEvent(OnPuzzleSuccessListener listener) 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void setPuzzleFailEvent(OnPuzzleFailListener listener)
	{
		// TODO Auto-generated method stub		
	}
	
	public void createPuzzle()
	{
	}
	
}
