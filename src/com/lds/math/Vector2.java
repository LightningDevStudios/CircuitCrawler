package com.lds.math;

/**
 * A 2-component vector.
 * @author Lightning Development Studios
 */
public final class Vector2
{	
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
	
	/**
	 * Initializes a new instance of the Vector2 class.
	 * @param v A vector containing the components of this vector.
	 * @deprecated We shouldn't be copying vectors like this. Make them immutable instead.
	 */
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
	
	/**
     * Turns all components of the vector into their absolute value equivalent.
     * @param v The vector to process.
     * @return A vector with all positive components.
     */
	public static Vector2 abs(Vector2 v)
	{
		return new Vector2(Math.abs(v.getX()), Math.abs(v.getY()));
	}
	
	/**
     * Adds two vectors together.
     * @param v The vector to be added to.
     * @param x The X component of the vector to add by.
     * @param y The Y component of the vector to add by.
     * @return The resultant vector of the addition.
     * @deprecated Use Vector2.add(v, new Vector3(x, y)) instead.
     */
	public static Vector2 add(Vector2 v, float x, float y)
	{
		return Vector2.add(v, new Vector2(x, y));
	}
	
	/**
     * Adds two vectors together.
     * @param v1 The vector to be added to.
     * @param v2 The vector to be added by.
     * @return The resultant vector of the addition.
     */
	public static Vector2 add(Vector2 v1, Vector2 v2)
	{
		return Vector2.add(v1, v2.getX(), v2.getY());
	}
	
	/**
     * Subtracts one vector from another.
     * @param v The vector to subtract from.
     * @param x X component of vector to subtract by.
     * @param y Y component of vector to subtract by.
     * @return The resultant vector of the subtraction.
     * @deprecated Use Vector2.subtract(v, new Vector2(x, y)) instead.
     */
	public static Vector2 subtract(Vector2 v, float x, float y)
	{
		return Vector2.subtract(v, new Vector2(x, y));
	}
	
	/**
     * Subtracts one vector from another.
     * @param v1 The vector to subtract from.
     * @param v2 The vector to subtract by.
     * @return The resultant vector of the subtraction.
     */
	public static Vector2 subtract(Vector2 v1, Vector2 v2)
	{
		return Vector2.subtract(v1, v2.getX(), v2.getY());
	}
	
	/**
     * Flips the sign of all the components of a vector.
     * @param v The vector to negate.
     * @return A vector equal in magnitude but opposite in direction.
     */
	public static Vector2 negate(Vector2 v)
	{
		return new Vector2(-v.getX(), -v.getY());
	}
	
	/**
     * Multiplies a vector by a scalar.
     * @param v The vector to scale.
     * @param scalar The amount to scale by.
     * @return A scaled vector in the direction of v.
     */
	public static Vector2 scale(Vector2 v, float scalar)
	{
		return new Vector2(scalar * v.getX(), scalar * v.getY());
	}
	
	/**
     * Normalizes a vector then scales it to a scalar.
     * @param v The vector to scale.
     * @param scalar The magnitude of the resultant vector.
     * @return A vector with a magnitude of scalar in the direction of v.
     */
	public static Vector2 scaleTo(Vector2 v, float scalar)
	{
		return Vector2.normalize(v).scale(scalar);
	}
	
	/**
     * Normalizes a vector.
     * @param v The vector to normalize.
     * @return A vector of magnitude 1 in the direction of v.
     */
	public static Vector2 normalize(Vector2 v)
	{
		if (v.x != 0.0f || v.y != 0.0f)
			return Vector2.scale(v, 1 / v.length());
		else
			return v;
	}
	
	/**
     * Creates a vector pointing to the midpoint of the directed line segment between two vectors.
     * \todo turn into a lerp method, add float 0-1 for distance.
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return A vector with components averaged from the two vectors.
     */
	public static Vector2 getMidpoint(Vector2 v1, Vector2 v2)
	{
		return new Vector2((v1.x + v2.x) / 2, (v1.y + v2.y) / 2);
	}
	
	/**
	 * Gets the vector orthogonal to v.
	 * @param v The vector.
	 * @return A vector orthogonal to v.
	 */
	public static Vector2 getNormal(Vector2 v)
	{
		return new Vector2(v.getY(), -v.getX());
	}
	
	/************************************************************************************************************
	 * Non-Static Methods - return and change vectors, calculate vector quantites (i.e. dot product, magnitude) *
	 ************************************************************************************************************/
	
	/**
	 * Sets the components of the vector.
	 * @param x The new X component of the vector.
	 * @param y The new Y component of the vector.
	 * @deprecated Make Vector2 immutable!
	 */
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the components of this vector to the components of v.
	 * @param v The vector to reference.
	 * @deprecated Make Vector2 immutable!
	 */
	public void copy(Vector2 v)
	{
		set(v.getX(), v.getY());
	}
	
	/**
	 * Absolute value.
	 * @return this
	 * @deprecated Make Vector2 immutable!
	 */
	public Vector2 abs()
	{
		x = Math.abs(x);
		y = Math.abs(y);
		return this;
	}
	
	/**
	 * Adds another vector to this one.
	 * @param x The X component to add.
	 * @param y The Y component to add.
	 * @return this
	 * @deprecated Make Vector2 immutable! Use Vector2.add instead.
	 */
	public Vector2 add(float x, float y)
	{
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**
	 * Adds another vector to this one.
	 * @param v The vector to add with.
	 * @return this
	 * @deprecated Make Vector2 immutable! Use Vector2.add instead.
	 */
	public Vector2 add(Vector2 v)
	{
		return add(v.getX(), v.getY());
	}
	
	/**
	 * Subtracts another vector from this one.
	 * @param x The X component to subtract.
	 * @param y The Y component to subtract.
	 * @return this
	 * @deprecated Make Vector2 immutable! Use Vector2.subtract instead.
	 */
	public Vector2 subtract(float x, float y)
	{
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	/**
	 * Subtract another vector from this one.
	 * @param v The vector to subtract.
	 * @return this
	 * @deprecated Make Vector2 immutable! Use Vector2.subtract instead.
	 */
	public Vector2 subtract(Vector2 v)
	{
		return subtract(v.getX(), v.getY());
	}
	
	/**
	 * Checks for equality with another vector.
	 * @param v The vector to compare against
	 * @return A value indicating whether the two vectors are equal.
	 * \todo Override Object.equals too!
	 */
	public boolean equals(Vector2 v)
	{
		return x == v.getX() && y == v.getY();
	}
	
	/**
	 * Checks for near equality of two vectors.
	 * @param v The vector to compare to.
	 * @param epsilon The accuracy of the comparison.
	 * @return A value indicating whether the two vectors are nearly equal.
	 */
	public boolean approxEquals(Vector2 v, float epsilon)
	{
		return Math.abs(x - v.x) < epsilon && Math.abs(y - v.y) < epsilon;
	}
	
	/**
	 * Negates the vector.
	 * @return this
	 * @deprecated Make Vector2 immutable! Use Vector2.negate instead.
	 */
	public Vector2 negate()
	{
		return scale(-1);
	}
	
	/**
	 * Scales the vector.
	 * @param scalar The scalar to scale by.
	 * @return this
	 * @deprecated Make Vector2 immutable!
	 */
	public Vector2 scale(float scalar)
	{
		x *= scalar;
		y *= scalar;
		return this;
	}
	
	/**
	 * Sets the magnitude of the vector to a scalar.
	 * @param scalar The new magnitude of the vector.
	 * @return this
	 * @deprecated Make Vector2 immutable!
	 */
	public Vector2 scaleTo(float scalar)
	{
		normalize();
		scale(scalar);
		return this;
	}
	
	/**
	 * Gets the length (magnitude) of the vector.
	 * @return The magnitude of the vector.
	 */
	public float length()
	{
		return (float)Math.sqrt((x * x) + (y * y));
	}
	
	/**
	 * Gets the dot product of this vector and another one.
	 * @param v The vector to operate with
	 * @return The dot product as a scalar.
	 * @deprecated Make Vector2 immutable!
	 */
	public float dot(Vector2 v)
	{
		return x * v.getX() + y * v.getY();
	}
	
	/**
	 * Normalizes a vector.
	 * @return this
	 * @deprecated Make Vector2 immutable!
	 */
	public Vector2 normalize()
	{
		if (x != 0.0f || y != 0.0f)
			scale(1 / length());
		return this;
	}
	
	/**
	 * Gets the angle between this vector and another one.
	 * @param v The vector to operate with
	 * @return The angle, in radians
	 * @deprecated Make Vector2 immutable!
	 */
	public float angle(Vector2 v)
	{
	    float rad = (float)Math.acos(Vector2.normalize(this).dot(Vector2.normalize(v)));
	    //clamp angle between 0 and 360
		if (rad == Math.PI * 2)
			rad = 0.0f;
		else if (rad > Math.PI * 2)
			rad -= Math.PI * 2 * (int)(rad / Math.PI * 2);
		else if (rad < 0.0f)
			rad += Math.PI * 2;
		
		return rad;
	}
	
	/**
	 * The angle of this vector compared to the unit X axis vector.
	 * @return The angle of this vector.
	 */
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
	
	/**
	 * Same as angleRad(), only in degrees.
	 * @return degrees
	 */
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
	
	/**
	 * Sets this vector to it's normal vector.
	 * @return this
	 * @deprecated Make Vector2 immutable!
	 */
	public Vector2 setNormal()
	{
		set(y, -x);
		return this;
	}
	
	/**
	 * Formats the vector for text output.
	 * @return A formatted string.
	 */
	public String toString()
	{
		return "<" + x + ", " + y + ">";
	}
	
	/***********************
	 * Getters and Setters *
	 ***********************/
	
	/**
	 * Gets the X component of the vector.
	 * @return The vector's X component.
	 */
	public float getX()
	{
	    return x;
	}
	
	/**
	 * Sets the X component of the vector.
	 * @param x The new X component.
	 * @deprecated Make Vector2 immutable!
	 */
	public void setX(float x)
	{
	    this.x = x;
	}
	
	/**
	 * Gets the Y component of the vector.
	 * @return The vector's Y component.
	 */
	public float getY()
	{
	    return y;
	}
	
	/**
	 * Sets the Y component of the vector.
	 * @param y The new Y component.
	 * @deprecated Make Vector2 immutable!
	 */
	public void setY(float y)
	{
	    this.y = y;
	}
	
	/*************
     * Constants *
     *************/
    
    /**
     * A unit vector in the X direction.
     * @return A vector.
     */
    public static Vector2 unitX()
    {
        return new Vector2(1, 0);
    }
    
    /**
     * A unit vector in the Y direction.
     * @return A vector.
     */
    public static Vector2 unitY()
    {
        return new Vector2(0, 1);
    }
    
    /**
     * A unit vector with all components equal to 0.
     * @return A vector.
     */
    public static Vector2 zero()
    {
        return new Vector2();
    }
}
