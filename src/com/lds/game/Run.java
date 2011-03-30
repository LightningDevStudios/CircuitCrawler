package com.lds.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
	public static boolean songOver;
	public static float timer = 0;
	public Context context;
	private MediaPlayer mp = new MediaPlayer();
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		
		switch (levelIndex)
		{
			case 0:
				levelId = R.xml.tutorial_level;
				break;
			case 1:
				levelId = R.xml.level2;
				break;
			case 2:
				levelId = R.xml.level5;
				break;
			case 3:
				levelId = R.xml.level2;
		}
		
		//Grab screen information
		DisplayMetrics screen = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(screen);
		float screenX = (float)screen.widthPixels;
		float screenY = (float)screen.heightPixels;
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		pd = ProgressDialog.show(this, "", "Loading...");
		//Copy mp3s from raw to /sdcard/
		try 
		{
			saveas(R.raw.song2);
			mp.setDataSource("/sdcard/circutCrawler/media/audio/songs/song2.mp3");
			if (SoundPlayer.enableMusic)
			{
				mp.prepare();
				mp.setVolume(SoundPlayer.musicVolume, SoundPlayer.musicVolume);
				mp.start();
			}
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
	                	if (SoundPlayer.enableMusic)
	        			{
	        				mp.prepare();
	        				mp.setDataSource("/sdcard/circutCrawler/media/audio/songs/song2.mp3");
	        				mp.setVolume(SoundPlayer.musicVolume, SoundPlayer.musicVolume);
	        				mp.start();
	        			}
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
	
	public boolean saveas(int ressound)
	{  
		 byte[] buffer=null;  
		 InputStream fIn = getBaseContext().getResources().openRawResource(ressound);  
		 int size=0;  
		  
		 try 
		 {  
			  size = fIn.available();  
			  buffer = new byte[size];  
			  fIn.read(buffer);  
			  fIn.close();  
		 } catch (IOException e) 
		 {  
		  return false;  
		 }  
		  
		 String path="/sdcard/circutCrawler/media/audio/songs/";  
		 String filename="song2"+".mp3";  
		  
		 boolean exists = (new File(path)).exists();  
		 if (!exists){new File(path).mkdirs();}  
		  
		 FileOutputStream save;  
		 try 
		 {  
			  save = new FileOutputStream(path+filename);  
			  save.write(buffer);  
			  save.flush();  
			  save.close();  
		 } 
		 catch (FileNotFoundException e) 
		 {  
		  return false;  
		 } 
		 catch (IOException e)
		 {    
		  return false;  
		 }      
		  
		 sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+path+filename)));  
		  
		 File k = new File(path, filename);  
		  
		 ContentValues values = new ContentValues();  
		 values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());  
		 values.put(MediaStore.MediaColumns.TITLE, "exampletitle");  
		 values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/ogg");  
		 values.put(MediaStore.Audio.Media.ARTIST, "cssounds ");  
		 values.put(MediaStore.Audio.Media.IS_RINGTONE, true);  
		 values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);  
		 values.put(MediaStore.Audio.Media.IS_ALARM, true);  
		 values.put(MediaStore.Audio.Media.IS_MUSIC, false);  
		  
		 //Insert it into the database  
		 this.getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath()), values);  
		  
		 return true;  
	}
	
	
	@Override
	public void onGameInitialized()
	{
		gameR.setGameOverEvent(this);
		gameR.setPuzzleActivatedEvent(this);
		
		//Copy mp3s from raw to /sdcard/
		try 
		{
			saveas(R.raw.song2);
			mp.setDataSource("/sdcard/circutCrawler/media/audio/songs/song2.mp3");
			mp.prepare();
			mp.setVolume(SoundPlayer.musicVolume, SoundPlayer.musicVolume); //penis
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
		
		pd.dismiss();
	}
	
	@Override
	public void onGameOver(boolean winning)
	{
		mp.stop();
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
			Intent i = new Intent(Run.this, Run.class);
			startActivity(i);
		}
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
			case R.id.quit:
				moveTaskToBack(true);
				finish();
				System.exit(0);
				android.os.Process.killProcess(android.os.Process.myPid());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		
		}
	}
	
	@Override
	public void onBackPressed()
	{
		Intent i = new Intent(Run.this, MainMenu.class);
		startActivity(i);
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume();
		glSurface.onResume();
		mp.start();
	}
	
	@Override
	protected void onPause ()
	{
		super.onPause();
		glSurface.onPause();
		mp.pause();
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
