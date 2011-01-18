package com.lds;

import android.os.SystemClock;

public class Stopwatch 
{
	private static int elapsedTimeMs;
	private static int elapsedTimeS;
	private static int elapsedTimeM;
	
	private static long startTimeMs;
		
	private Stopwatch() {}
	
	public static void restartTimer()
	{
		startTimeMs = SystemClock.elapsedRealtime();
	}
		
	public static void tick()
	{
		elapsedTimeMs = (int)(SystemClock.elapsedRealtime() - startTimeMs);
		elapsedTimeS = (int)(SystemClock.elapsedRealtime() - startTimeMs) / 1000;
		elapsedTimeM = (int)(SystemClock.elapsedRealtime() - startTimeMs) / 60000;
	}
	
	public static int elapsedTimeMs()
	{
		return elapsedTimeMs;
	}
	
	public static int elapsedTimeS()
	{
		return elapsedTimeS;
	}
	
	public static int elapsedTimeM()
	{
		return elapsedTimeM;
	}
	
	public static int elapsedTimeMsRemainder()
	{
		return elapsedTimeMs % 1000;
	}
	
	public static int elapsedTimeSRemainder()
	{
		return elapsedTimeS % 60;
	}
	
	public static int now()
	{
		return (int)SystemClock.elapsedRealtime();
	}
}
