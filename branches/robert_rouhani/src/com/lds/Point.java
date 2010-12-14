package com.lds;

public class Point 
{
	private float x;
	private float y;
	
	public Point (float _x, float _y)
	{
		x = _x;
		y = _y;
	}
	
	public Point ()
	{
		this(0.0f, 0.0f);
	}
	
	public float getX ()
	{
		return x;
	}
	
	public float getY ()
	{
		return y;
	}
	
	public void setX (float _x)
	{
		x = _x;
	}
	
	public void setY (float _y)
	{
		y = _y;
	}
}
