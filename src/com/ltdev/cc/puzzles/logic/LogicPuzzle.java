package com.ltdev.cc.puzzles.logic;

import android.content.Context;
import android.view.MotionEvent;

import com.ltdev.cc.event.PuzzleFailListener;
import com.ltdev.cc.event.PuzzleInitializedListener;
import com.ltdev.cc.event.PuzzleSuccessListener;
import com.ltdev.cc.puzzle.IPuzzle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class LogicPuzzle implements IPuzzle
{
	public void onDrawFrame(GL10 arg0)
	{
		// \TODO Auto-generated method stub
		
	}

	public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) 
	{
		// \TODO Auto-generated method stub
		
	}

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

	public void onTouchEvent(MotionEvent event)
	{
		//float xPos = event.getX();
		//float yPos = event.getY();
	}

	public void setContext(Context context) 
	{
		// \TODO Auto-generated method stub
	}

	public void setSyncObj(Object o) 
	{
		// \TODO Auto-generated method stub
	}

	public void setPuzzleInitializedEvent(PuzzleInitializedListener listener)
	{
		// \TODO Auto-generated method stub	
	}

	public void setPuzzleSuccessEvent(PuzzleSuccessListener listener) 
	{
		// \TODO Auto-generated method stub		
	}

	public void setPuzzleFailEvent(PuzzleFailListener listener)
	{
		// \TODO Auto-generated method stub		
	}
	
	public void createPuzzle()
	{
		
	}
}
