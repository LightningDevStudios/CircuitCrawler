package com.lds.math;

/**
 * A 2-component vector.
 * @author Lightning Development Studios
 */
public final class Vector2
{
    /*************
     * Constants *
     *************/
    
    /**
     * A unit vector in the X direction.
     */
    public static final Vector2 UNIT_X = new Vector2(1, 0);

    /**
     * A unit vector in the Y direction.
     */
    public static final Vector2 UINT_Y = new Vector2(0, 1);

    /**
     * A vector with all components equal to 0.
     */
    public static final Vector2 ZERO = new Vector2(0, 0);
    
    /**
     * A vector with all components equal to 1.
     */
    public static final Vector2 ONE = new Vector2(1, 1);
    
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
	
	/****************
	 * Constructors *
	 ****************/
	
	/**
	 * Initializes a new instance of the Vector2 class.
	 * @param x The X component of the vector.
	 * @param y The Y component of the vector.
	 */
	public Vector2(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Initializes a new instance of the Vector2 class from an angle.
	 * @param angle The angle (in degrees) which will be the vector's direction.
	 */
	public Vector2(float angle)
	{
		double angleRad = Math.toRadians(angle);
		x = (float)Math.cos(angleRad);
		y = (float)Math.sin(angleRad);
	}
	
	/********************
     * Instance Methods *
     ********************/
    
    /**
     * Checks for equality with another vector.
     * @param v The vector to compare against
     * @return A value indicating whether the two vectors are equal.
     * \todo Override Object.equals too!
     */
    public boolean equals(Vector2 v)
    {
        return x == v.x() && y == v.y();
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == null)
            return false;
        
        if (!(o instanceof Vector2))
            return false;
        
        Vector2 v = (Vector2)o;
        if (v.x() != x || v.y() != y)
            return false;
        
        return true;
    }
    
    @Override
    public int hashCode()
    {
        return new Float(x).hashCode() ^ new Float(y).hashCode();
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
     * Gets the length (magnitude) of the vector.
     * @return The magnitude of the vector.
     */
    public float length()
    {
        return (float)Math.sqrt((x * x) + (y * y));
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
     * Formats the vector for text output.
     * @return A formatted string.
     */
    @Override
    public String toString()
    {
        return "<" + x + ", " + y + ">";
    }
	
	/******************
	 * Static Methods *
	 ******************/
	
    /**
     * Transforms a Vector2 by a Matrix3x2.
     * @param v The Vector2 to transform.
     * @param mat A Matrix3x2 to transform by.
     * @return A transformed Vector2.
     */
    public static Vector2 transform(Vector2 v, Matrix3x2 mat)
    {
        float[] array = mat.array();
        
        float m11 = array[0], m12 = array[1], m13 = array[2],
              m21 = array[3], m22 = array[4], m23 = array[5];
        
        float vX = v.x();
        float vY = v.y();
        
        return new Vector2(vX * m11 + vY * m12 + m13,
                           vX * m21 + vY * m22 + m23);
    }
    
    
	/**
     * Turns all components of the vector into their absolute value equivalent.
     * @param v The vector to process.
     * @return A vector with all positive components.
     */
	public static Vector2 abs(Vector2 v)
	{
		return new Vector2(Math.abs(v.x()), Math.abs(v.y()));
	}
	
	/**
     * Adds two vectors together.
     * @param left The vector to be added to.
     * @param right The vector to be added by.
     * @return The resultant vector of the addition.
     */
	public static Vector2 add(Vector2 left, Vector2 right)
	{
		return new Vector2(left.x() + right.x(), left.y() + right.y());
	}
	
	/**
     * Subtracts one vector from another.
     * @param left The vector to subtract from.
     * @param right The vector to subtract by.
     * @return The resultant vector of the subtraction.
     */
	public static Vector2 subtract(Vector2 left, Vector2 right)
	{
		return new Vector2(left.x() - right.x(), left.y() - right.y());
	}
	
	/**
     * Flips the sign of all the components of a vector.
     * @param v The vector to negate.
     * @return A vector equal in magnitude but opposite in direction.
     */
	public static Vector2 negate(Vector2 v)
	{
		return new Vector2(-v.x(), -v.y());
	}
	
	/**
     * Multiplies a vector by a scalar.
     * @param v The vector to scale.
     * @param scalar The amount to scale by.
     * @return A scaled vector in the direction of v.
     */
	public static Vector2 scale(Vector2 v, float scalar)
	{
		return new Vector2(scalar * v.x(), scalar * v.y());
	}
	
	/**
     * Normalizes a vector then scales it to a scalar.
     * @param v The vector to scale.
     * @param scalar The magnitude of the resultant vector.
     * @return A vector with a magnitude of scalar in the direction of v.
     */
	public static Vector2 scaleTo(Vector2 v, float scalar)
	{
		return Vector2.scale(Vector2.normalize(v), scalar);
	}
	
	/**
     * Normalizes a vector.
     * @param v The vector to normalize.
     * @return A vector of magnitude 1 in the direction of v.
     */
	public static Vector2 normalize(Vector2 v)
	{
		if (v.x() != 0.0f || v.y() != 0.0f)
			return Vector2.scale(v, 1 / v.length());
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
	public static Vector2 getMidpoint(Vector2 left, Vector2 right)
	{
		return new Vector2((left.x() + right.x()) / 2, (left.y() + right.y()) / 2);
	}
	
	/**
	 * Gets the vector orthogonal to v.
	 * @param v The vector.
	 * @return A vector orthogonal to v.
	 */
	public static Vector2 getNormal(Vector2 v)
	{
		return new Vector2(v.y(), -v.x());
	}
	
	/**
	 * Finds the dot product of two vectors.
	 * @param left The left vector.
	 * @param right The right vector.
	 * @return The dot product.
	 */
	public static float dot(Vector2 left, Vector2 right)
	{
	    return left.x() * right.x() + left.y() * right.y();
	}
	
	/***********************
	 * Getters and Setters *
	 ***********************/
	
	/**
	 * Gets the X component of the vector.
	 * @return The vector's X component.
	 */
	public float x()
	{
	    return x;
	}
	
	/**
	 * Gets the Y component of the vector.
	 * @return The vector's Y component.
	 */
	public float y()
	{
	    return y;
	}
	
	/**
     * Converts the vector to a float array.
     * @return A float array containing the vector components.
     */
    public float[] array()
    {
        return new float[] { x, y };
    }
}
