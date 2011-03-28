package com.lds;

public class Vector2f
{
	public static final Vector2f empty = new Vector2f();
	private float x;
	private float y;
	
	/****************
	 * Constructors *
	 ****************/
	
	public Vector2f ()
	{
		x = 0.0f;
		y = 0.0f;
	}
	
	public Vector2f (float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2f (Vector2f v)
	{
		this.x = v.getX();
		this.y = v.getY();
	}
	
	public Vector2f (float angle)
	{
		float convertedAngle = (float)Math.toRadians(angle);
		this.x = (float)Math.cos(convertedAngle);
		this.y = (float)Math.sin(convertedAngle);
	}
	
	/*************************************************************************
	 * Static Methods - return new vectors, do not change plugged in vectors *
	 *************************************************************************/
	
	public static Vector2f abs (Vector2f v)
	{
		return new Vector2f(Math.abs(v.getX()), Math.abs(v.getY()));
	}
	
	public static Vector2f add (Vector2f v, float x, float y)
	{
		return new Vector2f(v.getX() + x, v.getY() + y);
	}
	
	public static Vector2f add (Vector2f v1, Vector2f v2)
	{
		return Vector2f.add(v1, v2.getX(), v2.getY());
	}
	
	public static Vector2f sub (Vector2f v, float x, float y)
	{
		return new Vector2f(v.getX() - x, v.getY() - y);
	}
	
	public static Vector2f sub (Vector2f v1, Vector2f v2)
	{
		return Vector2f.sub(v1, v2.getX(), v2.getY());
	}
	
	public static Vector2f neg (Vector2f v)
	{
		return new Vector2f(-v.getX(), -v.getY());
	}
	
	public static Vector2f scale (Vector2f v, float scalar)
	{
		return new Vector2f(scalar * v.getX(), scalar * v.getY());
	}
	
	public static Vector2f normalize (Vector2f v)
	{
		if (v.x != 0.0f || v.y != 0.0f)
			return Vector2f.scale(v, 1 / v.mag());
		else
			return v;
	}
	
	public static Vector2f getNormal (Vector2f v)
	{
		return new Vector2f(v.getY(), -v.getX());
	}
	
	public static Vector2f scaleTo (Vector2f v, float scalar)
	{
		return Vector2f.normalize(v).scale(scalar);
	}
	
	public static Vector2f getMidpoint (Vector2f v1, Vector2f v2)
	{
		return new Vector2f((v1.x + v2.x) / 2, (v1.y + v2.y) / 2);
	}
	
	/************************************************************************************************************
	 * Non-Static Methods - return and change vectors, calculate vector quantites (i.e. dot product, magnitude) *
	 ************************************************************************************************************/
	
	public void set (float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void set (Vector2f v)
	{
		set(v.getX(), v.getY());
	}
	
	public Vector2f abs ()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		return this;
	}
	
	public Vector2f add (float x, float y)
	{
		set(this.x + x, this.y + y);
		return this;
	}
	
	public Vector2f add (Vector2f v)
	{
		return add(v.getX(), v.getY());
	}
	
	public Vector2f sub (float x, float y)
	{
		set(this.x - x, this.y - y);
		return this;
	}
	
	public Vector2f sub (Vector2f v)
	{
		return sub(v.getX(), v.getY());
	}
	
	public boolean equals (Vector2f v)
	{
		if (x == v.getX() && y == v.getY())
		{
			return true;
		}
		return false;
	}
	
	public boolean approxEquals (Vector2f v, float marginOfError)
	{
		if (Math.abs(x - v.x) < marginOfError && Math.abs(y - v.y) < marginOfError)
		{
			return true;
		}
		return false;
	}
	
	public Vector2f neg ()
	{
		x *= -1;
		y *= -1;
		return this;
	}
	
	public Vector2f scale (float scalar)
	{
		x *= scalar;
		y *= scalar;
		return this;
	}
	
	public Vector2f scaleTo (float scalar)
	{
		normalize();
		scale(scalar);
		return this;
	}
	
	public float mag ()
	{
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public float dot (Vector2f v)
	{
		return x * v.getX() + y * v.getY();
	}
	
	public Vector2f normalize ()
	{
		if (x != 0.0f || y != 0.0f)
			scale(1 / mag());
		return this;
	}
	
	public float angle (Vector2f v)
	{
	    float rad = ((float)Math.acos(Vector2f.normalize(this).dot(Vector2f.normalize(v))));
	    //clamp angle between 0 and 360
		if (rad == Math.PI * 2)
			rad = 0.0f;
		else if (rad > Math.PI * 2)
			rad -= Math.PI * 2 * (int)(rad / Math.PI * 2);
		else if (rad < 0.0f)
			rad += Math.PI * 2;
		
		return rad;
	}
	
	public float angleRad()
	{
		float rad = (float)(Math.atan2(y, x));
		//clamp angle between 0 and 360
		if (rad == 2 * Math.PI)
			rad = 0.0f;
		else if (rad > 2 * Math.PI)
			rad -= 2 * Math.PI * (int)(rad / 2 * Math.PI);
		else if (rad < 0.0f)
			rad += 2 * Math.PI;
		
		return rad;
	}
	
	public float angleDeg()
	{
		float deg = (float)Math.toDegrees(Math.atan2(y, x));
		//clamp angle between 0 and 360
		if (deg == 360.0f)
			deg = 0.0f;
		else if (deg > 360.0f)
			deg -= 360 * (int)(deg / 360);
		else if (deg < 0.0f)
			deg += 360;
		
		return deg;
	}
	
	public String toString ()
	{
		return "<" + x + ", " + y + ">";
	}
	
	public Vector2f setNormal ()
	{
		set(y, -x);
		return this;
	}
	
	/************
	 * Mutators *
	 ************/
	
	public void setX (float x)
	{
		this.x = x;
	}
	
	public void setY (float y)
	{
		this.y = y;
	}
	
	/*************
	 * Accessors *
	 *************/
	
	public float getX ()
	{
		return x;
	}
	
	public float getY ()
	{
		return y;
	}
}
