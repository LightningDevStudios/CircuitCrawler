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
	private float size, xPos, yPos, xScale, yScale, angle;
	private int moveSpeed, rotateSpeed, scaleSpeed;
	private boolean isSolid, circular;
	private String color;

	public EntityData(HashMap<String, String> entHashMap)
	{
		size = Float.parseFloat(entHashMap.get("size"));
		xPos = Float.parseFloat(entHashMap.get("xPos"));
		yPos = Float.parseFloat(entHashMap.get("yPos"));
		xScale = Float.parseFloat((entHashMap.get("xScale")));
		yScale = Float.parseFloat(entHashMap.get("yScale"));
		angle = Float.parseFloat(entHashMap.get("angle"));
		
		moveSpeed = Integer.parseInt(entHashMap.get("moveSpeed")); 
		rotateSpeed = Integer.parseInt(entHashMap.get("rotateSpeed"));
		scaleSpeed = Integer.parseInt(entHashMap.get("scaleSpeed"));
		
		isSolid = Boolean.parseBoolean(entHashMap.get("isSolid"));
		circular = Boolean.parseBoolean(entHashMap.get("circular"));
		
		color = (entHashMap.get("color"));
	}
	
	//float setters/getters
	public void setSize(float newSize) 			{size = newSize;}
	public void setXPos (float newXPos)			{xPos = newXPos;}
	public void setYPos (float newYPos)			{yPos = newYPos;}
	public void setXScale(float newXScale)		{xScale = newXScale;}
	public void setYScale(float newYScale)		{yScale = newYScale;}
	public void setAngle(float newAngle)				{angle = newAngle;}
	
	public float getSize()			{return size;}
	public float getXPos() 			{return xPos;}
	public float getYPos()			{return yPos;}
	public float getXScale()		{return xScale;}
	public float getYScale()		{return yScale;}
	public float getAngle()			{return angle;}

	//int setters/getters
	public void setMoveSpeed(int newMoveSpeed)		{moveSpeed = newMoveSpeed;}
	public void setRotateSpeed(int newRotateSpeed)	{rotateSpeed = newRotateSpeed;}
	public void setScaleSpeed(int newScaleSpeed)	{scaleSpeed = newScaleSpeed;}
	
	public int getMoveSpeed()		{return moveSpeed;}
	public int getRotateSpeed()		{return	rotateSpeed;}
	public int getScaleSpeed()		{return scaleSpeed;}
	
	//bool setters/getters
	public void setIsSolid(boolean newIsSolid) 		{isSolid = newIsSolid;}
	public void setCircular(boolean newCircular)	{circular = newCircular;}
	
	public boolean getIsSolid()		{return isSolid;}
	public boolean getCircular()	{return circular;}
	
	//String setters/getters
	public void setColor(String newColor)	{color = newColor;}
	
	public String getColor()		{return color;}
}
