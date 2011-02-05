package com.lds.game;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer 
{
	public static final int SOUND_TEST = 1;
	private SoundPlayer p_sp;
	
	private SoundPool pool;
	private HashMap<Integer, Integer> poolMap;
	
	private SoundPlayer()	
	{
		pool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		poolMap = new HashMap<Integer, Integer>();
	}
	
	public void initialize(Context context)
	{
		poolMap.put(SOUND_TEST, pool.load(context, R.raw.testclick, 1));
	}
	
	public SoundPlayer getInstance()
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
}
