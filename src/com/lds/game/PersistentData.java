package com.lds.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class PersistentData 
{
	public static String vib, sounds, musics, shader;
	public static float anti, vol;
	
	public static void writeData(boolean vibration, boolean soundb, boolean music, boolean shaders, float antiAliasing, float volume)
	{
		try
		{
		    FileWriter fstream = new FileWriter("settings.txt");
		    BufferedWriter out = new BufferedWriter(fstream);
		    if(vibration)
		    {
		    	String vibr = "Vibration: on";
		    	vib = vibr;
		    	
		    }
		    else
		    {
		    	String vibr = "Vibration: off";
		    	vib = vibr;
		    }
		    if(soundb)
		    {
		    	String sound = "Sound: on";
		    	sounds = sound;
		    }
		    else
		    {
		    	String sound = "Sound: off";
		    	sounds = sound;
		    }
		    if(music)
		    {
		    	String musicss = "Music: on";
		    	musics = musicss;
		    }
		    else
		    {
		    	String musicss = "Music: off";
		    	musics = musicss;
		    }
		    if(shaders)
		    {
		    	String shaderss = "Shaders: on";
		    	shader = shaderss;
		    }
		    else
		    {
		    	String shaderss = "Shaders: off";
		    	shader = shaderss;
		    }
		    out.write(vib + "\n" + sounds + "\n" + musics + "\n" + shader + "\n" + anti + "\n" + vol);
		    out.close();
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
	public static void readData()
	{
		try
		{
		    FileInputStream fstream = new FileInputStream("settings.txt");
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String setting;

		    while ((setting = br.readLine()) != null)   
		    {
		      System.out.println (setting);
		    }

		    in.close();
		}
		catch (Exception e)
		{
		      System.err.println("Error: " + e.getMessage());
		}
	}
}
