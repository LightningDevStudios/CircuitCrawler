package com.lds.game.puzzle;

import com.lds.game.puzzle.IPuzzle;
import com.lds.game.puzzle.event.*;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class PuzzleActivity extends Activity implements OnPuzzleInitializedListener, OnPuzzleSuccessListener, OnPuzzleFailListener
{
	
	public PuzzleSurface glSurface;
	public IPuzzle puzzle;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//Enable fullscreen
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		//Dynamically load a puzzle based on a passed-in string
		String className = getIntent().getExtras().getString("PUZZLE_RENDERER");
		className = "com.lds.puzzles.".concat(className);
		
		if (className == null)
			finishPuzzleFailed();
				
		try 
		{
			puzzle = (IPuzzle)(Class.forName(className, true, Thread.currentThread().getContextClassLoader()).newInstance());
		} 
		catch (InstantiationException e) { finishPuzzleFailed(); }
		catch (IllegalAccessException e) { finishPuzzleFailed(); }
		catch (ClassNotFoundException e) { finishPuzzleFailed(); }
		if(puzzle != null)
		{
		Object syncObj = new Object();
		
		puzzle.setContext(this);
		puzzle.setSyncObj(syncObj);
		puzzle.setPuzzleFailEvent(this);
		puzzle.setPuzzleInitializedEvent(this);
		puzzle.setPuzzleSuccessEvent(this);
		glSurface = new PuzzleSurface(this, puzzle, syncObj);
		setContentView(glSurface);
		}
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
		System.out.println("FAILURE");
		this.setResult(RESULT_CANCELED);
		finish();
	}
	
	public void finishPuzzleSuccess()
	{
		System.out.println("SUCCESS");
		this.setResult(RESULT_OK);
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
