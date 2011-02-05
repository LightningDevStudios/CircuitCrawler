package com.lds.game;

import java.util.HashMap;

import android.media.SoundPool;

public class SoundPlayer 
{
	private SoundPlayer p_sp;
	
	private SoundPool pool;
	private HashMap<Integer, Integer> poolMap;
	
	private SoundPlayer()	
	{
		//pool = new SoundPool();
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
