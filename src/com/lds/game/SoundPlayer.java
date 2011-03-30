package com.lds.game;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer 
{
	public static final int SOUND_TEST = 1;
	public static final int SHOOT_SOUND = 2;
	public static final int ENEMY_DEATH = 3;
	public static final int PIT_FALL = 4;
	public static final int SONG1 = 5;
	public static final int SONG2 = 6;
	public static boolean enableSound = true, enableMusic = true;
	public static float effectVolume = 0, musicVolume = 0;
	
	private static SoundPlayer p_sp;
	
	private SoundPool pool;
	private HashMap<Integer, Integer> poolMap;
	private Context context;
	
	private SoundPlayer()	
	{
		pool = new SoundPool(6, AudioManager.STREAM_MUSIC, 100);
		poolMap = new HashMap<Integer, Integer>();
	}
		
	public static SoundPlayer getInstance()
	{
		if (p_sp == null)
		{
			synchronized(SoundPlayer.class)
			{
				if (p_sp == null)
				{
					p_sp = new SoundPlayer();
				}
			}
		}
		return p_sp;
	}
	
	public void initialize(Context context)
	{
		this.context = context;
		poolMap.put(SOUND_TEST, pool.load(context, R.raw.testclick, 1));
		poolMap.put(SHOOT_SOUND, pool.load(context, R.raw.shootsound, 1));
		poolMap.put(ENEMY_DEATH, pool.load(context, R.raw.enemydeath, 1));
		poolMap.put(PIT_FALL, pool.load(context, R.raw.pitfall, 1));
		//poolMap.put(SONG1, pool.load(context, R.raw.song1, 1));
		//poolMap.put(SONG2, pool.load(context, R.raw.song2, 1));
	}
	
	public void playSound(int sound)
	{
		if(enableSound)
		{
			AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax * effectVolume;
		
			pool.play(poolMap.get(sound), volume, volume, 1, 0, 1.0f);
		}
	}
	
	public void playMusic(int sound)
	{
		if(enableMusic)
		{
			AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax * musicVolume;
		
			pool.play(poolMap.get(sound), volume, volume, 1, 0, 1.0f);
		}
	}
}
