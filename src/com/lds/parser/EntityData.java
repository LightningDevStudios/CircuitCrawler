package com.lds.parser;

import java.util.HashMap;

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
	private float size, xPos, yPos, xScl, yScl, angle;
	private boolean isSolid, circular, willCollide;
	private String color;

	public EntityData(HashMap<String, String> entHashMap)
	{
		size = Float.parseFloat(entHashMap.get("size"));
		xPos = Float.parseFloat(entHashMap.get("xPos"));
		yPos = Float.parseFloat(entHashMap.get("yPos"));
		xScl = Float.parseFloat(entHashMap.get("xScl"));
		yScl = Float.parseFloat(entHashMap.get("yScl"));
		angle = Float.parseFloat(entHashMap.get("angle"));
		
		isSolid = Boolean.parseBoolean(entHashMap.get("isSolid"));
		circular = Boolean.parseBoolean(entHashMap.get("circular"));
		willCollide = Boolean.parseBoolean(entHashMap.get("willCollide"));
		
		color = (entHashMap.get("color"));
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
	
	
}
