package com.lds.parser;

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

public class PhysBlockData
{
	private float size, xPos, yPos, xScale, yScale;
	private int angle, moveSpeed, rotateSpeed, scaleSpeed;
	private boolean isSolid, circular;
	private String color;

	public PhysBlockData(String[] xmlPhysValues)
	{
		size = Float.parseFloat(xmlPhysValues[1]);
		xPos = Float.parseFloat(xmlPhysValues[2]);
		yPos = Float.parseFloat(xmlPhysValues[3]);
		xScale = Float.parseFloat(xmlPhysValues[4]);
		yScale = Float.parseFloat(xmlPhysValues[5]);
		
		angle = Integer.parseInt(xmlPhysValues[6]);
		moveSpeed = Integer.parseInt(xmlPhysValues[7]);
		rotateSpeed = Integer.parseInt(xmlPhysValues[8]);
		scaleSpeed = Integer.parseInt(xmlPhysValues[9]);
		
		isSolid = Boolean.parseBoolean(xmlPhysValues[10]);
		circular = Boolean.parseBoolean(xmlPhysValues[11]);
		
		color = xmlPhysValues[12];
	}
	
	//float setters/getters
	public void setSize(float newSize) 			{size = newSize;}
	public void setXPos (float newXPos)			{xPos = newXPos;}
	public void setYPos (float newYPos)			{yPos = newYPos;}
	public void setXScale(float newXScale)		{xScale = newXScale;}
	public void setYScale(float newYScale)		{yScale = newYScale;}
	
	public float getSize()			{return size;}
	public float getXPos() 			{return xPos;}
	public float getYPos()			{return yPos;}
	public float getXScale()		{return xScale;}
	public float getYScale()		{return yScale;}

	//int setters/getters
	public void setAngle(int newAngle)				{angle = newAngle;}
	public void setMoveSpeed(int newMoveSpeed)		{moveSpeed = newMoveSpeed;}
	public void setRotateSpeed(int newRotateSpeed)	{rotateSpeed = newRotateSpeed;}
	public void setScaleSpeed(int newScaleSpeed)	{scaleSpeed = newScaleSpeed;}
	
	public int getAngle()			{return angle;}
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
