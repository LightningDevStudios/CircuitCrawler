package com.lds.game.puzzle;

import com.lds.game.puzzle.IPuzzle;
import com.lds.game.puzzle.event.*;

import android.app.Activity;
import android.os.Bundle;

public class PuzzleActivity extends Activity implements OnPuzzleInitializedListener, OnPuzzleSuccessListener, OnPuzzleFailListener
{
	
	public PuzzleSurface glSurface;
	public IPuzzle puzzle;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		String className = getIntent().getExtras().getString("PUZZLE_RENDERER");
		className = "com.lds.puzzles.".concat(className);
		//if the className was not set, end the puzzle
		if (className == null)
		{
			finishPuzzleFailed();
		}
				
		try 
		{
			puzzle = (IPuzzle)(Class.forName(className, true, Thread.currentThread().getContextClassLoader()).newInstance());
		} 
		catch (InstantiationException e) { finishPuzzleFailed(); }
		catch (IllegalAccessException e) { finishPuzzleFailed(); }
		catch (ClassNotFoundException e) { finishPuzzleFailed(); }
		
		Object syncObj = new Object();
		
		puzzle.setContext(this);
		puzzle.setSyncObj(syncObj);
		glSurface = new PuzzleSurface(this, puzzle, syncObj);
		setContentView(glSurface);
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		glSurface.onPause();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		glSurface.onResume();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		finish();
	}
	
	public void finishPuzzleFailed()
	{
		this.setResult(0);
		finish();
	}
	
	public void finishPuzzleSuccess()
	{
		this.setResult(1);
		finish();
	}

	@Override
	public void onPuzzleFail() 
	{
		finishPuzzleFailed();
	}

	@Override
	public void onPuzzleSuccess() 
	{
		finishPuzzleSuccess();
	}

	@Override
	public void onInitialized() 
	{
		puzzle.setPuzzleFailEvent(this);
		puzzle.setPuzzleSuccessEvent(this);
	}
}
