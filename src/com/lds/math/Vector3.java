package com.lds.math;

/**
 * A 3-component vector.
 * @author Lightning Development Studios
 */
public final class Vector3
{
    /*************
     * Constants *
     *************/
    
    /**
     * A unit vector in the X direction.
     */
    public static final Vector3 UNIT_X = new Vector3(1, 0, 0);
    
    /**
     * A unit vector in the Y direction.
     */
    public static final Vector3 UNIT_Y = new Vector3(0, 1, 0);

    /**
     * A unit vector in the Z direction.
     */
    public static final Vector3 UNIT_Z = new Vector3(0, 0, 1);

    /**
     * A vector of all components equal to 1.
     */
    public static final Vector3 ONE = new Vector3(1, 1, 1);
    
    /**
     * A vector of all values components to 0.
     */
    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    
	/***********
	 * Members *
	 ***********/
	
	/**
	 * The vector's X component.
	 */
	private final float x;
	
	/**
	 * The vector's Y component.
	 */
	private final float y;
	
	/**
	 * The vector's Z component.
	 */
	private final float z;
	
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
	
	/**
	 * Turns all components of the vector into their absolute value equivalent.
	 * @param v The vector to process.
	 * @return A vector with all positive components.
	 */
	public static Vector3 abs(Vector3 v)
	{
		return new Vector3(Math.abs(v.getX()), Math.abs(v.getY()), Math.abs(v.getZ()));
	}
		
	/**
	 * Adds two vectors together.
	 * @param left The vector to be added to.
	 * @param right The vector to be added by.
	 * @return The resultant vector of the addition.
	 */
	public static Vector3 add(Vector3 left, Vector3 right)
	{
		float x1 = left.getX(), y1 = left.getY(), z1 = left.getZ(),
			  x2 = right.getX(), y2 = right.getY(), z2 = right.getZ();
		
		return new Vector3(x1 + x2, y1 + y2, z1 + z2);
	}
		
	/**
	 * Subtracts one vector from another.
	 * @param left The vector to subtract from.
	 * @param right The vector to subtract by.
	 * @return The resultant vector of the subtraction.
	 */
	public static Vector3 subtract(Vector3 left, Vector3 right)
	{
		float x1 = left.getX(), y1 = left.getY(), z1 = left.getZ(),
			  x2 = right.getX(), y2 = right.getY(), z2 = right.getZ();
		
		return new Vector3(x1 - x2, y1 - y2, z1 - z2);
	}
	
	/**
	 * Flips the sign of all the components of a vector.
	 * @param v The vector to negate.
	 * @return A vector equal in magnitude but opposite in direction.
	 */
	public static Vector3 negate(Vector3 v)
	{
		return new Vector3(-v.getX(), -v.getY(), -v.getZ());
	}
	
	/**
	 * Multiplies a vector by a scalar.
	 * @param v The vector to scale.
	 * @param scalar The amount to scale by.
	 * @return A scaled vector in the direction of v.
	 */
	public static Vector3 scale(Vector3 v, float scalar)
	{
		return new Vector3(scalar * v.getX(), scalar * v.getY(), scalar * v.getZ());
	}
	
	/**
	 * Normalizes a vector then scales it to a scalar.
	 * @param v The vector to scale.
	 * @param scalar The magnitude of the resultant vector.
	 * @return A vector with a magnitude of scalar in the direction of v.
	 */
	public static Vector3 scaleTo(Vector3 v, float scalar)
	{
		return Vector3.scale(Vector3.normalize(v), scalar);
	}
	
	/**
	 * Normalizes a vector.
	 * @param v The vector to normalize.
	 * @return A vector of magnitude 1 in the direction of v.
	 */
	public static Vector3 normalize(Vector3 v)
	{
		float mag = v.length();
		
		//prevent division by 0
		if (mag != 0)
			return Vector3.scale(v, 1.0f / mag);
		else
			return v;
	}
	
	/**
	 * Creates a vector pointing to the midpoint of the directed line segment between two vectors.
	 * \todo turn into a lerp method, add float 0-1 for distance.
	 * @param left The left vector.
	 * @param right The right vector.
	 * @return A vector with components averaged from the two vectors.
	 */
	public static Vector3 getMidpoint(Vector3 left, Vector3 right)
	{
		float x1 = left.getX(), y1 = left.getY(), z1 = left.getZ(),
			  x2 = right.getX(), y2 = right.getY(), z2 = right.getZ();
		
		return new Vector3((x1 + x2) / 2, 
						   (y1 + y2) / 2, 
						   (z1 + z2) / 2);
	}
	
	/**
	 * Calculates the dot product of two vectors.
	 * @param left The left vector.
	 * @param right The right vector.
	 * @return The dot product.
	 */
	public static float dot(Vector3 left, Vector3 right)
	{
	    return (left.getX() * right.getX()) + (left.getY() * right.getY()) + (left.getZ() * right.getZ());
	}
	
	/**
	 * Calculates the cross product of two vectors.
	 * @param left The left vector.
	 * @param right The right vector.
	 * @return The resultant vector of the cross product.
	 */
	public static Vector3 cross(Vector3 left, Vector3 right)
	{
		float x1 = left.getX(), y1 = left.getY(), z1 = left.getZ(),
			  x2 = right.getX(), y2 = right.getY(), z2 = right.getZ();

		return new Vector3(y1 * z2 - z1 * y2, 
						   z1 * x2 - x1 * z2, 
						   x1 * y2 - y1 * x2);
	}
	
	/********************
	 * Instance methods *
	 ********************/
	
	/*
	public boolean equals(Vector3 v)
	{
		return x == v.getX() && y == v.getY() && z == v.getZ();
	}
	
	public boolean approxEquals(Vector3 v, float epsilon)
	{
		return Math.abs(x - v.x) < epsilon && Math.abs(y - v.y) < epsilon && Math.abs(z - v.z) < epsilon;
	}
	*/
	
	/**
	 * Calculates the length (magnitude) of the vector.
	 * @return The magnitude of the vector.
	 */
	public float length()
	{
		return (float)Math.sqrt((x * x) + (y * y) + (z * z));
	}

	/***********
	 * Swizzle *
	 ***********/
	
	/**
	 * Gets the vector <x, y>.
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
	public float getX()
	{
	    return x;
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
	 * Gets the Z component of the vector.
	 * @return The vector's Z component.
	 */
	public float getZ()
	{
	    return z;
	}
}
