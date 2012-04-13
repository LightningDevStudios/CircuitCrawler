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

package com.ltdev.cc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ltdev.LevelSurfaceView;
import com.ltdev.cc.event.*;
import com.ltdev.cc.puzzle.PuzzleActivity;

import java.io.IOException;

/**
 * An activity that loads and plays a level.
 * @author Lightning Development Studios
 */
public class LevelActivity extends Activity implements GameOverListener, GameInitializedListener, PuzzleActivatedListener
{
	public static final int PUZZLE_ACTIVITY = 2;
	private int unlockedLevel, levelIndex, levelId;
	private LevelSurfaceView glSurface;
	private LevelRenderer levelRenderer;
	private MediaPlayer mp;
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		pd = ProgressDialog.show(this, "", "Loading...");
		
		mp = new MediaPlayer();
		
		levelIndex = getIntent().getExtras().getInt("levelIndex", -1);
		unlockedLevel = getIntent().getExtras().getInt("unlockedLevel", -1);
		
		if (levelIndex == -1 || unlockedLevel == -1)
			finish();
	
		switch (levelIndex)
		{
			case 0:
				levelId = R.xml.level1;
				break;
			case 1:
				levelId = R.xml.level2;
				break;
			case 2:
				levelId = R.xml.level3;
				break;
			case 3:
				levelId = R.xml.level4;
				break;
			case 4:
				levelId = R.xml.level5;
				break;
			case 5:
				levelId = R.xml.level6;
				break;
			case 6:
				levelId = R.xml.level7;
				break;
			case 7:
				levelId = R.xml.level8;
				break;
			case 8:
				levelId = R.xml.level9;
				break;
			case 9:
				levelId = R.xml.level10;
			default:
			    break;
		}
		
		//Grab screen information
		DisplayMetrics screen = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(screen);
		float screenX = (float)screen.widthPixels;
		float screenY = (float)screen.heightPixels;
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		int songChoice = (int)(Math.random() * 2);
		if (songChoice == 0)
		    playMusic(R.raw.song1);
		else if (songChoice == 1)
		    playMusic(R.raw.song2);
		
		
		//set up OpenGL rendering
		Object syncObj = new Object();
		levelRenderer = new LevelRenderer(screenX, screenY, this, syncObj, levelId);
				
		levelRenderer.setGameInitializedEvent(this);
		glSurface = new LevelSurfaceView(this, levelRenderer, syncObj);
		
		setContentView(glSurface);
	}
	
    /**
     * \todo These hax should be loaded from the constructor.
     */
	public void onGameInitialized()
	{
		levelRenderer.setGameOverEvent(this);
		levelRenderer.setPuzzleActivatedEvent(this);

	    pd.dismiss();
	}
	
	/**
	 * Called when the game is over.
	 * @param winning Determines whether the game was won or lost.
	 */
	public void onGameOver(boolean winning)
	{
		//mp.stop();
		//mp.reset();
		if (winning)
		{
			if (levelIndex == unlockedLevel)
				setResult(2);
			else
				setResult(1);
			
			finish();
		}
		else
		{
			setResult(100 + levelIndex);
			finish();
		}
	}
	
	/**
	 * Callback for when a puzzle is started.
	 */
	public void onPuzzleActivated()
	{
		levelRenderer.clearTouchInput();
		//glSurface.onPause();
		Intent i = new Intent(LevelActivity.this, PuzzleActivity.class);
		i.putExtra("PUZZLE_RENDERER", "circuit.CircuitPuzzle");
		startActivityForResult(i, PUZZLE_ACTIVITY);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		//glSurface.onResume();
		//setContentView(glSurface);
		switch(requestCode)
		{
		case PUZZLE_ACTIVITY:
			if (resultCode == RESULT_OK)
				glSurface.onPuzzleFailed();
			else
				glSurface.onPuzzleWon();
			break;
		default:
		}
	}
	
	/**
	 * Plays a background audio track from a resource.
	 * @param resource The resource ID for the audio track to be played. (e.g. R.raw.song1)
	 */
	public void playMusic(int resource)
	{	
		AssetFileDescriptor song1 = this.getResources().openRawResourceFd(resource); 
	    try 
		{
		    mp.reset();
		    mp.setDataSource(song1.getFileDescriptor(), song1.getStartOffset(), song1.getDeclaredLength());
            mp.prepare();
            mp.start();
        } 
		catch (IllegalStateException e) 
		{
            e.printStackTrace();
        }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		levelRenderer.setPausedState(true);
		return true;
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu)
	{
		super.onOptionsMenuClosed(menu);
		levelRenderer.setPausedState(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.restart:
				//restart game
				setResult(100 + levelIndex);
				finish();
				return true;
			case R.id.main_menu:
				//return to main menu
				//mp.stop();
				setResult(0);
				finish();
				return true;
			case R.id.quit:
				//mp.stop();
				setResult(3);
				finish();
				return true;
			default:
				//mp.start();
				return super.onOptionsItemSelected(item);
		
		}
	}
	
	@Override
	public void onBackPressed()
	{
		//mp.reset();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		mp.start();
		glSurface.onResume();	
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		glSurface.onPause();
	    pd.dismiss();
		mp.pause();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mp.stop();
		mp.release();
	}
}
