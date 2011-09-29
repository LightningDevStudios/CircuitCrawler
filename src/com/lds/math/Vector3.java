package com.lds.math;

/**
 * A 3-component vector.
 * @author Lightning Development Studios
 *
 */
public class Vector3
{
	/*************
	 * Constants *
	 *************/
	
	/**
	 * A unit vector in the X direction.
	 */
	public static final Vector3 UnitX = new Vector3(1, 0, 0);
	
	/**
	 * A unit vector in the Y direction.
	 */
	public static final Vector3 UnitY = new Vector3(0, 1, 0);
	
	/**
	 * A unit vector in the Z direction.
	 */
	public static final Vector3 UnitZ = new Vector3(0, 0, 1);
	
	/**
	 * A vector of all components equal to 1.
	 */
	public static final Vector3 One = new Vector3(1, 1, 1);
	
	/**
	 * A vector of all values components to 0.
	 */
	public static final Vector3 Zero = new Vector3(0, 0, 0);
	
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
	
	/**
	 * The vector's Z component.
	 */
	private float z;
	
	/****************
	 * Constructors *
	 ****************/

	/**
	 * Creates a new instance of the Vector3 class with all components equal to 0.
	 */
	public Vector3()
	{
		x = 0;
		y = 0;
		z = 0;
	}
	
	/**
	 * Creates a new instance of the Vector3 class.
	 * @param x The X component of the vector.
	 * @param y The Y component of the vector.
	 * @param z The Z component of the vector.
	 */
	public Vector3(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Creates a new instance of the Vector3 class.
	 * @param xy A vector containing the X and Y components of the vector.
	 * @param z The Z component of the vector.
	 */
	public Vector3(Vector2 xy, float z)
	{
		this.x = xy.getX();
		this.y = xy.getY();
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
	
	/***********
	 * Swizzle *
	 ***********/
	
	/**
	 * Gets the vector <x, y>
	 * @return A Vector2 containing the x and y components of this vector.
	 * @see Vector2
	 */
	public Vector2 xy()
	{
		return new Vector2(x, y);
	}
	
	/**************************
	 * Accessors and Mutators *
	 **************************/
	
	/**
	 * Gets the X component of the vector.
	 * @return The vector's X component.
	 */
	public float getX() { return x; }
	
	/**
	 * Gets the Y component of the vector.
	 * @return The vector's Y component.
	 */
	public float getY() { return y; }
	
	/**
	 * Gets the Z component of the vector.
	 * @return The vector's Z component.
	 */
	public float getZ() { return z; }
	
	/**
	 * Manually sets the X component of the vector.
	 * @param x A new X component.
	 */
	public void setX(float x) { this.x = x; }
	
	/**
	 * Manually sets the Y component of the vector.
	 * @param y A new Y component.
	 */
	public void setY(float y) { this.y = y; }
	
	/**
	 * Manually sets the Z component of the vector.
	 * @param z A new Z component.
	 */
	public void setZ(float z) { this.z = z; }
}
