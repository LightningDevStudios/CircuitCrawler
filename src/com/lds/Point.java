package com.lds;

public class Point 
{
	private float x;
	private float y;
	//used only for collision testing
	private float colC;
	
	public Point (float _x, float _y)
	{
		x = _x;
		y = _y;
		colC = 0;
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
	
	public float getColC ()
	{
		return colC;
	}
	
	public void setX (float _x)
	{
		x = _x;
	}
	
	public void setY (float _y)
	{
		y = _y;
	}
	
	public void setColC (float _colC)
	{
		colC = _colC;
	}
	
	public String toString ()
	{
		return x + " " + y + " " + colC;
	}
}
