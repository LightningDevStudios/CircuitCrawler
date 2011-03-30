package com.lds.game;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.lds.Graphics;
import com.lds.Stopwatch;
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
	public static boolean songOver;
	public static float timer = 0;
	public Context context;
	private MediaPlayer mp = new MediaPlayer();
	
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
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		//Copy mp3s from raw to /sdcard/
		try 
		{
			copyFileSong2();
			mp.setDataSource("/sdcard/song2.mp3");
			mp.prepare();
			mp.setVolume(SoundPlayer.musicVolume, SoundPlayer.musicVolume);
	        mp.start();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

        mp.setOnCompletionListener(new OnCompletionListener() 
        {
                public void onCompletion(MediaPlayer mp) 
                {
                	try
                	{
	                	mp.reset();
	                	mp.prepare();
	        			mp.setVolume(SoundPlayer.musicVolume, SoundPlayer.musicVolume);
	        	        mp.start();
	                }
	                catch (Exception e) 
	        		{
	        			e.printStackTrace();
	        		}
                }
        });
		

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
	
	public void copyFileSong2() throws IOException
	{ 
		File f = new File(Environment.getExternalStorageDirectory(), "song2.mp3");
		try 
		{
		    if (f.createNewFile())
		    {
		    FileWriter write = new FileWriter(f);
		    BufferedWriter buff = new BufferedWriter(write);
		    BufferedInputStream AudioReader = new BufferedInputStream(getResources().openRawResource(R.raw.song2));
		    while(AudioReader.available() > 0 )
		    {
		    	buff.write(AudioReader.read());
		    }
		    buff.close();
		    }
		}
		catch (IOException e) 
		{
	    e.printStackTrace();
		}
	}
	
	
	@Override
	public void onGameInitialized()
	{
		gameR.setGameOverEvent(this);
		gameR.setPuzzleActivatedEvent(this);
	}
	

	
	@Override
	public void onGameOver(boolean winning)
	{
		//Intent i = new Intent(Run.this, MainMenu.class);
		//startActivity(i);
		if (winning && levelIndex > unlockedLevel)
			unlockedLevel++;
		mp.stop();
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
		mp.stop();
	}
	
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		final Game game = gameR.game;
		return game;
	}
}
