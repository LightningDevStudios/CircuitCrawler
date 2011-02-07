package com.lds.puzzles.circuit;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.lds.puzzle.IPuzzle;
import com.lds.puzzle.event.*;

public class CircuitPuzzle implements IPuzzle
{
	boolean puzzleWon = false;
	
	public boolean end() 
	{
		System.out.println("Finished, true");
		return puzzleWon;
	}

	public void init() 
	{
		System.out.println("initializing...");		
	}

	public void run() 
	{
		for (int i = 0; i < 10; i++)
		{
			System.out.println(i);
		}
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDrawFrame(GL10 gl) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnPuzzleInitializedListener(OnPuzzleInitializedListener listener) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnPuzzleSuccessListener(OnPuzzleSuccessListener listener) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnPuzzleFailListener(OnPuzzleFailListener listener) 
	{
		// TODO Auto-generated method stub
		
	}
}