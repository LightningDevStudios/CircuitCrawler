/**
 * Copyright (c) 2010-2012 Lightning Development Studios <lightningdevelopmentstudios@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ltdev.cc.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ltdev.cc.event.PuzzleFailListener;
import com.ltdev.cc.event.PuzzleInitializedListener;
import com.ltdev.cc.event.PuzzleSuccessListener;

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
		className = "com.ltdev.cc.puzzles.".concat(className);
		
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
