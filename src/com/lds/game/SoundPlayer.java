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
	
	private static SoundPlayer p_sp;
	
	private SoundPool pool;
	private HashMap<Integer, Integer> poolMap;
	private Context context;
	
	private SoundPlayer()	
	{
		pool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
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
	}
	
	public void playSound(int sound)
	{
		AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;
		
		pool.play(poolMap.get(sound), volume, volume, 1, 0, 1.0f);
	}
}
