package com.lds.game;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * A static class that plays short audio clips from anywhere.
 * @author Lightning Development Studios
 */
public final class SoundPlayer 
{
	public static final int SOUND_TEST = 1;
	public static final int SHOOT_SOUND = 2;
	public static final int ENEMY_DEATH = 3;
	public static final int PIT_FALL = 4;
	public static final int TELEPORT = 5;
	private static boolean enableSound = true;
	private static boolean enableMusic = true;
	private static float effectVolume, musicVolume;
		
	private static SoundPool pool;
	private static HashMap<Integer, Integer> poolMap;
	private static Context context;
	
	/**
	 * Prevents initialization of SoundPlayer.
	 */
	private SoundPlayer()
	{
		
	}
	
	/**
	 * Initializes all the sounds that can be played in the game.
	 * @param context The Android Context containing all the audio resource references.
	 */
	public static void initialize(Context context)
	{
	    pool = new SoundPool(6, AudioManager.STREAM_MUSIC, 100);
        poolMap = new HashMap<Integer, Integer>();
	    
		SoundPlayer.context = context;
		poolMap.put(SOUND_TEST, pool.load(context, R.raw.testclick, 1));
		poolMap.put(SHOOT_SOUND, pool.load(context, R.raw.shootsound, 1));
		poolMap.put(ENEMY_DEATH, pool.load(context, R.raw.enemydeath, 1));
		poolMap.put(PIT_FALL, pool.load(context, R.raw.pitfall, 1));
		poolMap.put(TELEPORT, pool.load(context, R.raw.teleport, 1));
	}
	
	/**
	 * Plays a sound.
	 * @param sound The sound to play. Should be one of the constants in SoundPlayer.
	 * @see #SOUND_TEST
	 * @see #SHOOT_SOUND
	 * @see #ENEMY_DEATH
	 * @see #PIT_FALL
	 * @see #TELEPORT
	 */
	public static void playSound(int sound)
	{
		if (enableSound)
		{
			AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
			float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = streamVolumeCurrent / streamVolumeMax * effectVolume;
		
			pool.play(poolMap.get(sound), volume, volume, 1, 0, 1.0f);
		}
	}
	
	/**
	 * Gets a value indicating whether sound is enabled or not.
	 * @return A value indicating whether sound is enabled or not.
	 */
	public static boolean getSoundEnabled()
	{
	    return enableSound;
	}
	
	/**
	 * Gets a value indicating whether music is enabled or not.
	 * @return A value indicating whether music is enabled or not.
	 */
	public static boolean getMusicEnabled()
	{
	    return enableMusic;
	}
	
	/**
	 * Gets the volume for sound effects.
	 * @return A 0-1 clamped float representing volume.
	 */
	public static float getEffectVolume()
	{
	    return effectVolume;
	}
	
	/**
	 * Gets the volume for music.
	 * @return A 0-1 clamped float representing volume.
	 */
	public static float getMusicVolume()
	{
	    return musicVolume;
	}
	
	/**
	 * Enables or disables the ability to play sounds.
	 * @param state A value indicating whether or not sound can be played.
	 */
	public static void setSoundEnabled(boolean state)
	{
	    enableSound = state;
	}
	
	/**
	 * Enables or disables the ability to play music.
	 * @param state A value indicating whether or not music can be played.
	 */
	public static void setMusicEnabled(boolean state)
	{
	    enableMusic = state;
	}
	
	/**
	 * Sets the volume to play sound effects at.
	 * @param volume A 0-1 clamped float representing volume.
	 */
	public static void setEffectVolume(float volume)
	{
	    effectVolume = volume;
	}
	
	/**
	 * Sets the volume to play music at.
	 * @param volume A 0-1 clamped float representing volume.
	 */
	public static void setMusicVolume(float volume)
	{
	    musicVolume = volume;
	}
}
