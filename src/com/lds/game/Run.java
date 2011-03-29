package com.lds.game;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.lds.Graphics;
import com.lds.game.event.*;
import com.lds.game.menu.MainMenu;
import com.lds.game.puzzle.PuzzleActivity;

public class Run extends Activity implements OnGameOverListener, OnGameInitializedListener, OnPuzzleActivatedListener
{
	public static final int PUZZLE_ACTIVITY = 2;
	
	public Graphics glSurface;
	public GameRenderer gameR;
	public static boolean songOver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		 Thread background = new Thread (new Runnable()
		 {
	           public void run() 
	           {
	        	   //ProgressDialog pDialog = ProgressDialog.show(Run.this, "", "Loading...");
	        	   MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.song2); 
	        	   mp.start();
	           }
	        });
	    background.start();
		
		//RefreshHandler = new RefreshHandler();
		//new PlaySong().execute(mp);
		
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
		gameR = new GameRenderer(screenX, screenY, this, syncObj);
		
		if(data != null)
		{
			gameR.game = (Game)data;
		}
		
		gameR.setGameInitializedEvent(this);
		glSurface = new Graphics(this, gameR, syncObj);
		
		//pDialog.dismiss();
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
