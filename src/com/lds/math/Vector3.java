package com.lds.math;

public class Vector3
{
	/**
	 * Constant Vectors
	 */
	public static final Vector3 ZERO = new Vector3();
	public static final Vector3 UNIT_X = new Vector3(1, 0, 0);
	public static final Vector3 UNIT_Y = new Vector3(0, 1, 0);
	public static final Vector3 UNIT_Z = new Vector3(0, 0, 1);
	
	/**
	 * Private Variables
	 */
	private float x;
	private float y;
	private float z;
	
	/****************
	 * Constructors *
	 ****************/
	
	public Vector3()
	{
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/*************************************************************************
	 * Static Methods - return new vectors, do not change plugged in vectors *
	 *************************************************************************/
	
	public static Vector3 abs(Vector3 v)
	{
		return new Vector3(Math.abs(v.getX()), Math.abs(v.getY()), Math.abs(v.getZ()));
	}
	
	public static Vector3 add(Vector3 v, float x, float y, float z)
	{
		return new Vector3(v.getX() + x, v.getY() + y, v.getZ() + z);
	}
	
	public static Vector3 add(Vector3 v1, Vector3 v2)
	{
		return Vector3.add(v1, v2.getX(), v2.getY(), v2.getZ());
	}
	
	public static Vector3 subtract(Vector3 v, float x, float y, float z)
	{
		return new Vector3(v.getX() - x, v.getY() - y, v.getZ() - z);
	}
	
	public static Vector3 subtract(Vector3 v1, Vector3 v2)
	{
		return Vector3.subtract(v1, v2.getX(), v2.getY(), v2.getZ());
	}
	
	public static Vector3 negate(Vector3 v)
	{
		return new Vector3(-v.getX(), -v.getY(), -v.getZ());
	}
	
	public static Vector3 scale(Vector3 v, float scalar)
	{
		return new Vector3(scalar * v.getX(), scalar * v.getY(), scalar * v.getZ());
	}
	
	public static Vector3 scaleTo(Vector3 v, float scalar)
	{
		return Vector3.normalize(v).scale(scalar);
	}
	
	public static Vector3 normalize(Vector3 v)
	{
		if (v.x != 0 || v.y != 0 || v.z != 0)
			return Vector3.scale(v, 1 / v.mag());
		else
			return v;
	}
	
	public static Vector3 getMidpoint(Vector3 v1, Vector3 v2)
	{
		return new Vector3((v1.getX() + v2.getX()) / 2, (v1.getY() + v2.getY()) / 2, (v1.getZ() + v2.getZ()) / 2);
	}
	
	public static Vector3 cross(Vector3 v1, Vector3 v2)
	{
		float x = v1.getY() * v2.getZ() - v1.getZ() * v2.getY();
		float y = v1.getZ() * v2.getX() - v1.getX() * v2.getZ();
		float z = v1.getX() * v2.getY() - v1.getY() * v2.getX();
		return new Vector3(x, y, z);
	}
	
	/************************************************************************************************************
	 * Non-Static Methods - return and change vectors, calculate vector quantites (i.e. dot product, magnitude) *
	 ************************************************************************************************************/
	
	public void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void copy(Vector3 v)
	{
		set(v.getX(), v.getY(), v.getZ());
	}
	
	public Vector3 abs()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		return this;
	}
	
	public Vector3 add(float x, float y, float z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public Vector3 add(Vector3 v)
	{
		return add(v.getX(), v.getY(), v.getZ());
	}
	
	public Vector3 subtract(float x, float y, float z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	
	public Vector3 subtract(Vector3 v)
	{
		return subtract(v.getX(), v.getY(), v.getZ());
	}
	
	public boolean equals(Vector3 v)
	{
		return x == v.getX() && y == v.getY() && z == v.getZ();
	}
	
	public boolean approxEquals(Vector3 v, float epsilon)
	{
		return Math.abs(x - v.x) < epsilon && Math.abs(y - v.y) < epsilon && Math.abs(z - v.z) < epsilon;
	}
	
	public Vector3 negate()
	{
		return scale(-1);
	}
	
	public Vector3 scale(float scalar)
	{
		x *= scalar;
		y *= scalar;
		z *= scalar;
		return this;
	}
	
	public Vector3 scaleTo(float scalar)
	{
		normalize();
		scale(scalar);
		return this;
	}
	
	public float mag()
	{
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public float dot(Vector3 v)
	{
		return x * v.getX() + y * v.getY() + z * v.getZ();
	}
	
	public Vector3 normalize()
	{
		if (x != 0 || y != 0 || z != 0)
			scale(1 / mag());
		return this;
	}
	
	/***********************
	 * Getters and Setters *
	 ***********************/
	
	public float getX() { return x; } public void setX(float x) { this.x = x; }
	public float getY() { return y; } public void setY(float y) { this.y = y; }
	public float getZ() { return z; } public void setZ(float z) { this.z = z; }
}
