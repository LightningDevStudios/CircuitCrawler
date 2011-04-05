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
	private Graphics glSurface;
	private GameRenderer gameR;
	private MediaPlayer mp = new MediaPlayer();
	private ProgressDialog pd;
	private boolean playingSong1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		levelIndex = getIntent().getExtras().getInt("levelIndex", -1);
		System.out.println(levelIndex);
		unlockedLevel = getIntent().getExtras().getInt("unlockedLevel", -1);
		System.out.println(unlockedLevel);
		
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
		}
		
		//Grab screen information
		DisplayMetrics screen = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(screen);
		float screenX = (float)screen.widthPixels;
		float screenY = (float)screen.heightPixels;
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		pd = ProgressDialog.show(this, "", "Loading...");

		playMusic(true);
		
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
		playMusic(false);
	} 
	
	@Override 
    public void onPrepared(MediaPlayer mp) 
	{ 
		mp.setVolume(SoundPlayer.getInstance().getMusicVolume(), SoundPlayer.getInstance().getMusicVolume());
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
			setResult(100 + levelIndex);
			finish();
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
	
	public void playMusic(boolean random)
	{	
		mp.setOnPreparedListener(this);
		mp.setOnCompletionListener(this);
		int whichSong;
		if (random)
			whichSong = (int)(Math.random() * 2 + 1);
		else if (playingSong1)
			whichSong = 2;
		else
			whichSong = 1;
		if(whichSong == 2)
		{
			try 
			{
				saveas(R.raw.song2, "song2.mp3");
				mp.setDataSource("/sdcard/circutCrawler/media/audio/songs/song2.mp3");
				playingSong1 = false;
				if (SoundPlayer.getInstance().getMusicEnabled())
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
				playingSong1 = true;
				if (SoundPlayer.getInstance().getMusicEnabled())
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
				setResult(100 + levelIndex);
				finish();
				return true;
			case R.id.main_menu:
				//return to main menu
				mp.stop();
				setResult(0);
				finish();
				return true;
			case R.id.quit:
				mp.stop();
				setResult(3);
				finish();
				return true;
			default:
				mp.start();
				return super.onOptionsItemSelected(item);
		
		}
	}
	
	@Override
	public void onBackPressed()
	{
		mp.stop();
		finish();
	}
	
	@Override
	protected void onResume ()
	{
		super.onResume();
		//mp.start();
		glSurface.onResume();	
	}
	
	@Override
	protected void onPause ()
	{
		mp.pause();
		super.onPause();
		glSurface.onPause();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mp.stop();
	}
}
