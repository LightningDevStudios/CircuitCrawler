package com.lds.parser;

import java.util.HashMap;

import com.lds.Texture;
import com.lds.game.Game;

/*import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.lds.game.R;
import com.lds.Enums.RenderMode;

import android.content.Context;
import android.content.res.XmlResourceParser;*/

public class EntityData
{
	protected float size, xPos, yPos, xScl, yScl, angle, r, g, b, a;
	private boolean isSolid, circular, willCollide;
	private String color, tileCoords;
	protected float[] rgba;
	protected int[] xy;
	protected Texture tex;
	public EntityData(HashMap<String, String> entHM)
	{
		size = Float.parseFloat(entHM.get("size"));
		xPos = Float.parseFloat(entHM.get("xPos"));
		yPos = Float.parseFloat(entHM.get("yPos"));
		xScl = Float.parseFloat(entHM.get("xScl"));
		yScl = Float.parseFloat(entHM.get("yScl"));
		angle = Float.parseFloat(entHM.get("angle"));
		
		isSolid = Boolean.parseBoolean(entHM.get("isSolid"));
		circular = Boolean.parseBoolean(entHM.get("circular"));
		willCollide = Boolean.parseBoolean(entHM.get("willCollide"));
		
		//enable colorMode
		if(entHM.get("color") != null)
		{
			color = entHM.get("color");
			String tempColor = (entHM.get("color"));
			int i = 0;
			while(tempColor.indexOf(",") < 0)
			{
				if(tempColor.indexOf(",") == 0)
				tempColor = tempColor.substring(tempColor.indexOf("," + 1));
				i++;
				if(tempColor.indexOf(",") > -1)
					tempColor = tempColor.substring(tempColor.indexOf(","));
			}
		
			rgba = new float[i];
			for (float clr : rgba)
			{
				if(color.indexOf(",") == 0)
					color = color.substring(color.indexOf("," + 1));
				clr = Float.parseFloat(color.substring(0, color.indexOf("f") +1));
				if(tempColor.indexOf(",") > -1)
					color = color.substring(color.indexOf(","));
			}  //if u want to enable color mode, use .enableColor(rgba[0], rgba[1], rgba[2], rgba[3]);
		}
		else 
		{
			rgba = null;
		}
			//enableTilesetMode
		if (entHM.get("tileCoords") != null && entHM.get("texture") != null)
		{
			tileCoords = entHM.get("tileCoords");
			String tempTileCoords = entHM.get("tileCoords");
		
			String texture = entHM.get("texture");
			if(texture.equalsIgnoreCase("tilesetcolors"))
				tex = Game.tilesetcolors;
			else if(texture.equalsIgnoreCase("tilesetwire"))
				tex = Game.tilesetwire;
			else if(texture.equalsIgnoreCase("randomthings"))
				tex = Game.randomthings;
			else if(texture.equalsIgnoreCase("text"))
				tex = Game.text;
			else
				System.out.println("You didn't input a real texture");
			
			int k = 0;
			while(tempTileCoords.indexOf(",") < 0)
			{
				if(tempTileCoords.indexOf(",") == 0)
					tempTileCoords = tempTileCoords.substring(tempTileCoords.indexOf("," + 1));
				k++;
				if(tempTileCoords.indexOf(",") > -1)
					tempTileCoords = tempTileCoords.substring(tempTileCoords.indexOf(","));
			}
		
			xy = new int[k];
			for (float clr : rgba)
			{
				if(tileCoords.indexOf(",") == 0)
					tileCoords = tileCoords.substring(tileCoords.indexOf("," + 1));
				clr = Float.parseFloat(tileCoords.substring(0, tileCoords.indexOf("f") +1));
				if(tempTileCoords.indexOf(",") > -1)
					tileCoords = tileCoords.substring(tileCoords.indexOf(","));
			}  //if u want to enable tilesetMode, use .enableTilesetMode(tex, xy[0], xy[1]);
		}
		else 
		{
			tex = null;
			xy = null;
		}
	}
	
	
	//float setters/getters
	public void setSize(float newSize) 			{size = newSize;}
	public void setXPos (float newXPos)			{xPos = newXPos;}
	public void setYPos (float newYPos)			{yPos = newYPos;}
	public void setXScl(float newXScl)		{xScl = newXScl;}
	public void setYScl(float newYScl)		{yScl = newYScl;}
	public void setAngle(float newAngle)				{angle = newAngle;}
	
	public float getSize()			{return size;}
	public float getXPos() 			{return xPos;}
	public float getYPos()			{return yPos;}
	public float getXScl()		{return xScl;}
	public float getYScl()		{return yScl;}
	public float getAngle()			{return angle;}
	
	//bool setters/getters
	public void setIsSolid(boolean newIsSolid) 		{isSolid = newIsSolid;}
	public void setCircular(boolean newCircular)	{circular = newCircular;}
	
	public boolean getIsSolid()		{return isSolid;}
	public boolean getCircular()	{return circular;}
	
	//String setters/getters
	public void setColor(String newColor)	{color = newColor;}
	
	public String getColor()		{return color;}
	
	public void createInst()
	{
	}
}
