package com.lds.math;

/**
 * A 2-component vector.
 * @author Lightning Development Studios
 *
 */
public class Vector2
{
	/*************
	 * Constants *
	 *************/
	
	/**
	 * A unit vector in the X direction.
	 * @return A vector.
	 */
	public static Vector2 unitX() { return new Vector2(1, 0); }
	
	/**
	 * A unit vector in the Y direction.
	 * @return A vector.
	 */
	public static Vector2 unitY() { return new Vector2(0, 1); }
	
	/**
	 * A unit vector with all components equal to 0.
	 * @return A vector.
	 */
	public static Vector2 zero() { return new Vector2(); }
	
	/***********
	 * Members *
	 ***********/
	
	/**
	 * The vector's X component.
	 */
	private float x;
	
	/**
	 * The vector's Y component.
	 */
	private float y;
	
	/****************
	 * Constructors *
	 ****************/
	
	/**
	 * Creates a new instance of the Vector2 class with all components equal to 0.
	 */
	public Vector2()
	{
		x = 0;
		y = 0;
	}
	
	/**
	 * Creates a new instance of the Vector2 class.
	 * @param x The X component of the vector.
	 * @param y The Y component of the vector.
	 */
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	//TODO same as copy?
	public Vector2(Vector2 v)
	{
		x = v.getX();
		y = v.getY();
	}
	
	/**
	 * Creates a new instance of the Vector2 class from an angle.
	 * @param angle The angle (in degrees) which will be the vector's direction.
	 */
	public Vector2(float angle)
	{
		double angleRad = Math.toRadians(angle);
		x = (float)Math.cos(angleRad);
		y = (float)Math.sin(angleRad);
	}
	
	/*************************************************************************
	 * Static Methods - return new vectors, do not change plugged in vectors *
	 *************************************************************************/
	
	public static Vector2 abs(Vector2 v)
	{
		return new Vector2(Math.abs(v.getX()), Math.abs(v.getY()));
	}
	
	public static Vector2 add(Vector2 v, float x, float y)
	{
		return new Vector2(v.getX() + x, v.getY() + y);
	}
	
	public static Vector2 add(Vector2 v1, Vector2 v2)
	{
		return Vector2.add(v1, v2.getX(), v2.getY());
	}
	
	public static Vector2 subtract(Vector2 v, float x, float y)
	{
		return new Vector2(v.getX() - x, v.getY() - y);
	}
	
	public static Vector2 subtract(Vector2 v1, Vector2 v2)
	{
		return Vector2.subtract(v1, v2.getX(), v2.getY());
	}
	
	public static Vector2 negate(Vector2 v)
	{
		return new Vector2(-v.getX(), -v.getY());
	}
	
	public static Vector2 scale(Vector2 v, float scalar)
	{
		return new Vector2(scalar * v.getX(), scalar * v.getY());
	}
	
	public static Vector2 scaleTo(Vector2 v, float scalar)
	{
		return Vector2.normalize(v).scale(scalar);
	}
	
	public static Vector2 normalize(Vector2 v)
	{
		if (v.x != 0.0f || v.y != 0.0f)
			return Vector2.scale(v, 1 / v.magnitude());
		else
			return v;
	}
	
	public static Vector2 getMidpoint(Vector2 v1, Vector2 v2)
	{
		return new Vector2((v1.x + v2.x) / 2, (v1.y + v2.y) / 2);
	}
	
	public static Vector2 getNormal(Vector2 v)
	{
		return new Vector2(v.getY(), -v.getX());
	}
	
	/************************************************************************************************************
	 * Non-Static Methods - return and change vectors, calculate vector quantites (i.e. dot product, magnitude) *
	 ************************************************************************************************************/
	
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void copy(Vector2 v)
	{
		set(v.getX(), v.getY());
	}
	
	public Vector2 abs()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		return this;
	}
	
	public Vector2 add(float x, float y)
	{
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2 add(Vector2 v)
	{
		return add(v.getX(), v.getY());
	}
	
	public Vector2 subtract(float x, float y)
	{
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	public Vector2 subtract(Vector2 v)
	{
		return subtract(v.getX(), v.getY());
	}
	
	public boolean equals(Vector2 v)
	{
		return x == v.getX() && y == v.getY();
	}
	
	public boolean approxEquals(Vector2 v, float epsilon)
	{
		return Math.abs(x - v.x) < epsilon && Math.abs(y - v.y) < epsilon;
	}
	
	public Vector2 negate()
	{
		return scale(-1);
	}
	
	public Vector2 scale(float scalar)
	{
		x *= scalar;
		y *= scalar;
		return this;
	}
	
	public Vector2 scaleTo(float scalar)
	{
		normalize();
		scale(scalar);
		return this;
	}
	
	public float magnitude()
	{
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public float dot(Vector2 v)
	{
		return x * v.getX() + y * v.getY();
	}
	
	public Vector2 normalize()
	{
		if (x != 0.0f || y != 0.0f)
			scale(1 / magnitude());
		return this;
	}
	
	public float angle(Vector2 v)
	{
	    float rad = ((float)Math.acos(Vector2.normalize(this).dot(Vector2.normalize(v))));
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
	
	public Vector2 setNormal()
	{
		set(y, -x);
		return this;
	}
	
	public String toString()
	{
		return "<" + x + ", " + y + ">";
	}
	
	/***********************
	 * Getters and Setters *
	 ***********************/
	
	public float getX() { return x; } public void setX(float x) { this.x = x; }
	public float getY() { return y; } public void setY(float y) { this.y = y; }
}
