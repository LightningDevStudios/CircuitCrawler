package com.lds.game;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.lds.Graphics;
import com.lds.game.event.*;
import com.lds.game.menu.MainMenu;
import com.lds.game.puzzle.PuzzleActivity;

public class Run extends Activity implements OnGameOverListener, OnGameInitializedListener, OnPuzzleActivatedListener
{
	public static final int PUZZLE_ACTIVITY = 2;
	public static int unlockedLevel = 0;
	public static int levelIndex = 0;
	public int levelId;
	public Bundle savedInstanceState;
	
	public Graphics glSurface;
	public GameRenderer gameR;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		
		switch (levelIndex)
		{
			case 0:
				levelId = R.xml.level1;
				break;
			case 1:
				levelId = R.xml.level2;
		}
		
		//Grab screen information
		DisplayMetrics screen = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(screen);
		float screenX = (float)screen.widthPixels;
		float screenY = (float)screen.heightPixels;
		
		//set proper volume to adjust with +/- buttons
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		//retrieve game if the activity restarts
		final Object data = getLastNonConfigurationInstance();
		
		//set up OpenGL rendering
		Object syncObj = new Object();
		gameR = new GameRenderer(screenX, screenY, this, syncObj, levelId);
		
		if(data != null)
		{
			gameR.game = (Game)data;
		}
		
		gameR.setGameInitializedEvent(this);
		glSurface = new Graphics(this, gameR, syncObj);
		setContentView(glSurface);
	}
	
	@Override
	public void onGameInitialized()
	{
		gameR.setGameOverEvent(this);
		gameR.setPuzzleActivatedEvent(this);
	}
	
	@Override
	public void onGameOver()
	{
		//Intent i = new Intent(Run.this, MainMenu.class);
		//startActivity(i);
		finish();
	}
	
	@Override
	public void onPuzzleActivated()
	{
		gameR.clearTouchInput();
		//glSurface.onPause();
		Intent i = new Intent(Run.this, PuzzleActivity.class);
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
			if(resultCode == RESULT_OK)
				glSurface.onPuzzleFailed();
			else
				glSurface.onPuzzleWon();
			break;
		default:
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		gameR.paused = true;
		return true;
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu)
	{
		super.onOptionsMenuClosed(menu);
		gameR.paused = false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.restart:
				//restart game
				onCreate(savedInstanceState);
				return true;
			case R.id.main_menu:
				//return to main menu
				Intent i = new Intent(Run.this, MainMenu.class);
				startActivity(i);
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		
		}
	}
	
	@Override
	public void onBackPressed()
	{
		
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
		//finish();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		final Game game = gameR.game;
		return game;
	}
}
