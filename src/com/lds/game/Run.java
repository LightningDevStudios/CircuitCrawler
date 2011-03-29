package com.lds.game;

import java.io.File;
import java.io.FileOutputStream;
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
		
		DisplayMetrics screen = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(screen);
		float screenX = (float)screen.widthPixels;
		float screenY = (float)screen.heightPixels;
		File isSong1 = new File("/sdcard/song1.mp3");
		File isSong2 = new File("/sdcard/song2.mp3");
		
		//set proper volume to adjust with +/- buttons
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		if(!isSong1.exists())
		{
			try 
			{
				copyFileSong1();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		if(!isSong2.exists())
		{
			try 
			{
				copyFileSong2();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		mp.reset();
        try {
			mp.setDataSource("/sdcard/song2.mp3");
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        mp.start();

        mp.setOnCompletionListener(new OnCompletionListener() 
        {
                public void onCompletion(MediaPlayer arg0) 
                {
                	mp.reset();
                    try {
						mp.setDataSource("/sdcard/song2.mp3");
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    try {
						mp.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    mp.start();   
                }
        });
		

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
		
		setContentView(glSurface);
	}
		

	public void copyFileSong1() throws IOException
	{ 
        OutputStream copyFilesStream = new FileOutputStream("/sdcard/"); 
        InputStream copyFilesInputStream = context.getResources().openRawResource(R.raw.song1); 
        byte[] buffer = new byte[1]; 
        int length; 
        while ((length = copyFilesInputStream.read(buffer)) > 0 ) 
        { 
        		copyFilesStream.write(buffer); 
                Log.w("Bytes: ", ((Integer)length).toString()); 
                Log.w("value", buffer.toString()); 
        } 
        //Close the streams 
        copyFilesStream.flush(); 
        copyFilesStream.close(); 
        copyFilesInputStream.close(); 
	} 
	
	public void copyFileSong2() throws IOException
	{ 
        OutputStream copyFilesStream = new FileOutputStream("/sdcard/"); 
        InputStream copyFilesInputStream = context.getResources().openRawResource(R.raw.song2); 
        byte[] buffer = new byte[1]; 
        int length; 
        while ((length = copyFilesInputStream.read(buffer)) > 0 ) 
        { 
        		copyFilesStream.write(buffer); 
                Log.w("Bytes: ", ((Integer)length).toString()); 
                Log.w("value", buffer.toString()); 
        } 
        //Close the streams 
        copyFilesStream.flush(); 
        copyFilesStream.close(); 
        copyFilesInputStream.close(); 
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
