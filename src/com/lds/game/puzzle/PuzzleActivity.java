package com.lds.game.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.lds.game.event.PuzzleFailListener;
import com.lds.game.event.PuzzleInitializedListener;
import com.lds.game.event.PuzzleSuccessListener;

public class PuzzleActivity extends Activity implements PuzzleInitializedListener, PuzzleSuccessListener, PuzzleFailListener
{
	
	private PuzzleSurface glSurface;
	private IPuzzle puzzle;
	
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
		
		catch (InstantiationException e)
		{
		    finishPuzzleFailed();
		}
		
		catch (IllegalAccessException e)
		{
		    finishPuzzleFailed();
		}
		
		catch (ClassNotFoundException e)
		{
		    finishPuzzleFailed();
		}
		
		if (puzzle != null)
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
	
	/**
	 * Returns to the main game with a failed result.
	 */
	public void finishPuzzleFailed()
	{
		this.setResult(RESULT_CANCELED);
		finish();
	}
	
	/**
	 * Returns to the game with a success result.
	 */
	public void finishPuzzleSuccess()
	{
		this.setResult(RESULT_OK);
		finish();
	}

	public void onPuzzleFail() 
	{
		finishPuzzleFailed();
	}

	public void onPuzzleSuccess() 
	{
		finishPuzzleSuccess();
	}

	public void onInitialized() 
	{
		puzzle.setPuzzleFailEvent(this);
		puzzle.setPuzzleSuccessEvent(this);
	}
}
