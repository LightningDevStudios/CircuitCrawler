package com.lds.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.lds.Graphics;
import com.lds.game.event.*;
import com.lds.game.puzzle.PuzzleActivity;

public class Run extends Activity implements OnGameOverListener, OnGameInitializedListener, OnPuzzleActivatedListener, OnPreparedListener, OnCompletionListener
{
	public static final int PUZZLE_ACTIVITY = 2;
	private int unlockedLevel, levelIndex, levelId;
	private Bundle savedInstanceState;
	private Graphics glSurface;
	private GameRenderer gameR;
	private MediaPlayer mp = new MediaPlayer();
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		
		levelIndex = getIntent().getExtras().getInt("levelIndex", -1);
		unlockedLevel = getIntent().getExtras().getInt("unlockedLevel", -1);
		
		if (levelIndex == -1 || unlockedLevel == -1)
			finish();
		
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
		}
		
		//Grab screen information
		DisplayMetrics screen = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(screen);
		float screenX = (float)screen.widthPixels;
		float screenY = (float)screen.heightPixels;
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		pd = ProgressDialog.show(this, "", "Loading...");

		playMusic();
		
		//set up OpenGL rendering
		Object syncObj = new Object();
		gameR = new GameRenderer(screenX, screenY, this, syncObj, levelId);
				
		gameR.setGameInitializedEvent(this);
		glSurface = new Graphics(this, gameR, syncObj);
		
		setContentView(glSurface);
	}
	
	public boolean saveas(int ressound, String fileName)
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
		  
		 String path="/sdcard/CircutCrawler/media/audio/songs/";  
		 String filename= fileName;
		  
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
	public void onCompletion(MediaPlayer mp) 
	{
		mp.reset();
		playMusic();
	} 
	
	@Override 
    public void onPrepared(MediaPlayer mp) 
	{ 
		mp.setVolume(SoundPlayer.musicVolume, SoundPlayer.musicVolume);
		mp.start(); 
    } 

	@Override
	public void onGameInitialized()
	{
		gameR.setGameOverEvent(this);
		gameR.setPuzzleActivatedEvent(this);
		pd.dismiss();
	}
	
	@Override
	public void onGameOver(boolean winning)
	{
		mp.stop();
		mp.reset();
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
	
	public void playMusic()
	{	
		mp.setOnPreparedListener(this);
		mp.setOnCompletionListener(this);
		if((Math.random()*50 + 1) >= 25)
		{
			try 
			{
				saveas(R.raw.song2, "song2.mp3");
				mp.setDataSource("/sdcard/circutCrawler/media/audio/songs/song2.mp3");
				if (SoundPlayer.enableMusic)
				{
					mp.prepare();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			try 
			{
				saveas(R.raw.song1, "song1.mp3");
				mp.setDataSource("/sdcard/circutCrawler/media/audio/songs/song1.mp3");
				if (SoundPlayer.enableMusic)
				{
					mp.prepare();
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
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
				setResult(0);
				finish();
				return true;
			case R.id.quit:
				setResult(3);
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		
		}
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume();
		glSurface.onResume();
		//mp.start();
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
}
